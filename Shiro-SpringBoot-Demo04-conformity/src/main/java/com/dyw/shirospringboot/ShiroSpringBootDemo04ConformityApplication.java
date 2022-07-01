package com.dyw.shirospringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.dyw.shirospringboot.mapper")
@SpringBootApplication
public class ShiroSpringBootDemo04ConformityApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroSpringBootDemo04ConformityApplication.class, args);
    }

}
