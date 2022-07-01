package com.dyw.shiro.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Devil
 * @since 2022-06-23-23:25
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> {
    private Boolean success;

    private String message;

    private Integer code;

    private T data;
}
