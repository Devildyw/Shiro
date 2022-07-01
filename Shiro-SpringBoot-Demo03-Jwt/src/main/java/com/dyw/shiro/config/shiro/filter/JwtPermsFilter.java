package com.dyw.shiro.config.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.dyw.shiro.response.Result;
import com.dyw.shiro.response.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
/**
 * @Description：自定义jwt的资源校验
 */
public class JwtPermsFilter extends PermissionsAuthorizationFilter {

    /**
     * @Description 访问拒绝时调用
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        //判断当前请求头中是否带有jwtToken的字符串
        String jwtToken = WebUtils.toHttp(request).getHeader("token");
        //如果有：返回json的应答
        if (!StringUtils.isEmpty(jwtToken)){
            Result<String> result = ResultUtil.fail("没有权限");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSONObject.toJSONString(result));
            return false;
        }
        //如果没有：走原始方式
        return super.onAccessDenied(request, response);
    }
}
