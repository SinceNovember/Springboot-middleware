package com.simple.mybatis.boot;

import com.simple.mybatis.boot.dao.IUserDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"com.simple.mybatis"})
public class ApiTest {

    public static void main(String[] args) {
        IUserDao userDao = SpringApplication.run(ApiTest.class, args).getBean("IUserDao", IUserDao.class);
        System.out.println(userDao.queryUserInfoById(1L));
    }

}

