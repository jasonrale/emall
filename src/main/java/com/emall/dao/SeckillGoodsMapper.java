package com.emall.dao;

import com.emall.entity.SeckillGoods;

public interface SeckillGoodsMapper {
    int deleteBySeckillGoodsId(String seckillGoodsId);

    int insert(SeckillGoods seckillGoods);

    int insertSelective(SeckillGoods seckillGoods);

    SeckillGoods selectBySeckillGoodsId(String seckillGoodsId);

    int updateBySeckillGoodsIdSelective(SeckillGoods seckillGoods);
}