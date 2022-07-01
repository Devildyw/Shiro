package com.dyw.shiro.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Devil
 * @since 2022-06-22-22:11
 */
@Data
@TableName(value = "user", autoResultMap = true)
public class User {
    private Integer id;

    private String username;

    private String password;
}
