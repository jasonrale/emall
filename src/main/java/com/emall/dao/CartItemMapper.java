package com.emall.dao;

import com.emall.entity.CartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车明细数据接口层
 */
public interface CartItemMapper {
    int deleteByCartItemId(String cartItemId);

    int insert(CartItem cartItem);

    CartItem selectByCartItemId(String cartItemId);

    Integer countByUserId(@Param("userId") String userId);

    List<CartItem> queryAllByUserId(@Param("userId") String userId);
}