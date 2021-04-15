package com.simple.es.infrastructure.dao;


import com.simple.es.infrastructure.po.User;

public interface IUserDao {
    User queryUserInfoById(Long id);
}
