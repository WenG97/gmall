package com.weng.gulimall.common.exception;

import com.weng.gulimall.common.execption.GmallException;
import com.weng.gulimall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GmallException.class)
    public Result gmallExceptionHandler(GmallException gmallException, Model model){

        gmallException.printStackTrace();
        Result<Object> result = new Result<>();
        result.setCode(gmallException.getCode());
        result.setMessage(gmallException.getMessage());
        // result.setCode(gmallException.getCode());
        result.setData(null);
        return result;
    }
}
