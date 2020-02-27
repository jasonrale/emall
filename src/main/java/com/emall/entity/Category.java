package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;

/**
 * 商品类别实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class Category {
    private String categoryId;          //商品类别id

    @NotEmpty(message = "品类名称不能为空")
    private String categoryName;        //商品类别名称
}