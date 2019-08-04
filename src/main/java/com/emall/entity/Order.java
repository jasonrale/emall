package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 *   订单实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class Order {
    private String orderId;         //订单id

    private String userId;          //订单用户id

    private String orderPayment;    //订单总付款

    private String orderStatus;     //订单状态--未支付 ：0，待发货 ：1，待收货 ：2，已完成 ：3

    private Date orderCreateTime;   //订单创建时间

    private Date orderPaymentTime;  //订单支付时间

    private Date orderSendTime;     //订单商品发货时间

    private Date orderEndTime;      //订单完成时间
}