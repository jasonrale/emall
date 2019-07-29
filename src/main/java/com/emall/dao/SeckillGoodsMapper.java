package com.emall.dao;

import com.emall.entity.SeckillGoods;

public interface SeckillGoodsMapper {

    int deleteByPrimaryKey(String sgId);

    int insert(SeckillGoods record);

    int insertSelective(SeckillGoods record);

    SeckillGoods selectByPrimaryKey(String sgId);

    int updateByPrimaryKeySelective(SeckillGoods record);

    int updateByPrimaryKey(SeckillGoods record);

}