package com.dyw.shiro.config.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author Devil
 * @since 2022-06-23-23:46
 */
public class DefinitionToken implements AuthenticationToken {
    private String username;

    private String password;


    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }



}
