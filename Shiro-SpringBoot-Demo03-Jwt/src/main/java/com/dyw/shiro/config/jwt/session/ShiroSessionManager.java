package com.dyw.shiro.config.jwt.session;

import com.dyw.shiro.config.jwt.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @author Devil
 * @since 2022-06-29-22:50
 */
@Slf4j
public class ShiroSessionManager extends DefaultWebSessionManager {

    private static final String AUTHORIZATION = "token";

    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    public ShiroSessionManager(){
        super();
    }


    /**
     * 这个方法主要是让我们自定义获取sessionId的方法 在将sessionId返回让后续的流程去获取session 然后鉴权
     * @param request
     * @param response
     * @return
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response){
        String jwtToken = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        if(StringUtils.isEmpty(jwtToken)){
            //如果没有携带id参数则按照父类的方式在cookie进行获取
            return super.getSessionId(request, response);
        }else{
            //如果请求头中有 authToken 则其值为jwtToken，然后解析出会话id sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,REFERENCED_SESSION_ID_SOURCE);
            Claims decode = JwtTokenUtil.decodeToken(jwtToken);
            //获取我们创建jwt时填入的sessionId
            String id = (String) decode.get("jti");
            log.info(id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID,id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID,Boolean.TRUE);
            return id;
        }
    }

}
