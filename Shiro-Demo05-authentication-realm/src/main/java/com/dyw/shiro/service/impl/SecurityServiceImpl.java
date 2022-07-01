package com.dyw.shiro.service.impl;

import com.dyw.shiro.service.SecurityService;
import com.dyw.shiro.utils.DigestsUtil;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<String> findRoleByloginName(String loginName) {
        List<String> list = new ArrayList<>();
        list.add("admin");
        list.add("dev");
        return list;
    }

    @Override
    public List<String>  findPermissionByloginName(String loginName) {
        List<String> list = new ArrayList<>();
        list.add("order:add");
        list.add("order:list");
        list.add("order:del");
        return list;
    }
}
