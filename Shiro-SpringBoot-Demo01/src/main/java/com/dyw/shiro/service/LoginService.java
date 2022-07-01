package com.dyw.shiro.service;

import com.dyw.shiro.DTO.LoginDTO;

/**
 * @author Devil
 * @since 2022-06-22-22:51
 */

public interface LoginService {

    Boolean login(LoginDTO loginDTO);
}
