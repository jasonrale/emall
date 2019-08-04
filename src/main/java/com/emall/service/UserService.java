package com.emall.service;

import com.emall.dao.UserMapper;
import com.emall.entity.User;
import com.emall.utils.SnowFlakeConfig;
import com.emall.vo.PasswordVo;
import com.emall.vo.UserUpdateVo;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.emall.shiro.ShiroEncrypt.shiroEncrypt;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlakeConfig.SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 根据用户名查询用户对象
     *
     * @param userName
     * @return User
     */
    public User selectByUsername(String userName) {
        return userMapper.selectByUserName(userName);
    }

    /**
     * 注册验证
     *
     * @param user
     * @return Result
     */
    public boolean registerValidate(User user) {
        String userName = user.getUserName();
        String userPassword = user.getUserPassword();

        Assert.isTrue(!userMapper.isExistByName(userName), "用户名已存在");

        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        userPassword = shiroEncrypt(userPassword, salt);
        user.setUserId(String.valueOf(snowflakeIdWorker.nextId()));
        user.setUserPassword(userPassword);
        user.setUserSalt(salt);
        //设置为普通用户
        user.setUserRole(0);

        return userMapper.insert(user) != 0;
    }

    /**
     * 用户信息修改
     * @param userUpdateVo
     * @return
     */
    public boolean userUpdate(@Valid UserUpdateVo userUpdateVo) {
        return userMapper.updateByUserId(userUpdateVo) != 0;
    }

    public boolean password(PasswordVo passwordVo) {
        return userMapper.pwdById(passwordVo) != 0;
    }
}
