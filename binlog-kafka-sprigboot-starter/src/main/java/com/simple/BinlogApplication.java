package com.simple;

import com.simple.annoation.EnableBinlogKafka;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBinlogKafka
public class BinlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BinlogApplication.class, args);
    }
}
