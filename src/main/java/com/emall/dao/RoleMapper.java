package com.emall.dao;

import com.emall.entity.Role;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {

    int deleteByPrimaryKey(Integer rId);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(@Param("rId") Integer rId);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}