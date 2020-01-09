package com.sale.supermarket.dao;

import com.sale.supermarket.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserDao {
    void delete();
    void update(User param);
    User get(@Param("username") String username, @Param("password") String password);
    void add();
}
