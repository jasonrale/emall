package com.emall.service;


import com.emall.dao.UserMapper;
import com.emall.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    /**
     * 根据用户名查询用户对象
     * @param uName
     * @return User
     */
    public User selectByUsername(String uName) {
        return userMapper.selectByUsername(uName);
    }
}
