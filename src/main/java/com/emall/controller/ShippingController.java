package com.emall.controller;

import com.emall.entity.Shipping;
import com.emall.result.Result;
import com.emall.service.ShippingService;
import com.emall.utils.LoginSession;
import com.emall.utils.SnowFlakeConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/shipping")
public class ShippingController {
    @Resource
    ShippingService shippingService;

    @Resource
    LoginSession loginSession;

    @Resource
    SnowFlakeConfig.SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 查询所有收货地址
     *
     * @return
     */
    @GetMapping("/all")
    @ResponseBody
    public Result<List> queryAll() {
        return Result.success("查询所有收货地址成功", shippingService.queryAll(loginSession.getUserSession().getUserId()));
    }

    @PutMapping("")
    @ResponseBody
    public Result insert(@Valid @RequestBody Shipping shipping) {
        shipping.setShippingId(String.valueOf(snowflakeIdWorker.nextId()));
        shipping.setUserId(loginSession.getUserSession().getUserId());

        return shippingService.insert(shipping) ? Result.success("新建收货地址成功", null) :
                Result.error("新建收货地址失败");
    }

    @PostMapping("")
    @ResponseBody
    public Result update(@Valid @RequestBody Shipping shipping) {
        return shippingService.update(shipping) ? Result.success("修改收货地址成功", null) :
                Result.error("修改收货地址失败");
    }
}
