package com.dyw.shirospringboot;

import com.dyw.shirospringboot.mapper.UserMapper;
import com.dyw.shirospringboot.utils.DigestsUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest
class ShiroSpringBootDemo04ConformityApplicationTests {
    @Resource
    UserMapper userMapper;

    @Test
//6906747cdab95d238531de7bed006190
    void contextLoads() {
        Map<String, String> map = DigestsUtil.encryptPassword("123456");
        System.out.println(map.get("password"));
        System.out.println(map.get("salt"));
    }

}
