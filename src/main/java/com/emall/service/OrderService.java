package com.emall.service;

import com.emall.dao.CartItemMapper;
import com.emall.dao.GoodsMapper;
import com.emall.dao.OrderItemMapper;
import com.emall.dao.OrderMapper;
import com.emall.entity.CartItem;
import com.emall.entity.Goods;
import com.emall.entity.Order;
import com.emall.entity.OrderItem;
import com.emall.redis.RedisKeyUtil;
import com.emall.utils.PageModel;
import com.emall.utils.SnowflakeIdWorker;
import com.emall.vo.CartOrderSubmitVo;
import com.emall.vo.OrderManageVo;
import com.emall.vo.OrderSubmitVo;
import com.emall.vo.OrderVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 订单业务层
 */
@Service
public class OrderService {
    @Resource
    OrderItemMapper orderItemMapper;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    OrderMapper orderMapper;

    @Resource
    GoodsMapper goodsMapper;

    @Resource
    CartItemMapper cartItemMapper;

    @Resource
    SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 查询当前用户所有订单列表（包含订单明细）
     * @param userId
     * @param pageModel
     * @return
     */
    public PageModel<OrderVo> queryCurrentUser(String userId, PageModel<OrderVo> pageModel) {
        String listKey = RedisKeyUtil.orderVoByUserId(userId, pageModel.getCurrentNo(), pageModel.getPageSize());
        String versionKey = RedisKeyUtil.versionByUserId(userId);

        if (redisTemplate.hasKey(listKey)) {
            int newVersion = (int) redisTemplate.opsForValue().get(versionKey);
            int oldVersion = (int) redisTemplate.opsForList().index(listKey, -1);
            //判断缓存版本是否改变
            if (newVersion == oldVersion) {
                List<String> orderVoKeyList = redisTemplate.opsForList().range(listKey, 0, -3);
                if (orderVoKeyList != null && orderVoKeyList.size() != 0) {
                    int count = (int) redisTemplate.opsForList().index(listKey, -2);
                    return getFromRedis(count, orderVoKeyList, pageModel);
                }
            }
        }

        return queryCurrentUserFromDB(userId, pageModel);
    }

    /**
     * 根据用户id从数据库查询订单业务对象
     *
     * @param userId
     * @param pageModel
     * @return
     */
    private PageModel<OrderVo> queryCurrentUserFromDB(String userId, PageModel<OrderVo> pageModel) {
        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        int count = orderMapper.countByUserId(userId);
        List<OrderVo> orderVoList = orderMapper.queryCurrentUser(userId, limit, offset);

        return pageToRedis(userId, orderVoList, count, pageModel);
    }

    /**
     * 从缓存获取分页数据
     *
     * @param count
     * @param orderVoKeyList
     * @param pageModel
     * @return
     */
    private PageModel<OrderVo> getFromRedis(int count, List<String> orderVoKeyList, PageModel<OrderVo> pageModel) {
        List<OrderVo> orderVoList = new ArrayList<>();

        for (String orderVoKey : orderVoKeyList) {
            if (redisTemplate.hasKey(orderVoKey)) {
                //通过键获取商品对象填充到页面list
                orderVoList.add((OrderVo) redisTemplate.opsForValue().get(orderVoKey));
            } else {
                String[] part = orderVoKey.split(":");
                orderVoList.add(orderMapper.queryByOrderId(part[1]));
            }

        }

        pageModel.setCount(count);
        pageModel.setList(orderVoList);
        pageModel.setTotalPages();

        return pageModel;
    }

