package com.emall.dao;

import com.emall.entity.Goods;
import com.emall.entity.SeckillGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 秒杀商品数据接口层
 */
public interface SeckillGoodsMapper {
    int deleteBySeckillGoodsId(@Param("seckillGoodsId") String seckillGoodsId);

    int insert(SeckillGoods seckillGoods);

    int update(SeckillGoods seckillGoods);

    int count();

    int countOnShelf();

    int countByKeyWord(@Param("keyWord") String keyWord);

    SeckillGoods selectBySeckillGoodsId(@Param("seckillGoodsId") String seckillGoodsId);

    List<SeckillGoods> queryAll(@Param("limit") long limit, @Param("offset") long offset);

    List<SeckillGoods> queryByKeyWord(@Param("keyWord") String keyWord, @Param("limit") long limit, @Param("offset") long offset);

    int put(SeckillGoods seckillGoods);

    int pull(@Param("seckillGoodsId") String seckillGoodsId);

    void changeStatus(@Param("seckillGoodsId") String seckillGoodsId, @Param("complete") Integer complete);

    int reduceStock(@Param("seckillGoodsId") String seckillGoodsId);
}