package com.simple.redis.service;

import com.simple.redis.annotation.XRedis;

@XRedis
public interface IRedisService {

    String get(String key);

    void set(String key, String val);

}
