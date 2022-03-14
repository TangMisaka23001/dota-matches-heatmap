package com.misaka.service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {

    private int code;
    private T result;
    private String message;

    public static <T> Result<T> success(T data) {
        return new Result<>(0, data, "success");
    }

    public static Result<String> error(String message) {
        return new Result<>(1, message, "error");
    }
}
