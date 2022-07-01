package com.dyw.shiro.config.shiro.cache;

import lombok.NoArgsConstructor;
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Devil
 * @since 2022-06-24-23:17
 */
@NoArgsConstructor
public class RedisCacheManager extends AbstractCacheManager {

    @Override
    protected Cache createCache(String s) throws CacheException {
        return new RedisCache(s);
    }
}
