package com.emall.dao;

import com.emall.entity.Goods;

public interface GoodsMapper {
    int deleteByPrimaryKey(Long gId);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long gId);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);
}