    /**
     * 分页数据存入缓存
     *
     * @param userId
     * @param orderVoList
     * @param count
     * @param pageModel
     * @return
     */
    private PageModel<OrderVo> pageToRedis(String userId, List<OrderVo> orderVoList, int count, PageModel<OrderVo> pageModel) {
        String listKey = RedisKeyUtil.orderVoByUserId(userId, pageModel.getCurrentNo(), pageModel.getPageSize());

        //如果存在该商品列表缓存，则清空，重新缓存
        if (redisTemplate.hasKey(listKey)) {
            redisTemplate.delete(listKey);
        }

        for (OrderVo orderVo : orderVoList) {
            String key = RedisKeyUtil.orderVoById(orderVo);
            //将查询到的每个商品缓存到Redis
            redisTemplate.opsForValue().set(key, orderVo, 1800, TimeUnit.SECONDS);
            //将Redis缓存中每个商品的键存入list
            redisTemplate.opsForList().rightPush(listKey, key);
        }
        //将商品总数量追加到list末尾
        redisTemplate.opsForList().rightPush(listKey, count);

        //如果不存在缓存版本号则设置缓存版本
        String versionKey = RedisKeyUtil.versionByUserId(userId);
        if (!redisTemplate.hasKey(versionKey)) {
            redisTemplate.opsForValue().set(versionKey, 1);
        }
        //将缓存版本追加到list末尾
        redisTemplate.opsForList().rightPush(listKey, redisTemplate.opsForValue().get(versionKey));

        redisTemplate.expire(listKey, 1800, TimeUnit.SECONDS);

        pageModel.setCount(count);
        pageModel.setList(orderVoList);
        pageModel.setTotalPages();

        return pageModel;
    }

    /**
     * 根据订单id查询订单业务信息
     *
     * @param orderId
     * @return
     */
    public OrderVo queryByOrderId(String orderId) {
        String orderVoKey = RedisKeyUtil.ORDER_VO_PREFIX + orderId;

        if (redisTemplate.hasKey(orderVoKey)) {
            return (OrderVo) redisTemplate.opsForValue().get(orderVoKey);
        }

        return orderMapper.queryByOrderId(orderId);
    }

    /**
     * 减库存
     *
     * @param goodsId
     * @param count
     * @return
     */
    @Transactional
    public boolean reduceStock(String goodsId, Integer count) {
        //先减库存
        boolean success = orderMapper.reduceStock(goodsId, count) != 0;

        if (success) {
            String goodsKey = RedisKeyUtil.GOODS_PREFIX + goodsId;

            //缓存失效
            if (redisTemplate.hasKey(goodsKey)) {
                redisTemplate.delete(goodsKey);
            }
        }

        return success;
    }

    /**
     * 订单提交（立即购买）
     *
     * @param userId
     * @param orderSubmitVo
     * @return
     */
    @Transactional
    public String normalSubmit(String userId, OrderSubmitVo orderSubmitVo) {
        Goods goods = orderSubmitVo.getGoods();
        String shippingId = orderSubmitVo.getShippingId();
        Integer count = orderSubmitVo.getCount();
        String goodsId = goods.getGoodsId();


        //生成订单
        Order order = new Order(String.valueOf(snowflakeIdWorker.nextId()), userId, goods.getGoodsPrice().multiply(new BigDecimal(count)),
                Order.UNPAID, new Date(), null, null, null, shippingId);

        insert(order);

        String orderId = order.getOrderId();

        //生成订单明细
        OrderItem orderItem = new OrderItem(String.valueOf(snowflakeIdWorker.nextId()), orderId, goodsId, goods.getGoodsName(),
                goods.getGoodsImage(), goods.getGoodsPrice(), count, order.getOrderPayment());

        orderItemMapper.insert(orderItem);

        return orderId;
    }

    /**
     * 订单提交（购物车）
     *
     * @param userId
     * @param cartOrderSubmitVo
     * @return
     */
    @Transactional
    public String fromCartSubmit(String userId, CartOrderSubmitVo cartOrderSubmitVo) {
        List<CartItem> cartItemList = cartOrderSubmitVo.getCartItemList();

        //生成订单
        Order order = new Order(String.valueOf(snowflakeIdWorker.nextId()), userId, cartOrderSubmitVo.getTotalPrice(), Order.UNPAID,
                new Date(), null, null, null, cartOrderSubmitVo.getShippingId());

        insert(order);

        String orderId = order.getOrderId();

        //生成订单明细并删除购物车明细
        for (CartItem cartItem : cartItemList) {
            String goodsId = cartItem.getGoodsId();
            Integer count = cartItem.getGoodsCount();

            OrderItem orderItem = new OrderItem(String.valueOf(snowflakeIdWorker.nextId()), orderId, goodsId, cartItem.getGoodsName(),
                    cartItem.getGoodsImage(), cartItem.getGoodsPrice(), count, cartItem.getCartItemSubtotal());

            orderItemMapper.insert(orderItem);
            cartItemMapper.deleteByCartItemId(cartItem.getCartItemId());
        }

        return orderId;
    }

