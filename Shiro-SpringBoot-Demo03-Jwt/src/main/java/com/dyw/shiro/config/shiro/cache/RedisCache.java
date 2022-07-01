package com.dyw.shiro.config.shiro.cache;//package com.dyw.shiro.config.shiro.cache;

import com.dyw.shiro.util.ApplicationContextUtils;
import lombok.NoArgsConstructor;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collection;
import java.util.Set;

/**
 * @author Devil
 * @since 2022-06-24-23:17
 */
@NoArgsConstructor
public class RedisCache<k,V> implements Cache<k,V> {

    private String cacheName;

    public RedisCache (String cacheName){
        this.cacheName = cacheName;
    }

    //获取redis操作对象
    public RedisTemplate getRedisTemplate(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        return redisTemplate;
    }
    @Override
    public V get(k k) throws CacheException {
        System.out.println(cacheName+":获取缓存方法，传入参数：" + k+",此时的redisTemplate:"+getRedisTemplate());
        return (V) getRedisTemplate().opsForHash().get(cacheName,k.toString());
    }

    @Override
    public V put(k k, V v) throws CacheException {
        System.out.println("加入缓存方法，传入参数 K:" + k+",V:"+v);
        //放入redis中的值，一定要是序列化的对象
        getRedisTemplate().opsForHash().put(cacheName.toString(),k.toString(),v);
        return null;
    }

    @Override
    public V remove(k k) throws CacheException {
        System.out.println("调用了remove方法,传入参数："+k.toString());
        getRedisTemplate().opsForHash().delete(cacheName,k.toString());
        return null;
    }

    @Override
    public void clear() throws CacheException {
        System.out.println("调用了clear方法");
        getRedisTemplate().opsForHash().delete(cacheName);
    }

    @Override
    public int size() {
        return getRedisTemplate().opsForHash().size(cacheName).intValue();
    }

    @Override
    public Set<k> keys() {
        return getRedisTemplate().opsForHash().keys(cacheName);
    }

    @Override
    public Collection<V> values() {
        return getRedisTemplate().opsForHash().values(cacheName);
    }
}
