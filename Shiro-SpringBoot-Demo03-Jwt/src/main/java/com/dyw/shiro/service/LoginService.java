package com.dyw.shiro.service;


import com.dyw.shiro.config.shiro.DTO.LoginDTO;
import com.dyw.shiro.response.Result;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;

import java.util.Map;

/**
 * @author Devil
 * @since 2022-06-22-22:51
 */

public interface LoginService {

    Boolean login(LoginDTO loginDTO);

    Result loginForJwt(LoginDTO loginDTO);
}
