package com.sale.supermarket.service;


import com.sale.supermarket.dao.*;
import com.sale.supermarket.pojo.*;
import com.sale.supermarket.utils.OrderItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author aaa
 */
@Service
public class SupermarketService {
    @Autowired
    SuperMarketDao superMarketDao;
    @Autowired
    UserDao userDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    CommodityDao commodityDao;




    public User getUser(String username, String password) {
        return userDao.get(username, password);
    }

    public Commodity getCommodity(int commodityId) {
        return commodityDao.getCommodity(commodityId);
    }

    public List<Commodity> get(int commodityId) {

        return commodityDao.get(commodityId);
    }

    public void add( Commodity commodity) {

        commodityDao.add(commodity);
    }

    public List<Commodity> removeBoughtCommodity(Integer shoppingNum, Commodity commodity) {
        return null;
    }

    /**
     * 添加商品
     * @param commodity
     */
    public void inputCommodity(Commodity commodity){
        commodityDao.add(commodity);
    }

    /**
     *
     * @param type
     * @param shoppingNum
     * @return
     */
    public List<Commodity> checkout(Integer type, Integer shoppingNum) {
        return null;
    }

    public List<Member> getMember(int id) {

        return memberDao.get(id);
    }
    public void addMember(Member member){
        memberDao.add(member);
    }

    public void updateMember(Member member) {
        memberDao.update(member);
    }

    public void deleteCommodity(int id) {
        commodityDao.delete(id);
    }

    public void addOrder(int shoppingNumber) {
        Order order = new Order();
        order.setOrderNumber(shoppingNumber);
        order.setCheckoutType(0);
        orderDao.add(order);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItemDao.add(orderItem);
    }

    public void updateOrderItem(int orderNum , int commodityID ) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCommodityId(commodityID);
        orderItem.setOrderNumber(orderNum);
        orderItem.setIsChecked(2);
        orderItemDao.update(orderItem);
    }

    public List<OrderItemVO> getAllOrder(int shoppingNumber) {
        return orderItemDao.getAllOrder(shoppingNumber);
    }

    public void updateOrder(int orderId) {
        Order order = new Order();
        //todo 实体赋值

        orderDao.update(order);
    }
}
