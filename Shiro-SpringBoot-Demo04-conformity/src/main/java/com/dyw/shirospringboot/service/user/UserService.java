package com.dyw.shirospringboot.service.user;

import java.util.List;

/**
 * @author Devil
 * @since 2022-06-30-22:44
 */
public interface UserService {
    /**
     * 通过用户名查找到用户对应的密码
     * @param username 用户名
     * @return 密码
     */
    String selectPasswordByUserName(String username);

    /**
     * 通过用户名查找到用户的角色列表
     * @param username 用户名
     * @return 角色列表
     */
    List<String> selectUserRolesByUserName(String username);

    /**
     * 通过用户名查找到用户的权限信息列表
     * @param username 用户名
     * @return 权限信息列表
     */
    List<String> selectUserPermissionByUserName(String username);
}
