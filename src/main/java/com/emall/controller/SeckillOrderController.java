package com.emall.controller;

import com.emall.entity.*;
import com.emall.result.Result;
import com.emall.service.*;
import com.emall.utils.LoginSession;
import com.emall.utils.SnowflakeIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 秒杀订单控制层
 */
@Controller
@RequestMapping("seckillOrder")
public class SeckillOrderController {
    private static final Logger logger = LoggerFactory.getLogger(SeckillOrderController.class);

    @Resource
    LoginSession loginSession;

    @Resource
    SeckillService seckillService;

    @Resource
    SeckillGoodsService seckillGoodsService;

    @Resource
    SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    OrderService orderService;

    @Resource
    OrderItemService orderItemService;

    @Resource
    SeckillOrderService seckillOrderService;

    /**
     * 生成完整秒杀订单
     * @param seckillGoodsId
     * @param shippingId
     * @param path
     * @return
     */
    @PutMapping("")
    @ResponseBody
    @Transactional
    public Result<String> insert(@RequestParam("seckillGoodsId") String seckillGoodsId, @RequestParam("shippingId") String shippingId, @RequestParam("path") String path) {
        logger.info("生成完整秒杀订单");

        User user = loginSession.getCustomerSession();
        String userId = user.getUserId();

        boolean valid = seckillService.pathValid(user, seckillGoodsId, path);
        if (!valid) {
            return Result.error("请求非法");
        }

        SeckillGoods seckillGoods = seckillGoodsService.selectBySeckillGoodsId(seckillGoodsId);

        //生成订单
        Order order = new Order();
        order.setOrderId(String.valueOf(snowflakeIdWorker.nextId()));
        order.setUserId(userId);
        order.setOrderPayment(seckillGoods.getSeckillGoodsPrice());
        order.setOrderStatus(Order.UNPAID);
        order.setOrderCreateTime(new Date());
        order.setShippingId(shippingId);
        orderService.insert(order);

        String orderId = order.getOrderId();

        //生成订单明细
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(String.valueOf(snowflakeIdWorker.nextId()));
        orderItem.setOrderId(orderId);
        orderItem.setGoodsId(seckillGoodsId);
        orderItem.setGoodsName(seckillGoods.getSeckillGoodsName());
        orderItem.setGoodsImage(seckillGoods.getSeckillGoodsImage());
        orderItem.setGoodsPrice(seckillGoods.getSeckillGoodsPrice());
        orderItem.setGoodsCount(1);
        orderItem.setOrderItemSubtotal(seckillGoods.getSeckillGoodsPrice());
        orderItemService.insert(orderItem);

        //生成秒杀订单
        SeckillOrder seckillOrder = seckillOrderService.selectByUserIdGoodsId(userId, seckillGoodsId);
        seckillOrder.setOrderId(orderId);
        seckillOrderService.insert(seckillOrder);

        return Result.success("订单已提交，快去看看吧", orderId);
    }
}
