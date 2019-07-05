package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 *   用户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class User {
    //角色常量
    public static final String GENERAL_USER = "普通用户";

    public static final String SYSTEM_ADMIN = "系统管理员";

    public static final String SERVICE_ADMIN = "业务管理员";

    private Integer uId;            //用户id

    private String uName;           //用户名称

    private String uPassword;       //用户密码

    private Integer uSex;           //用户性别，1 ：男，0 ：女

    private String uMobileNumber;   //用户手机号码

    private Integer uRole;          //用户角色，0 ：普通用户，1 ：系统管理员，2 ：业务管理员

    private String uSalt;           //用户密码盐值
}