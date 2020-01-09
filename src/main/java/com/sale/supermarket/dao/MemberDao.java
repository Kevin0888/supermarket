package com.sale.supermarket.dao;

import com.sale.supermarket.pojo.Member;
import com.sale.supermarket.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MemberDao {
    void delete();
    void update(Member param);
    List<Member> get(int id);
    void add(Member member);
}
