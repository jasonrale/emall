package com.emall.result;

import lombok.*;

/**
 * RESTFUL接口Json数据格式
 * @param <T>
 */
@Data
public class Result<T> {
    private boolean status;
    private String msg;
    private T obj;

    private Result(boolean status, String msg, T obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public static<T> Result<T> success(String msg, T obj) {
        return new Result<T>(true, msg, obj);
    }

    public static<T> Result<T> error(String msg) {
        return new Result<T>(false, msg, null);
    }
}
