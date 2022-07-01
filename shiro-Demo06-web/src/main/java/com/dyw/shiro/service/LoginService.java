package com.dyw.shiro.service;

import org.apache.shiro.authc.UsernamePasswordToken;

import java.lang.management.LockInfo;

/**
 * @Description：登录服务
 */
public interface LoginService {

    /**
     * @Description 登录方法
     * @param token 登录对象
     * @return
     */
    boolean login(UsernamePasswordToken token);

    /**
     * @Description 登出方法
     */
    void logout();
}
