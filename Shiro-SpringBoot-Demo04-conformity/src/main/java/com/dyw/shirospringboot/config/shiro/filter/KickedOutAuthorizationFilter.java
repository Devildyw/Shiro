package com.dyw.shirospringboot.config.shiro.filter;

import com.dyw.shirospringboot.config.shiro.session.RedisSessionDAO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.redisson.api.RDeque;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @author Devil
 * @since 2022-07-02-17:34
 */
@Slf4j
@Component
public class KickedOutAuthorizationFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private DefaultWebSessionManager defaultWebSessionManager;

    @Resource
    private RedisSessionDAO redisSessionDAO;

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);

        if (!subject.isAuthenticated()){
            //如果没有登录,直接进行之后的流程处理
            return true;
        }

        String sessionId = (String) subject.getSession().getId();
        String loginName = (String) subject.getPrincipal();

        //通过Redisson获取一个Redis中的队列 如果没有就初始化一个
        RDeque<Object> deque = redissonClient.getDeque("KickedOutAuthorization:" + loginName);
        //判断sessionId是否存在于此用户的队列中
        boolean flag = deque.contains(sessionId);
        //如果不存在就将该SessionId存入redis的队列中
        if (!flag){
            deque.addLast(sessionId);
        }

        //如果此时队列大于1(说明该用户同时在线人数超过1人),则开始踢人
        if (deque.size()>1){
            sessionId = (String) deque.getFirst();
            //队列中移除第一个登录的用户
            deque.removeFirst();
            //除了队列中需要移除外 为了让用户被踢出去 还需要删除Redis中的对应sessionId的session
            Session session = null;
            try{
                session = defaultWebSessionManager.getSession(new DefaultSessionKey(sessionId));
            }catch (UnknownSessionException e){
                log.info("session已经失效了");
            }catch (ExpiredSessionException e){
                log.info("session已经过期了");
            }
            if (session!=null){
                redisSessionDAO.delete(session);
            }
        }
        return true;
    }
}
