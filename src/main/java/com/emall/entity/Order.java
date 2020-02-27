package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

/**
 *   订单实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class Order {
    public static Integer TOBESHIPPED = 0;  //待发货

    public static Integer UNPAID = 1;       //未支付

    public static Integer TOBERECEVIED = 2; //待收货

    public static Integer COMPLETED = 3;    //已完成

    public static Integer CANCELED = 4;    //已取消

    private String orderId;                 //订单id

    private String userId;                  //订单用户id

    private BigDecimal orderPayment;        //订单总付款

    private Integer orderStatus;            //订单状态

    private Date orderCreateTime;           //订单创建时间

    private Date orderPaymentTime;          //订单支付时间

    private Date orderSendTime;             //订单商品发货时间

    private Date orderEndTime;              //订单完成时间

    private String shippingId;              //订单收货地址
}