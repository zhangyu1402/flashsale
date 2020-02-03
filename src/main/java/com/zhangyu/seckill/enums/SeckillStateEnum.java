package com.zhangyu.seckill.enums;

public enum SeckillStateEnum {
    SUCCESS(1, "SUCCESS"),
    INNER_ERROR(2,"INNER ERROR"),
    NO_LOGIN(3,"phone number miss"),
    ACCESS_LIMIT(4,"ACCESS_LIMIT"),
    DATA_INVALID(5,"Invalid data"),
    REPEAT_KILL(6,"REPEAT_KILL"),
    ENQUEUE_PRE_SECKILL(7, "waiting in queue..."),
    DB_CONCURRENCY_ERROR(8, "sold out"),
    END(0, "seckill process finish"),
    SOLD_OUT(2, "sold out");
    private int state;

    private String stateInfo;
    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }



}
