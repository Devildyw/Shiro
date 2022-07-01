package com.dyw.shiro.service.impl;

import com.dyw.shiro.DTO.LoginDTO;
import com.dyw.shiro.service.LoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

/**
 * @author Devil
 * @since 2022-06-22-22:52
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public Boolean login(LoginDTO loginDTO) {
        Subject subject = SecurityUtils.getSubject();
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        subject.login(token);
        if (subject.isAuthenticated()){
            return true;
        }
        return false;
    }
}
