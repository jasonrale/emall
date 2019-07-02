package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;

/**
 *   角色实体类
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Repository
public class Role {
    private Integer rId;            //角色id

    private String rName;           //角色名称

    private String rPerm;           //角色权限
}