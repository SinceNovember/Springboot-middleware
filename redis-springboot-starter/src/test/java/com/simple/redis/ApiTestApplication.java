package com.simple.redis;

import com.simple.redis.service.IRedisService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiTestApplication {
    public static void main(String[] args) {
        IRedisService redisService = SpringApplication.run(ApiTestApplication.class, args).getBean("IRedisService", IRedisService.class);
        System.out.println(redisService.get("key_info_user"));
    }

}
