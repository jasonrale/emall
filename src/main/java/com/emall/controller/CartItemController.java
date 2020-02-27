package com.emall.controller;

import com.emall.entity.CartItem;
import com.emall.entity.User;
import com.emall.result.Result;
import com.emall.service.CartItemService;
import com.emall.utils.LoginSession;
import com.emall.utils.SnowflakeIdWorker;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 购物车明细控制层
 */
@Controller
@RequestMapping("/cartItem")
public class CartItemController {
    @Resource
    CartItemService cartItemService;

    @Resource
    SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    LoginSession loginSession;

    /**
     * 根据用户id获取所有购物车明细
     * @return
     */
    @GetMapping("")
    @ResponseBody
    public Result queryAll() {
        String userId = loginSession.getCustomerSession().getUserId();

        List<CartItem> cartItemList = cartItemService.queryAllByUserId(userId);

        return cartItemList.size() != 0 ? Result.success("查询购物车明细成功", cartItemList) : Result.error("您的购物车空空如也");
    }

    /**
     * 加入购物车
     * @param cartItem
     * @return
     */
    @PutMapping("")
    @ResponseBody
    public Result cartAdd(@RequestBody CartItem cartItem, HttpServletRequest request) {
        User user = loginSession.getCustomerSession();

        if (user == null) {
            return Result.error("Authc");
        }

        String userId = user.getUserId();

        cartItem.setCartItemId(String.valueOf(snowflakeIdWorker.nextId()));
        cartItem.setUserId(userId);

        return cartItemService.insert(cartItem) ? Result.success("已加入购物车", cartItem) : Result.error("加入购物车失败");
    }

    /**
     * 删除单个购物车明细
     * @param cartItemId
     * @return
     */
    @DeleteMapping("")
    @ResponseBody
    public Result delete(@RequestBody String cartItemId) {
        if (StringUtils.isEmpty(cartItemId)) {
            return Result.error("删除失败");
        }

        return cartItemService.deleteByCartItemId(cartItemId) ? Result.success("删除成功", null) : Result.error("删除失败");
    }

    /**
     * 删除所选中的购物车明细
     * @param cartItemIdList
     * @return
     */
    @DeleteMapping("/select")
    @ResponseBody
    public Result deleteSelect(@RequestBody List<String> cartItemIdList) {
        if (cartItemIdList.size() == 0) {
            return Result.error("没有选择商品");
        }

        return cartItemService.deleteSelect(cartItemIdList) ? Result.success("删除成功", null) : Result.error("删除失败");
    }

    /**
     * 根据用户id获取购物车明细数量
     * @return
     */
    @GetMapping("/count")
    @ResponseBody
    public Result<Integer> count() {
        String userId = loginSession.getCustomerSession().getUserId();

        return Result.success("获取购物车明细数量", cartItemService.countByUserId(userId));
    }

    /**
     * 订单确认
     * @return
     */
    @PostMapping("/orderConfirm")
    @ResponseBody
    public Result orderConfirm(@RequestBody List<String> cartItemIdList) {

        List<CartItem> cartItemList = cartItemService.selectByCartItemIdList(cartItemIdList);

        return cartItemList.size() != 0 ? Result.success("查询购物车明细成功", cartItemList) : Result.error("哪里出了一点点问题");
    }
}
