package com.emall.dao;

import com.emall.entity.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(Order record);

    Order selectByPrimaryKey(String orderId);

    int updateByPrimaryKey(Order record);
}