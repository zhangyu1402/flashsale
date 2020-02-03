package com.zhangyu.seckill.dao;

import com.zhangyu.seckill.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;

public interface OrderDao {
    /**
     * 插入购买明细,可过滤重复
     * @param id
     * @param userPhone
     * @return
     * 插入的行数
     */
    int insertOrder(@Param("id") long id, @Param("userPhone") long userPhone,
                       @Param("nowTime") Date nowTime);

    /**
     * 根据id查询SuccessKilled并携带秒杀产品对象实体
     * @param id
     * @return
     */
    Order queryById(@Param("id") long id, @Param("userPhone") long userPhone);
}
