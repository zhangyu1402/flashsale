package com.zhangyu.seckill.service.Impl;

import com.zhangyu.seckill.MessageQueue.MQProducer;
import com.zhangyu.seckill.constant.RedisPrefix;
import com.zhangyu.seckill.dao.InventoryDao;
import com.zhangyu.seckill.dao.OrderDao;
import com.zhangyu.seckill.dto.SeckillMsg;
import com.zhangyu.seckill.dto.SeckillResult;
import com.zhangyu.seckill.entity.Order;
import com.zhangyu.seckill.entity.Product;
import com.zhangyu.seckill.enums.SeckillStateEnum;
import com.zhangyu.seckill.exception.SeckillException;
import com.zhangyu.seckill.service.RateLimitService;
import com.zhangyu.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;

@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String salt = "aksksks*&&^%%aaaa&^^%%*";
    @Autowired
    private RateLimitService rateLimitService;

    @Resource(name = "initJedisPool")
    private JedisPool jedisPool;

    @Autowired
    private MQProducer mqProducer;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Override
    public SeckillResult executeSeckill(long id, long userPhone, String md5) throws SeckillException {
        if (rateLimitService.tryAcquireSeckill()) {
            return handleSeckillAsync(id,userPhone,md5);
        } else {
            logger.info("--->ACCESS_LIMITED-->seckillId={},userPhone={}", id, userPhone);
            throw new SeckillException(SeckillStateEnum.ACCESS_LIMIT);
        }
    }

    private SeckillResult handleSeckillAsync(long productId, long userPhone, String md5) throws SeckillException {
//        if (md5 == null || !md5.equals(getMD5(productId))) {
//            logger.info("DATA_INVALID!!!. seckillId={},userPhone={}", productId, userPhone);
//            throw new SeckillException(SeckillStateEnum.DATA_INVALID);
//        }

        Jedis jedis = jedisPool.getResource();
        String inventoryKey = RedisPrefix.SECKILL_INVENTORY + productId;
        String userKey = RedisPrefix.SECKILL_USERS + productId;

        String inventoryStr = jedis.get(inventoryKey);
        int inventory = Integer.valueOf(inventoryStr);
        /*
            sold out
         */
        if (inventory <= 0) {
            jedis.close();
            logger.info("SOLD_OUT. productId={},userPhone={}", productId, userPhone);
            throw new SeckillException(SeckillStateEnum.SOLD_OUT);
        }
        /*
            duplicated order
         */
        if (jedis.sismember(userKey, String.valueOf(userPhone))) {
            jedis.close();
            logger.info("SECKILL_REPEATED. seckillId={},userPhone={}", productId, userPhone);
            throw new SeckillException(SeckillStateEnum.REPEAT_KILL);
        } else {
            jedis.close();
            // enter into the queue.
            SeckillMsg msgBody = new SeckillMsg();
            msgBody.setSeckillId(productId);
            msgBody.setUserPhone(userPhone);
            mqProducer.send(msgBody);

            Order order = new Order.Builder().phone(userPhone).productId(productId).state(SeckillStateEnum.ENQUEUE_PRE_SECKILL.getState()).build();

            logger.info("ENQUEUE_PRE_SECKILL>>>seckillId={},userPhone={}", productId, userPhone);
            return new SeckillResult(productId, SeckillStateEnum.ENQUEUE_PRE_SECKILL, order);

        }
    }

    @Override
    public void handleInRedis(long seckillId, long userPhone) throws SeckillException {
        Jedis jedis = jedisPool.getResource();

        String inventoryKey = RedisPrefix.SECKILL_INVENTORY + seckillId;
        String boughtKey = RedisPrefix.SECKILL_USERS + seckillId;

        String inventoryStr = jedis.get(inventoryKey);
        int inventory = Integer.valueOf(inventoryStr);
        logger.info("handleInRedis product is = {} inventory = {}", seckillId, inventory);
        if (inventory <= 0) {
            logger.info("handleInRedis SECKILLSOLD_OUT. seckillId={},userPhone={}", seckillId, userPhone);
            throw new SeckillException(SeckillStateEnum.SOLD_OUT);
        }
        if (jedis.sismember(boughtKey, String.valueOf(userPhone))) {
            logger.info("handleInRedis SECKILL_REPEATED. seckillId={},userPhone={}", seckillId, userPhone);
            throw new SeckillException(SeckillStateEnum.REPEAT_KILL);
        }
        jedis.decr(inventoryKey);
        jedis.sadd(boughtKey, String.valueOf(userPhone));

//        store the data in MySQL
        updateInventory(seckillId,userPhone);
        logger.info("handleInRedis_done");
    }

    /**
     * TO reduce the inventory in MySQL and insert the purchase record
     * @param seckillId
     * @param userPhone
     * @return
     * @throws SeckillException
     */

    @Override
    @Transactional
    public SeckillResult updateInventory(long seckillId, long userPhone) throws SeckillException {

        Date nowTime = new Date();
        try {
            // insert the purchase record
            int insertCount = orderDao.insertOrder(seckillId, userPhone, nowTime);
            if (insertCount <= 0) {
                // somebody already bought this product
                logger.info("seckill REPEATED.insertCount={} seckillId={},userPhone={}", insertCount,seckillId, userPhone);
                throw new SeckillException(SeckillStateEnum.REPEAT_KILL);
            } else {
                // reduceNumber是update操作，开启作用在表seckill上的行锁
                Product currentSeckill = inventoryDao.queryById(seckillId);
                boolean validTime = false;
                if (currentSeckill != null) {
                    long nowStamp = nowTime.getTime();
                    if (nowStamp > currentSeckill.getStartTime().getTime() && nowStamp < currentSeckill.getEndTime().getTime()
                            && currentSeckill.getQuantity() > 0 && currentSeckill.getVersion() > -1) {
                        validTime = true;
                    }
                }

                if (validTime) {
                    long oldVersion = currentSeckill.getVersion();
                    int updateCount = inventoryDao.reduceInventory(seckillId, oldVersion, oldVersion + 1);
                    if (updateCount <= 0) {
                        //rollback
                        logger.info("seckill_DATABASE_CONCURRENCY_ERROR!!!. seckillId={},userPhone={}", seckillId, userPhone);
                        throw new SeckillException(SeckillStateEnum.DB_CONCURRENCY_ERROR);
                    } else {
                        //seckill success commit
                        Order payOrder = orderDao.queryById(seckillId, userPhone);
                        logger.info("seckill SUCCESS->>>. seckillId={},userPhone={}", seckillId, userPhone);
                        return new SeckillResult(seckillId, SeckillStateEnum.SUCCESS, payOrder);
                    }
                } else {
                    logger.info("seckill_END. seckillId={},userPhone={}", seckillId, userPhone);
                    throw new SeckillException(SeckillStateEnum.END);
                }
            }
        } catch (SeckillException e1) {
            throw e1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException(SeckillStateEnum.INNER_ERROR);
        }
    }

    private String getMD5(long id) {
        String base = id + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());

        return md5;
    }
}
