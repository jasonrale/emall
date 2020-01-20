package com.emall.controller;

import com.emall.entity.User;
import com.emall.exception.GeneralException;
import com.emall.result.Result;
import com.emall.service.UserService;
import com.emall.shiro.ShiroEncrypt;
import com.emall.utils.ClassCastUtil;
import com.emall.utils.LoginSession;
import com.emall.vo.LoginVo;
import com.emall.vo.PasswordVo;
import com.emall.vo.UserUpdateVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private LoginSession loginSession;

    @Resource
    private UserService userService;

    /**
     * 获取用户登录信息
     * @param
     * @return Result
     */
    @GetMapping("")
    @ResponseBody
    public Result<Object> userInfo() {
        logger.info("获取用户登录信息中......");

        User userInfo = loginSession.getCustomerSession();

        return userInfo != null ? Result.success("用户" + userInfo.getUserName() + "已登录", userInfo) : Result.error("用户未登录");
    }

    /**
     * 获取管理员登录信息
     * @param
     * @return Result
     */
    @GetMapping("/admin")
    @ResponseBody
    public Result<Object> adminInfo() {
        logger.info("获取管理员登录信息中......");

        User adminInfo = loginSession.getAdminSession();

        return adminInfo != null ? Result.success("管理员" + adminInfo.getUserName() + "已登录", adminInfo) : null;
    }

    /**
     * 登录认证
     * @param loginVo
     * @return Result
     */
    @PostMapping("/loginValidate")
    @ResponseBody
    public Result<User> loginValidate(@Valid @RequestBody LoginVo loginVo) {
        //认证
        User user = login(loginVo);
        return Result.success("登录成功", user);
    }

    /**
     * 拦截未登录请求后的认证方法
     * @param loginVo
     * @param request
     * @return
     */
    @PostMapping("/unauthorized")
    @ResponseBody
    public Result<User> authenticate(@Valid @RequestBody LoginVo loginVo, ServletRequest request) {
        //认证
        User user = login(loginVo);
        SavedRequest saveRequest = WebUtils.getSavedRequest(request);
        String url = saveRequest.getRequestUrl();
        return Result.success(url, user);
    }

    public User login(LoginVo loginVo) {
        logger.info("登录验证--" + "用户名：" + loginVo.getUserName());
        //获得Subject对象
        Subject subject = SecurityUtils.getSubject();
        //将用户输入的用户名写密码封装到一个UsernamePasswordToken对象中
        UsernamePasswordToken token = new UsernamePasswordToken(loginVo.getUserName(), loginVo.getUserPassword());
        subject.login(token);

        return (User) subject.getPrincipal();
    }

    /**
     * 注册验证
     * @param user
     * @return Result
     */
    @PutMapping("")
    @ResponseBody
    public Result registerValidate(@Valid @RequestBody User user) {
        logger.info("注册验证");
        return userService.registerValidate(user) ?
                Result.success("注册成功", null) : Result.error("注册失败");
    }

    /**
     * 登出
     * @return
     */
    @GetMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout(); // session删除、RememberMe cookie也将被删除
        return "redirect:/user/login.html";
    }

    /**
     * 修改用户信息
     * @param userUpdateVo
     * @return Result
     */
    @PostMapping("")
    @ResponseBody
    public Result userUpdate(@Valid @RequestBody UserUpdateVo userUpdateVo) {
        logger.info("修改用户信息");

        Result result = userService.userUpdate(userUpdateVo) ?
                Result.success("用户信息修改成功", null) : Result.error("用户信息修改失败");

        if (result.isStatus()) {
            User user = loginSession.getCustomerSession();

            if (user != null) {
                user.setUserName(userUpdateVo.getUserName());
                user.setUserMobileNumber(userUpdateVo.getUserMobileNumber());
            }

            loginSession.setUserSession(user);
        }

        return result;
    }

    /**
     * 修改密码
     * @param passwordVo
     * @return
     */
    @PostMapping("/password")
    @ResponseBody
    public Result password(@Valid @RequestBody PasswordVo passwordVo) {
        User user = loginSession.getCustomerSession();
        //输入原密码加密验证
        String pwdOld = ShiroEncrypt.shiroEncrypt(passwordVo.getPasswordOld(), user.getUserSalt());
        //密码校验
        if (!pwdOld.equals(user.getUserPassword())) {
            throw new GeneralException("原密码输入错误");
        } else if (!passwordVo.getPasswordNew().equals(passwordVo.getPasswordConfirm())) {
            throw new GeneralException("两次输入密码不一致");
        }

        String pwdNew = ShiroEncrypt.shiroEncrypt(passwordVo.getPasswordNew(), user.getUserSalt());
        passwordVo.setUserId(user.getUserId());
        passwordVo.setPasswordNew(pwdNew);
        passwordVo.setPasswordConfirm(pwdNew);

        return userService.password(passwordVo) ? Result.success("密码修改成功，请重新登录", null) : Result.error("密码修改失败");
    }
}
