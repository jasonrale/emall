package com.emall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "result")
public class ResultController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView result(ModelAndView mv) {
        mv.setViewName("result/result");
        mv.addObject("resultType", "register");
        return mv;
    }
}
