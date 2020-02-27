package com.emall.vo;

import com.emall.entity.Shipping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单管理业务对象类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderManageVo {
    private String orderId;                 //订单id

    private String userId;                  //订单用户id

    private BigDecimal orderPayment;        //订单总付款

    private Integer orderStatus;            //订单状态

    private Date orderCreateTime;           //订单创建时间

    private Date orderPaymentTime;          //订单支付时间

    private Date orderSendTime;             //订单商品发货时间

    private Date orderEndTime;              //订单完成时间

    private Shipping shipping;              //订单收货地址
}
