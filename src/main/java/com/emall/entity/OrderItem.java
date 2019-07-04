package com.emall.entity;

import lombok.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 *   订单明细实体类
 */
@Data
@Repository
public class OrderItem {
    private Integer oiId;           //订单明细id

    private Integer oId;            //订单id

    private Integer uId;            //订单用户id

    private Integer gId;            //订单商品id

    private String gName;           //订单商品名称

    private String gImage;          //订单商品图片地址

    private BigDecimal gPrice;      //订单商品价格

    private String gCount;          //订单商品数量

    private BigDecimal oiSubtotal;  //订单明细小计
}