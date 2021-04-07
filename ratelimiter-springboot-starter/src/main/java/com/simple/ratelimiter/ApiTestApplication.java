package com.simple.ratelimiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.simple.ratelimiter.*"})
public class ApiTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiTestApplication.class, args);
    }
}
