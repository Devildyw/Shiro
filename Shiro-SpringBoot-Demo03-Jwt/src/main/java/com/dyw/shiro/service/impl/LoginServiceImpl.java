package com.dyw.shiro.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dyw.shiro.config.jwt.JwtTokenUtil;
import com.dyw.shiro.config.shiro.DTO.LoginDTO;
import com.dyw.shiro.entity.User;
import com.dyw.shiro.mapper.UserMapper;
import com.dyw.shiro.response.Result;
import com.dyw.shiro.response.ResultUtil;
import com.dyw.shiro.service.LoginService;
import com.dyw.shiro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Devil
 * @since 2022-06-22-22:52
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Resource
    RedissonClient redissonClient;

    @Autowired
    UserService userService;


    @Override
    public Boolean login(LoginDTO loginDTO) {
        Subject subject = SecurityUtils.getSubject();
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        subject.login(token);
        if (subject.isAuthenticated()) {
            return true;
        }
        return false;
    }

    @Resource
    UserMapper userMapper;
    @Override
    public Result loginForJwt(LoginDTO loginDTO) {
        Map<String, String> map = new HashMap<>();
        String jwtToken = null;
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(loginDTO.getUsername(), loginDTO.getPassword());
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            String shiroSessionId = (String) subject.getSession().getId();
            //登录后颁发的令牌
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getUsername,loginDTO.getUsername());
            User shiroUser = userMapper.selectOne(userLambdaQueryWrapper);
            jwtToken = JwtTokenUtil.IssuedToken("system", subject.getSession().getTimeout(),shiroSessionId,shiroUser.getId());
            map.put("jwtToken",jwtToken );
            log.info("jwtToken:{}",map.toString());
            //创建缓存
//            this.loadAuthorityToCache();
        } catch (Exception ex) {
            return ResultUtil.fail("登录失败");
        }

        return ResultUtil.succeed(map);
    }



}
