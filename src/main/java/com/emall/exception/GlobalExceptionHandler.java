package com.emall.exception;

import com.emall.result.Result;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = GeneralException.class)
    public Result ExceptionHandler(HttpServletRequest request, GeneralException exception) {
        return Result.error(exception.getMessage());
    }

    @ExceptionHandler(value = BindException.class)
    public Result ExceptionHandler(HttpServletRequest request, BindException exception) {
        ObjectError error = exception.getAllErrors().get(0);
        return Result.error(error.getDefaultMessage());
    }

    @ExceptionHandler(value = UnknownAccountException.class)
    public Result ExceptionHandler(HttpServletRequest request, UnknownAccountException exception) {
        return Result.error(exception.getMessage());
    }

    @ExceptionHandler(value = IncorrectCredentialsException.class)
    public Result ExceptionHandler(HttpServletRequest request, IncorrectCredentialsException exception) {
        return Result.error(exception.getMessage());
    }
}
