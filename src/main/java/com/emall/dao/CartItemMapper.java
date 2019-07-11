package com.emall.dao;

import com.emall.entity.CartItem;

public interface CartItemMapper {
    int deleteByPrimaryKey(Long ciId);

    int insert(CartItem record);

    int insertSelective(CartItem record);

    CartItem selectByPrimaryKey(Long ciId);

    int updateByPrimaryKeySelective(CartItem record);

    int updateByPrimaryKey(CartItem record);
}