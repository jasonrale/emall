package com.emall.controller;

import com.emall.rabbitmq.MessageProducer;
import com.emall.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class SampleController {
    @Resource
    MessageProducer producer;

    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq() {
        producer.send("Hello World");
        return Result.success("Send message:", "Hello World" );
    }
}
