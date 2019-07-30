package com.emall.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 用户密码修改业务对象类
 */
@Data
@NoArgsConstructor
public class PasswordVo {
    private String uId;               //用户id

    private String salt;              //用户密码盐值

    private String passwordReal;      //正确原密码

    @NotEmpty(message = "请输入原密码")
    private String passwordOld;       //输入原密码

    @NotEmpty(message = "用户密码不能为空")
    @Size(min = 6, message = "密码长度不能少于6位")
    private String passwordNew;       //输入新密码

    @NotEmpty(message = "用户密码不能为空")
    @Size(min = 6, message = "密码长度不能少于6位")
    private String pwdConfirm;        //确认密码
}
