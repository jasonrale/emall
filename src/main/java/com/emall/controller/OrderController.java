package com.emall.controller;

import com.emall.entity.*;
import com.emall.result.Result;
import com.emall.service.GoodsService;
import com.emall.service.OrderItemService;
import com.emall.service.OrderService;
import com.emall.service.ShippingService;
import com.emall.utils.LoginSession;
import com.emall.utils.PageModel;
import com.emall.utils.SnowFlakeConfig;
import com.emall.vo.OrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Resource
    OrderService orderService;

    @Resource
    OrderItemService orderItemService;

    @Resource
    GoodsService goodsService;

    @Resource
    ShippingService shippingService;

    @Resource
    LoginSession loginSession;

    @Resource
    SnowFlakeConfig.SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 根据用户id分页查询所有订单列表
     *
     * @param pageModel
     * @return
     */
    @GetMapping("")
    @ResponseBody
    public Result<PageModel> queryAll(@Valid PageModel<OrderVo> pageModel) {
        logger.info("根据用户id查询商品--第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");
        User user = loginSession.getUserSession();

        return Result.success("订单号验证成功", orderService.queryAll(user.getUserId(), pageModel));
    }

    /**
     * 获取订单信息
     *
     * @param orderId
     * @return
     */
    @GetMapping("/{orderId}/orderId")
    @ResponseBody
    public Result<OrderVo> selectByOrderId(@PathVariable("orderId") String orderId) {
        User user = loginSession.getUserSession();
        Order order = orderService.selectByOrderId(orderId);
        if (order == null) {
            return Result.error("未查询到该订单");
        }

        if (!orderService.orderIdValid(user.getUserId(), orderId)) {
            return Result.error("请求非法");
        }

        List<OrderItem> orderItemList = orderItemService.selectByOrderId(orderId);

        String shippingId = order.getShippingId();
        Shipping shipping = shippingService.selectByShippingId(shippingId);

        return Result.success("查询订单详情成功", new OrderVo(order, shipping, orderItemList));
    }

    /**
     * 提交订单(立即购买)
     *
     * @param goodsId
     * @param shippingId
     * @param count
     * @return
     */
    @PutMapping("/normal")
    @ResponseBody
    @Transactional
    public Result<String> normalSubmit(@RequestParam("goodsId") String goodsId, @RequestParam("shippingId") String shippingId, @RequestParam("count") Integer count) {
        User user = loginSession.getUserSession();
        String userId = user.getUserId();

        Goods goods = goodsService.selectByGoodsId(goodsId);

        //生成订单
        Order order = new Order();
        order.setOrderId(String.valueOf(snowflakeIdWorker.nextId()));
        order.setUserId(userId);
        order.setOrderPayment(goods.getGoodsPrice().multiply(new BigDecimal(count)));
        order.setOrderStatus(Order.UNPAID);
        order.setOrderCreateTime(new Date());
        order.setShippingId(shippingId);
        orderService.insert(order);

        String orderId = order.getOrderId();

        //生成订单明细
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(String.valueOf(snowflakeIdWorker.nextId()));
        orderItem.setOrderId(orderId);
        orderItem.setGoodsId(goodsId);
        orderItem.setGoodsName(goods.getGoodsName());
        orderItem.setGoodsImage(goods.getGoodsImage());
        orderItem.setGoodsPrice(goods.getGoodsPrice());
        orderItem.setGoodsCount(count);
        orderItem.setOrderItemSubtotal(order.getOrderPayment());
        orderItemService.insert(orderItem);

        //减库存
        goodsService.reduceStock(goodsId, count);

        return goodsService.reduceStock(goodsId, count) ? Result.success("订单已提交，快去看看吧", orderId) : Result.success("订单提交失败", orderId);
    }

    /**
     * 订单号验证
     * @param orderId
     * @return
     */
    @GetMapping("/valid/{orderId}/orderId")
    @ResponseBody
    public Result<String> orderIdValid(@PathVariable("orderId") String orderId) {
        User user = loginSession.getUserSession();

        return orderService.orderIdValid(user.getUserId(), orderId) ? Result.success("订单号验证成功", orderId) : Result.error("请求非法");
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    @PostMapping("/cancel")
    @ResponseBody
    public Result<String> orderCancel(@RequestBody String orderId) {
        User user = loginSession.getUserSession();

        if (!orderService.orderIdValid(user.getUserId(), orderId)) {
            return Result.error("请求非法");
        }

        return orderService.orderCancel(orderId) ? Result.success("订单取消成功", orderId) : Result.error("订单取消失败");
    }
}
