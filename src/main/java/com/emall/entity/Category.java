package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;

/**
 *   商品类别实体类
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Repository
public class Category {
    private Integer cId;            //商品类别id

    private String cName;           //商品类别名称
}