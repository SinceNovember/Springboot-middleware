package com.simple.schedule;

import com.simple.schedule.annotation.EnableDcsScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDcsScheduling
public class ApiTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiTestApplication.class, args);
    }
}
