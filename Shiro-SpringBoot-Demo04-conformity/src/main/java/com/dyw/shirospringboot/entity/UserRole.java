package com.dyw.shirospringboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Devil
 * @since 2022-06-30-18:55
 */
@Data
@TableName("user_role")
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1111115L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer uid;

    private Integer rid;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
