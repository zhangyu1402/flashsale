package com.zhangyu.seckill.service.Impl;

import com.google.common.util.concurrent.RateLimiter;
import com.zhangyu.seckill.dto.SeckillResult;
import com.zhangyu.seckill.service.RateLimitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RateLimitServiceImpl implements RateLimitService {
    @Value("${ratelimiter.rate}")
    private int rate;

    private RateLimiter seckillRateLimiter = RateLimiter.create(1000);

    @Override
    public boolean tryAcquireSeckill() {
        return seckillRateLimiter.tryAcquire();
    }


}
