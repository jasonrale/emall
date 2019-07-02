package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;

/**
 *   用户实体类
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Repository
public class User {
    private Integer uId;            //用户id

    private String uName;           //用户名称

    private String uPassword;       //用户密码

    private String uSex;            //用户性别

    private String uMobileNumber;   //用户手机号码

    private String uSalt;           //用户密码盐值

    private Integer rId;            //用户角色id
}