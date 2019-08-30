package com.emall.service;

import com.emall.dao.SeckillOrderMapper;
import com.emall.entity.SeckillGoods;
import com.emall.entity.SeckillOrder;
import com.emall.entity.User;
import com.emall.redis.RedisKeyUtil;
import com.emall.utils.SnowflakeIdWorker;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class SeckillOrderService {
    @Resource
    SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    SeckillOrderMapper seckillOrderMapper;

    @Resource
    RedisTemplate redisTemplate;

    /**
     * 缓存秒杀订单
     *
     * @param user
     * @param seckillGoods
     * @return
     */
    @Transactional
    public SeckillOrder insertCache(User user, SeckillGoods seckillGoods) {
        String userId = user.getUserId();
        String seckillGoodsId = seckillGoods.getSeckillGoodsId();
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setSeckillOrderId(String.valueOf(snowflakeIdWorker.nextId()));
        seckillOrder.setSeckillGoodsId(seckillGoodsId);
        seckillOrder.setUserId(userId);

        redisTemplate.opsForValue().set(RedisKeyUtil.seckillOrder(userId, seckillGoodsId), seckillOrder);

        return seckillOrder;
    }

    /**
     * 生成完整秒杀订单
     *
     * @param seckillOrder
     * @return
     */
    public boolean insert(SeckillOrder seckillOrder) {
        return seckillOrderMapper.insert(seckillOrder) != 0;
    }

    /**
     * 从缓存获取秒杀订单
     *
     * @param userId
     * @param seckillGoodsId
     * @return
     */
    public SeckillOrder selectByUserIdGoodsId(String userId, String seckillGoodsId) {
        return (SeckillOrder) redisTemplate.opsForValue().get(RedisKeyUtil.seckillOrder(userId, seckillGoodsId));
    }

    /**
     * 才能够数据库获取秒杀订单
     *
     * @param seckillOrderId
     * @return
     */
    public SeckillOrder selectBySeckillOrderId(String seckillOrderId) {
        return seckillOrderMapper.selectBySeckillOrderId(seckillOrderId);
    }
}
