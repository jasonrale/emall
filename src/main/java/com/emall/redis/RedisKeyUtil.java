package com.emall.redis;

import com.emall.entity.Category;
import com.emall.entity.Goods;
import com.emall.entity.SeckillGoods;

public class RedisKeyUtil {
    public static final String GOODS_PREFIX = "Goods:";
    public static final String CATEGORY_PREFIX = "Category:";
    public static final String SECKILL_GOODS_PREFIX = "SeckillGoods:";

    public static String goodsByGoodsId(Goods goods) {
        return GOODS_PREFIX + goods.getGoodsId();
    }

    public static String categoryById(Category category) {
        return CATEGORY_PREFIX + category.getCategoryId();
    }

    public static String seckillGoodsById(SeckillGoods seckillGoods) {
        return SECKILL_GOODS_PREFIX + seckillGoods.getSeckillGoodsId();
    }
}
