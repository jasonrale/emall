package com.emall.vo;

import com.emall.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车订单提交业务对象类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartOrderSubmitVo {
    private List<CartItem> cartItemList;        //购物车明细列表

    private String shippingId;                  //收货地址id

    private BigDecimal totalPrice;              //总价
}
