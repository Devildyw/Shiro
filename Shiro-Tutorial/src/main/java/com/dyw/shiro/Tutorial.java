package com.dyw.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 下面介绍JavaSE中 Shiro的使用 并且这里并没有使用外部的数据库等数据源 而是使用了shiro.ini的配置文件中的用户数据
 */
@SuppressWarnings("all")
public class Tutorial {

    private static final transient Logger log = LoggerFactory.getLogger(Tutorial.class);

    public static void main(String[] args) {
        log.info("My First Apache Shiro Application");

        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // get the currently executing user: 这里的subject用于获取当前对象该对象在web应用中可以是一个请求可以是一个线程
        Subject currentUser = SecurityUtils.getSubject();

        // Do some stuff with a Session (no need for a web or EJB container!!!)
        // 获取当前用户的session 如果是非web应用那么shiro使用的是shiro自带的session 如果是web应用那么session则使用http的
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");
        String value = (String) session.getAttribute("someKey");
        if (value.equals("aValue")) {
            log.info("Retrieved the correct value! [" + value + "]");
        }

        // let's login the current user so we can check against roles and permissions:
        // 如果用户没有登录 这里我们给他登录一下
        if (!currentUser.isAuthenticated()) {
            //通过用户的账户密码生成一个验证令牌
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
            //设置为记住我
            token.setRememberMe(true);
            try {
                //登录
                currentUser.login(token);
                //下面是登录失败的一些异常捕获 更具异常类名可以很清楚的理解含义
            } catch (UnknownAccountException uae) {
                log.info("There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                log.info("Password for account " + token.getPrincipal() + " was incorrect!");
            } catch (LockedAccountException lae) {
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
            }
        }

        //say who they are:
        //print their identifying principal (in this case, a username): 如果登录成功 理应走到这一步 打印出用户的用户名
        log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");

        //test a role: 这里当前用户是否由特定的角色
        if (currentUser.hasRole("schwartz")) {
            log.info("May the Schwartz be with you!");
        } else {
            log.info("Hello, mere mortal.");
        }

        //test a typed permission (not instance-level) 判断当前用户是不是有某种权限
        if (currentUser.isPermitted("lightsaber:wield")) {
            log.info("You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }

        //a (very powerful) Instance Level permission: 判断当前用户是不是有某种权限
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                    "Here are the keys - have fun!");
        } else {
            log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
        }

        //all done - log out! 登出
        currentUser.logout();
        //程序终止
        System.exit(0);
    }
}
