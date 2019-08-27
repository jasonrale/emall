package com.emall.dao;

import com.emall.entity.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    Shipping selectByShippingId(@Param("shippingId") String shippingId);

    int deleteByShippingId(@Param("shippingId") String shippingId);

    int insert(Shipping shipping);

    int update(Shipping shipping);

    List<Shipping> queryAll(@Param("userId") String userId);
}