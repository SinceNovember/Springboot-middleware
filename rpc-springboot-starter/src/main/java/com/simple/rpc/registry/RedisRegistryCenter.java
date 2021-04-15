package com.simple.rpc.registry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 模拟RPC注册中心
 */
public class RedisRegistryCenter {

    private static Jedis jedis;

    //初始化redis
    public static void init(String host, int port) {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setTestOnBorrow(false);
        JedisPool jedisPool = new JedisPool(config, host, port);
        jedis = jedisPool.getResource();
    }

    /**
     * 注册生产者
     * @param nozzle
     * @param alias
     * @param info
     * @return
     */
    public static Long registryProvider(String nozzle, String alias, String info) {
        return jedis.sadd(nozzle + "_" + alias, info);
    }

    /**
     * 获取生产者
     * @param nozzle
     * @param alias
     * @return
     */
    public static String obtainProvider(String nozzle, String alias) {
        if (jedis == null) {
            init("127.0.0.1", 6379);
        }
        return jedis.srandmember(nozzle + "_" + alias);
    }

    public static Jedis jedis() {
        return jedis;
    }

}
