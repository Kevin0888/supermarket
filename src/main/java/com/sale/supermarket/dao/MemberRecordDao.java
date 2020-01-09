package com.sale.supermarket.dao;

import com.sale.supermarket.pojo.MemberRecord;
import com.sale.supermarket.pojo.User;

public interface MemberRecordDao {
    void delete();
    void update(MemberRecord param);
    User get(String username, String password);
    void add();
}
