package com.simple.config;

import com.google.gson.GsonBuilder;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * jest客户端
 */
@Configuration
public class ElasticConfig {

    @Value("${es.host}")
    public String host;


    @Value("${es.scheme}")
    public String scheme;

    /**
     * 建立客户端
     * @return
     */

    @Bean
    public JestClient jestClient() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder(host)
                .multiThreaded(true)
                //一个route 默认不超过2个连接  路由是指连接到某个远程注解的个数。总连接数=route个数 * defaultMaxTotalConnectionPerRoute
                .defaultMaxTotalConnectionPerRoute(2)
                //所有route连接总数
                .maxTotalConnection(2)
                .connTimeout(10000)
                .readTimeout(10000)
                .gson(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create())
                .build());
        return factory.getObject();
    }

}
