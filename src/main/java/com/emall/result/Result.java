package com.emall.result;

import lombok.*;

@Data
public class Result<T> {
    private boolean status;
    private String msg;
    private T data;

    private Result(boolean status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static<T> Result<T> success(String msg, T data) {
        return new Result<T>(true, msg, data);
    }

    public static<T> Result<T> error(String msg) {
        return new Result<T>(false, msg, null);
    }
}
