package com.emall.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

@Service
public class RedisService {
    @Resource
    private JedisPool jedisPool;

    /**
     * 通过key获取redis中的值然后转换成java对象
     * @param key
     * @param clazz
     * @param <T>
     * @return T
     */
    public <T> T get(Key key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = jedis.get(key.getKey());
            return stringToBean(str, clazz);
        } finally {
            returnToPool(jedis);
        }

    }

    /**
     * 将java对象传换成字符串,设置键值对与失效时间，
     * @param key
     * @param value
     * @param <T>
     * @return boolean
     */
    public<T> boolean set(Key key, T value) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if (str == null || str.length() == 0) {
                return false;
            }
            if (key.getExpireSeconds() != 0) {
                jedis.setex(key.getKey(), key.getExpireSeconds(), str);
            } else {
                jedis.set(key.getKey(), str);
            }

            return true;
        } finally {
            returnToPool(jedis);
        }

    }

    /**
     * 判断是否存在此键
     * @param key
     * @param <T>
     * @return Long
     */
    public <T> boolean exists(Key key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key.getKey());
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加键的数值大小，若key不存在或错误类型则置零
     * @param key
     * @param <T>
     * @return Long
     */
    public <T> Long incr(Key key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(key.getKey());
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减小键的数值大小，若key不存在或错误类型则置零
     * @param key
     * @param <T>
     * @return Long
     */
    public <T> Long decr(Key key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.decr(key.getKey());
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * Java对象转JSON字符串
     * @param value
     * @param <T>
     * @return String
     */
    private<T> String beanToString(T value) {
        if (value == null) {
            return null;
        }

        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * JSON字符串转Java对象
     * @param str
     * @param clazz
     * @param <T>
     * @return T
     */
    private <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() == 0 || clazz == null) {
            return null;
        }

        if (clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T)Long.valueOf(str);
        } else if (clazz == String.class) {
            return (T)str;
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    /**
     * 返回redis连接对象至连接池
     * @param jedis
     */
    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
