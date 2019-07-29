package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 *   商品类别实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class Category {
    private String cId;           //商品类别id

    private String cName;      //商品类别名称
}