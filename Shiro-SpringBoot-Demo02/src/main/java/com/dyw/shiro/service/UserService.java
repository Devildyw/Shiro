package com.dyw.shiro.service;

import java.util.List;

/**
 * @author Devil
 * @since 2022-06-22-22:15
 */

public interface UserService {
    /**
     * 根据用户名查找用户密码
     * @param username 用户名
     * @return password
     */
    String queryUserPasswordByUsername(String username);

    List<String> queryUserPermissionByUsername(String username);

    List<String> queryUserRoleByUsername(String username);
}
