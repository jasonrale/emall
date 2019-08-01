package com.emall.controller;

import com.emall.entity.User;
import com.emall.exception.GeneralException;
import com.emall.result.Result;
import com.emall.service.UserService;
import com.emall.shiro.ShiroEncrypt;
import com.emall.utils.ClassCastUtil;
import com.emall.vo.LoginVo;
import com.emall.vo.PasswordVo;
import com.emall.vo.UserUpdateVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
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
    private UserService userService;

    @Resource
    private ClassCastUtil castUtil;

    /**
     * 登录认证
     * @param loginVo
     * @return Result
     */
    @PostMapping("/loginValidate")
    @ResponseBody
    public Result<User> loginValidate(@Valid LoginVo loginVo) {
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
    @PostMapping("/authenticate")
    @ResponseBody
    public Result<User> authenticate(@Valid LoginVo loginVo, ServletRequest request) {
        //认证
        User user = login(loginVo);
        SavedRequest saveRequest = WebUtils.getSavedRequest(request);
        String url = saveRequest.getRequestUrl();
        return Result.success(url, user);
    }

    public User login(LoginVo loginVo) {
        logger.info("登录验证--" + "用户名：" + loginVo.getUName());
        //获得Subject对象
        Subject subject = SecurityUtils.getSubject();
        //将用户输入的用户名写密码封装到一个UsernamePasswordToken对象中
        UsernamePasswordToken token = new UsernamePasswordToken(loginVo.getUName(), loginVo.getUPassword());
        subject.login(token);

        return (User) subject.getPrincipal();
    }

    /**
     * 获取用户登录信息
     * @param
     * @return Result
     */
    @GetMapping("/userInfo")
    @ResponseBody
    public Result<Object> userInfo() {
        logger.info("获取用户登录信息中......");

        User userInfo = null;
        try {
            Object object = SecurityUtils.getSubject().getSession().getAttribute("CurrentUser");
            if (object != null) {
                userInfo = castUtil.classCast(object, User.class);
            }
        } catch (IllegalAccessException | InstantiationException | UnknownSessionException e) {
            throw new GeneralException("登录已过期");
        }

        return userInfo != null ? Result.success("用户" + userInfo.getUName() + "已登录", userInfo) : Result.error("用户未登录");
    }

    /**
     * 注册验证
     * @param user
     * @return Result
     */
    @PutMapping("/registerValidate")
    @ResponseBody
    public Result registerValidate(@Valid User user) {
        logger.info("注册验证--" + "用户名：" + user.getUName() + "  性别：" + user.getUSex()
                + "  手机号码：" + user.getUMobileNumber());
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
    @PostMapping("/userInfo")
    @ResponseBody
    public Result userUpdate(@Valid UserUpdateVo userUpdateVo) {
        logger.info("修改用户信息--" + "用户名：" + userUpdateVo.getUName() + "  性别：" + userUpdateVo.getUSex()
                + "  手机号码：" + userUpdateVo.getUMobileNumber());

        Result result = userService.userUpdate(userUpdateVo) ?
                Result.success("用户信息修改成功", null) : Result.error("用户信息修改失败");

        if (result.isStatus()) {
            Session session = SecurityUtils.getSubject().getSession();
            Object object = session.getAttribute("CurrentUser");
            User user;
            try {
                user = castUtil.classCast(object, User.class);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new GeneralException("登录已过期");
            }
            if (user != null) {
                user.setUName(userUpdateVo.getUName());
                user.setUMobileNumber(userUpdateVo.getUMobileNumber());
            } else {
                session.removeAttribute("CurrentUser");
            }

            session.setAttribute("CurrentUser", user);
        }

        return result;
    }

    @PostMapping("/password")
    @ResponseBody
    public Result password(@Valid PasswordVo passwordVo) {
        //输入原密码加密验证
        String pwdOld = ShiroEncrypt.shiroEncrypt(passwordVo.getPasswordOld(), passwordVo.getSalt());
        //密码校验
        if (! pwdOld.equals(passwordVo.getPasswordReal())) {
            throw new GeneralException("原密码输入错误");
        } else if (! passwordVo.getPasswordNew().equals(passwordVo.getPwdConfirm())) {
            throw new GeneralException("两次输入密码不一致");
        }

        String pwdNew = ShiroEncrypt.shiroEncrypt(passwordVo.getPasswordNew(), passwordVo.getSalt());
        passwordVo.setPasswordNew(pwdNew);
        passwordVo.setPwdConfirm(pwdNew);

        return userService.password(passwordVo) ? Result.success("密码修改成功，请重新登录", null) : Result.error("密码修改失败");
    }
}
