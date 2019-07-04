package com.emall.entity;

import com.emall.validator.IsMobile;
import lombok.*;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 *   用户实体类
 */
@Data
@Repository
public class User {
    private Integer uId;            //用户id

    @NotEmpty(message = "用户名不能为空")
    private String uName;           //用户名称

    @NotEmpty(message = "用户密码不能为空")
    @Size(min = 6)
    private String uPassword;       //用户密码

    private String uSex;            //用户性别
    
    @IsMobile(message = "手机号码格式错误")
    private String uMobileNumber;   //用户手机号码

    private String uSalt;           //用户密码盐值

    private Integer rId;            //用户角色id, 普通用户：0，系统管理员：,1，商城管理员：2
}