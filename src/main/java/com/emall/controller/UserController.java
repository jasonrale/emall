package com.emall.controller;

import com.emall.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    /**
     * 跳转到登陆页面
     * @return 登录页面url
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){
        return "user/login";
    }

    @RequestMapping(value = "/loginValidate", method = RequestMethod.POST)
    public String loginValidate(@Valid User user) {

        return null;
    }

    /**
     * 跳转到注册页面
     * @return 注册页面url
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(){
        return "user/register";
    }
}
