package com.emall.utils;


import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@Configuration
public class MultipartConfig {
    /**
     * 文件上传临时路径
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        File tmpFile = new File("E:/ImageTemp");
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        factory.setLocation("E:/ImageTemp");
        return factory.createMultipartConfig();
    }
}
