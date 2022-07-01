package com.dyw.shiro.response;

import org.cuit.epoch.result.R;

/**
 * @author Devil
 * @since 2022-06-23-23:29
 */
public class ResultUtil {
    public static <T> Result<T> succeed(T data){
        return new Result<>(true,"成功",200,data);
    }

    public static Result<String> fail(String message){
        return new Result<>(false,message,1000,null);
    }
}
