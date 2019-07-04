package com.emall.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Value;

public class ShiroEncrypt {
    @Value("${shiro.password.algorithmName}")
    private static String algorithmName;

    @Value("${shiro.password.hashIterations}")
    private static int hashIterations;

    /**
     * SHA-256加密
     * @param password
     * @param salt
     * @return String
     */
    public static String shiroEncrypt(String password,String salt) {
        // 返回加密后的密码
        return new SimpleHash(algorithmName, password, salt, hashIterations).toBase64();
    }
}
