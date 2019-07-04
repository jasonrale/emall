package com.emall.entity;

import lombok.*;
import org.springframework.stereotype.Repository;

/**
 *   商品类别实体类
 */
@Data
@Repository
public class Category {
    private Integer cId;            //商品类别id

    private String cName;           //商品类别名称
}