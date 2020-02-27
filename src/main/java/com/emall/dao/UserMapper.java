package com.emall.dao;

import com.emall.entity.User;
import com.emall.vo.PasswordVo;
import com.emall.vo.UserUpdateVo;
import org.apache.ibatis.annotations.Param;

/**
 * 用户数据接口层
 */
public interface UserMapper {
    int insert(User user);

    int updateByUserId(UserUpdateVo userUpdateVo);

    int pwdById(PasswordVo passwordVo);

    User selectByUserName(@Param("userName") String userName);

    boolean isExistByName(@Param("userName") String userName);
}