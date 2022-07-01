package com.dyw.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;

public class HelloShiro {
    public static void main(String[] args) {
        //导入权限ini文件构建权限工厂
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        //工厂构建安全管理器
        SecurityManager securityManager = factory.getInstance();
        //使用SecurityUtils工具生效安全管理器
        SecurityUtils.setSecurityManager(securityManager);
        //使用SecurityUtils工具类获得主体
        Subject subject = SecurityUtils.getSubject();
        //构建账号token
        UsernamePasswordToken token = new UsernamePasswordToken("jay", "123");

        //登录操作
        subject.login(token);
        System.out.println("登录是否成功: "+subject.isAuthenticated());
        //登出操作
        subject.logout();
        System.out.println("注销是否成功: "+!subject.isAuthenticated());
    }
}
