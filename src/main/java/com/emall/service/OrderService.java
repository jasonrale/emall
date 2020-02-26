package com.emall.service;

import com.emall.dao.OrderMapper;
import com.emall.entity.Order;
import com.emall.entity.OrderItem;
import com.emall.utils.PageModel;
import com.emall.vo.OrderManageVo;
import com.emall.vo.OrderVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Resource
    OrderItemService orderItemService;

    @Resource
    GoodsService goodsService;

    @Resource
    OrderMapper orderMapper;

    /**
     * 查询当前用户所有订单列表（包含订单明细）
     *
     * @param pageModel
     * @return
     */
    public PageModel<OrderVo> queryCurrentUser(String userId, PageModel<OrderVo> pageModel) {
        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        int count = orderMapper.countByUserId(userId);
        List<OrderVo> orderVoList = orderMapper.queryCurrentUser(userId, limit, offset);

        pageModel.setCount(count);
        pageModel.setList(orderVoList);
        pageModel.setTotalPages();

        return pageModel;
    }

    /**
     * 生成订单
     *
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
     *
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
     *
     * @param orderId
     * @return
     */
    @Transactional
    public boolean orderCancel(String orderId) {
        List<OrderItem> orderItemList = orderItemService.selectByOrderId(orderId);

        for (OrderItem orderItem : orderItemList) {
            goodsService.recoverStock(orderItem.getGoodsId(), orderItem.getGoodsCount());
        }

        return orderMapper.orderCancel(orderId) != 0;
    }

    /**
     * 订单支付,修改订单状态
     *
     * @param orderId
     * @return
     */
    public boolean pay(String orderId) {
//        Date currentDate = new Date(System.currentTimeMillis());
        return orderMapper.pay(orderId, new Date()) != 0;
    }

    /**
     * 查询所有订单
     *
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
     *
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
     * 根据订单id查询订单
     *
     * @param orderId
     * @return
     */
    public OrderManageVo queryByOrderId(String orderId) {
        return orderMapper.queryByOrderId(orderId);
    }

    /**
     * 订单发货，修改订单状态
     *
     * @param orderId
     * @return
     */
    public boolean send(String orderId) {
//        Date currentDate = new Date(System.currentTimeMillis());
        return orderMapper.send(orderId, new Date()) != 0;
    }

    /**
     * 确认收货,修改订单状态
     *
     * @param orderId
     * @return
     */
    public boolean received(String orderId) {
//        Date currentDate = new Date(System.currentTimeMillis());
        return orderMapper.received(orderId, new Date()) != 0;
    }
}
