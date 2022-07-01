package com.dyw.shirospringboot.controller;

import com.dyw.shirospringboot.DTO.register.RegisterDTO;
import com.dyw.shirospringboot.response.Result;
import com.dyw.shirospringboot.service.register.RegisterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Devil
 * @since 2022-06-30-23:56
 */
@RestController
public class RegisterController {
    @Resource
    private RegisterService registerService;

    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO){
        return registerService.register(registerDTO);
    }
}
