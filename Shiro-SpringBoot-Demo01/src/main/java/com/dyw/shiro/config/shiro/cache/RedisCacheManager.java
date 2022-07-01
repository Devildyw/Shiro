package com.dyw.shiro.config.shiro.cache;

import lombok.NoArgsConstructor;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import javax.annotation.Resource;

/**
 * @author Devil
 * @since 2022-06-24-23:17
 */
public class RedisCacheManager implements CacheManager {
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        System.out.println("进入到了自定义缓存管理器,传入参数cacheName："+ s);
        return new RedisCache<>(s);
    }
}
