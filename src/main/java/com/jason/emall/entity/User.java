package com.jason.emall.entity;

import lombok.*;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;

/**
 *   用户实体类。
 */
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class User {
    private Integer uId;            //用户id

    @NotEmpty
    private String uName;           //用户名称

    @NotEmpty
    private String uPassword;       //用户密码

    private String uSex;            //用户性别

    private String uMobileNumber;   //用户手机号码

    private Integer uRole;          //用户角色
}