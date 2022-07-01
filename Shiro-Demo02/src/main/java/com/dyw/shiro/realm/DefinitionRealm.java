package com.dyw.shiro.realm;

import com.dyw.shiro.service.impl.SecurityServiceImpl;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author Devil
 * @since 2022-06-21-14:13
 * 声明自定义realm
 */
public class DefinitionRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 登录接口
     * @param token 传递登录token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //从AuthenticationToken中获得登录名称
        String principal = (String) token.getPrincipal();
        String password = new SecurityServiceImpl().findPasswordByLoginName(principal);
        if (" ".equals(password)||password==null){
            throw new UnknownAccountException("账户不存在");
        }
        //传递账号和和密码
        return new SimpleAuthenticationInfo(principal,password,getName());
    }
}
