package com.zhangyu.seckill.dto;

import java.io.Serializable;

public class SeckillMsg implements Serializable {

    private long productId;
    private long userPhone;

    public long getSeckillId() {
        return productId;
    }

    public void setSeckillId(long seckillId) {
        this.productId = seckillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }
}
