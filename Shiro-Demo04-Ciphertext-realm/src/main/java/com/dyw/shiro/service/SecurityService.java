package com.dyw.shiro.service;

import java.util.Map;

/**
 * @author Devil
 * @since 2022-06-21-15:24
 */
public interface SecurityService {
    /**
     * 查找密码按用户登录名
     * @param loginName 登录名称
     * @return map
     */
    Map<String,String> findPasswordByLoginName(String loginName);
}
