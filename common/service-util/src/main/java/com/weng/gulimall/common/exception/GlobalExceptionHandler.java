package com.weng.gulimall.common.exception;

import com.weng.gulimall.common.execption.GmallException;
import com.weng.gulimall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GmallException.class)
    public Result gmallExceptionHandler(GmallException gmallException){

        gmallException.printStackTrace();

        return Result.build(null, gmallException.getResultCodeEnum());
    }
}
