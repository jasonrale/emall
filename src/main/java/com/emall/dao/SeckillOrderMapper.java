package com.emall.dao;

import com.emall.entity.SeckillOrder;
import org.apache.ibatis.annotations.Param;

public interface SeckillOrderMapper {
    int insert(SeckillOrder seckillOrder);

    SeckillOrder selectBySeckillOrderId(@Param("seckillOrderId") String seckillOrderId);
}