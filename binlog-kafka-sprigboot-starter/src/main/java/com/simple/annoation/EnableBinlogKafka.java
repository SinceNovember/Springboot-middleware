package com.simple.annoation;

import com.simple.config.BinlogKafkaAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({BinlogKafkaAutoConfiguration.class})
@EnableAsync
public @interface EnableBinlogKafka {
}
