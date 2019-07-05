package com.emall.controller;

import com.emall.entity.User;
import com.emall.result.Result;
import com.emall.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    /**
     * 跳转到登陆页面
     * @return 登录页面url
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){
        return "user/login";
    }

    /**
     * 登录认证
     * @param user
     * @return Result
     */
    @RequestMapping(value = "/loginValidate", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> loginValidate(@Valid User user) {
        logger.info("登录验证--" + "用户名：" + user.getUName());
        //获得Subject对象
        Subject subject = SecurityUtils.getSubject();
        //将用户输入的用户名写密码封装到一个UsernamePasswordToken对象中
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUName(), user.getUPassword());
        token.setRememberMe(true);
        subject.login(token);

        user = (User) subject.getPrincipal();
        return Result.success("登录成功", user.getURole());
    }

    /**
     * 跳转到注册页面
     * @return 注册页面url
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(){
        return "user/register";
    }

    /**
     * 注册验证
     * @param user
     * @return Result
     */
    @RequestMapping(value = "/registerValidate", method = RequestMethod.POST)
    @ResponseBody
    public Result<User> registerValidate(@Valid User user) {
        logger.info("注册验证--" + "用户名：" + user.getUName() + "  性别：" + user.getUSex()
                + "  手机号码：" + user.getUMobileNumber());
        return userService.registerValidate(user);
    }
}
