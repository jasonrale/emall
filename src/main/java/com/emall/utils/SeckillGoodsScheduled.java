package com.emall.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 秒杀商品线程池
 */
@Configuration
public class SeckillGoodsScheduled {
    @Bean
    public ScheduledExecutorService getScheduledThreadPool() {
        //延时任务线程池
        return Executors.newScheduledThreadPool(12);
    }
}
