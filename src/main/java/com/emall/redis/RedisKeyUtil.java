package com.emall.redis;

import com.emall.entity.Goods;
import com.emall.entity.SeckillGoods;
import com.emall.utils.StringUnicode;
import com.emall.vo.OrderVo;

/**
 * Redis键生成类
 */
public class RedisKeyUtil {
    public static final String GOODS_PREFIX = "Goods:";
    public static final String GOODS_VERSION = "Goods:Version";
    public static final String ORDER_VO_PREFIX = "OrderVo:";
    private static final String ORDER_VO_VERSION_PREFIX = "OrderVo:Version:";
    public static final String CATEGORY_PREFIX = "Category:";
    public static final String SECKILL_GOODS_PREFIX = "SeckillGoods:";
    private static final String SECKILL_ORDER_PREFIX = "SeckillOrder:";
    private static final String ACCESS_LIMIT_PREFIX = "AccessLimit:";


    /**
     * 所有商品分页键
     *
     * @param currentNo
     * @param pageSize
     * @return
     */
    public static String goodsAll(int currentNo, int pageSize) {
        return GOODS_PREFIX + "All:" + currentNo + ":" +  pageSize;
    }

    /**
     * 商品id键
     * @param goods
     * @return
     */
    public static String goodsByGoodsId(Goods goods) {
        return GOODS_PREFIX + goods.getGoodsId();
    }

    /**
     * 商品关键字分页键
     * @param keyWord
     * @param currentNo
     * @param pageSize
     * @return
     */
    public static String goodsByKeyWord(String keyWord, int currentNo, int pageSize) {
        return StringUnicode.stringToUnicode(GOODS_PREFIX + keyWord + ":" + currentNo + ":" + pageSize);
    }

    /**
     * 所有上架秒杀商品键
     * @return
     */
    public static String seckillGoodsAll() {
        return SECKILL_GOODS_PREFIX + "All";
    }

    /**
     * 秒杀商品id键
     * @param seckillGoods
     * @return
     */
    public static String seckillGoodsById(SeckillGoods seckillGoods) {
        return SECKILL_GOODS_PREFIX + seckillGoods.getSeckillGoodsId();
    }

    /**
     * 秒杀库存id键
     * @param seckillGoodsId
     * @return
     */
    public static String seckillStockById(String seckillGoodsId) {
        return SECKILL_GOODS_PREFIX + "Stock:" + seckillGoodsId;
    }

    /**
     * 验证码结果键
     * @param userId
     * @param seckillGoodsId
     * @return
     */
    public static String captcha(String userId, String seckillGoodsId) {
        return SECKILL_GOODS_PREFIX + "Captcha:" + ":" + userId + ":" + seckillGoodsId;
    }

    /**
     * 秒杀路径键
     *
     * @param userId
     * @param seckillGoodsId
     * @return
     */
    public static String seckillPath(String userId, String seckillGoodsId) {
        return SECKILL_GOODS_PREFIX + "Path:" + userId + ":" + seckillGoodsId;
    }

    /**
     * 秒杀库存标记键
     *
     * @param seckillGoodsId
     * @return
     */
    public static String seckillStockOver(String seckillGoodsId) {
        return SECKILL_GOODS_PREFIX + "StockOver:" + seckillGoodsId;
    }

    /**
     * 秒杀订单键
     *
     * @param userId
     * @param seckillGoodsId
     * @return
     */
    public static String seckillOrder(String userId, String seckillGoodsId) {
        return SECKILL_ORDER_PREFIX + userId + ":" + seckillGoodsId;
    }

    /**
     * 商品关键字排序分页键
     * @param keyWord
     * @param sort
     * @param currentNo
     * @param pageSize
     * @return
     */
    public static String keyWordOfSort(String keyWord, String sort, int currentNo, int pageSize) {
        return sort.equals("none") ? StringUnicode.stringToUnicode(GOODS_PREFIX + "User:" + keyWord + ":" + "none:" + currentNo + ":" + pageSize) :
                sort.equals("asc") ? StringUnicode.stringToUnicode(GOODS_PREFIX + "User:" + keyWord + ":" + Goods.PRICE_ASC + ":" + currentNo + ":" + pageSize) :
                        StringUnicode.stringToUnicode(GOODS_PREFIX + "User:" + keyWord + ":" + Goods.PRICE_DESC + ":" + currentNo + ":" + pageSize);
    }

    /**
     * 商品类别排序分页键
     * @param categoryId
     * @param sort
     * @param currentNo
     * @param pageSize
     * @return
     */
    public static String categoryOfSort(String categoryId, String sort, int currentNo, int pageSize) {
        return sort.equals("none") ? GOODS_PREFIX + "User:" + categoryId + ":" + "none:" + currentNo + ":" + pageSize :
                sort.equals("asc") ? GOODS_PREFIX + "User:" + categoryId + ":" + Goods.PRICE_ASC + ":" + currentNo + ":" + pageSize :
                        GOODS_PREFIX + "User:" + categoryId + ":" + Goods.PRICE_DESC + ":" + currentNo + ":" + pageSize;
    }

    /**
     * 访问限制次数键
     *
     * @param userId
     * @return
     */
    public static String accessLimit(String userId) {
        return ACCESS_LIMIT_PREFIX + userId;
    }

    /**
     * 订单业务对象键
     *
     * @param orderVo
     * @return
     */
    public static String orderVoById(OrderVo orderVo) {
        return ORDER_VO_PREFIX + orderVo.getOrderId();
    }

    /**
     * 用户id订单业务分页键
     *
     * @param userId
     * @param currentNo
     * @param pageSize
     * @return
     */
    public static String orderVoByUserId(String userId, int currentNo, int pageSize) {
        return ORDER_VO_PREFIX + userId + ":" + currentNo + ":" + pageSize;
    }

    /**
     * 用户订单业务缓存版本键
     *
     * @param userId
     * @return
     */
    public static String versionByUserId(String userId) {
        return ORDER_VO_VERSION_PREFIX + userId;
    }
}
