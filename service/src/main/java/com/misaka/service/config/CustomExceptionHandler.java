package com.misaka.service.config;


import com.misaka.service.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

  @ExceptionHandler
  public Result<String> exception(Exception e) {
    e.printStackTrace();
    return Result.error(e.getMessage());
  }

}
