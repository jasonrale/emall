package com.emall.service;

import com.emall.dao.GoodsMapper;
import com.emall.entity.Goods;
import com.emall.utils.PageModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsService {

    @Resource
    GoodsMapper goodsMapper;

    public PageModel<Goods> queryAll(PageModel<Goods> pageModel) {
        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        List<Goods> goodsList = goodsMapper.queryAll(limit, offset);
        long count = goodsMapper.count();

        pageModel.setCount(count);
        pageModel.setList(goodsList);
        pageModel.setTotalPages();

        return pageModel;
    }
}
