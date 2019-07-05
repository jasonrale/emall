package com.emall.dao;

import com.emall.entity.CartItem;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemMapper {
    int deleteByPrimaryKey(Integer ciId);

    int insert(CartItem record);

    int insertSelective(CartItem record);

    CartItem selectByPrimaryKey(Integer ciId);

    int updateByPrimaryKeySelective(CartItem record);

    int updateByPrimaryKey(CartItem record);
}