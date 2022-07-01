package com.dyw.shirospringboot.response;

/**
 * @author Devil
 * @since 2022-06-30-23:17
 */
public class R {
    public static <T> Result<T> success(T data){
        return new Result<>(true,200,"操作成功",data);
    }

    public static Result<String> fail(Integer code, String message){
        return new Result<>(false,code,message,null);
    }
}
