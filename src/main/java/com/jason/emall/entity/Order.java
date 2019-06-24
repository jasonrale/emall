package com.jason.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 *   订单实体类
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class Order {
    private Integer oId;            //订单id

    private Integer uId;            //订单用户id

    private String oPayment;        //订单总付款

    private String oStatus;         //订单状态

    private Date oCreateTime;       //订单创建时间

    private Date oPaymentTime;      //订单支付时间

    private Date oSendTime;         //订单商品发货时间

    private Date oEndTime;          //订单完成时间
}