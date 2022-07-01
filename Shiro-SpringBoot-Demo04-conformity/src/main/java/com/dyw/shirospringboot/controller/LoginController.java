package com.dyw.shirospringboot.controller;

import com.dyw.shirospringboot.DTO.login.LoginDTO;
import com.dyw.shirospringboot.response.Result;
import com.dyw.shirospringboot.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Devil
 * @since 2022-06-30-23:56
 */
@RestController
public class LoginController {
    @Resource
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginDTO loginDTO){
        return null;
    }
}