    /**
     * 生成订单
     * @param order
     * @return
     */
    public boolean insert(Order order) {
        boolean success = orderMapper.insert(order) != 0;

        //用户订单业务缓存版本
        String versionKey = RedisKeyUtil.versionByUserId(order.getUserId());
        if (success) {
            redisTemplate.opsForValue().increment(versionKey);
        }

        return success;
    }

    /**
     * 根据订单id查询订单
     * @param orderId
     * @return
     */
    public Order selectByOrderId(String orderId) {
        return orderMapper.selectByOrderId(orderId);
    }

    /**
     * 订单id验证
     * @param userId
     * @param orderId
     * @return
     */
    public boolean orderIdValid(String userId, String orderId) {
        Order order;
        return !StringUtils.isEmpty(orderId) && (order = selectByOrderId(orderId)) != null && userId.equals(order.getUserId());
    }

    /**
     * 查询所有订单
     * @param pageModel
     * @return
     */
    public PageModel<OrderManageVo> queryAll(PageModel<OrderManageVo> pageModel) {
        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        int count = orderMapper.count();
        List<OrderManageVo> orderList = orderMapper.queryAll(limit, offset);

        pageModel.setCount(count);
        pageModel.setList(orderList);
        pageModel.setTotalPages();

        return pageModel;
    }

    /**
     * 根据用户id分页查询订单
     * @param userId
     * @param pageModel
     * @return
     */
    public PageModel<OrderManageVo> queryByUserId(String userId, PageModel<OrderManageVo> pageModel) {
        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        int count = orderMapper.countByUserId(userId);
        List<OrderManageVo> orderManageVoList = orderMapper.queryByUserId(userId, limit, offset);

        pageModel.setCount(count);
        pageModel.setList(orderManageVoList);
        pageModel.setTotalPages();

        return pageModel;
    }

    /**
     * 根据订单id查询订单管理信息
     * @param orderId
     * @return
     */
    public OrderManageVo queryManageByOrderId(String orderId) {
        return orderMapper.queryManageByOrderId(orderId);
    }

    /**
     * 订单支付,修改订单状态
     *
     * @param orderId
     * @return
     */
    public boolean pay(String orderId) {
        boolean success = orderMapper.pay(orderId, new Date()) != 0;

        //订单业务缓存失效
        deleteOrderVoCache(orderId);

        return success;
    }

    /**
     * 订单发货，修改订单状态
     * @param orderId
     * @return
     */
    public boolean send(String orderId) {
        boolean success = orderMapper.send(orderId, new Date()) != 0;

        //订单业务缓存失效
        deleteOrderVoCache(orderId);

        return success;
    }

    /**
     * 确认收货,修改订单状态
     * @param orderId
     * @return
     */
    public boolean received(String orderId) {
        boolean success = orderMapper.received(orderId, new Date()) != 0;

        //订单业务缓存失效
        deleteOrderVoCache(orderId);

        return success;
    }

    /**
     * 取消订单，恢复库存
     *
     * @param orderId
     * @return
     */
    @Transactional
    public boolean cancel(String orderId) {
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderId(orderId);

        for (OrderItem orderItem : orderItemList) {

            //先减库存
            if (goodsMapper.recoverStock(orderItem.getGoodsId(), orderItem.getGoodsCount()) != 0) {
                //缓存失效
                String goodsKey = RedisKeyUtil.GOODS_PREFIX + orderItem.getGoodsId();

                //缓存失效
                if (redisTemplate.hasKey(goodsKey)) {
                    redisTemplate.delete(goodsKey);
                }
            } else {
                return false;
            }
        }

        boolean success = orderMapper.cancel(orderId) != 0;

        //订单业务缓存失效
        deleteOrderVoCache(orderId);

        return success;
    }

    /**
     * 根据订单id删除订单业务缓存
     *
     * @param orderId
     */
    public void deleteOrderVoCache(String orderId) {
        String orderVoKey = RedisKeyUtil.ORDER_VO_PREFIX + orderId;

        if (redisTemplate.hasKey(orderVoKey)) {
            redisTemplate.delete(orderVoKey);
        }
    }
}
