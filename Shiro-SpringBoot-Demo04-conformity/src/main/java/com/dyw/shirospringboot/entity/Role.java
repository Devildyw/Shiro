package com.dyw.shirospringboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @author Devil
 * @since 2022-06-30-18:55
 */
@Data
@TableName(value = "role",autoResultMap = true)
public class Role {
    @TableId(value = "r_id",type = IdType.AUTO)
    private Integer rId;

    private String rName;

    private String rState;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
