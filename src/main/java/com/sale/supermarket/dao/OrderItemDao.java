package com.sale.supermarket.dao;

import com.sale.supermarket.pojo.OrderItem;
import com.sale.supermarket.pojo.User;

public interface OrderItemDao {
    void delete();
    void update(OrderItem param);
    User get(String username, String password);
    void add();
}
