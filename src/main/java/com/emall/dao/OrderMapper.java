package com.emall.dao;

import com.emall.entity.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {

    int deleteByPrimaryKey(@Param("orderId") String orderId);

    int insert(Order order);

    int insertSelective(Order order);

    Order selectByOrderId(@Param("orderId") String orderId);

    int updateByPrimaryKeySelective(Order order);

    int updateByPrimaryKey(Order order);
}