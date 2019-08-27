package com.emall.vo;

import com.emall.entity.Order;
import com.emall.entity.OrderItem;
import com.emall.entity.Shipping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 订单业务对象类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo {
    private Order order;                    //订单

    private Shipping shipping;              //订单收货地址

    private List<OrderItem> orderItemList;  //订单明细列表
}
