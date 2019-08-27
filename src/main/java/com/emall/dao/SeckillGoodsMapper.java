package com.emall.dao;

import com.emall.entity.Goods;
import com.emall.entity.SeckillGoods;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SeckillGoodsMapper {
    int deleteBySeckillGoodsId(@Param("seckillGoodsId") String seckillGoodsId);

    int insert(Goods goods);

    int update(SeckillGoods seckillGoods);

    int count();

    int countOnShelf();

    List<SeckillGoods> queryAllOnShelf();

    SeckillGoods selectBySeckillGoodsId(@Param("seckillGoodsId") String seckillGoodsId);

    List<SeckillGoods> queryAll(@Param("limit") long limit, @Param("offset") long offset);

    int pull(@Param("seckillGoodsId") String seckillGoodsId);

    void changeStatus(@Param("seckillGoodsId") String seckillGoodsId, @Param("complete") Integer complete);

    int reduceStock(@Param("seckillGoodsId") String seckillGoodsId);
}