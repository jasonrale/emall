package com.emall.dao;

import com.emall.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(@Param("orderItemId") String orderItemId);

    int insert(OrderItem orderItem);

    OrderItem selectByOrderItemId(@Param("orderItemId") String orderItemId);

    int updateByPrimaryKey(OrderItem orderItem);

    List<OrderItem> selectByOrderId(@Param("orderId") String orderId);
}