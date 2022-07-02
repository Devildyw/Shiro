package com.dyw.shirospringboot.config.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.dyw.shirospringboot.response.R;
import com.dyw.shirospringboot.response.Result;
import com.dyw.shirospringboot.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author Devil
 * @since 2022-07-02-18:16
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter {
//    /**
//     * @Description 是否允许访问
//     */
//    @Override
//    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//        //这里是用来判断用户是否有权限访问的 但是我们这里不做权限判断 只是为了判断用户是否右token
//    }

    /**
     * @Description 访问拒绝时调用
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //判断当前请求头中是否带有jwtToken的字符串
        String jwtToken = WebUtils.toHttp(request).getHeader("token");
        //如果有：返回json的应答
        if (!StringUtils.isEmpty(jwtToken)){
            Result<String> result = R.fail(997,"没有登录");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSONObject.toJSONString(result));
            return false;
        }
        //如果没有：走原始方式
        return super.onAccessDenied(request, response);
    }
}
