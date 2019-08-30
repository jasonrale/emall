package com.emall.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SnowFlake配置
 */
@Configuration
public class SnowFlakeConfig {
    @Bean
    public SnowflakeIdWorker getIdWorker() {
        return new SnowflakeIdWorker(0, 0);
    }
}
