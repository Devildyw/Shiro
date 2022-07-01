package com.dyw.shiro.config.shiro.realm;

import com.dyw.shiro.config.shiro.token.DefinitionToken;
import com.dyw.shiro.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class UserRealm extends AuthorizingRealm {


    @Autowired
    private UserService userService;

//    @Override
//    public boolean supports(AuthenticationToken token) {
//        return token instanceof DefinitionToken;
//    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String principal = (String) principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<String> roles = userService.queryUserRoleByUsername(principal);
        List<String> permissions = userService.queryUserPermissionByUsername(principal);

        authorizationInfo.addRoles(roles);
        authorizationInfo.addStringPermissions(permissions);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String principal = (String) token.getPrincipal();
        //通过我们配置的数据源查询用户密码
        String password = userService.queryUserPasswordByUsername(principal);
        if (StringUtils.isBlank(password)){
            throw new UnknownAccountException("账户不存在");
        }
        return new SimpleAuthenticationInfo(principal,password,getName());
    }

    public static void main(String[] args) {
        System.out.println(new SimpleHash("MD5", "123456").toString());
    }
}
