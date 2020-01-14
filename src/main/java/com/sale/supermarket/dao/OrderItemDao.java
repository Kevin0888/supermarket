package com.sale.supermarket.dao;

import com.sale.supermarket.pojo.OrderItem;
import com.sale.supermarket.pojo.User;
import com.sale.supermarket.utils.OrderItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemDao {
    void delete();
    void update(OrderItem param);
    List<OrderItem> get(int orderNum);
    void add(OrderItem orderItem);

    List<OrderItemVO> getAllOrder(@Param("shopNum") int shopNum);
}
