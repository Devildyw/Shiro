package com.dyw.shirospringboot.handler;

import com.dyw.shirospringboot.response.R;
import com.dyw.shirospringboot.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Devil
 * @since 2022-07-01-0:08
 * 全局异常处理类
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result error(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return R.fail(999, e.getMessage());
    }
}
