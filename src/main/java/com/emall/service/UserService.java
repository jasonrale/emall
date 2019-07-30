package com.emall.service;

import com.emall.dao.UserMapper;
import com.emall.entity.User;
import com.emall.exception.GeneralException;
import com.emall.result.Result;
import com.emall.utils.SnowflakeIdWorker;
import com.emall.vo.PasswordVo;
import com.emall.vo.UserUpdateVo;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.emall.shiro.ShiroEncrypt.shiroEncrypt;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 根据用户名查询用户对象
     *
     * @param uName
     * @return User
     */
    public User selectByUsername(String uName) {
        return userMapper.selectByUserName(uName);
    }

    /**
     * 注册验证
     *
     * @param user
     * @return Result
     */
    public boolean registerValidate(User user) {
        String uName = user.getUName();
        String uPassword = user.getUPassword();

        User userInfo = selectByUsername(uName);
        if (userInfo != null) {
            throw new GeneralException("用户名已存在");
        } else {
            String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
            uPassword = shiroEncrypt(uPassword, salt);
            user.setUId(String.valueOf(snowflakeIdWorker.nextId()));
            user.setUPassword(uPassword);
            user.setUSalt(salt);
            //设置为普通用户
            user.setURole(0);

            return userMapper.insert(user) != 0;
        }
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
