package com.sale.supermarket.dao;

import com.sale.supermarket.pojo.Order;
import com.sale.supermarket.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderDao {
    void delete();
    void update(Order order);
    List<Order> get();
    void add(Order order);
}
