package com.dyw.shiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.dyw.shiro.mapper")
@SpringBootApplication
public class ShiroSpringBootDemo02Application {

    public static void main(String[] args) {
        SpringApplication.run(ShiroSpringBootDemo02Application.class, args);
    }

}
