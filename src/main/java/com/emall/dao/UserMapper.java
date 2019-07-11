package com.emall.dao;

import com.emall.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Long uId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long uId);

    User selectByUserName(@Param("uName") String uName);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}