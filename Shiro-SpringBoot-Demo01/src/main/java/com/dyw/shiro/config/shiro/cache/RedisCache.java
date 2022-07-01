package com.dyw.shiro.config.shiro.cache;

import lombok.NoArgsConstructor;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

/**
 * @author Devil
 * @since 2022-06-24-23:17
 */
@Component
@NoArgsConstructor
public class RedisCache<k,V> implements Cache<k,V> {

    private String cacheName;

    @Resource
    RedisTemplate redisTemplate;

    public static RedisTemplate redisTemplateSelf;


    @PostConstruct
    public void getRedisTemplate(){
        this.redisTemplateSelf = redisTemplate;
    }
    public RedisCache (String cacheName){
        this.cacheName = cacheName;
    }

    @Override
    public V get(k k) throws CacheException {
        System.out.println(cacheName+":获取缓存方法，传入参数：" + k+",此时的redisTemplate:"+redisTemplateSelf);
        //获取缓存中数据时一定要为k加toStirng方法，否则会报错序列化的错
        if(null != redisTemplateSelf.opsForValue().get(cacheName.toString()+":"+k.toString())){
            return (V)redisTemplateSelf.opsForValue().get(cacheName.toString()+":"+k.toString());
        }
        return null;
    }

    @Override
    public V put(k k, V v) throws CacheException {
        System.out.println("加入缓存方法，传入参数 K:" + k+",V:"+v);
        //放入redis中的值，一定要是序列化的对象
        redisTemplateSelf.opsForValue().set(cacheName.toString()+":"+k.toString(),v);
        return null;
    }

    @Override
    public V remove(k k) throws CacheException {
        System.out.println("调用了remove方法,传入参数："+k.toString());
        Object andDelete = redisTemplateSelf.opsForValue().getAndDelete(cacheName.toString() + ":" + k.toString());
        return (V) andDelete;
    }

    @Override
    public void clear() throws CacheException {
        System.out.println("调用了clear方法");
        redisTemplateSelf.delete(cacheName);
    }

    @Override
    public int size() {
        return redisTemplateSelf.opsForHash().size(cacheName).intValue();
    }

    @Override
    public Set<k> keys() {
        return redisTemplateSelf.opsForHash().keys(cacheName);
    }

    @Override
    public Collection<V> values() {
        return redisTemplateSelf.opsForHash().values(cacheName);    }
}