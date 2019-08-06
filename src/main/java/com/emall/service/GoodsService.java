package com.emall.service;

import com.emall.dao.GoodsMapper;
import com.emall.entity.Goods;
import com.emall.redis.RedisKeyUtil;
import com.emall.utils.PageModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class GoodsService {
    @Resource
    GoodsMapper goodsMapper;

    @Resource
    RedisTemplate redisTemplate;

    /**
     * 分页查询全部商品
     * @param pageModel
     * @return
     */
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

    /**
     * 根据商品id查询商品
     * @param goodsId
     * @return
     */
    public Goods selectByGoodsId(String goodsId) {
        String goodsKey = RedisKeyUtil.GOODS_PREFIX + goodsId;
        if (redisTemplate.hasKey(goodsKey)) {
            return (Goods) redisTemplate.opsForValue().get(goodsKey);
        }

        return goodsMapper.selectByGoodsId(goodsId);
    }

    /**
     * 根据关键字查询商品列表的完整逻辑
     * @param keyWord
     * @param pageModel
     * @return
     */
    public PageModel<Goods> selectByKeyWord(String keyWord, PageModel<Goods> pageModel) {
        String listKey = RedisKeyUtil.goodsByKeyWord(keyWord, pageModel.getCurrentNo(), pageModel.getPageSize());

        if (redisTemplate.hasKey(listKey)) {
            //从Redis缓存中获取列表中所有商品的键
            List<String> goodsKeyList = redisTemplate.opsForList().range(listKey, 0, -2);

            if (goodsKeyList == null) {
                return selectByKeyWordFromDB(keyWord, pageModel);
            }

           return getFromRedis(listKey, goodsKeyList, pageModel);
        }

        return selectByKeyWordFromDB(keyWord, pageModel);
    }


    /**
     * 根据关键字在数据库中查询商品列表并缓存到Redis
     * @param keyWord
     * @param pageModel
     * @return
     */
    public PageModel<Goods> selectByKeyWordFromDB(String keyWord, PageModel<Goods> pageModel) {
        String listKey = RedisKeyUtil.goodsByKeyWord(keyWord, pageModel.getCurrentNo(), pageModel.getPageSize());

        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        List<Goods> goodsList = goodsMapper.selectByKeyWord(keyWord, limit, offset);
        long count = goodsMapper.countByKeyWord(keyWord);

        return pageToRedis(listKey, goodsList, count, pageModel);
    }

    public PageModel<Goods> selectByCategoryId(String categoryId, PageModel<Goods> pageModel) {
        String listKey = RedisKeyUtil.goodsByCategoryId(categoryId, pageModel.getCurrentNo(), pageModel.getPageSize());

        if (redisTemplate.hasKey(listKey)) {
            //从Redis缓存中获取列表中所有商品的键
            List<String> goodsKeyList = redisTemplate.opsForList().range(listKey, 0, -2);

            if (goodsKeyList == null) {
                return selectByCategoryIdFromDB(categoryId, pageModel);
            }

            return getFromRedis(listKey, goodsKeyList, pageModel);
        }
        return selectByCategoryIdFromDB(categoryId, pageModel);
    }

    public PageModel<Goods> selectByCategoryIdFromDB(String categoryId, PageModel<Goods> pageModel) {
        String listKey = RedisKeyUtil.goodsByCategoryId(categoryId, pageModel.getCurrentNo(), pageModel.getPageSize());

        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        List<Goods> goodsList = goodsMapper.selectByCategoryId(categoryId, limit, offset);
        long count = goodsMapper.countByCategoryId(categoryId);

        return pageToRedis(listKey, goodsList, count, pageModel);
    }

    public PageModel<Goods> pageToRedis(String listKey, List<Goods> goodsList, long count, PageModel<Goods> pageModel) {
        for (Goods goods : goodsList) {
            String key = RedisKeyUtil.goodsByGoodsId(goods);
            //将查询到的每个商品缓存到Redis
            redisTemplate.opsForValue().set(key, goods, 1800, TimeUnit.SECONDS);
            //将Redis缓存中每个商品的键存入list
            redisTemplate.opsForList().rightPush(listKey, key);
        }
        //将商品总数量追加到list末尾
        redisTemplate.opsForList().rightPush(listKey, count);
        redisTemplate.expire(listKey, 1800, TimeUnit.SECONDS);

        pageModel.setCount(count);
        pageModel.setList(goodsList);
        pageModel.setTotalPages();

        return pageModel;
    }

    public PageModel<Goods> getFromRedis(String listKey, List<String> goodsKeyList, PageModel pageModel) {
        List<Goods> goodsList = new ArrayList<>();

        for (String goodsKey : goodsKeyList) {
            if (redisTemplate.hasKey(goodsKey)) {
                //通过键获取商品对象填充到页面list
                goodsList.add((Goods) redisTemplate.opsForValue().get(goodsKey));
            } else {
                String[] part = goodsKey.split(":");
                goodsList.add(selectByGoodsId(part[1]));
            }

        }

        //从Redis缓存中获取商品总数量
        long count = (long) redisTemplate.opsForList().index(listKey, -1);

        pageModel.setCount(count);
        pageModel.setList(goodsList);
        pageModel.setTotalPages();

        return pageModel;
    }
}
