package com.emall.entity;

import lombok.*;
import org.springframework.stereotype.Repository;

/**
 *   角色实体类
 */
@Data
@Repository
public class Role {
    private Integer rId;            //角色id

    private String rName;           //角色名称

    private String rPerm;           //角色权限
}