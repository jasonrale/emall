package com.emall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 登录业务对象类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo {
    @NotEmpty(message = "用户名不能为空")
    @Size(min = 3, message = "用户名长度不小于三个字符")
    private String userName;          //用户名称

    @NotEmpty(message = "用户密码不能为空")
    @Size(min = 6, message = "密码长度不能少于6位")
    private String userPassword;       //用户密码
}
