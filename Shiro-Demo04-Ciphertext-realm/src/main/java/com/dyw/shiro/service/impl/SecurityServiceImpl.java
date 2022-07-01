package com.dyw.shiro.service.impl;

import com.dyw.shiro.service.SecurityService;
import com.dyw.shiro.utils.DigestsUtil;

import java.util.Map;

/**
 * @author Devil
 * @since 2022-06-21-15:24
 */
public class SecurityServiceImpl implements SecurityService {
    @Override
    public Map<String, String> findPasswordByLoginName(String loginName) {
        return DigestsUtil.entryPtPassword("123");
    }
}
