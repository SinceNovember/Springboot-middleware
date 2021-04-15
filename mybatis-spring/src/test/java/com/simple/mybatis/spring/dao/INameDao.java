package com.simple.mybatis.spring.dao;

import com.simple.mybatis.spring.po.User;

public interface INameDao {
    User queryUserInfoById(Long id);

}
