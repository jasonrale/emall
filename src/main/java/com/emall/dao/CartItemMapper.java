package com.emall.dao;

import com.emall.entity.CartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartItemMapper {
    int deleteByCartItemId(String cartItemId);

    int insert(CartItem cartItem);

    CartItem selectByCartItemId(String cartItemId);

    Integer countByUserId(@Param("userId") String userId);

    List<CartItem> queryAllByUserId(@Param("userId") String userId);
}