package com.simple.rpc.annotation;

import com.simple.rpc.config.ServerAutoConfiguration;
import com.simple.rpc.config.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableConfigurationProperties(ServerProperties.class)
@Import({ServerAutoConfiguration.class})
@ComponentScan("com.simple.*")
public @interface EnableRpc {
}
