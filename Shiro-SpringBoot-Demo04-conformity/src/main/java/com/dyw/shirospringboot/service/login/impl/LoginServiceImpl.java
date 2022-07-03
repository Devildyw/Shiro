package com.dyw.shirospringboot.service.login.impl;

import com.dyw.shirospringboot.DTO.login.LoginDTO;
import com.dyw.shirospringboot.config.shiro.session.RedisSessionDAO;
import com.dyw.shirospringboot.response.R;
import com.dyw.shirospringboot.response.Result;
import com.dyw.shirospringboot.service.login.LoginService;
import com.dyw.shirospringboot.service.user.UserService;
import com.dyw.shirospringboot.utils.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Devil
 * @since 2022-07-01-0:04
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private DefaultWebSessionManager defaultWebSessionManager;

    @Resource
    private RedisSessionDAO redisSessionDAO;

    @Override
    public Result login(LoginDTO loginDTO) {
        //进行登录操作 登录无误返回token到前端
        String password = loginDTO.getPassword();
        String username = loginDTO.getUsername();

        if (StringUtils.isEmpty(username)) {
            return R.fail(1001, "账号不能为空");
        }

        if (StringUtils.isEmpty(password)) {
            return R.fail(1001, "密码不能为空");
        }

        /*
        因为我们这里的逻辑是登录无误返回token到前端 token有需要过期时间 用户id 和 sessionId
        所以这里的步骤都是在为它服务
         */
        Subject subject = SecurityUtils.getSubject();
        //拼接Shiro token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //如果登录失败会自动抛出异常的 由全局异常类捕获
        subject.login(token);
        Session session = subject.getSession();
        String sessionId = (String) session.getId();

        String uToken = TokenUtils.createToken();

        HashMap<String, Object> map = new HashMap<>();
        redisTemplate.opsForValue().set("token:" + uToken, sessionId, session.getTimeout(), TimeUnit.MILLISECONDS);
        map.put("token", uToken);
        return R.success(map);
    }

    /**
     * 注销 清除redis中的缓存
     * @param request
     * @return
     */
    @Override
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        Session session = subject.getSession();
        redisTemplate.delete("token:"+token);
        redisTemplate.delete("KickedOutAuthorization:" + username);
        subject.logout();
        return R.success(null);
    }
}
