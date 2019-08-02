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

    public PageModel<Goods> selectAllGoods(PageModel<Goods> pageModel) {
        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        List<Goods> goodsList = goodsMapper.selectAllGoods(limit, offset);
        pageModel.setList(goodsList);
        pageModel.setTotalPages();

        return pageModel;
    }
}
