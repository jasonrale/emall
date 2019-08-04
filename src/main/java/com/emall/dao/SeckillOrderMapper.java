package com.emall.dao;

import com.emall.entity.SeckillOrder;

public interface SeckillOrderMapper {
    int deleteByPrimaryKey(String seckillOrderId);

    int insert(SeckillOrder record);

    SeckillOrder selectByPrimaryKey(String seckillOrderId);

    int updateByPrimaryKey(SeckillOrder record);
}