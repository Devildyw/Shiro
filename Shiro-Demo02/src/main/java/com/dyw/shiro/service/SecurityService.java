package com.dyw.shiro.service;

/**
 * @author Devil
 * @since 2022-06-21-14:09
 * 权限服务接口
 */
public interface SecurityService {
    /**
     * 查找密码按用户登录名
     * @param loginName 登录名
     * @return 密码
     */
    String findPasswordByLoginName(String loginName);
}
