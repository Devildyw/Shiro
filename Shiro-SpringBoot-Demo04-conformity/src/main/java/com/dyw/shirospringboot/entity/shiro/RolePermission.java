package com.dyw.shirospringboot.entity.shiro;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Devil
 * @since 2022-06-30-18:55
 */
@Data
@TableName("role_permission")
public class RolePermission implements Serializable {
    private static final long serialVersionUID = 1111113L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer rid;

    private Integer pid;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
