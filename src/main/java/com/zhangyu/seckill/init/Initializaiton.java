package com.zhangyu.seckill.init;

import com.zhangyu.seckill.MessageQueue.MQConsumer;
import com.zhangyu.seckill.constant.RedisPrefix;
import com.zhangyu.seckill.dao.InventoryDao;
import com.zhangyu.seckill.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


@Component
public class Initializaiton implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Initializaiton.class);

    @Resource(name = "initJedisPool")
    private JedisPool jedisPool;

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private MQConsumer mqConsumer;


    @Override
    public void run(String... args) throws Exception {
        initRedis();
        logger.info("Start to Consume--->");
        mqConsumer.receive();
    }

    private void initRedis() {
        Jedis jedis = jedisPool.getResource();
        //clean the redis
        jedis.flushDB();
        List<Product> products = inventoryDao.queryAll(0, 10);
        if (products == null || products.size()< 1) {
            logger.info("--FatalError!!! seckill_list_data is empty");
            return;
        }

        for (Product product : products) {
            jedis.sadd(RedisPrefix.SECKILL_ID_SET, product.getId() + "");

            String inventoryKey = RedisPrefix.SECKILL_INVENTORY + product.getId();
            logger.error("[debug] set {} : {}",inventoryKey,String.valueOf(product.getQuantity()));
            jedis.set(inventoryKey, String.valueOf(product.getQuantity()));
        }
        jedis.close();
        logger.info("Redis initialization finished ! ");
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o[1]));
    }

}
