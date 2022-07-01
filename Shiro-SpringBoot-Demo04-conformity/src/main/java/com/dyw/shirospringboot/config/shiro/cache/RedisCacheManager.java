package com.dyw.shirospringboot.config.shiro.cache;

import lombok.NoArgsConstructor;
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

/**
 * @author Devil
 * @since 2022-07-01-22:27
 */
@NoArgsConstructor
public class RedisCacheManager extends AbstractCacheManager {
    @Override
    protected Cache createCache(String s) throws CacheException {
        return new RedisCache(s);
    }
}
