package com.emall.redis;

import com.emall.entity.Goods;

public class RedisKeyUtil {

    public static final String GOODS_PREFIX = "Goods:";

    public static String goodsAll(int currentNo, int pageSize) {
        return GOODS_PREFIX + currentNo + ":" + pageSize;
    }

    public static String goodsByGoodsId(Goods goods) {
        return GOODS_PREFIX + goods.getGoodsId();
    }

    public static String goodsByKeyWord(String keyWord, int currentNo, int pageSize) {
        return GOODS_PREFIX + keyWord + ":" + currentNo + ":" + pageSize;
    }

    public static String goodsByCategoryId(String categoryId, int currentNo, int pageSize) {
        return GOODS_PREFIX + categoryId + ":" + currentNo + ":" + pageSize;
    }
}
