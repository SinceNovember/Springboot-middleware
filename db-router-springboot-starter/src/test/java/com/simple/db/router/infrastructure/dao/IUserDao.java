package com.simple.db.router.infrastructure.dao;

import com.simple.db.router.annotation.DBRouter;
import com.simple.db.router.infrastructure.po.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserDao {
    @DBRouter(key = "userId")
    User queryUserInfoByUserId(User req);

    @DBRouter(key = "userId")
    void insertUser(User req);
}
