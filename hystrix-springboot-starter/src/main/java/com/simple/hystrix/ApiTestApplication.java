package com.simple.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan(basePackages = {"com.simple.hystrix.*"})
public class ApiTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiTestApplication.class, args);
    }
}
