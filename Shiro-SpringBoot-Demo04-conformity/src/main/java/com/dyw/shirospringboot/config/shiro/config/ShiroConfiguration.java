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
        //????????????????????? Shiro??????
        HashedCredentialsMatcher matcher = new RetryLimitCredentialsMatcher();
        //????????????????????????hash??????

        //???????????????????????????????????? ??????????????????????????????????????????????????????????????????????????????
        matcher.setHashAlgorithmName(DigestsUtil.ALGORITHM_NAME);
        matcher.setHashIterations(DigestsUtil.HASH_ITERATIONS);
        //???????????????????????????????????????????????? true?????????Hex false?????????Base64 ????????????????????????????????????SimpleHash???toString??????????????????toHex
        matcher.setStoredCredentialsHexEncoded(true);
        userRealm.setCredentialsMatcher(matcher);

        userRealm.setAuthenticationCachingEnabled(true);
        userRealm.setAuthorizationCacheName("authenticationCache"); //??????????????????--??????
        userRealm.setAuthenticationCacheName("authorizationCache"); //??????????????????--??????
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
        //??????session????????????
        sessionManager.setGlobalSessionTimeout(3600 * 1000);
        sessionManager.setSessionIdCookie(simpleCookie());
        sessionManager.setSessionIdCookieEnabled(true);
        //??????????????????????????????
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(false);
        //????????????????????????SessionDAO
        sessionManager.setSessionDAO(redisSessionDAO);

        return sessionManager;
    }

    // ???????????????????????? ??????jwt???????????????????????? ???????????????????????????????????????????????????
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
