package com.dyw.shirospringboot.service.user.impl;

import com.dyw.shirospringboot.mapper.UserMapper;
import com.dyw.shirospringboot.service.user.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Devil
 * @since 2022-06-30-22:49
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public String selectPasswordByUserName(String username) {
        return userMapper.findUserPasswordByUserName(username);
    }

    @Override
    public List<String> selectUserRolesByUserName(String username) {
        return userMapper.findUserRolesByUserName(username);
    }

    @Override
    public List<String> selectUserPermissionByUserName(String username) {
        return userMapper.findUserPermissionByUserName(username);
    }
}
