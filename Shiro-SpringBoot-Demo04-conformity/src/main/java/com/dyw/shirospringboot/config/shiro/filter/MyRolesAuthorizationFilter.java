package com.dyw.shirospringboot.config.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.dyw.shirospringboot.response.R;
import com.dyw.shirospringboot.response.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

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
        //如果有：返回json的应答
        if (!StringUtils.isEmpty(jwtToken)) {
            Result<String> result = R.fail(996, "没有角色");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSONObject.toJSONString(result));
            return false;
        }
        //如果没有：走原始方式
        return super.onAccessDenied(request, response);
    }
}