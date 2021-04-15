package com.simple;

import com.simple.rpc.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@Configuration
@EnableRpc
@ImportResource(locations = {"classpath:spring-config.xml"})
public class ApiTestApplication {
    public static void main(String[] args) {
        String[] names = SpringApplication.run(ApiTestApplication.class, args).getBeanDefinitionNames();
        System.out.println(names);
    }
}
