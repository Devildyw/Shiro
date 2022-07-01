package com.dyw.shiro.controller;

import com.dyw.shiro.config.shiro.DTO.LoginDTO;
import com.dyw.shiro.response.Result;
import com.dyw.shiro.response.ResultUtil;
import com.dyw.shiro.service.LoginService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Devil
 * @since 2022-06-29-23:15
 */
@RestController
public class JwtController {
    @Autowired
    LoginService loginService;

    @RequestMapping("/login-jwt")
    public Result LoginForJwt(@RequestBody LoginDTO loginDTO){
        return loginService.loginForJwt(loginDTO);
    }

    @GetMapping("/jwt-prem")
    @ResponseBody
    public Result premsForJwt(){
        System.out.println("123456");
        return ResultUtil.succeed("成功");
    }
}
