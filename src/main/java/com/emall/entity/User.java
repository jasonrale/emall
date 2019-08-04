package com.emall.entity;

import com.emall.annotation.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *   用户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class User implements Serializable {
    public static final int CUSTOMER = 0;

    public static final int ADMIN = 1;


    private String userId;               //用户id

    @NotEmpty(message = "用户名不能为空")
    @Size(min = 3, message = "用户名长度不小于三个字符")
    private String userName;          //用户名称

    @NotEmpty(message = "用户密码不能为空")
    @Size(min = 6, message = "密码长度不能少于6位")
    private String userPassword;       //用户密码

    private Integer userSex;           //用户性别--男 ：1，女 ：0

    @IsMobile
    private String userMobileNumber;  //用户手机号码

    private Integer userRole;          //用户角色--普通用户 ：0，系统管理员 ：1

    private String userSalt;           //用户密码盐值
}