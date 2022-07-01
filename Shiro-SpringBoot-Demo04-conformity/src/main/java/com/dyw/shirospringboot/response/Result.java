package com.dyw.shirospringboot.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Devil
 * @since 2022-06-30-23:16
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> {
    private boolean succeed;

    private Integer code;

    private String message;

    private T data;


}
