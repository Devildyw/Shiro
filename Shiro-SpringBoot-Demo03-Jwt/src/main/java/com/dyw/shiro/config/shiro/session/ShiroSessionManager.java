package com.dyw.shiro.config.shiro.session;


import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

import java.io.Serializable;

/**
 * @author Devil
 * @since 2022-06-29-22:20
 */
public class ShiroSessionManager extends DefaultWebSessionManager {
    @Override
    public Serializable getSessionId(SessionKey key) {
        return super.getSessionId(key);
    }
}
