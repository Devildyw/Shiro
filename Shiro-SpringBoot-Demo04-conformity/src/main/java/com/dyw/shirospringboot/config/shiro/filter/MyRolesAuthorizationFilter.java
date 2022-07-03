package com.dyw.shirospringboot.config.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.dyw.shirospringboot.response.R;
import com.dyw.shirospringboot.response.Result;
import com.dyw.shirospringboot.utils.ApplicationContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Description 自定义jwt角色校验
 */
public class MyRolesAuthorizationFilter extends RolesAuthorizationFilter {

    /**
     * @Description 访问拒绝时调用
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        //判断当前请求头中是否带有jwtToken的字符串
        String jwtToken = WebUtils.toHttp(request).getHeader("token");
        RedisTemplate redisTemplate = ApplicationContextUtil.getBean(RedisTemplate.class);
        //如果没有：返回json的应答
        if (StringUtils.isEmpty(jwtToken)||(!redisTemplate.hasKey("token:" + jwtToken)||Boolean.FALSE.equals(redisTemplate.hasKey(redisTemplate.opsForValue().get("token:" + jwtToken))))) {
            redisTemplate.delete("token:" + jwtToken);
            Result<String> result = R.fail(996, "没有登录");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSONObject.toJSONString(result));
            return false;
        }
        //如果有：刷新token 并且返回没有权限
        else {
            System.out.println("刷新token存在时间");
            //存在则刷新存在时间
            String sessionId = (String) redisTemplate.opsForValue().get("token:" + jwtToken);
            redisTemplate.expire("session:" + sessionId, 3600 * 1000, TimeUnit.MILLISECONDS);
            redisTemplate.expire("token:" + jwtToken, 3600 * 1000, TimeUnit.MILLISECONDS);
        }

        Result<String> result = R.fail(996, "没有角色");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONObject.toJSONString(result));
        return false;
    }
}
