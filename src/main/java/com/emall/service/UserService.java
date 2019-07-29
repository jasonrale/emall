package com.emall.service;

import com.emall.dao.UserMapper;
import com.emall.entity.User;
import com.emall.exception.GeneralException;
import com.emall.result.Result;
import com.emall.utils.SnowflakeIdWorker;
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
     * 判断用户名是否已经存在
     *
     * @param userName
     * @return Result
     */
    public boolean userNameExist(String userName) {
        User userInfo = selectByUsername(userName);
        if (userInfo != null) {
            return true;
        }
        return false;
    }

    /**
     * 注册验证
     *
     * @param user
     * @return Result
     */
    public Result<User> registerValidate(User user) {
        String uName = user.getUName();
        String uPassword = user.getUPassword();

        User userInfo = selectByUsername(uName);
        if (userInfo != null) {
            throw new GeneralException("用户名已存在");
        } else {
            String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
            uPassword = shiroEncrypt(uPassword, salt);
            user.setUId(snowflakeIdWorker.nextId());
            user.setUPassword(uPassword);
            user.setUSalt(salt);
            //设置为普通用户
            user.setURole(0);
            userMapper.insert(user);
            return Result.success("注册成功！", user);
        }
    }

    /**
     * 用户信息修改
     * @param userUpdateVo
     * @return
     */
    public Result<UserUpdateVo> userUpdate(@Valid UserUpdateVo userUpdateVo) {
        if (userNameExist(userUpdateVo.getUName())) {
            return Result.error("用户已存在");
        }
        userMapper.updateByUserId(userUpdateVo);
        return Result.success("用户信息修改成功", userUpdateVo);
    }
}
