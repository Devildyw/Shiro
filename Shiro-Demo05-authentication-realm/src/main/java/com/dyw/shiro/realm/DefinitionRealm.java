package com.dyw.shiro.realm;

import com.dyw.shiro.service.SecurityService;
import com.dyw.shiro.service.impl.SecurityServiceImpl;
import com.dyw.shiro.utils.DigestsUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.List;
import java.util.Map;

/**
 * @author Devil
 * @since 2022-06-21-14:13
 * 声明自定义realm
 */
public class DefinitionRealm extends AuthorizingRealm {
    public DefinitionRealm() {
        //指定密码匹配方式为sha1
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(DigestsUtil.SHA256);
        //指定密码迭代次数
        matcher.setHashIterations(DigestsUtil.ITERATIONS);
        //使用父亲方法使匹配方式生效
        setCredentialsMatcher(matcher);
    }

    /**
     * @Description 授权方法
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //拿到用户认证凭证信息
        String loginName = (String) principals.getPrimaryPrincipal();
        //从数据库中查询对应的角色和资源
        SecurityService securityService = new SecurityServiceImpl();
        List<String> roles = securityService.findRoleByloginName(loginName);
        List<String> permissions = securityService.findPermissionByloginName(loginName);
        //构建资源校验
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(roles);
        authorizationInfo.addStringPermissions(permissions);
        return authorizationInfo;
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
        SecurityServiceImpl securityService = new SecurityServiceImpl();
        Map<String, String> map = securityService.findPasswordByLoginName(principal);
        if (map.isEmpty()){
            throw new UnknownAccountException("账户不存在");
        }
        String salt = map.get("salt");
        String password = map.get("password");
        //传递账号和和密码:参数1：缓存对象，参数2：明文密码，参数三：字节salt,参数4：当前DefinitionRealm名称
        return new SimpleAuthenticationInfo(principal,password, ByteSource.Util.bytes(salt),getName());
    }
}
