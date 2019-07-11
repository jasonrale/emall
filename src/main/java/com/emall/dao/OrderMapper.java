package com.emall.dao;

import com.emall.entity.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Long oId);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long oId);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}