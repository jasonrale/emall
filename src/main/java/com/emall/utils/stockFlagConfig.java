package com.emall.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 秒杀商品库存本地内存标记
 */
@Configuration
public class stockFlagConfig {
    @Bean
    public HashMap stockFlagMap() {
        return new HashMap<String, Boolean>();
    }
}
