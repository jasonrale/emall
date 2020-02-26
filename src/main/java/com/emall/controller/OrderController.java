package com.emall.controller;

import com.emall.entity.*;
import com.emall.result.Result;
import com.emall.service.*;
import com.emall.utils.LoginSession;
import com.emall.utils.PageModel;
import com.emall.utils.SnowflakeIdWorker;
import com.emall.vo.CartOrderSubmitVo;
import com.emall.vo.OrderManageVo;
import com.emall.vo.OrderSubmitVo;
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
    CartItemService cartItemService;

    @Resource
    LoginSession loginSession;

    @Resource
    SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 分页查询当前用户所有订单列表（包含订单明细）
     *
     * @param pageModel
     * @return
     */
    @GetMapping("/currentUser")
    @ResponseBody
    public Result<PageModel> queryCurrentUser(@Valid PageModel<OrderVo> pageModel) {
        logger.info("查询当前用户订单--第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");
        User user = loginSession.getCustomerSession();

        return Result.success("订单查询成功", orderService.queryCurrentUser(user.getUserId(), pageModel));
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
        logger.info("获取订单信息，订单号=" + orderId);
        Order order = orderService.selectByOrderId(orderId);
        if (order == null) {
            return Result.error("未查询到该订单");
        }

        List<OrderItem> orderItemList = orderItemService.selectByOrderId(orderId);

        String shippingId = order.getShippingId();
        Shipping shipping = shippingService.selectByShippingId(shippingId);
        OrderVo orderVo = new OrderVo(order.getOrderId(), order.getUserId(), order.getOrderPayment(), order.getOrderStatus(), order.getOrderCreateTime(),
                order.getOrderPaymentTime(), order.getOrderSendTime(), order.getOrderEndTime(), order.getShippingId(), shipping, orderItemList);

        return Result.success("查询订单详情成功", orderVo);
    }

    /**
     * 提交订单(立即购买)
     *
     * @param orderSubmitVo
     * @return
     */
    @PutMapping("/normal")
    @ResponseBody
    @Transactional
    public Result<String> normalSubmit(@RequestBody OrderSubmitVo orderSubmitVo) {
        User user = loginSession.getCustomerSession();
        String userId = user.getUserId();

        Goods goods = orderSubmitVo.getGoods();
        String shippingId = orderSubmitVo.getShippingId();
        Integer count = orderSubmitVo.getCount();
        String goodsId = goods.getGoodsId();

        //减库存
        boolean outOfStock = goodsService.reduceStock(goodsId, count);
        if (!outOfStock) {
            return Result.error("库存不足");
        }

        //生成订单
        Order order = new Order(String.valueOf(snowflakeIdWorker.nextId()), userId, goods.getGoodsPrice().multiply(new BigDecimal(count)),
                Order.UNPAID, new Date(), null, null, null, shippingId);

        orderService.insert(order);

        String orderId = order.getOrderId();

        //生成订单明细
        OrderItem orderItem = new OrderItem(String.valueOf(snowflakeIdWorker.nextId()), orderId, goodsId, goods.getGoodsName(),
                goods.getGoodsImage(), goods.getGoodsPrice(), count, order.getOrderPayment());

        orderItemService.insert(orderItem);


        return Result.success("订单已提交，快去看看吧", orderId);
    }

    /**
     * 提交订单(购物车)
     *
     * @param cartOrderSubmitVo
     * @return
     */
    @PutMapping("/cart")
    @ResponseBody
    @Transactional
    public Result<String> fromCartSubmit(@RequestBody CartOrderSubmitVo cartOrderSubmitVo) {
        User user = loginSession.getCustomerSession();
        String userId = user.getUserId();

        //减库存
        boolean success = true;
        List<CartItem> cartItemList = cartOrderSubmitVo.getCartItemList();
        for (CartItem cartItem : cartItemList) {
            String goodsId = cartItem.getGoodsId();
            Integer count = cartItem.getGoodsCount();

            if (!goodsService.reduceStock(goodsId, count)) {
                success = false;
                break;
            }
        }

        if (!success) {
            return Result.error("库存不足");
        }

        //生成订单
        Order order = new Order(String.valueOf(snowflakeIdWorker.nextId()), userId, cartOrderSubmitVo.getTotalPrice(), Order.UNPAID,
                new Date(), null, null, null, cartOrderSubmitVo.getShippingId());

        orderService.insert(order);

        String orderId = order.getOrderId();

        //生成订单明细并删除购物车明细
        for (CartItem cartItem : cartItemList) {
            String goodsId = cartItem.getGoodsId();
            Integer count = cartItem.getGoodsCount();

            OrderItem orderItem = new OrderItem(String.valueOf(snowflakeIdWorker.nextId()), orderId, goodsId, cartItem.getGoodsName(),
                    cartItem.getGoodsImage(), cartItem.getGoodsPrice(), count, cartItem.getCartItemSubtotal());

            orderItemService.insert(orderItem);
            cartItemService.deleteByCartItemId(cartItem.getCartItemId());
        }

        return Result.success("订单已提交，快去看看吧", orderId);
    }


    /**
     * 订单号验证
     * @param orderId
     * @return
     */
    @GetMapping("/valid/{orderId}/orderId")
    @ResponseBody
    public Result<String> orderIdValid(@PathVariable("orderId") String orderId) {
        User user = loginSession.getCustomerSession();

        return orderService.orderIdValid(user.getUserId(), orderId) ? Result.success("订单号验证成功", orderId) : Result.error("请求非法");
    }

    /**
     * 取消订单，恢复库存
     *
     * @param orderId
     * @return
     */
    @PostMapping("/cancel")
    @ResponseBody
    public Result<String> orderCancel(@RequestBody String orderId) {
        logger.info("取消订单，订单号=" + orderId);
        User user = loginSession.getCustomerSession();

        if (!orderService.orderIdValid(user.getUserId(), orderId)) {
            return Result.error("请求非法");
        }

        return orderService.orderCancel(orderId) ? Result.success("订单取消成功", orderId) : Result.error("订单取消失败");
    }

    /**
     * 订单支付,修改订单状态
     *
     * @param orderId
     * @return
     */
    @PostMapping("/pay/{orderId}/orderId")
    @ResponseBody
    public Result<String> pay(@PathVariable("orderId") String orderId) {
        logger.info("订单支付中，订单号=" + orderId);
        User user = loginSession.getCustomerSession();

        if (!orderService.orderIdValid(user.getUserId(), orderId)) {
            return Result.error("请求非法");
        }

        return orderService.pay(orderId) ? Result.success("订单支付成功", orderId) : Result.error("订单支付失败");
    }


    /**
     * 后台管理--根据查询类型查询所有订单列表
     *
     * @param pageModel
     * @return
     */
    @GetMapping("/{listType}/{param}")
    @ResponseBody
    public Result queryAllByUserId(@Valid PageModel<OrderManageVo> pageModel, @PathVariable("listType") String listType, @PathVariable("param") String param) {
        logger.info("查询订单--By " + listType + "--第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");

        switch (listType) {
            case "all":
                return Result.success("分页查询所有订单", orderService.queryAll(pageModel));
            case "orderId":
                return Result.success("根据订单id查询订单", orderService.queryByOrderId(param));
            case "userId":
                return Result.success("根据用户id分页查询订单", orderService.queryAllByUserId(param, pageModel));
            default:
                return Result.error("查询失败");
        }
    }

    /**
     * 订单发货,修改订单状态
     *
     * @param orderId
     * @return
     */
    @PostMapping("/send")
    @ResponseBody
    public Result<String> send(@RequestBody String orderId) {
        logger.info("订单发货，订单号=" + orderId);
        return orderService.send(orderId) ? Result.success("订单发货成功", orderId) : Result.error("订单发货失败");
    }

    /**
     * 确认收货,修改订单状态
     *
     * @param orderId
     * @return
     */
    @PostMapping("/received")
    @ResponseBody
    public Result<String> received(@RequestBody String orderId) {
        logger.info("确认收货，订单号=" + orderId);
        return orderService.received(orderId) ? Result.success("订单发货成功", orderId) : Result.error("订单发货失败");
    }
}
