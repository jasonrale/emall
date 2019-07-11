package com.emall.dao;

import com.emall.entity.Shipping;

public interface ShippingMapper {
    int deleteByPrimaryKey(Long sId);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Long sId);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
}