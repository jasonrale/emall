package com.emall.dao;

import com.emall.entity.Goods;
import com.emall.entity.SeckillGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SeckillGoodsMapper {
    int deleteBySeckillGoodsId(String seckillGoodsId);

    int insert(Goods record);

    int insertSelective(SeckillGoods record);

    List<SeckillGoods> queryAll(@Param("limit") long limit, @Param("offset") long offset);

    SeckillGoods selectBySeckillGoodsId(String seckillGoodsId);

    int updateBySeckillGoodsIdSelective(SeckillGoods record);

    long count();
}