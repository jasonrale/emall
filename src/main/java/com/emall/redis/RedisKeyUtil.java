package com.emall.redis;

import com.emall.entity.Category;
import com.emall.entity.Goods;
import com.emall.entity.SeckillGoods;

public class RedisKeyUtil {
    public static final String GOODS_PREFIX = "Goods:";
    public static final String CATEGORY_PREFIX = "Category:";
    public static final String SECKILL_GOODS_PREFIX = "SeckillGoods:";

    public static String goodsAll(int currentNo, int pageSize) {
        return GOODS_PREFIX + "All:" + currentNo + ":" +  pageSize;
    }

    public static String goodsByGoodsId(Goods goods) {
        return GOODS_PREFIX + goods.getGoodsId();
    }

    public static String categoryById(Category category) {
        return CATEGORY_PREFIX + category.getCategoryId();
    }

    public static String seckillGoodsById(SeckillGoods seckillGoods) {
        return SECKILL_GOODS_PREFIX + seckillGoods.getSeckillGoodsId();
    }

    public static String goodsByKeyWord(String keyWord, int currentNo, int pageSize) {
        return GOODS_PREFIX + keyWord + ":" + currentNo + ":" +  pageSize;
    }
}
