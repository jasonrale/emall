package com.jason.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 *   购物车实体类
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class Cart {
    private Integer ctId;           //购物车id

    private Integer uId;            //用户id

    private Integer gId;            //商品id

    private String gCount;          //商品数量

    private BigDecimal gPrice;      //商品价格

    private String gImage;          //商品图片地址

    private BigDecimal ctSubtotal;  //购物车小计
}