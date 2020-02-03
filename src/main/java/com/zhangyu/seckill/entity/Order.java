package com.zhangyu.seckill.entity;

import java.util.Date;

public class Order {

    private long productId;

    private long phone;

    private int state;

    private Date createTime;

    public Order() {

    }

    public Order(long productId, long phone, int state, Date createTime) {
        this.productId = productId;
        this.phone = phone;
        this.state = state;
        this.createTime = createTime;
    }

    private Order(Builder builder) {
        productId = builder.ProductId;
        phone = builder.phone;
        state = builder.state;
        createTime = builder.createTime;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getProductId() {
        return productId;
    }

    public long getPhone() {
        return phone;
    }

    public int getState() {
        return state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public static final class Builder {
        private long ProductId;
        private long phone;
        private int state;
        private Date createTime;

        public Builder() {
        }

        public Builder productId(long val) {
            ProductId = val;
            return this;
        }

        public Builder phone(long val) {
            phone = val;
            return this;
        }

        public Builder state(int val) {
            state = val;
            return this;
        }

        public Builder createTime(Date val) {
            createTime = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
