package com.emall.service;

import com.emall.dao.OrderMapper;
import com.emall.entity.Order;
import com.emall.entity.OrderItem;
import com.emall.redis.RedisKeyUtil;
import com.emall.utils.PageModel;
import com.emall.vo.OrderManageVo;
import com.emall.vo.OrderVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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
    OrderItemService orderItemService;

    @Resource
    GoodsService goodsService;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    OrderMapper orderMapper;

    /**
     * 查询当前用户所有订单列表（包含订单明细）
     * @param userId
     * @param pageModel
     * @return
     */
    public PageModel<OrderVo> queryCurrentUser(String userId, PageModel<OrderVo> pageModel) {
        String listKey = RedisKeyUtil.orderVoByUserId(userId, pageModel.getCurrentNo(), pageModel.getPageSize());

        if (redisTemplate.hasKey(listKey)) {
            //从Redis缓存中获取列表中所有订单的键（排除订单总数量）
            List<String> orderVoKeyList = redisTemplate.opsForList().range(listKey, 0, -2);
            if (orderVoKeyList == null || orderVoKeyList.size() == 0) {
                return queryCurrentUserFromDB(userId, pageModel);
            }

            int count = (int) redisTemplate.opsForList().index(listKey, -1);
            return getFromRedis(count, orderVoKeyList, pageModel);
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
        String listKey = RedisKeyUtil.orderVoByUserId(userId, pageModel.getCurrentNo(), pageModel.getPageSize());

        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        int count = orderMapper.countByUserId(userId);
        List<OrderVo> orderVoList = orderMapper.queryCurrentUser(userId, limit, offset);

        return pageToRedis(listKey, orderVoList, count, pageModel);
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
     * @param listKey
     * @param orderVoList
     * @param count
     * @param pageModel
     * @return
     */
    private PageModel<OrderVo> pageToRedis(String listKey, List<OrderVo> orderVoList, int count, PageModel<OrderVo> pageModel) {
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
     * 生成订单
     * @param order
     * @return
     */
    public boolean insert(Order order) {
        return orderMapper.insert(order) != 0;
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
     * 取消订单，恢复库存
     * @param orderId
     * @return
     */
    @Transactional
    public boolean orderCancel(String orderId) {
        List<OrderItem> orderItemList = orderItemService.selectByOrderId(orderId);

        for (OrderItem orderItem : orderItemList) {
            goodsService.recoverStock(orderItem.getGoodsId(), orderItem.getGoodsCount());
        }

        boolean success = orderMapper.orderCancel(orderId) != 0;

        //订单管理业务缓存失效
        deleteOrderVoCache(orderId);

        return success;
    }

    /**
     * 订单支付,修改订单状态
     * @param orderId
     * @return
     */
    public boolean pay(String orderId) {
        boolean success = orderMapper.pay(orderId, new Date()) != 0;

        //订单管理业务缓存失效
        deleteOrderVoCache(orderId);

        return success;
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
    public PageModel<OrderManageVo> queryAllByUserId(String userId, PageModel<OrderManageVo> pageModel) {
        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        int count = orderMapper.countByUserId(userId);
        List<OrderManageVo> orderManageVoList = orderMapper.queryAllByUserId(userId, limit, offset);

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
     * 订单发货，修改订单状态
     * @param orderId
     * @return
     */
    public boolean send(String orderId) {
        boolean success = orderMapper.send(orderId, new Date()) != 0;

        //订单管理业务缓存失效
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

        //订单管理业务缓存失效
        deleteOrderVoCache(orderId);

        return success;
    }

    /**
     * 根据订单id删除订单管理业务缓存
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
