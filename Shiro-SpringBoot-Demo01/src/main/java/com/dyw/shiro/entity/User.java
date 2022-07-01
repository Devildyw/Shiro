package com.dyw.shiro.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Devil
 * @since 2022-06-22-22:11
 */
@Data
@TableName(value = "user", autoResultMap = true)
public class User implements Serializable {
    private static final long serialVersionUID = 42L;

    private Integer id;

    private String username;

    private String password;
}
