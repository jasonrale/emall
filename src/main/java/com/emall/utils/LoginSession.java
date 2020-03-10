package com.emall.utils;

import com.emall.entity.User;
import com.emall.exception.GeneralException;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 获取登录信息工具
 */
@Component
public class LoginSession {
    @Resource
    private ClassCastUtil castUtil;

    /**
     * 获取顾客登录信息
     *
     * @return
     */
    public User getCustomerSession() {
        User user = null;
        Object object = SecurityUtils.getSubject().getSession().getAttribute("Customer");
        if (object != null) {
            try {
                user = castUtil.classCast(object, User.class);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new GeneralException("登录已过期");
            }
        }
        return user;
    }

    /**
     * 获取管理员登录信息
     *
     * @return
     */
    public User getAdminSession() {
        User admin = null;
        Object object = SecurityUtils.getSubject().getSession().getAttribute("Admin");
        if (object != null) {
            try {
                admin = castUtil.classCast(object, User.class);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new GeneralException("登录已过期");
            }
        }
        return admin;
    }

    /**
     * 设置用户登录信息
     *
     * @param user
     */
    public void setUserSession(User user) {
        SecurityUtils.getSubject().getSession().setAttribute(user.getUserRole() == 0 ? "Customer" : "Admin", user);
    }
}
