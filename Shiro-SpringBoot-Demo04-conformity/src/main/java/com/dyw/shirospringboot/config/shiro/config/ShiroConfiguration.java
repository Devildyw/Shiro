package com.dyw.shirospringboot.config.shiro.config;

import com.dyw.shirospringboot.config.shiro.cache.RedisCacheManager;
import com.dyw.shirospringboot.config.shiro.filter.KickedOutAuthorizationFilter;
import com.dyw.shirospringboot.config.shiro.filter.MyFormAuthenticationFilter;
import com.dyw.shirospringboot.config.shiro.filter.MyPermissionsAuthorizationFilter;
import com.dyw.shirospringboot.config.shiro.filter.MyRolesAuthorizationFilter;
import com.dyw.shirospringboot.config.shiro.passwordMatcher.RetryLimitCredentialsMatcher;
import com.dyw.shirospringboot.config.shiro.realm.UserRealm;
import com.dyw.shirospringboot.config.shiro.session.RedisSessionDAO;
import com.dyw.shirospringboot.config.shiro.session.ShiroSessionManager;
import com.dyw.shirospringboot.utils.DigestsUtil;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.*;

/**
 * @author Devil
 * @since 2022-06-30-19:08
 */
@Configuration
public class ShiroConfiguration {
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        shiroFilterFactoryBean.setFilters(filters());


        Map<String, String> filters = new LinkedHashMap<>();
        filters.put("/mail", "Design-Roles[123]");
        filters.put("/login", "anon");
        filters.put("/logout","Design-Authc");
        filters.put("/register", "anon");
        filters.put("/**", "kick-out");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filters);
//        shiroFilterFactoryBean.setGlobalFilters(globalFilters());

        return shiroFilterFactoryBean;
    }


    @Bean("securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(UserRealm userRealm, ShiroSessionManager shiroSessionManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(shiroSessionManager);
        securityManager.setRealm(userRealm);
        return securityManager;
    }


    @Bean
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        //新建密码比较器 Shiro自带
        HashedCredentialsMatcher matcher = new RetryLimitCredentialsMatcher();
        //指定密码比较器的hash次数

        //指定密码比较器的算法名称 这里的所有参数都要与用户注册时密码的加密参数设置一致
        matcher.setHashAlgorithmName(DigestsUtil.ALGORITHM_NAME);
        matcher.setHashIterations(DigestsUtil.HASH_ITERATIONS);
        //设置此系统存储的凭证哈希解码依据 true表示为Hex false表示为Base64 因为注册时加密就使用的时SimpleHash的toString方法底层就为toHex
        matcher.setStoredCredentialsHexEncoded(true);
        userRealm.setCredentialsMatcher(matcher);

        userRealm.setAuthenticationCachingEnabled(true);
        userRealm.setAuthorizationCacheName("authenticationCache"); //设置缓存名称--认证
        userRealm.setAuthenticationCacheName("authorizationCache"); //设置缓存名称--授权
        userRealm.setCacheManager(new RedisCacheManager());

        return userRealm;
    }

    @Bean
    public SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("Devildyw.Shiro");
        simpleCookie.setMaxAge(3600 * 1000);
        return simpleCookie;
    }

    @Bean
    public ShiroSessionManager shiroSessionManager(RedisSessionDAO redisSessionDAO) {
        ShiroSessionManager sessionManager = new ShiroSessionManager();
        //设置session过期时间
        sessionManager.setGlobalSessionTimeout(3600 * 1000);
        sessionManager.setSessionIdCookie(simpleCookie());
        sessionManager.setSessionIdCookieEnabled(true);
        //设置是否删除无效会话
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(false);
        //添加我们自定义的SessionDAO
        sessionManager.setSessionDAO(redisSessionDAO);

        return sessionManager;
    }

    // 配置自定义拦截器 整合jwt后的重写的过滤器 控制同一时间同一用户在线人数过滤器
    @Resource
    KickedOutAuthorizationFilter kickedOutAuthorizationFilter;

    public Map<String, Filter> filters() {
        HashMap<String, Filter> map = new HashMap<>(16);
        map.put("kick-out", kickedOutAuthorizationFilter);
        map.put("Design-Authc", new MyFormAuthenticationFilter());
        map.put("Design-Perms", new MyPermissionsAuthorizationFilter());
        map.put("Design-Roles", new MyRolesAuthorizationFilter());
        return map;
    }

    public List<String> globalFilters() {
        ArrayList<String> filters = new ArrayList<>();
        filters.add("kick-out");
        return filters;
    }
}
