package com.sale.supermarket.dao;


import com.sale.supermarket.pojo.*;
import com.sale.supermarket.pojo.Commodity;
import com.sale.supermarket.pojo.OrderItem;
import com.sale.supermarket.pojo.User;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class SuperMarketDao extends BaseDao {
    public User getUser(String username, String password) {
        String sql = "select * from tb_user where username = ? and password = ?";
        Object[] paramValue = {username, password};
        List<User> users = super.query(sql, paramValue, User.class);

        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

    public Commodity getCommodity(int commodityId) {
        String sql = "select * from tb_commodity where id = ?";
        Object[] paramValue = {commodityId};
        List<Commodity> commodities = super.query(sql, paramValue, Commodity.class);

        if (commodities.size() > 0) {
            return commodities.get(0);
        }
        return null;
    }

    public void insertCommodity(Commodity commodity){
        String select_sql = "select * from tb_commodity where id = ?";
        String insert_sql = "insert tb_commodity values(?,?,?,?,?,?)";

        int id = commodity.getId();
        double price = commodity.getPrice();
        String name = commodity.getName();
        String specification = commodity.getSpecification();
        String units = commodity.getUnits();
        int stock = commodity.getStock();

        Object[] select_paramValue = {commodity.getId()};
        List<Commodity> commodities = super.query(select_sql, select_paramValue, Commodity.class);

        if (commodities != null && commodities.size()>0){
            Commodity oldCommodity = commodities.get(0);
            stock = stock + oldCommodity.getStock();
        }
        Object[] insert_paramValue = {id,name,specification,units,price,stock};

        super.update(insert_sql,insert_paramValue);
    }
    public List<Commodity> getCommodities() {
        String sql = "select * from tb_commodity";
        Object[] paramValue = {};
        List<Commodity> commodities = super.query(sql, paramValue, Commodity.class);
        return commodities;
    }

    public Member getMember(int memberId) {
        String sql = "select * from tb_member where id = ?";
        Object[] paramValue = {memberId};
        List<Member> members = super.query(sql, paramValue, Member.class);

        if (members.size() > 0) {
            return members.get(0);
        }
        return null;
    }

    public List<Member> getMembers() {
        String sql = "select * from tb_member";
        Object[] paramValue = {};
        List<Member> members = super.query(sql, paramValue, Member.class);
        return members;
    }

    public void insertOrder(Order order) {

    }

    public void insertOrderItem(OrderItem orderItem) {

    }

    public Order getOrder(int orderId) {
        return null;
    }

    public List<OrderItem> getOrderItems(int orderId) {
        return null;
    }
}
