package com.dyw.shirospringboot.controller;

import com.dyw.shirospringboot.DTO.login.LoginDTO;
import com.dyw.shirospringboot.response.Result;
import com.dyw.shirospringboot.service.login.LoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Devil
 * @since 2022-06-30-23:56
 */
@RestController
public class LoginController {
    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO){
        return loginService.login(loginDTO);
    }

    @GetMapping("/logout")
    public Result logout(HttpServletRequest request){
        return loginService.logout(request);
    }
}
