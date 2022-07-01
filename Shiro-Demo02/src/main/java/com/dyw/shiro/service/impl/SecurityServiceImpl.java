package com.dyw.shiro.service.impl;

import com.dyw.shiro.service.SecurityService;

/**
 * @author Devil
 * @since 2022-06-21-14:11
 * 权限服务层
 */
public class SecurityServiceImpl implements SecurityService {
    @Override
    public String findPasswordByLoginName(String loginName) {
        return "123";
    }
}
