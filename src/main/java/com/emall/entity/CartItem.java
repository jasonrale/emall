package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 购物车明细实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class CartItem {
    private String cartItemId;              //购物车明细id

    private String userId;                  //购物车用户id

    private String goodsId;                 //购物车商品id

    private String goodsName;               //购物车商品名称

    private Integer goodsCount;              //购物车商品数量

    private BigDecimal goodsPrice;          //购物车商品价格

    private String goodsImage;              //购物车商品图片地址

    private BigDecimal cartItemSubtotal;    //购物车明细小计
}