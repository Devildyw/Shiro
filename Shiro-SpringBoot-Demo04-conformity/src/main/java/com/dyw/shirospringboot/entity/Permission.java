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
@TableName(value = "permission", autoResultMap = true)
public class Permission implements Serializable {
    private static final long serialVersionUID = 1111111L;

    @TableId(type = IdType.AUTO)
    private Integer pId;

    private String pName;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
