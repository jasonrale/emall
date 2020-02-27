package com.emall.service;

import com.emall.dao.OrderItemMapper;
import com.emall.entity.OrderItem;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单明细业务层
 */
@Service
public class OrderItemService {
    @Resource
    OrderItemMapper orderItemMapper;

    /**
     * 生成订单明细
     * @param orderItem
     * @return
     */
    public boolean insert(OrderItem orderItem) {
        return orderItemMapper.insert(orderItem) != 0;
    }

    /**
     * 根据订单id查询订单明细列表
     * @param orderId
     * @return
     */
    public List<OrderItem> selectByOrderId(String orderId) {
        return orderItemMapper.selectByOrderId(orderId);
    }
}
