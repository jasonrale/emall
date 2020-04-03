package com.emall.dao;

import com.emall.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品类别数据接口层
 */
public interface CategoryMapper {
    Category selectByCategoryId(@Param("categoryId") String categoryId);

    int deleteByCategoryId(@Param("categoryId") String categoryId);

    boolean isExistByName(@Param("categoryName") String categoryName);

    List<Category> queryAll();

    List<Category> adminQueryAll(@Param("limit") long limit, @Param("offset") long offset);

    int insert(Category category);

    int update(Category category);

    int count();
}