package com.dyw.shiro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dyw.shiro.entity.User;
import com.dyw.shiro.mapper.UserMapper;
import com.dyw.shiro.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Devil
 * @since 2022-06-22-22:17
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public String queryUserPasswordByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username).last("limit 1");
        return userMapper.selectOne(queryWrapper).getPassword();
    }

    @Override
    public List<String> queryUserPermissionByUsername(String username) {
        return userMapper.selectUserPermissionById(username);
    }

    @Override
    public List<String> queryUserRoleByUsername(String username) {
        return userMapper.selectRoleById(username);
    }
}
