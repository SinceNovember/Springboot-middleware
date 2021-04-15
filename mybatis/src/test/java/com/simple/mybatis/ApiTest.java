package com.simple.mybatis;

import com.alibaba.fastjson.JSON;
import com.simple.mybatis.po.User;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

public class ApiTest {
    @Test
    public void test_queryUserInfoById() {
        String resource = "mybatis-config-datasource.xml";
        Reader reader;
        try {
            reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

            SqlSession session = sqlSessionFactory.openSession();
            try {
                User user = session.selectOne("com.simple.mybatis.dao.IUserDao.queryUserInfoById", 1L);
                System.out.println(JSON.toJSONString(user));
            }finally {
                session.close();
                reader.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
