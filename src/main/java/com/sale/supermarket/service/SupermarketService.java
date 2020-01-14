package com.sale.supermarket.service;


import com.sale.supermarket.dao.*;
import com.sale.supermarket.pojo.*;
import com.sale.supermarket.utils.CommodityVO;
import com.sale.supermarket.utils.IDUtil;
import com.sale.supermarket.utils.OrderItemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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


    /**
     * 查询管理员信息
     *
     * @param username
     * @param password
     * @return
     */
    public User getUser(String username, String password) {
        return userDao.get(username, password);
    }

    /**
     * 删除商品库存
     *
     * @param commodityId
     * @return
     */
    public void deleteCommodity(int commodityId) {

        commodityDao.delete(commodityId);
    }

    /**
     * 添加商品
     *
     * @param commodity
     */
    public void inputCommodity(Commodity commodity) {
        commodityDao.add(commodity);
    }

    /**
     * 查询单个商品
     *
     * @param commodityId
     * @return
     */
    public Commodity getCommodity(int commodityId) {
        return commodityDao.getCommodity(commodityId);
    }

    /**
     * 查询商品
     *
     * @param id
     * @return
     */
    public List<Commodity> getCommodities(int id) {
        return commodityDao.get(id);
    }

    /**
     * 查询会员
     *
     * @param id
     * @return
     */
    public Member getMember(int id) {

        return memberDao.get(id);
    }

    /**
     * 查询会员
     *
     * @param id
     * @return
     */
    public List<Member> getMembers(int id) {

        return memberDao.getMembers(id);
    }

    /**
     * 添加会员
     *
     * @param member
     */
    public void addMember(Member member) {
        memberDao.add(member);
    }

    public void updateMember(Member member) {
        memberDao.update(member);
    }

    /**
     * 删除会员
     *
     * @param id
     */
    public void deleteMember(int id) {
        memberDao.delete(id);
    }

    /**
     * 添加订单
     *
     * @param shoppingNumber
     */
    public void addOrder(int shoppingNumber) {
        Order order = new Order();
        order.setOrderNumber(shoppingNumber);
        order.setCheckoutType(0);
        orderDao.add(order);
    }

    /**
     * 查询该订单号的所有记录
     *
     * @param shoppingNumber
     * @return
     */
    public List<OrderItemVO> getAllOrder(int shoppingNumber) {
        return orderItemDao.getAllOrder(shoppingNumber);
    }

    /**
     * 更新订单表
     *
     * @param orderNum
     */
    public void updateOrder(int orderNum, double totalPrice) {
        Order order = new Order();
        order.setOrderNumber(orderNum);
        order.setSum(totalPrice);
        order.setCheckoutType(1);
        orderDao.update(order);

    }

    /**
     * 查询订单详情信息
     *
     * @return
     */
    public List<OrderItem> getOrders(int orderNum) {
        return orderItemDao.get(orderNum);
    }

    /**
     * 添加订单详情
     *
     * @param orderItem
     */
    public void addOrderItem(OrderItem orderItem) {
        orderItemDao.add(orderItem);
    }

    /**
     * 更新订单详情结账状态
     *
     * @param orderNumber
     * @param isChecked
     */
    public void updateOrderItem(int orderNumber, int commodityId, int isChecked) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderNumber(orderNumber);
        orderItem.setCommodityId(commodityId);
        orderItem.setIsChecked(isChecked);
        orderItemDao.update(orderItem);
    }

    /**
     * 更新库存数量
     *
     * @param commodityID
     */
    public void updateCommodityChecked(int commodityID, int newStock) {
        Commodity commodity = new Commodity();
        commodity.setId(commodityID);
        commodity.setStock(newStock);
        commodityDao.update(commodity);
    }

    /**
     * 现金结账流程，添加事务
     *
     * @param orderNumber
     * @param total
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int checkoutByCash(String orderNumber, String total) {
        if (orderNumber != "" && orderNumber != null && total != "" && total != null) {
            int orderNum = Integer.parseInt(orderNumber);
            double totalCost = Double.parseDouble(total);
            //更新订单信息
            updateOrder(orderNum, totalCost);
            //更新库存数量commdity表
            List<OrderItem> orderItemList = getOrders(orderNum);
            for (OrderItem item : orderItemList) {
                int commodityID = item.getCommodityId();
                Commodity commodity = getCommodity(commodityID);
                int stock = commodity.getStock();
                int count = item.getCount();
                int newStock = stock - count;
                if (newStock < 0) {
                    newStock = 0;
                }
                updateCommodityChecked(commodityID, newStock);
                //更新订单详情状态为已结账
                int isCheck = 1;
//                int commodityId = 0;
                updateOrderItem(orderNum, commodityID, isCheck);
            }

            return 0;
        }
        return 1;
    }

    /**
     * 添加商品流程，添加事务
     * @param commodityID
     * @param count
     * @param shoppingNumStr
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int addCommodity(String commodityID, String count, String shoppingNumStr) {
        int shoppingNumber = 0;
        //根据Id查商品详情
        Commodity commodity = getCommodity(Integer.parseInt(commodityID));
        if (commodity != null && commodity.getStock() > 0) {
            //根据流水号判断是否添加订单
            if (shoppingNumStr != null && shoppingNumStr != "") {
                shoppingNumber = Integer.parseInt(shoppingNumStr);
                if (shoppingNumber == 0) {
                    //初次购买，添加订单记录
                    shoppingNumber = IDUtil.getId();
                    addOrder(shoppingNumber);
                }
            } else {
                shoppingNumber = IDUtil.getId();
                addOrder(shoppingNumber);
            }
            //转换实体CommodityOV，
            CommodityVO commodityVO = new CommodityVO();
            BeanUtils.copyProperties(commodity, commodityVO);
            commodityVO.setCount(Integer.parseInt(count));
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderNumber(shoppingNumber);
            orderItem.setCommodityId(Integer.parseInt(commodityID));
            orderItem.setCommodityName(commodity.getName());
            orderItem.setPrice(commodity.getPrice());
            orderItem.setCount(Integer.parseInt(count));
            orderItem.setTotal(commodityVO.getTotalPrice());
            orderItem.setIsChecked(0);
            //添加订单详情
            addOrderItem(orderItem);
            return shoppingNumber;
        }
        return 0;
    }
}
