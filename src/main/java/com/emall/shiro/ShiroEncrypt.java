package com.emall.shiro;

import lombok.Data;
import org.apache.shiro.crypto.hash.SimpleHash;

@Data
public class ShiroEncrypt {
    public static final String algorithmName = "SHA-256";

    public static final int hashIterations = 1024;

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
