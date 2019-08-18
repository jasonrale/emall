package com.emall.utils;

import com.emall.entity.User;
import com.emall.exception.GeneralException;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 获取登录信息
 */
@Component
public class LoginSession {
    @Resource
    private ClassCastUtil castUtil;

    /**
     * 获取用户登录信息
     *
     * @return
     */
    public User getUserSession() {
        User user = null;
        Object object = SecurityUtils.getSubject().getSession().getAttribute("CurrentUser");
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
        Object object = SecurityUtils.getSubject().getSession().getAttribute("SysAdmin");
        if (object != null) {
            try {
                admin = castUtil.classCast(object, User.class);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new GeneralException("登录已过期");
            }
        }
        return admin;
    }
}
