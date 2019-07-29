package com.emall.controller;

import com.emall.entity.Category;
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

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> mqFanout() {
        producer.sendFanout("Hello World");
        Category category = new Category("111111", "手机");
        producer.sendFanout(category);
        return Result.success("Send success:", "null");
    }

    @RequestMapping("/mq/direct")
    @ResponseBody
    public Result<String> mqDirect() {
        producer.sendDirect("Hello World");
        Category category = new Category("111111", "手机");
        producer.sendDirect(category);
        return Result.success("Send success:", "null");
    }

    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> mqTopic() {
        producer.sendTopic("Hello World");
        Category category = new Category("111111", "手机");
        producer.sendTopic(category);
        return Result.success("Send success:", "null");
    }

    @RequestMapping("/mq/headers")
    @ResponseBody
    public Result<String> mqHeaders() {
        producer.sendHeaders("Hello World");
        Category category = new Category("111111", "手机");
        producer.sendHeaders(category);
        return Result.success("Send success:", "null");
    }
}
