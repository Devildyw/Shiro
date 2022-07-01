package com.dyw.shirospringboot.service.login.impl;

import com.dyw.shirospringboot.DTO.login.LoginDTO;
import com.dyw.shirospringboot.response.Result;
import com.dyw.shirospringboot.service.login.LoginService;
import org.springframework.stereotype.Service;

/**
 * @author Devil
 * @since 2022-07-01-0:04
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Override
    public Result login(LoginDTO loginDTO) {
        //todo: 进行登录操作 登录无误返回token到前端
        return null;
    }
}
