package com.dyw.shirospringboot.config.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.dyw.shirospringboot.response.R;
import com.dyw.shirospringboot.response.Result;
import com.dyw.shirospringboot.utils.ApplicationContextUtil;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author Devil
 * @since 2022-07-02-18:16
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter {
    /**
     * @Description 是否允许访问
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    /**
     * @Description 访问拒绝时调用
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //判断当前请求头中是否带有jwtToken的字符串
        String jwtToken = WebUtils.toHttp(request).getHeader("token");
        //全局token刷新
        RedisTemplate redisTemplate = ApplicationContextUtil.getBean(RedisTemplate.class);
        //判断是key否存在
        if (redisTemplate.hasKey("token:"+jwtToken)){
            //存在则刷新存在时间
            String sessionId = (String) redisTemplate.opsForValue().get("token:" + jwtToken);
            if (redisTemplate.hasKey("session:"+sessionId)){
                redisTemplate.expire("session:"+sessionId,3600 * 1000, TimeUnit.MILLISECONDS);
                redisTemplate.expire("token:" + jwtToken,3600 * 1000, TimeUnit.MILLISECONDS);
                return true;
            }else{
                redisTemplate.delete("token:" + jwtToken);
            }
        }
        Result<String> result = R.fail(997, "没有登录");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONObject.toJSONString(result));

        return false;

    }
}
