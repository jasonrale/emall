package com.emall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "result")
public class ResultController {
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String register() {
        return "result/result.html?resultType=register";
    }
}
