package com.emall.dao;

import com.emall.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    int deleteByCategoryId(@Param("categoryId") String categoryId);

    boolean isExistByName(@Param("categoryName") String categoryName);

    List<Category> queryAll();

    List<Category> adminQueryAll(@Param("limit") long limit, @Param("offset") long offset);

    int insert(Category record);

    Category selectByPrimaryKey(String categoryId);

    int updateByPrimaryKey(Category record);

    long count();
}