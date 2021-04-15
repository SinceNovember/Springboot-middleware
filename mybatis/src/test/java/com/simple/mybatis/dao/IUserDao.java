package com.simple.mybatis.dao;

import com.simple.mybatis.po.User;

public interface IUserDao {
    User queryUserInfoById(Long id);

}
