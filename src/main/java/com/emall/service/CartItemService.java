package com.emall.service;

import com.emall.dao.CartItemMapper;
import com.emall.entity.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车明细业务层
 */
@Service
public class CartItemService {
    @Resource
    CartItemMapper cartItemMapper;

    /**
     * 根据用户id获取所有购物车明细
     * @param userId
     * @return
     */
    public List<CartItem> queryAllByUserId(String userId) {
        return cartItemMapper.queryAllByUserId(userId);
    }

    /**
     * 加入购物车
     * @param cartItem
     * @return
     */
    public boolean insert(CartItem cartItem) {
        return cartItemMapper.insert(cartItem) != 0;
    }

    /**
     * 根据用户id获取购物车明细数量
     * @param userId
     * @return
     */
    public Integer countByUserId(String userId) {
        return cartItemMapper.countByUserId(userId);
    }

    /**
     * 根据购物车明细id删除
     * @param cartItemId
     * @return
     */
    public boolean deleteByCartItemId(String cartItemId) {
        return cartItemMapper.deleteByCartItemId(cartItemId) != 0;
    }

    /**
     * 批量删除选中购物车明细
     * @param cartItemIdList
     * @return
     */
    @Transactional
    public boolean deleteSelect(List<String> cartItemIdList) {
        boolean success = true;
        for (String cartItemId : cartItemIdList) {
            if (cartItemMapper.deleteByCartItemId(cartItemId) == 0) {
                success = false;
                break;
            }
        }

        return success;
    }

    /**
     * 根据购物车明细id列表查询购物车明细
     * @param cartItemIdList
     * @return
     */
    public List<CartItem> selectByCartItemIdList(List<String> cartItemIdList) {
        List<CartItem> cartItemList = new ArrayList<>();

        for (String cartItemId : cartItemIdList) {
            cartItemList.add(cartItemMapper.selectByCartItemId(cartItemId));
        }

        return cartItemList;
    }
}
