package com.emall.dao;

import com.emall.entity.CartItem;

public interface CartItemMapper {
    int deleteByPrimaryKey(Integer ciId);

    int insert(CartItem record);

    int insertSelective(CartItem record);

    CartItem selectByPrimaryKey(Integer ciId);

    int updateByPrimaryKeySelective(CartItem record);

    int updateByPrimaryKey(CartItem record);
}