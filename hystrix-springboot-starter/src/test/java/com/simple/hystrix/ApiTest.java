package com.simple.hystrix;


import com.simple.hystrix.test.interfaces.UserInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private URL base;


    @Before
    public void setUp() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        System.out.println(String.format("port is : [%d]", port));
        this.base = new URL(url);
    }


    @Test
    public void getUserByIdTest() throws Exception {

        ResponseEntity<UserInfo> response = this.restTemplate.getForEntity(
                this.base.toString() + "/api/queryUserInfo?userid=1", UserInfo.class, "");
        System.out.println(String.format("测试结果为：%s", response.getBody().toString()));
    }


}
