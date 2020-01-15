package com.sale.supermarket.dao;

import com.sale.supermarket.pojo.MemberRecord;
import com.sale.supermarket.pojo.User;

import java.util.List;

public interface MemberRecordDao {
    void deleteRecord();
    void updateRecord(MemberRecord param);
    List<MemberRecord> getRecord();
    void addRecord(MemberRecord param);
}
