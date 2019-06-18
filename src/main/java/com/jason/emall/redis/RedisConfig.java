package com.jason.emall.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {
    private String host;                //服务器地址

    private int port;                   //服务器端口号

    private int timeout;                //客户端超时时间

    private String password;            //密码

    private int poolMaxTotal;           //最大连接数

    private int poolMaxIdle;            //最大空闲数

    private int poolMaxWaitMillis;      //最大建立连接等待时间
}
