package com.emall.controller;

import com.emall.entity.Shipping;
import com.emall.result.Result;
import com.emall.service.ShippingService;
import com.emall.utils.LoginSession;
import com.emall.utils.SnowflakeIdWorker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 收货地址控制层
 */
@Controller
@RequestMapping("/shipping")
public class ShippingController {
    @Resource
    ShippingService shippingService;

    @Resource
    LoginSession loginSession;

    @Resource
    SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 查询所有收货地址
     * @return
     */
    @GetMapping("/all")
    @ResponseBody
    public Result<List> queryAll() {
        return Result.success("查询所有收货地址成功", shippingService.queryAll(loginSession.getCustomerSession().getUserId()));
    }

    /**
     * 新建收货地址
     * @param shipping
     * @return
     */
    @PutMapping("")
    @ResponseBody
    public Result insert(@Valid @RequestBody Shipping shipping) {
        String userId = loginSession.getCustomerSession().getUserId();

        if (shippingService.count(userId) >= Shipping.MAXCOUNT) {
            return Result.error("收货地址数量已达上限");
        }

        shipping.setShippingId(String.valueOf(snowflakeIdWorker.nextId()));
        shipping.setUserId(userId);

        return shippingService.insert(shipping) ? Result.success("新建收货地址成功", null) : Result.error("新建收货地址失败");
    }

    /**
     * 修改收货地址信息
     * @param shipping
     * @return
     */
    @PostMapping("")
    @ResponseBody
    public Result update(@Valid @RequestBody Shipping shipping) {
        return shippingService.update(shipping) ? Result.success("修改收货地址成功", null) :
                Result.error("修改收货地址失败");
    }

    /**
     * 删除收货地址
     * @param shippingId
     * @return
     */
    @DeleteMapping("")
    @ResponseBody
    public Result delete(@RequestBody String shippingId) {
        String userId = loginSession.getCustomerSession().getUserId();
        if (!shippingService.shippingIdValid(userId, shippingId)) {
            return Result.error("请求非法");
        }
        return shippingService.delete(shippingId) ? Result.success("删除收货地址成功", null) : Result.error("删除收货地址失败");
    }
}
