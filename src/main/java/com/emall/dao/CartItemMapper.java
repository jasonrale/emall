package com.emall.dao;

import com.emall.entity.CartItem;

public interface CartItemMapper {
    int deleteByPrimaryKey(String cartItemId);

    int insert(CartItem record);

    CartItem selectByPrimaryKey(String cartItemId);

    int updateByPrimaryKey(CartItem record);
}