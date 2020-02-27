package com.emall.annotation;

import com.alibaba.fastjson.JSON;
import com.emall.redis.RedisKeyUtil;
import com.emall.result.Result;
import com.emall.utils.LoginSession;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 访问拦截器
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {
    @Resource
    LoginSession loginSession;

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            String userId = loginSession.getCustomerSession().getUserId();
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            String limitKey = RedisKeyUtil.accessLimit(userId);

            Integer count = (Integer) redisTemplate.opsForValue().get(limitKey);
            if (count == null) {
                redisTemplate.opsForValue().set(limitKey, 1, seconds, TimeUnit.SECONDS);
            } else if (count < maxCount) {
                redisTemplate.opsForValue().increment(limitKey);
            } else {
                render(response, "访问太频繁");
                return false;
            }
        }

        return true;
    }

    /**
     * 向前端响应
     *
     * @param response
     * @param msg
     */
    private void render(HttpServletResponse response, String msg) {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            String str = JSON.toJSONString(Result.error(msg));
            out.write(str.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
