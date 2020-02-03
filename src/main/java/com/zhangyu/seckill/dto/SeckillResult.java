package com.zhangyu.seckill.dto;

import com.zhangyu.seckill.entity.Order;
import com.zhangyu.seckill.enums.SeckillStateEnum;

import java.io.Serializable;

public class SeckillResult implements Serializable {

    private long id;

    private int state;

    private String stateInfo;

    private Order order;

    public SeckillResult(long id, int state, String stateInfo, com.zhangyu.seckill.entity.Order order) {
        this.id = id;
        this.state = state;
        this.stateInfo = stateInfo;
        this.order = order;
    }
    public SeckillResult(long seckillId, SeckillStateEnum stateEnum, Order payOrder) {
        this.id = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.order = payOrder;
    }

    public SeckillResult(SeckillStateEnum state) {
        this.state = state.getState();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public long getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public Order getOrder() {
        return order;
    }
}
