package com.zhangyu.seckill.exception;

import com.zhangyu.seckill.enums.SeckillStateEnum;

public class SeckillException extends Exception {

    private SeckillStateEnum seckillStateEnum;

    public SeckillException(SeckillStateEnum seckillStateEnum) {
        super(seckillStateEnum.toString());
        this.seckillStateEnum = seckillStateEnum;

    }

    public SeckillStateEnum getSeckillStateEnum() {
        return seckillStateEnum;
    }
}
