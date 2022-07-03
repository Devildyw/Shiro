package com.dyw.shirospringboot;

import com.dyw.shirospringboot.mapper.UserMapper;
import com.dyw.shirospringboot.utils.ApplicationContextUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
class ShiroSpringBootDemo04ConformityApplicationTests {
    @Resource
    UserMapper userMapper;

    @Test
//6906747cdab95d238531de7bed006190
    void contextLoads() {
        RedisTemplate bean = ApplicationContextUtil.getBean(RedisTemplate.class);
        System.out.println(bean);
    }

}
