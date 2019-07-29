package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 *   订单明细实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class OrderItem {
    private String oiId;              //订单明细id

    private String oId;              //订单id

    private String uId;              //订单用户id

    private String gId;              //订单商品id

    private String gName;         //订单商品名称

    private String gImage;         //订单商品图片地址

    private BigDecimal gPrice;     //订单商品价格

    private String gCount;        //订单商品数量

    private BigDecimal oiSubtotal; //订单明细小计
}