package com.emall.controller;

import com.emall.entity.User;
import com.emall.exception.GeneralException;
import com.emall.result.Result;
import com.emall.utils.ClassCastUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.UnknownSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Resource
    private ClassCastUtil castUtil;

    /**
     * 获取管理员登录信息
     * @param
     * @return Result
     */
    @GetMapping("")
    @ResponseBody
    public Result<Object> adminInfo() {
        logger.info("获取管理员登录信息中......");

        User adminInfo = null;
        try {
            Object object = SecurityUtils.getSubject().getSession().getAttribute("SysAdmin");
            if (object != null) {
                adminInfo = castUtil.classCast(object, User.class);
            }

        } catch (IllegalAccessException | InstantiationException | UnknownSessionException e) {
            throw new GeneralException("登录已过期");
        }

        return adminInfo != null ? Result.success("管理员" + adminInfo.getUserName() + "已登录", adminInfo) : null;
    }
}
