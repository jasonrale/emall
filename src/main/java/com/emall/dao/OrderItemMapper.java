package com.emall.dao;

import com.emall.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单明细数据接口层
 */
public interface OrderItemMapper {
    int insert(OrderItem orderItem);

    List<OrderItem> selectByOrderId(@Param("orderId") String orderId);
}