package com.dyw.shiro.config.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Collections;
import java.util.Set;

/**
 * @author Devil
 * @since 2022-06-24-18:50
 *
 * 原来的RolesAuthorizationFilter 需要满足指定的所有角色例如 roles["root","admin"] 需要满足root和admin
 * 自定义的RolesOrAuthorizationFilter 只需要满足其中一个即可
 */
public class RolesOrAuthorizationFilter extends AuthorizationFilter {


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //获取subject 底层调用的同样是SecurityUtils.getSubject()
        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;

        if (rolesArray.length==0||rolesArray==null){
            //对于角色没有要求直接返回true
            return true;
        }
        Set<String> roles = CollectionUtils.asSet(rolesArray);
        for (String role : roles) {
            //满足一个身份就返回true
            if (subject.hasRole(role)){
                return true;
            }
        }
        return false;
    }
}
