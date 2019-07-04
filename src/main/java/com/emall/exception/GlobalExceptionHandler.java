package com.emall.exception;

import com.emall.result.Result;
import com.sun.xml.internal.fastinfoset.util.ValueArrayResourceException;
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
        String msg = error.getDefaultMessage();
        return Result.error(msg);
    }
}
