package com.dyw.shirospringboot.config.shiro.session;

import com.dyw.shirospringboot.utils.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author Devil
 * @since 2022-07-01-23:09
 */
@Slf4j
@Component
public class RedisSessionDAO extends AbstractSessionDAO {

    private static final String KEY = "session";
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session,sessionId);

        //将session存入redis中缓存
        redisTemplate.opsForValue().set(KEY+":"+session.getId().toString(),session,session.getTimeout(), TimeUnit.MILLISECONDS);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return sessionId==null?null: (Session) redisTemplate.opsForValue().get(KEY+":"+sessionId);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session!=null&&session.getId()!=null){
            redisTemplate.opsForValue().set(KEY+":"+session.getId().toString(),session,session.getTimeout(), TimeUnit.MILLISECONDS);

        }
    }

    @Override
    public void delete(Session session) {
        if (session!=null&&session.getId()!=null){
            redisTemplate.opsForValue().getOperations().delete(KEY+":"+session.getId().toString());
        }
    }

    /**
     * 获取所有还存活的Session 即获取redis中所有未过期的session
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions() {
        return redisTemplate.keys(KEY);
    }
}
