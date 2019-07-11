package com.emall.dao;

import com.emall.entity.OrderItem;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Long oiId);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Long oiId);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
}