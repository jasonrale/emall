package com.emall.dao;

import com.emall.entity.Shipping;

public interface ShippingMapper {
    int deleteByPrimaryKey(String shippingId);

    int insert(Shipping record);

    Shipping selectByPrimaryKey(String shippingId);

    int updateByPrimaryKey(Shipping record);
}