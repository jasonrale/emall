package com.emall.dao;

import com.emall.entity.Order;
import com.emall.vo.OrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {

    int count();

    List<OrderVo> queryAll(@Param("userId") String userId, @Param("limit") long limit, @Param("offset") long offset);

    int deleteByPrimaryKey(@Param("orderId") String orderId);

    int insert(Order order);

    int insertSelective(Order order);

    Order selectByOrderId(@Param("orderId") String orderId);

    int updateByPrimaryKeySelective(Order order);

    int updateByPrimaryKey(Order order);

    int orderCancel(@Param("orderId") String orderId);
}