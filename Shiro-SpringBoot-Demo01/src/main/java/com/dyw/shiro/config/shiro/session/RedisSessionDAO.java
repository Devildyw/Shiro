package com.dyw.shiro.config.shiro.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author Devil
 * @since 2022-06-28-17:43
 */
@Slf4j
@Component
public class RedisSessionDAO extends AbstractSessionDAO {
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session,sessionId);

        redisTemplate.opsForValue().set(session.getId().toString(),session,60, TimeUnit.MINUTES);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {

        return sessionId == null ? null : (Session) redisTemplate.opsForValue().get(sessionId.toString());
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session!=null&&session.getId()!=null){
            session.setTimeout(3600 * 1000);
        }
        redisTemplate.opsForValue().set(session.getId().toString(),session,60,TimeUnit.MINUTES);
    }

    @Override
    public void delete(Session session) {
        if (session!=null&session.getId()!=null){
            redisTemplate.opsForValue().getOperations().delete(session.getId().toString());
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return redisTemplate.keys("*");
    }
}
