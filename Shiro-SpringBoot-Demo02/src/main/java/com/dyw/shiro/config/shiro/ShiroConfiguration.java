package com.dyw.shiro.config.shiro;

import com.dyw.shiro.config.shiro.filter.KickedOutAuthorizationFilter;
import com.dyw.shiro.config.shiro.realm.UserRealm;
import com.dyw.shiro.config.shiro.session.RedisSessionDAO;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        filterMap.put("/login","anon");
        filterMap.put("/user/add", "perms[insert]");
        filterMap.put("/user/update", "authc");
        filterMap.put("/**","kickedOut");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);


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
        securityManager.setSessionManager(defaultWebSessionManager(redisSessionDAO));
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    //创建 realm 对象( 步骤一
    @Bean
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(new HashedCredentialsMatcher("MD5"));
        userRealm.setAuthenticationCachingEnabled(true);
        userRealm.setAuthorizationCacheName("authenticationCache"); //设置缓存名称--认证
        userRealm.setAuthenticationCacheName("authorizationCache"); //设置缓存名称--授权
//        userRealm.setCacheManager(new RedisCacheManager());//设置为我们刚刚自定义的RedisCacheManager
        return userRealm;
    }

//    @Bean
//    public RedisCache getRedisCache(){
//        RedisCache redisCache = new RedisCache();
//        return redisCache;
//    }

    @Resource
    private KickedOutAuthorizationFilter kickedOutAuthorizationFilter;

    private Map<String, Filter> filters() {
        HashMap<String, Filter> map = new HashMap<>();
        map.put("kickedOut",kickedOutAuthorizationFilter);
        return map;
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
}
