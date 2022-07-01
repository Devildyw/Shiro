package com.dyw.shirospringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dyw.shirospringboot.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Devil
 * @since 2022-06-30-19:04
 */
public interface UserMapper extends BaseMapper<User> {
    @Select("select password from shiro.user where username = #{username} limit 1")
    String findUserPasswordByUserName(String username);

    @Select("select * from ((shiro.user inner join shiro.user_role on user.id = user_role.uid) inner join shiro.role on user_role.rid = r_id) where username = #{username}")
    List<String> findUserRolesByUserName(String username);

    @Select("select * from ((((shiro.user inner join shiro.user_role on user.id = user_role.uid) inner join shiro.role on user_role.rid = r_id) inner join shiro.role_permission on role_permission.rid = r_id) inner join shiro.permission on p_id=role_permission.pid) where username = #{username}")
    List<String> findUserPermissionByUserName(String username);
}
