package com.simple.mybatis.boot.dao;


import com.simple.mybatis.boot.po.User;

public interface IUserDao {
    User queryUserInfoById(Long id);

}
