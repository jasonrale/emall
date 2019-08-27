package com.emall.controller;

import com.emall.entity.*;
import com.emall.result.Result;
import com.emall.service.OrderItemService;
import com.emall.service.OrderService;
import com.emall.service.ShippingService;
import com.emall.utils.LoginSession;
import com.emall.utils.PageModel;
import com.emall.vo.OrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    ShippingService shippingService;

    @Resource
    LoginSession loginSession;

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
