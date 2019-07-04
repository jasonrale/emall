package com.emall.shiro;

import com.emall.entity.Role;
import com.emall.entity.User;
import com.emall.service.RoleService;
import com.emall.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 *  实现AuthorizingRealm接口用户认证
 */
public class ShiroRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Resource
    UserService userService;

    @Resource
    RoleService roleService;

    //角色权限和对应权限添加
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录用户名
        String uName= (String) principalCollection.getPrimaryPrincipal();
        //查询用户对象
        User user = userService.selectByUsername(uName);
        //获取用户的角色与权限
        Role role = roleService.selectByPrimaryKey(user.getRId());
        String[] perms = role.getRPerm().split(",");

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //添加角色
        simpleAuthorizationInfo.addRole(role.getRName());
        for (String permission : perms) {
            //添加权限
            simpleAuthorizationInfo.addStringPermission(permission);
        }
        return simpleAuthorizationInfo;
    }

    //用户认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String uName = token.getUsername();
        String uPassword = Arrays.toString(token.getPassword());
        logger.info("开始认证--username : " + uName + "password : " + uPassword );

        User user = userService.selectByUsername(uName);
        if (user == null) {
            logger.error("用户名或密码错误");
            throw new UnknownAccountException("用户名或密码错误");
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            return new SimpleAuthenticationInfo(uName, user.getUPassword(), getName());
        }
    }
}