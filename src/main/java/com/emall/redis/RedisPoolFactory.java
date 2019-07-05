package com.emall.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * redis数据连接池工厂类
 */
@Service
public class RedisPoolFactory {
    @Resource
    private RedisConfig redisConfig;

    /**
     * redis数据连接池工厂
     * @return JedisPool
     */
    @Bean
    public JedisPool JedisPoolFactory() {
        //获取连接池配置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWaitMillis());

        //生产连接池对象
        JedisPool jedisPool;
        jedisPool = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(),
                redisConfig.getTimeout(), redisConfig.getPassword());

        return jedisPool;
    }
}
