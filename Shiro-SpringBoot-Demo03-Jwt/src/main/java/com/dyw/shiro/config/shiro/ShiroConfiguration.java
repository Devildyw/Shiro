package com.dyw.shiro.config.shiro;

import com.dyw.shiro.config.jwt.JwtTokenUtil;
import com.dyw.shiro.config.jwt.session.ShiroSessionManager;
import com.dyw.shiro.config.shiro.cache.RedisCacheManager;
import com.dyw.shiro.config.shiro.filter.JwtAuthcFilter;
import com.dyw.shiro.config.shiro.filter.JwtPermsFilter;
import com.dyw.shiro.config.shiro.filter.JwtRolesFilter;
import com.dyw.shiro.config.shiro.filter.KickedOutAuthorizationFilter;
import com.dyw.shiro.config.shiro.realm.UserRealm;
import com.dyw.shiro.config.shiro.session.RedisSessionDAO;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Devil
 * @since 2022-06-22-20:25
 */
@Configuration
public class ShiroConfiguration {


    //创建 ShiroFilterFactoryBean
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilters(filters());
            /*
                添加Shiro内置过滤器，常用的有如下过滤器：
                anon： 无需认证就可以访问
                authc： 必须认证才可以访问
                user： 如果使用了记住我功能就可以直接访问
                perms: 拥有某个资源权限才可以访问
                role： 拥有某个角色权限才可以访问
                */
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/**","kickedOut");
        filterMap.put("/login-jwt","anon");
        filterMap.put("/jwt-prem","jwt-perms[insert]");
        filterMap.put("/login","anon");
        filterMap.put("/user/add", "jwt-perms[insert]");
        filterMap.put("/user/update", "authc");



        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        shiroFilterFactoryBean.setUnauthorizedUrl("/login");
        //修改到要跳转的login页面；
        shiroFilterFactoryBean.setLoginUrl("/toLogin");

        return shiroFilterFactoryBean;
    }


    //创建 DefaultWebSecurityManager( 步骤二
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm, RedisSessionDAO redisSessionDAO) {
        DefaultWebSecurityManager securityManager = new
                DefaultWebSecurityManager();
        //关联Realm
        securityManager.setRememberMeManager(null);
        securityManager.setSessionManager(shiroSessionManager(redisSessionDAO));
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    /**
     * @Description 会话管理器
     */
    @Bean(name="sessionManager")
    public ShiroSessionManager shiroSessionManager(RedisSessionDAO redisSessionDAO){
        ShiroSessionManager sessionManager = new ShiroSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        sessionManager.setSessionValidationSchedulerEnabled(false);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(simpleCookie());
        //设置超时
        sessionManager.setGlobalSessionTimeout(3600*1000);
        return sessionManager;
    }
    //创建 realm 对象( 步骤一
    @Bean
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(new HashedCredentialsMatcher("MD5"));
        userRealm.setAuthenticationCachingEnabled(true);
        userRealm.setAuthorizationCacheName("authenticationCache"); //设置缓存名称--认证
        userRealm.setAuthenticationCacheName("authorizationCache"); //设置缓存名称--授权
        userRealm.setCacheManager(new RedisCacheManager());//设置为我们刚刚自定义的RedisCacheManager
        return userRealm;
    }

//    @Bean
//    public RedisCache getRedisCache(RedisTemplate redisTemplate){
//        RedisCache redisCache = new RedisCache();
//        return redisCache;
//    }

    /**
     * @Description 自定义过滤器定义
     */
    private Map<String, Filter> filters() {
        Map<String, Filter> map = new HashMap<String, Filter>();
        map.put("kickedOut", kickedOutAuthorizationFilter);
        map.put("jwt-authc", new JwtAuthcFilter());
        map.put("jwt-perms", new JwtPermsFilter());
        map.put("jwt-roles", new JwtRolesFilter());
        return map;
    }
    @Resource
    private KickedOutAuthorizationFilter kickedOutAuthorizationFilter;


    /**
     * @Description 创建cookie对象
     */
    @Bean(name="sessionIdCookie")
    public SimpleCookie simpleCookie(){
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("ShiroSession");
        return simpleCookie;
    }

    @Bean
    public DefaultWebSessionManager defaultWebSessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setGlobalSessionTimeout(3600 * 1000);
        defaultWebSessionManager.setDeleteInvalidSessions(true);

        defaultWebSessionManager.setSessionDAO(redisSessionDAO);
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
//        defaultWebSessionManager.setDeleteInvalidSessions(true);
        /**
         * 修改Cookie中的SessionId的key，默认为JSESSIONID，自定义名称
         */
        defaultWebSessionManager.setSessionIdCookie(new SimpleCookie("JSESSIONID"));
        return defaultWebSessionManager;
    }

    /**
     * @Description 保证实现了Shiro内部lifecycle函数的bean执行
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * @Description AOP式方法级权限检查
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * @Description 配合DefaultAdvisorAutoProxyCreator事项注解权限校验
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return new AuthorizationAttributeSourceAdvisor();
    }
}
