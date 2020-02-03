package com.zhangyu.seckill.dao;

import com.zhangyu.seckill.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryDao {
    /**
     * reduce Inventory,
     *
     * @param id
     * @return 如果影响行数>1，表示更新的记录行数
     */
    int reduceInventory(@Param("id") long id, @Param("oldVersion") long oldVersion,
                        @Param("newVersion") long newVersion);

    Product queryById(long id);

    List<Product> queryAll(@Param("offset") int offet, @Param("limit") int limit);


}
