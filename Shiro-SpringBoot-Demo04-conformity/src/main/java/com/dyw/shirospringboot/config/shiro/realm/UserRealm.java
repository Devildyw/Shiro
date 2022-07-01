package com.dyw.shirospringboot.config.shiro.realm;

import com.dyw.shirospringboot.config.redis.MyByteSource;
import com.dyw.shirospringboot.entity.User;
import com.dyw.shirospringboot.service.user.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Devil
 * @since 2022-06-30-22:39
 */
public class UserRealm extends AuthorizingRealm {
    @Resource
    UserService userService;

    /**
     * 查询出授权和角色信息
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();

        //从数据库中获取得到用户的权限信息
        List<String> permissions = userService.selectUserPermissionByUserName(username);
        List<String> roles = userService.selectUserRolesByUserName(username);


        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        authorizationInfo.addStringPermissions(permissions);
        authorizationInfo.addRoles(roles);

        return authorizationInfo;
    }

    /**
     * 查询出登录验证信息
     * @param token the authentication token containing the user's principal and credentials.
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        token = (UsernamePasswordToken) token;
        String username = (String) token.getPrincipal();

        User user = userService.selectUserByUserName(username);
        if (user == null) {
            throw new UnknownAccountException("该账户不存在");
        }
        return new SimpleAuthenticationInfo(username, user.getPassword(), MyByteSource.Util.bytes(user.getSalt()), this.getName());
    }
}
