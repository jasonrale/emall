package com.emall.shiro;

import com.emall.entity.User;
import com.emall.redis.Key;
import com.emall.redis.RedisService;
import com.emall.service.UserService;
import com.emall.utils.SnowflakeIdWorker;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

import static com.emall.entity.User.*;


/**
 *  继承AuthorizingRealm抽象类用户认证
 */
public class ShiroRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Resource
    private UserService userService;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    private RedisService redisService;

    @Override
    public String getName() {
        return "shiroRealm";
    }

    /**
     * 角色添加
     * @param principalCollection
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("角色添加");
        //获得登录的对象
        User user = (User) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //添加角色
        info.addRole(user.getUId() == GENERAL_USER ? "普通用户" : user.getUId() == SYSTEM_ADMIN ? "系统管理员" : "业务管理员");
        return info;
    }

    /**
     * 用户认证
     * @param authenticationToken
     * @return AuthenticationInfo
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取token中的用户信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String tName = token.getUsername();
        logger.info("开始认证--username : " + tName);

        //查询是否存在该用户
        User user = userService.selectByUsername(tName);
        if (user == null) {
            logger.error("用户名不存在");
            throw new UnknownAccountException("用户名或密码错误");
        }
        //从数据库取出密码并转换成字符数组
        char[] password = user.getUPassword().toCharArray();
        //从数据库取出盐转换为byte类型的盐
        ByteSource byteSalt = ByteSource.Util.bytes(user.getUSalt());
        //这里验证authenticationToken和simpleAuthenticationInfo的信息
        try {
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, byteSalt, getName());
            //认证通过后将用户信息放在session里
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute("CurrentUser", user);
            //生成cookie
            String uuId = String.valueOf(snowflakeIdWorker.nextId());
            Key CurrentUser = new Key(uuId, 0);
//            redisService.set(CurrentUser, user);
            Cookie cookie = new SimpleCookie();
            return info;
        } catch (IncorrectCredentialsException exception) {
            throw new IncorrectCredentialsException("用户名或密码错误");
        }
    }
}