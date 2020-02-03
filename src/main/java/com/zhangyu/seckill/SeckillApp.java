package com.zhangyu.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhangyu.seckill.dao")
public class SeckillApp {
    public static void main(String[] args) {
        SpringApplication.run(SeckillApp.class, args);
    }
}
