package com.emall.dao;

import com.emall.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    int deleteByPrimaryKey(Integer uId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer uId);

    User selectByUsername(@Param("uName") String uName);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}