package com.emall.service;

import com.emall.dao.RoleMapper;
import com.emall.entity.Role;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RoleService {
    @Resource
    RoleMapper roleMapper;

    /**
     * 根据主键角色id查询角色对象
     * @param rId
     * @return Role
     */
    public Role selectByPrimaryKey(Integer rId) {
        return roleMapper.selectByPrimaryKey(rId);
    }
}
