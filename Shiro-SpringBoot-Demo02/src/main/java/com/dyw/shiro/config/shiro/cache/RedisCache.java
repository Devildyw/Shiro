//package com.dyw.shiro.config.shiro.cache;
//
//import lombok.NoArgsConstructor;
//import org.apache.shiro.cache.Cache;
//import org.apache.shiro.cache.CacheException;
//import org.cuit.epoch.util.RedisService;
//
//import javax.annotation.Resource;
//import java.util.Collection;
//import java.util.Set;
//
///**
// * @author Devil
// * @since 2022-06-24-23:17
// */
//@NoArgsConstructor
//public class RedisCache<k,V> implements Cache<k,V> {
////    @Resource
////    RedisService redisService;
//
//    private String cacheName;
//
//    public RedisCache (String cacheName){
//        this.cacheName = cacheName;
//    }
//
//    @Override
//    public V get(k k) throws CacheException {
//        System.out.println(cacheName+":获取缓存方法，传入参数：" + k+",此时的redisTemplate:"+redisService);
//        //获取缓存中数据时一定要为k加toStirng方法，否则会报错序列化的错
//        if (redisService.hGet(cacheName.toString(),k.toString())!=null){
//            return (V)redisService.hGet(cacheName.toString(),k.toString());
//        }
//        return null;
//    }
//
//    @Override
//    public V put(k k, V v) throws CacheException {
//        System.out.println("加入缓存方法，传入参数 K:" + k+",V:"+v);
//        //放入redis中的值，一定要是序列化的对象
//        redisService.hSet(cacheName.toString(),k.toString(),v);
//        return null;
//    }
//
//    @Override
//    public V remove(k k) throws CacheException {
//        System.out.println("调用了remove方法,传入参数："+k.toString());
//        redisService.hDel(cacheName.toString(),k.toString());
//        return null;
//    }
//
//    @Override
//    public void clear() throws CacheException {
//        System.out.println("调用了clear方法");
//        redisService.del(cacheName.toString());
//    }
//
//    @Override
//    public int size() {
//        return 1;
//    }
//
//    @Override
//    public Set<k> keys() {
//        return null;
//    }
//
//    @Override
//    public Collection<V> values() {
//        return null;
//    }
//}
