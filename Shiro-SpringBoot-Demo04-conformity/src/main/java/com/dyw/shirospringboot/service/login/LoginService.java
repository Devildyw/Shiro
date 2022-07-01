package com.dyw.shirospringboot.service.login;

import com.dyw.shirospringboot.DTO.login.LoginDTO;
import com.dyw.shirospringboot.response.Result;

/**
 * @author Devil
 * @since 2022-07-01-0:03
 */
public interface LoginService {

    Result login(LoginDTO loginDTO);
}
