package com.emall.dao;

import com.emall.entity.Category;

public interface CategoryMapper {

    int deleteByPrimaryKey(String cId);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(String cId);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

}