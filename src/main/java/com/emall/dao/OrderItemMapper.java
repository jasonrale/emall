package com.emall.dao;

import com.emall.entity.OrderItem;

public interface OrderItemMapper {

    int deleteByPrimaryKey(String oiId);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(String oiId);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

}