package com.emall.dao;

import com.emall.entity.SeckillOrder;

public interface SeckillOrderMapper {

    int deleteByPrimaryKey(String soId);

    int insert(SeckillOrder record);

    int insertSelective(SeckillOrder record);

    SeckillOrder selectByPrimaryKey(String soId);

    int updateByPrimaryKeySelective(SeckillOrder record);

    int updateByPrimaryKey(SeckillOrder record);

}