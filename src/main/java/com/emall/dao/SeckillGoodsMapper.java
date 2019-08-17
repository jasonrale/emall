package com.emall.dao;

import com.emall.entity.Goods;
import com.emall.entity.SeckillGoods;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SeckillGoodsMapper {
    int deleteBySeckillGoodsId(String seckillGoodsId);

    int insert(Goods goods);

    int count();

    int countOnShelf();

    List<SeckillGoods> queryAllOnShelf();

    SeckillGoods selectBySeckillGoodsId(String seckillGoodsId);

    List<SeckillGoods> queryAll(@Param("limit") long limit, @Param("offset") long offset);

    int pull(@Param("seckillGoodsId") String seckillGoodsId);

    int put(@Param("seckillGoodsId") String seckillGoodsId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);


}