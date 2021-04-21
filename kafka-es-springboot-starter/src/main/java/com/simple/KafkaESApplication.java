package com.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class KafkaESApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaESApplication.class);
    }
}
