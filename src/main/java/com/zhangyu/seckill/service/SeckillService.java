package com.zhangyu.seckill.service;

import com.zhangyu.seckill.dto.SeckillResult;
import com.zhangyu.seckill.exception.SeckillException;


public interface SeckillService {
    SeckillResult executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException;

    /**
     * 在Redis real operation in Redis for flash
     * @param seckillId
     * @param userPhone
     * @throws SeckillException
     */
    void handleInRedis(long seckillId, long userPhone) throws SeckillException;

    SeckillResult updateInventory(long seckillId, long userPhone)
            throws SeckillException;
}
