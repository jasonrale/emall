package com.emall.service;

import com.emall.dao.SeckillGoodsMapper;
import com.emall.entity.SeckillGoods;
import com.emall.redis.RedisKeyUtil;
import com.emall.utils.PageModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public PageModel queryAllPaged(PageModel pageModel) {
        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        List seckillGoodsList = seckillGoodsMapper.queryAll(limit, offset);
        int count = seckillGoodsMapper.count();

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

    /**
     * 下架秒杀商品
     *
     * @param seckillGoodsId
     * @return
     */
    public boolean pull(String seckillGoodsId) {
        String seckillGoodsKey = RedisKeyUtil.SECKILL_GOODS_PREFIX + seckillGoodsId;
        String allKey = RedisKeyUtil.seckillGoodsAll();

        if (selectBySeckillGoodsId(seckillGoodsId).getSeckillGoodsStatus() != 1) {
            return false;
        }

        boolean flag = seckillGoodsMapper.pull(seckillGoodsId) != 0;

        if (redisTemplate.hasKey(seckillGoodsKey)) {
            redisTemplate.delete(seckillGoodsKey);
        }

        if (redisTemplate.hasKey(allKey)) {
            redisTemplate.delete(allKey);
        }

        return flag;
    }

    /**
     * 上架秒杀商品
     * @param seckillGoodsId
     * @return
     */
    public boolean put(String seckillGoodsId, Date startTime, Date endTime) {
        String seckillGoodsKey = RedisKeyUtil.SECKILL_GOODS_PREFIX + seckillGoodsId;
        String allKey = RedisKeyUtil.seckillGoodsAll();

        boolean flag = seckillGoodsMapper.put(seckillGoodsId, startTime, endTime) != 0;

        if (redisTemplate.hasKey(seckillGoodsKey)) {
            redisTemplate.delete(seckillGoodsKey);
        }

        if (redisTemplate.hasKey(allKey)) {
            redisTemplate.delete(allKey);
        }

        return flag;
    }

    /**
     * 查看已上架的秒杀商品数
     * @return
     */
    public int countOnShelf() {
        return seckillGoodsMapper.countOnShelf();
    }

    /**
     * 用户端查询所有秒杀商品
     *
     * @return
     */
    public List<SeckillGoods> queryAll() {
        String listKey = RedisKeyUtil.seckillGoodsAll();

        if (redisTemplate.hasKey(listKey)) {
            List<String> seckillGoodsKeyList = redisTemplate.opsForList().range(listKey, 0, -1);
            if (seckillGoodsKeyList == null || seckillGoodsKeyList.size() == 0) {
                return queryAllForUser();
            }
            return getFromRedis(seckillGoodsKeyList);
        }

        return queryAllForUser();
    }

    /**
     * 用户端从数据库查询所有秒杀商品
     *
     * @return
     */
    public List<SeckillGoods> queryAllForUser() {
        String redisKey = RedisKeyUtil.seckillGoodsAll();

        List<SeckillGoods> list = seckillGoodsMapper.queryAllOnShelf();
        for (SeckillGoods seckillGoods : list) {
            String seckillGoodsKey = RedisKeyUtil.seckillGoodsById(seckillGoods);
            redisTemplate.opsForValue().set(seckillGoodsKey, seckillGoods, 1800, TimeUnit.SECONDS);
            redisTemplate.opsForList().rightPush(redisKey, seckillGoodsKey);
        }

        redisTemplate.expire(redisKey, 1800, TimeUnit.SECONDS);

        return list;
    }

    /**
     * 从缓存查询所有秒杀商品
     *
     * @return
     */
    public List<SeckillGoods> getFromRedis(List<String> seckillGoodsKeyList) {
        List<SeckillGoods> seckillGoodsList = new ArrayList<>();

        for (String seckillGoodsKey : seckillGoodsKeyList) {
            if (redisTemplate.hasKey(seckillGoodsKey)) {
                //通过键获取商品对象填充到页面list
                seckillGoodsList.add((SeckillGoods) redisTemplate.opsForValue().get(seckillGoodsKey));
            } else {
                String[] part = seckillGoodsKey.split(":");
                seckillGoodsList.add(selectBySeckillGoodsId(part[1]));
            }
        }

        return seckillGoodsList;
    }
}
