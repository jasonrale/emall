package com.emall.dao;

import com.emall.entity.User;
import com.emall.vo.PasswordVo;
import com.emall.vo.UserUpdateVo;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    int deleteByPrimaryKey(String uId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String uId);

    User selectByUserName(@Param("uName") String uName);

    int updateByUserId(UserUpdateVo record);

    int updateByPrimaryKey(User record);

    int pwdById(PasswordVo passwordVo);
}