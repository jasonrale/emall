package com.emall.dao;

import com.emall.entity.Order;
import com.emall.vo.OrderManageVo;
import com.emall.vo.OrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 订单数据接口层
 */
public interface OrderMapper {
    int count();

    int countByUserId(@Param("userId") String userId);

    List<OrderVo> queryCurrentUser(@Param("userId") String userId, @Param("limit") long limit, @Param("offset") long offset);

    OrderVo queryByOrderId(@Param("orderId") String orderId);

    int reduceStock(@Param("goodsId") String goodsId, @Param("count") Integer count);

    int insert(Order order);

    Order selectByOrderId(@Param("orderId") String orderId);

    List<OrderManageVo> queryAll(@Param("limit") long limit, @Param("offset") long offset);

    List<OrderManageVo> queryByUserId(@Param("userId") String userId, @Param("limit") long limit, @Param("offset") long offset);

    OrderManageVo queryManageByOrderId(@Param("orderId") String orderId);

    int pay(@Param("orderId") String orderId, Date currentDate);

    int send(@Param("orderId") String orderId, Date currentDate);

    int received(@Param("orderId") String orderId, Date currentDate);

    int cancel(@Param("orderId") String orderId);
}