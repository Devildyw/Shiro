package com.dyw.shiro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

@SpringBootTest
class ShiroSpringBootDemo02ApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void contextLoads() {
        RedisAtomicInteger atomicInteger = new RedisAtomicInteger("ding", redisTemplate.getConnectionFactory());
        System.out.println(atomicInteger.get());
    }

}
