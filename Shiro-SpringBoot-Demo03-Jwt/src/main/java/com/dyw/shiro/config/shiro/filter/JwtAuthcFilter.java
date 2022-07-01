package com.dyw.shiro.config.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.dyw.shiro.config.jwt.JwtTokenUtil;
import com.dyw.shiro.response.Result;
import com.dyw.shiro.response.ResultUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author Devil
 * @since 2022-06-29-22:59
 *
 * @Description：自定义登录验证过滤器
 */
public class JwtAuthcFilter extends FormAuthenticationFilter {


    /**
     * @Description 是否允许访问
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //判断当前请求头中是否带有jwtToken的字符串
        String jwtToken = WebUtils.toHttp(request).getHeader("token");
        //如果有：走jwt校验
        if (!StringUtils.isEmpty(jwtToken)){
            Claims claims = JwtTokenUtil.decodeToken(jwtToken);
            if (!claims.isEmpty()){
                return super.isAccessAllowed(request, response, mappedValue);
            }else {
                return false;
            }
        }
        //没有携带token：走原始校验
        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * @Description 访问拒绝时调用
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //判断当前请求头中是否带有jwtToken的字符串
        String jwtToken = WebUtils.toHttp(request).getHeader("token");
        //如果有：返回json的应答
        if (!StringUtils.isEmpty(jwtToken)){
            Result<String> result = ResultUtil.fail("没有登录");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSONObject.toJSONString(result));
            return false;
        }
        //如果没有：走原始方式
        return super.onAccessDenied(request, response);
    }
}
