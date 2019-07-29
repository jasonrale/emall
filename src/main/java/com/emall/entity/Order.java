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
    private String oId;           //订单id

    private String uId;           //订单用户id

    private String oPayment;   //订单总付款

    private String oStatus;     //订单状态--未支付 ：0，待发货 ：1，待收货 ：2，已完成 ：3

    private Date oCreateTime;  //订单创建时间

    private Date oPaymentTime;//订单支付时间

    private Date oSendTime;    //订单商品发货时间

    private Date oEndTime;     //订单完成时间
}