package com.simple.whitelist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 加载配置文件
 */
@ConfigurationProperties("simple.whitelist")
public class WhiteListProperties {
    private String users;

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
}
