package com.emall.service;

import com.emall.dao.OrderMapper;
import com.emall.entity.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderService {
    @Resource
    OrderMapper orderMapper;

    /**
     * 生成订单
     *
     * @param order
     * @return
     */
    public boolean insert(Order order) {
        return orderMapper.insert(order) != 0;
    }

    public Order selectByOrderId(String orderId) {
        return orderMapper.selectByOrderId(orderId);
    }
}
