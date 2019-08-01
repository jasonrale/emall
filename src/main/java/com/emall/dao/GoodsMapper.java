package com.emall.dao;

import com.emall.entity.Goods;
import com.emall.utils.PageModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {

    List<Goods> selectAllGoods(@Param("limit") long limit, @Param("offset") long offset);

    int deleteByPrimaryKey(String gId);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(String gId);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);
}