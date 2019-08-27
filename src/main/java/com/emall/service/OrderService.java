package com.emall.service;

import com.emall.dao.OrderMapper;
import com.emall.entity.Order;
import com.emall.utils.PageModel;
import com.emall.vo.OrderVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Service
public class OrderService {
    @Resource
    OrderMapper orderMapper;

    /**
     * 根据用户id分页查询所有订单列表
     *
     * @param pageModel
     * @return
     */
    public PageModel<OrderVo> queryAll(String userId, PageModel<OrderVo> pageModel) {
        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        int count = orderMapper.count();
        List<OrderVo> orderVoList = orderMapper.queryAll(userId, limit, offset);

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
     * 取消订单
     *
     * @param orderId
     * @return
     */
    public boolean orderCancel(String orderId) {
        return orderMapper.orderCancel(orderId) != 0;
    }
}
