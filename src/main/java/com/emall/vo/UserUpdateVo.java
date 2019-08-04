package com.emall.vo;

import com.emall.annotation.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 用户信息修改业务对象类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateVo {
    private String userId;               //用户id

    @NotEmpty(message = "用户名不能为空")
    @Size(min = 3, message = "用户名长度不小于三个字符")
    private String userName;          //用户名称

    private Integer userSex;           //用户性别--男 ：1，女 ：0

    @IsMobile
    private String userMobileNumber;  //用户手机号码
}
