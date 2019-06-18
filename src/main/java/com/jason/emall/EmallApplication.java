package com.jason.emall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jason.emall.dao")
public class EmallApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmallApplication.class, args);
    }

}
