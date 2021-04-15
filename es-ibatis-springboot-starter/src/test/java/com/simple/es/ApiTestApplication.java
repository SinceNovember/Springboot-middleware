package com.simple.es;

import com.simple.es.infrastructure.dao.IUserDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"com.simple"})
public class ApiTestApplication {

    public static void main(String[] args) throws IOException {
        IUserDao userDao = SpringApplication.run(ApiTestApplication.class, args).getBean(IUserDao.class);
        System.out.println(userDao.queryUserInfoById(1L));



//        CreateIndexRequest request = new CreateIndexRequest("user");
//        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost("localhost", 9200, "http")
//                       ));
//        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
//        System.out.println(createIndexResponse);
//
//        // 初始化数据
//        IndexRequest indexRequest = new IndexRequest("user");
//        User user_01 = new User();
//        user_01.setId(1L);
//        user_01.setUserId("184172133");
//        user_01.setUserNickName("小傅哥");
//        user_01.setUserHead("01_50");
//        user_01.setUserPassword("123456");
//        user_01.setCreateTime(new java.util.Date());
//        user_01.setUpdateTime(new java.util.Date());
//        indexRequest.source(JSONObject.toJSONString(user_01), XContentType.JSON);
//        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

    }

}
