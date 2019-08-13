package com.emall.service;

import com.emall.dao.SeckillGoodsMapper;
import com.emall.entity.SeckillGoods;
import com.emall.redis.RedisKeyUtil;
import com.emall.utils.PageModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillGoodsService {
    @Resource
    SeckillGoodsMapper seckillGoodsMapper;

    @Resource
    RedisTemplate redisTemplate;

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

    /**
     * 根据秒杀商品id查询商品并缓存
     * @param seckillGoodsId
     * @return
     */
    public SeckillGoods selectBySeckillGoodsId(String seckillGoodsId) {
        String seckillGoodsKey = RedisKeyUtil.SECKILL_GOODS_PREFIX + seckillGoodsId;
        if (redisTemplate.hasKey(seckillGoodsKey)) {
            return (SeckillGoods) redisTemplate.opsForValue().get(seckillGoodsKey);
        }

        SeckillGoods seckillGoods = seckillGoodsMapper.selectBySeckillGoodsId(seckillGoodsId);
        redisTemplate.opsForValue().set(seckillGoodsKey, seckillGoods, 1800, TimeUnit.SECONDS);
        return seckillGoods;
    }

    /**
     * 根据秒杀商品id删除缓存与数据库记录
     * @param seckillGoodsId
     * @return
     */
    public boolean deleteBySeckillGoodsId(String seckillGoodsId) {
        if (selectBySeckillGoodsId(seckillGoodsId).getSeckillGoodsStatus() != 0) {
            return false;
        }

        String seckillGoodsKey = RedisKeyUtil.SECKILL_GOODS_PREFIX + seckillGoodsId;
        boolean flag = seckillGoodsMapper.deleteBySeckillGoodsId(seckillGoodsId) != 0;

        if (redisTemplate.hasKey(seckillGoodsKey)) {
            redisTemplate.delete(seckillGoodsKey);
        }

        return flag;
    }

    public boolean pull(String seckillGoodsId) {
        String seckillGoodsKey = RedisKeyUtil.SECKILL_GOODS_PREFIX + seckillGoodsId;
        if (selectBySeckillGoodsId(seckillGoodsId).getSeckillGoodsStatus() != 1) {
            return false;
        }

        boolean flag = seckillGoodsMapper.pull(seckillGoodsId) != 0;

        if (redisTemplate.hasKey(seckillGoodsKey)) {
            redisTemplate.delete(seckillGoodsKey);
        }

        return flag;
    }

    public boolean put(String seckillGoodsId, Date startTime, Date endTime) {
        String seckillGoodsKey = RedisKeyUtil.SECKILL_GOODS_PREFIX + seckillGoodsId;

        boolean flag = seckillGoodsMapper.put(seckillGoodsId, startTime, endTime) != 0;

        if (redisTemplate.hasKey(seckillGoodsKey)) {
            redisTemplate.delete(seckillGoodsKey);
        }

        return flag;
    }

    public int countOnShelf() {
        return seckillGoodsMapper.countOnShelf();
    }
}
