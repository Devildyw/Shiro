package com.dyw.shiro.config.shiro.realm;

import com.dyw.shiro.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;


public class UserRealm extends AuthorizingRealm {


    @Autowired
    private UserService userService;

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

    /**
     * @Description 密码匹配器
     */
//    @PostConstruct
//    public void initCredentialsMatcher(){
//        HashedCredentialsMatcher md5 = new HashedCredentialsMatcher("MD5");
//        //设置提交的AuthenticationToken的凭据在与存储在系统中的凭据进行比较之前将被散列的次数。 根据用户注册是密码加密的次数的设置相映
//        setCredentialsMatcher(md5);
//    }


    public static void main(String[] args) {
        System.out.println(new SimpleHash("MD5", "123456").toString());
    }
}
