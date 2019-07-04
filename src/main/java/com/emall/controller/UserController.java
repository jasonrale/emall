package com.emall.controller;

import com.emall.entity.User;
import com.emall.result.Result;
import com.emall.service.UserService;
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
    UserService userService;
    /**
     * 跳转到登陆页面
     * @return 登录页面url
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){
        return "user/login";
    }

//    @RequestMapping(value = "/loginValidate", method = RequestMethod.POST)
//    public String loginValidate(@Valid User user) {
//
//        return null;
//    }

    /**
     * 跳转到注册页面
     * @return 注册页面url
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(){
        return "user/register";
    }

    @RequestMapping(value = "/registerValidate", method = RequestMethod.POST)
    @ResponseBody
    public Result<User> registerValidate(@Valid User user) {
        logger.info(user.toString());
        return userService.registerValidate(user);
    }


}
