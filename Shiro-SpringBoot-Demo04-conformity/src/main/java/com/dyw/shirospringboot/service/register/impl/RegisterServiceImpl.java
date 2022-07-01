package com.dyw.shirospringboot.service.register.impl;

import com.dyw.shirospringboot.DTO.register.RegisterDTO;
import com.dyw.shirospringboot.entity.User;
import com.dyw.shirospringboot.mapper.UserMapper;
import com.dyw.shirospringboot.response.R;
import com.dyw.shirospringboot.response.Result;
import com.dyw.shirospringboot.service.register.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Devil
 * @since 2022-07-01-0:00
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    @Resource
    private UserMapper userMapper;
    @Override
    public Result register(RegisterDTO registerDTO) {
        String username = registerDTO.getUsername();
        String password = registerDTO.getPassword();
        String name = registerDTO.getName();
        String mail = registerDTO.getMail();
        Integer age = registerDTO.getAge();

        //todo: 检验请求参数

        //todo:密码进行密文加密
        User user = new User(username, password, name, mail, age);

        userMapper.insert(user);

        return R.success(null);
    }
}
