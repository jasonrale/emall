package com.emall.dao;

import com.emall.entity.Order;
import com.emall.vo.OrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int count();

    List<OrderVo> queryAll(@Param("userId") String userId, @Param("limit") long limit, @Param("offset") long offset);

    int insert(Order order);

    Order selectByOrderId(@Param("orderId") String orderId);

    int orderCancel(@Param("orderId") String orderId);
}