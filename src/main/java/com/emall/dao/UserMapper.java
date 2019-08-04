package com.emall.dao;

import com.emall.entity.User;
import com.emall.vo.PasswordVo;
import com.emall.vo.UserUpdateVo;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(@Param("userId") String userId);

    int insert(User user);

    User selectByPrimaryKey(@Param("userId") String userId);

    int updateByUserId(UserUpdateVo userUpdateVo);

    int pwdById(PasswordVo passwordVo);

    int updateByPrimaryKey(User record);

    User selectByUserName(@Param("userName") String userName);

    boolean isExistByName(@Param("userName") String userName);
}