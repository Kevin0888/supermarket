package com.sale.supermarket.dao;

import com.sale.supermarket.pojo.OrderItem;
import com.sale.supermarket.pojo.User;

public interface OrderItemDao {
    void delete();
    void update(OrderItem param);
    OrderItem get(int orderNum);
    void add(OrderItem orderItem);
}
