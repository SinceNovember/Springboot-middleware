package com.simple.db.router.infrastructure;

import com.simple.db.router.infrastructure.dao.IUserDao;
import com.simple.db.router.infrastructure.po.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"com.simple"})
public class ApiTestApplication {

    public static void main(String[] args) {
        IUserDao userDao = SpringApplication.run(ApiTestApplication.class, args).getBean("IUserDao", IUserDao.class);
        User user = new User();
        user.setUserId("980765514");
        user.setUserNickName("小傅哥");
        user.setUserHead("01_50");
        user.setUserPassword("123456");
        userDao.insertUser(user);
//        System.out.println(userDao.queryUserInfoByUserId(user));
    }

}
