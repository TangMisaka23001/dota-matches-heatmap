package com.misaka.service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {

    private int code;
    private T data;
    private String message;

    public static <T> Result<T> success(T data) {
        return new Result<>(0, data, "success");
    }
}
