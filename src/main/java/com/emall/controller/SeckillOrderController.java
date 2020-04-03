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
    SeckillOrderService seckillOrderService;

    /**
     * 生成完整秒杀订单存入数据库
     * @param seckillGoodsId
     * @param shippingId
     * @param path
     * @return
     */
    @PutMapping("")
    @ResponseBody
    @Transactional
    public Result<String> insert(@RequestParam("seckillGoodsId") String seckillGoodsId, @RequestParam("shippingId") String shippingId, @RequestParam("path") String path) {
        logger.info("生成完整秒杀订单存入数据库");

        User user = loginSession.getCustomerSession();

        boolean valid = seckillService.pathValid(user, seckillGoodsId, path);
        if (!valid) {
            return Result.error("请求非法");
        }

        String orderId = seckillOrderService.insert(user.getUserId(), seckillGoodsId, shippingId);

        return Result.success("订单已提交，快去看看吧", orderId);
    }
}
