package com.emall.dao;

import com.emall.entity.OrderItem;

public interface OrderItemMapper {
    int deleteByPrimaryKey(String orderItemId);

    int insert(OrderItem record);

    OrderItem selectByPrimaryKey(String orderItemId);

    int updateByPrimaryKey(OrderItem record);
}