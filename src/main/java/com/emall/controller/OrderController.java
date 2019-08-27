package com.emall.controller;

import com.emall.entity.Order;
import com.emall.entity.OrderItem;
import com.emall.entity.Shipping;
import com.emall.entity.User;
import com.emall.result.Result;
import com.emall.service.OrderItemService;
import com.emall.service.OrderService;
import com.emall.service.ShippingService;
import com.emall.utils.LoginSession;
import com.emall.vo.OrderVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Resource
    OrderService orderService;

    @Resource
    OrderItemService orderItemService;

    @Resource
    ShippingService shippingService;

    @Resource
    LoginSession loginSession;

    /**
     * 获取订单信息
     *
     * @param orderId
     * @return
     */
    @GetMapping("/{orderId}/orderId")
    public Result<OrderVo> selectByOrderId(@PathVariable("orderId") String orderId) {
        User user = loginSession.getUserSession();
        Order order = orderService.selectByOrderId(orderId);
        if (order == null) {
            return Result.error("未查询到该订单");
        }

        if (!order.getUserId().equals(user.getUserId())) {
            return Result.error("请求非法");
        }

        List<OrderItem> orderItemList = orderItemService.selectByOrderId(orderId);

        String shippingId = order.getShippingId();
        Shipping shipping = shippingService.selectByShippingId(shippingId);

        return Result.success("查询订单详情成功", new OrderVo(order, orderItemList, shipping));
    }

    @GetMapping("/valid/{orderId}/orderId")
    public Result<String> orderIdValid(@PathVariable("orderId") String orderId) {
        User user = loginSession.getUserSession();
        Order order = orderService.selectByOrderId(orderId);

        if (order == null || !order.getUserId().equals(user.getUserId())) {
            return Result.error("请求非法");
        }

        return Result.success("订单号验证成功", orderId);
    }
}
