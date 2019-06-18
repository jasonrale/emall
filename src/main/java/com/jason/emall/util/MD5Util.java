package com.jason.emall.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    private static final String salt = "1a2b3c";

    private static String md5(String src) {
       return DigestUtils.md5Hex(src);
    }

    /**
     * 将用户输入的密码转换成表单提交的密码
     * @param input
     * @return String
     */
    private static String inputToForm(String input) {
        String str = salt.charAt(0) + salt.charAt(4) + input + salt.charAt(5) +salt.charAt(3);
        return md5(str);
    }

    /**
     * 将表单提交的密码转换成数据库插入的密码
     * @param form
     * @return String
     */
    private static String formToDatabase(String form, String salt) {
        String str = salt.charAt(3) + salt.charAt(5) + form + salt.charAt(2) +salt.charAt(1);
        return md5(str);
    }

    /**
     * 将用户输入的密码转换成数据库插入的密码
     * @param input
     * @return String
     */
    public static String inputToDatabase(String input, String salt) {
        return md5(formToDatabase(inputToForm(input), salt));
    }


}
