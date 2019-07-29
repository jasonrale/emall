package com.emall.dao;

import com.emall.entity.Goods;

public interface GoodsMapper {

    int deleteByPrimaryKey(String gId);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(String gId);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

}