package com.emall.dao;

import com.emall.entity.CartItem;

public interface CartItemMapper {

    int deleteByPrimaryKey(String ciId);

    int insert(CartItem record);

    int insertSelective(CartItem record);

    CartItem selectByPrimaryKey(String ciId);

    int updateByPrimaryKeySelective(CartItem record);

    int updateByPrimaryKey(CartItem record);

}