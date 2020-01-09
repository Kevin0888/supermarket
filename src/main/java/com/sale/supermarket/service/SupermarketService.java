package com.sale.supermarket.service;


import com.sale.supermarket.dao.*;
import com.sale.supermarket.pojo.Commodity;
import com.sale.supermarket.pojo.Member;
import com.sale.supermarket.pojo.User;
import com.sale.supermarket.utils.CommodityVO;
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
     * ÃÌº”…Ã∆∑
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
}
