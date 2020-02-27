package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 订单明细实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class OrderItem {
    private String orderItemId;             //订单明细id

    private String orderId;                 //订单id

    private String goodsId;                 //订单商品id

    private String goodsName;               //订单商品名称

    private String goodsImage;              //订单商品图片地址

    private BigDecimal goodsPrice;          //订单商品价格

    private Integer goodsCount;             //订单商品数量

    private BigDecimal orderItemSubtotal;   //订单明细小计
}