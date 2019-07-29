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
    private String ciId;              //购物车明细id

    private String uId;               //用户id

    private String gId;               //商品id

    private String gCount;          //商品数量

    private BigDecimal gPrice;      //商品价格

    private String gImage;          //商品图片地址

    private BigDecimal ciSubtotal;  //购物车明细小计
}