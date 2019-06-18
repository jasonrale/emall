package com.jason.emall.controller;

import com.jason.emall.redis.Key;
import com.jason.emall.redis.RedisService;
import com.jason.emall.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/redis")
public class RedisController {
    @Resource
    RedisService redisService;

    @RequestMapping(value = "/key", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> redisGet() {
        Key key = new Key("userName", 0);
        String username = redisService.get(key, String.class);
        return Result.success(username);
    }

    @RequestMapping(value = "/keyValue", method = RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> redisSet() {
        Key key = new Key("userName", 0);
        Boolean flag = redisService.set(key, "jason");
        if (flag) {
            return Result.success(true);
        }
        return Result.error(flag);

    }
}
