package com.emall.dao;

import com.emall.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(@Param("uId") Integer uId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(@Param("uId") Integer uId);

    User selectByUserName(@Param("uName") String uName);

    String selectUserOwnedRole(@Param("uName") String uName);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}