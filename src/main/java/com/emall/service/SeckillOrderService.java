package com.emall.service;

import com.emall.dao.OrderItemMapper;
import com.emall.dao.OrderMapper;
import com.emall.dao.SeckillGoodsMapper;
import com.emall.dao.SeckillOrderMapper;
import com.emall.entity.*;
import com.emall.redis.RedisKeyUtil;
import com.emall.utils.SnowflakeIdWorker;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 秒杀订单业务层
 */
@Service
public class SeckillOrderService {
    @Resource
    SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    SeckillOrderMapper seckillOrderMapper;

    @Resource
    SeckillGoodsMapper seckillGoodsMapper;

    @Resource
    OrderMapper orderMapper;

    @Resource
    OrderItemMapper orderItemMapper;

    @Resource
    RedisTemplate redisTemplate;

    /**
     * 缓存秒杀订单
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
     * @param userId
     * @param seckillGoodsId
     * @param shippingId
     * @return
     */
    @Transactional
    public String insert(String userId, String seckillGoodsId, String shippingId) {
        SeckillGoods seckillGoods = seckillGoodsMapper.selectBySeckillGoodsId(seckillGoodsId);

        //生成订单
        Order order = new Order();
        order.setOrderId(String.valueOf(snowflakeIdWorker.nextId()));
        order.setUserId(userId);
        order.setOrderPayment(seckillGoods.getSeckillGoodsPrice());
        order.setOrderStatus(Order.UNPAID);
        order.setOrderCreateTime(new Date());
        order.setShippingId(shippingId);

        boolean success = orderMapper.insert(order) != 0;
        String versionKey = RedisKeyUtil.versionByUserId(order.getUserId());
        if (success) {
            redisTemplate.opsForValue().increment(versionKey);
        }

        String orderId = order.getOrderId();

        //生成订单明细
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(String.valueOf(snowflakeIdWorker.nextId()));
        orderItem.setOrderId(orderId);
        orderItem.setGoodsId(seckillGoodsId);
        orderItem.setGoodsName(seckillGoods.getSeckillGoodsName());
        orderItem.setGoodsImage(seckillGoods.getSeckillGoodsImage());
        orderItem.setGoodsPrice(seckillGoods.getSeckillGoodsPrice());
        orderItem.setGoodsCount(1);
        orderItem.setOrderItemSubtotal(seckillGoods.getSeckillGoodsPrice());
        orderItemMapper.insert(orderItem);

        //生成秒杀订单
        SeckillOrder seckillOrder = selectByUserIdGoodsId(userId, seckillGoodsId);
        seckillOrder.setOrderId(orderId);
        seckillOrderMapper.insert(seckillOrder);

        return orderId;
    }

    /**
     * 从缓存获取秒杀订单
     * @param userId
     * @param seckillGoodsId
     * @return
     */
    public SeckillOrder selectByUserIdGoodsId(String userId, String seckillGoodsId) {
        return (SeckillOrder) redisTemplate.opsForValue().get(RedisKeyUtil.seckillOrder(userId, seckillGoodsId));
    }
}
