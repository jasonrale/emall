package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 *   购物车明细实体类
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Repository
public class CartItem {
    private Integer ciId;           //购物车明细id

    private Integer uId;            //购物车用户id

    private Integer gId;            //购物车商品id

    private String gCount;          //购物车商品数量

    private BigDecimal gPrice;      //购物车商品价格

    private String gImage;          //购物车商品图片地址

    private BigDecimal ciSubtotal;  //购物车明细小计
}