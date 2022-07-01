package com.dyw.shirospringboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Devil
 * @since 2022-06-30-18:33
 */
@Data
@NoArgsConstructor
@TableName(value = "User", autoResultMap = true)
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String mail;

    private String name;

    private Integer age;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public User(String username, String password, String mail, String name, Integer age) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.name = name;
        this.age = age;
    }
}
