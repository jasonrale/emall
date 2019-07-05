package com.emall.service;

import com.emall.dao.UserMapper;
import com.emall.entity.User;
import com.emall.exception.GeneralException;
import com.emall.result.Result;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.emall.shiro.ShiroEncrypt.shiroEncrypt;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    /**
     * 根据用户名查询用户对象
     *
     * @param uName
     * @return User
     */
    public User selectByUsername(String uName) {
        return userMapper.selectByUsername(uName);
    }

    public Result<User> registerValidate(User user) {
        String uName = user.getUName();
        String uPassword = user.getUPassword();

        User userInfo = selectByUsername(uName);
        if (userInfo != null) {
            throw new GeneralException("用户名已存在");
        } else {
            String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
            uPassword = shiroEncrypt(uPassword, salt);
            user.setUPassword(uPassword);
            user.setUSalt(salt);
            //设置为普通用户
            user.setURole(0);
            userMapper.insertSelective(user);
            return Result.success("注册成功！", user);
        }
    }
}
