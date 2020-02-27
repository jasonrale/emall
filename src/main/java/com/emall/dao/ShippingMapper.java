package com.emall.dao;

import com.emall.entity.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收货地址数据接口层
 */
public interface ShippingMapper {
    Shipping selectByShippingId(@Param("shippingId") String shippingId);

    int deleteByShippingId(@Param("shippingId") String shippingId);

    int insert(Shipping shipping);

    int update(Shipping shipping);

    List<Shipping> queryAll(@Param("userId") String userId);

    int count(@Param("userId") String userId);
}