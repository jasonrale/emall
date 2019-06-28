package com.emall.result;

import lombok.Getter;

@Getter
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static<T> Result<T> success(T data) {
        return new Result<T>(1, "success", data);
    }

    public static<T> Result<T> error(T data) {
        return new Result<T>(0, "error", data);
    }
}
