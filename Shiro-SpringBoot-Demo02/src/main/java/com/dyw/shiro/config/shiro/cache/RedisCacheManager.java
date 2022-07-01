//package com.dyw.shiro.config.shiro.cache;
//
//import io.lettuce.core.support.caching.RedisCache;
//import lombok.NoArgsConstructor;
//import org.apache.shiro.cache.Cache;
//import org.apache.shiro.cache.CacheException;
//import org.apache.shiro.cache.CacheManager;
//
///**
// * @author Devil
// * @since 2022-06-24-23:17
// */
//@NoArgsConstructor
//public class RedisCacheManager implements CacheManager {
//    @Override
//    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
//        System.out.println("进入到了自定义缓存管理器,传入参数cacheName："+ s);
//        return new RedisCache<K,V>(s);
//    }
//}
