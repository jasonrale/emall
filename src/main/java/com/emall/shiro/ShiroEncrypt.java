package com.emall.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroEncrypt {
    private static String algorithmName;

    private static int hashIterations;

    @Value("${shiro.password.algorithmName}")
    public void setAlgorithmName(String algorithmName) {
        ShiroEncrypt.algorithmName = algorithmName;
    }

    @Value("${shiro.password.hashIterations}")
    public void setHashIterations(int hashIterations) {
        ShiroEncrypt.hashIterations = hashIterations;
    }

    /**
     * SHA-256加密
     * @param password
     * @param salt
     * @return String
     */
    public static String shiroEncrypt(String password, String salt) {
        // 返回加密后的密码
        return new SimpleHash(algorithmName, password, salt, hashIterations).toString();
    }
}
