package com.emall.dao;

import com.emall.entity.SeckillGoods;

public interface SeckillGoodsMapper {
    int deleteByPrimaryKey(String seckillGoodsId);

    int insert(SeckillGoods record);

    SeckillGoods selectByPrimaryKey(String seckillGoodsId);

    int updateByPrimaryKeySelective(SeckillGoods record);
}