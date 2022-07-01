package com.dyw.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dyw.shiro.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Devil
 * @since 2022-06-22-22:14
 */
public interface UserMapper extends BaseMapper<User> {


    @Select("SELECT permission.p_name FROM ((`user` inner JOIN user_role on `user`.id = user_role.uid) INNER JOIN role_permission on user_role.rid = role_permission.rid) INNER JOIN permission on role_permission.pid = permission.p_id where user.username = #{username}")
    List<String> selectUserPermissionById(String username);

    @Select("select role.r_name  from (user inner join user_role on user.id = user_role.uid) inner join role on user_role.rid = role.r_id where user.username = #{username}")
    List<String> selectRoleById(String username);
}
