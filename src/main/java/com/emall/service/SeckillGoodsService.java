package com.emall.service;

import com.emall.dao.SeckillGoodsMapper;
import com.emall.entity.SeckillGoods;
import com.emall.utils.PageModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SeckillGoodsService {
    @Resource
    SeckillGoodsMapper seckillGoodsMapper;

    /**
     * 分页查询全部秒杀商品
     *
     * @param pageModel
     * @return
     */
    public PageModel queryAll(PageModel pageModel) {
        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        List seckillGoodsList = seckillGoodsMapper.queryAll(limit, offset);
        long count = seckillGoodsMapper.count();

        pageModel.setCount(count);
        pageModel.setList(seckillGoodsList);
        pageModel.setTotalPages();

        return pageModel;
    }
}
