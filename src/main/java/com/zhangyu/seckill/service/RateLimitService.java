package com.zhangyu.seckill.service;

public interface RateLimitService {
    boolean tryAcquireSeckill();
}
