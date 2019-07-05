package com.emall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "admin")
public class AdminController {
    /**
     * 跳转到后台管理首页
     * @return 后台管理首页url
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String register(){
        return "authenticated/admin/index";
    }
}
