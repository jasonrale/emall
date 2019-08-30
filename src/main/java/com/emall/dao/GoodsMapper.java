package com.emall.dao;

import com.emall.entity.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {
    int insert(Goods goods);

    int count();

    int countByKeyWord(@Param("keyWord") String keyWord);

    int countByKeyWordForUser(@Param("keyWord") String keyWord);

    int countByCategoryIdForUser(@Param("categoryId") String categoryId);

    Goods selectByGoodsId(@Param("goodsId") String goodsId);

    List<Goods> queryAll(@Param("limit") long limit, @Param("offset") long offset);


    List<Goods> selectByKeyWord(@Param("keyWord") String keyWord, @Param("limit") long limit, @Param("offset") long offset);

    List<Goods> selectByKeyWordPaged(@Param("keyWord") String keyWord, @Param("limit") long limit, @Param("offset") long offset);

    List<Goods> selectByKeyWordAsc(@Param("keyWord") String keyWord, @Param("limit") long limit, @Param("offset") long offset);

    List<Goods> selectByKeyWordDesc(@Param("keyWord") String keyWord, @Param("limit") long limit, @Param("offset") long offset);


    List<Goods> selectByCategoryId(@Param("categoryId") String categoryId, @Param("limit") long limit, @Param("offset") long offset);

    List<Goods> selectByCategoryIdAsc(@Param("categoryId") String categoryId, @Param("limit") long limit, @Param("offset") long offset);

    List<Goods> selectByCategoryIdDesc(@Param("categoryId") String categoryId, @Param("limit") long limit, @Param("offset") long offset);


    int pull(@Param("goodsId") String goodsId);

    int put(@Param("goodsId") String goodsId);

    int updateByGoodsId(Goods goods);

    int reduceStock(@Param("goodsId") String goodsId, @Param("count") Integer count);
}