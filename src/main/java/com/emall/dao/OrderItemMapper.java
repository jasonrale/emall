package com.emall.dao;

import com.emall.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int insert(OrderItem orderItem);

    List<OrderItem> selectByOrderId(@Param("orderId") String orderId);
}