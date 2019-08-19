package com.emall.redis;

import com.emall.entity.Category;
import com.emall.entity.Goods;
import com.emall.entity.SeckillGoods;
import com.emall.utils.StringUnicode;

public class RedisKeyUtil {
    public static final String GOODS_VERSION = "Goods:Version";
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

    public static String goodsByKeyWord(String keyWord, int currentNo, int pageSize) {
        return StringUnicode.stringToUnicode(GOODS_PREFIX + keyWord + ":" + currentNo + ":" + pageSize);
    }

    public static String seckillGoodsAll() {
        return SECKILL_GOODS_PREFIX + "All";
    }

    public static String seckillGoodsById(SeckillGoods seckillGoods) {
        return SECKILL_GOODS_PREFIX + seckillGoods.getSeckillGoodsId();
    }

    public static String seckillStockById(String seckillGoodsId) {
        return SECKILL_GOODS_PREFIX + "Stock:" + seckillGoodsId;
    }

    public static String captcha(String userId, String seckillGoodsId) {
        return SECKILL_GOODS_PREFIX + "Captcha:" + ":" + userId + ":" + seckillGoodsId;
    }

    public static String keyWordOfSort(String keyWord, String sort, int currentNo, int pageSize) {
        return sort.equals("none") ? StringUnicode.stringToUnicode(GOODS_PREFIX + "User:" + keyWord + ":" + "none:" + currentNo + ":" + pageSize) :
                sort.equals("asc") ? StringUnicode.stringToUnicode(GOODS_PREFIX + "User:" + keyWord + ":" + Goods.PRICE_ASC + ":" + currentNo + ":" + pageSize) :
                        StringUnicode.stringToUnicode(GOODS_PREFIX + "User:" + keyWord + ":" + Goods.PRICE_DESC + ":" + currentNo + ":" + pageSize);
    }

    public static String categoryOfSort(String categoryId, String sort, int currentNo, int pageSize) {
        return sort.equals("none") ? GOODS_PREFIX + "User:" + categoryId + ":" + "none:" + currentNo + ":" + pageSize :
                sort.equals("asc") ? GOODS_PREFIX + "User:" + categoryId + ":" + Goods.PRICE_ASC + ":" + currentNo + ":" + pageSize :
                        GOODS_PREFIX + "User:" + categoryId + ":" + Goods.PRICE_DESC + ":" + currentNo + ":" + pageSize;
    }
}
