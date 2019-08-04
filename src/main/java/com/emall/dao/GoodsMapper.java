package com.emall.dao;

import com.emall.entity.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {
    int deleteByPrimaryKey(String goodsId);

    int insert(Goods record);

    List<Goods> queryAll(@Param("limit") long limit, @Param("offset") long offset);

    Goods selectByPrimaryKey(String goodsId);

    int updateByPrimaryKey(Goods record);

    long count();
}