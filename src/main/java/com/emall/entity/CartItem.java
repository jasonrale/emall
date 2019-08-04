package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 *   购物车明细实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class CartItem {
    private String cartItemId;              //购物车明细id

    private String userId;                  //用户id

    private String goodsId;                 //商品id

    private String goodsCount;              //商品数量

    private BigDecimal goodsPrice;          //商品价格

    private String goodsImage;              //商品图片地址

    private BigDecimal cartItemSubtotal;    //购物车明细小计
}