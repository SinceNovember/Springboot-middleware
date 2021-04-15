package com.simple.schedule.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration("simple-schedule-starterAutoConfig")
@EnableConfigurationProperties(StarterServiceProperties.class)
public class StarterAutoConfig {

    @Resource
    private StarterServiceProperties properties;

    public StarterServiceProperties getProperties() {
        return properties;
    }

    public void setProperties(StarterServiceProperties properties) {
        this.properties = properties;
    }

}
