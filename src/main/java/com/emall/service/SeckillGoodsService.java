package com.emall.service;

import com.emall.dao.SeckillGoodsMapper;
import com.emall.entity.SeckillGoods;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SeckillGoodsService {
    @Resource
    SeckillGoodsMapper seckillGoodsMapper;

    public boolean insert(SeckillGoods seckillGoods) {
        return seckillGoodsMapper.insert(seckillGoods) != 0;
    }
}
