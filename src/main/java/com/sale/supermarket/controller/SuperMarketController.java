package com.sale.supermarket.controller;

import com.sale.supermarket.pojo.Commodity;
import com.sale.supermarket.pojo.Member;
import com.sale.supermarket.pojo.OrderItem;
import com.sale.supermarket.pojo.User;
import com.sale.supermarket.service.SupermarketService;
import com.sale.supermarket.utils.CommodityVO;
import com.sale.supermarket.utils.DateUtil;
import com.sale.supermarket.utils.IDUtil;
import com.sale.supermarket.utils.OrderItemVO;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author aaa
 */
@Controller
@RequestMapping("/supermarket")
public class SuperMarketController {

    @Autowired
    SupermarketService supermarketService;
    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);


    /**
     * 登录
     *
     * @param req
     * @return string
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/login")
    public String login(HttpServletRequest req) {
        logger.info("----进入login方法");
        String role = req.getParameter("role");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        User user = supermarketService.getUser(username, password);

        if (user != null) {
            if ("1".equals(role)) {
                return "manager";
            } else if ("2".equals(role)) {
                req.setAttribute("shoppingNum", 0);
                return "cashier";
            } else if ("3".equals(role)) {
                return "commodity";
            }
        } else {
            return "error";
        }
        return "error";
    }

    /**
     * 添加会员
     *
     * @param req
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/addMember")
    public String addMember(HttpServletRequest req) {

        Member member = new Member();
        member.setId(Integer.parseInt(req.getParameter("id")));
        member.setName(req.getParameter("name"));
        member.setPhone(req.getParameter("phone"));
        member.setPoints(Integer.parseInt(req.getParameter("points")));
        member.setTotal(Integer.parseInt(req.getParameter("total")));
//        member.setRegisterTime(DateUtil.getFormatString(6,new Date()));
        supermarketService.addMember(member);
        List<Member> list = supermarketService.getMembers(Integer.parseInt(req.getParameter("id")));
        req.setAttribute("members", list);
        return "manager";
    }

    /**
     * 查询会员
     *
//     * @param req
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/getMember")
    public String getMember(HttpServletRequest req) {
        String memberID = req.getParameter("memberID");

        String shopNum = req.getParameter("shopNum");
        if (memberID != null || memberID != "") {
            Member member = supermarketService.getMember(Integer.parseInt(memberID));
            req.setAttribute("members", member);
            if (shopNum !=null && shopNum != "") {
                req.setAttribute("shoppingNum", String.valueOf(Integer.parseInt(shopNum)));
            }else{
                req.setAttribute("shoppingNum", 0);
            }
            return "cashier";
        }

        return null;
    }

    /**
     * 返回收银页面
     *
     * @param req
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/back2cashier", method = RequestMethod.GET)
    public void back2cashier(HttpServletRequest req) {

    }

    /**
     * 买商品，添加订单详情
     *
     * @param req
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/addCommodity", method = RequestMethod.POST)
    public String addCommodity(HttpServletRequest req) {
        String commodityID = req.getParameter("commodityID");
        String count = req.getParameter("count");
        String shoppingNumStr = req.getParameter("shoppingNum").trim();
        int shopNumber = supermarketService.addCommodity(commodityID, count, shoppingNumStr);
        if (shopNumber!= 0){
        Double totalCost = 0.0;
        int category = 0;
            //回显页面,先查该订单号的所有信息，
            List<OrderItemVO> ord = supermarketService.getAllOrder(shopNumber);
            for (OrderItemVO item1 : ord) {
                totalCost += item1.getTotal();
            }
            category = ord.size();
            req.setAttribute("shoppingNum", String.valueOf(shopNumber));
            req.setAttribute("orderItemList", ord);
            req.setAttribute("totalCost", String.valueOf(totalCost));
            req.setAttribute("category", String.valueOf(category));
            return "cashier";
        } else {
            return "商品不存在或没有库存";
        }

    }

    /**
     * 移除订单商品
     *
     * @param req
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/removeCommodity", method = RequestMethod.POST)
    public String removeCommodity(HttpServletRequest req) {
        String commodityId = req.getParameter("commodityID").trim();
        String shoppingNumStr = req.getParameter("shoppingNum").trim();
        if (commodityId != null && commodityId != "" && shoppingNumStr != null && shoppingNumStr != "") {
            int ischeck = 2;
            supermarketService.updateOrderItem(Integer.parseInt(shoppingNumStr), Integer.parseInt(commodityId), ischeck);
            double totalCost = 0.00;
            //回显页面
            //先查该订单号的所有信息，
            List<OrderItemVO> ord = supermarketService.getAllOrder(Integer.parseInt(shoppingNumStr));
            for (OrderItemVO item1 : ord) {
                totalCost += item1.getTotal();
            }
            int category = ord.size();
            req.setAttribute("shoppingNum", String.valueOf(Integer.parseInt(shoppingNumStr)));
            req.setAttribute("orderItemList", ord);
            req.setAttribute("totalCost", String.valueOf(totalCost));
            req.setAttribute("category", String.valueOf(category));
            return "cashier";
        }
        return null;
    }

    /**
     * 查询商品信息
     *
     * @param req
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/getToCommodities", method = RequestMethod.POST)
    public String getToCommodities(HttpServletRequest req) {

        return "commodity";
    }

    /**
     * 查询商品列表
     *
     * @param req
     * @return
     */
    @RequestMapping(path = "/getCommodities", method = RequestMethod.POST)
    public String getCommodities(HttpServletRequest req) {
        String id = req.getParameter("commodityId").trim();
        if (id != "" && id != null) {
            int Id = Integer.parseInt(id);
            List<Commodity> commodity = supermarketService.getCommodities(Id);
            req.setAttribute("commodityList", commodity);
            return "commodity";
        }
        return null;
    }

    /**
     * 添加商品库存
     *
     * @param req
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/inputCommodities", method = RequestMethod.POST)
    public String inputCommodities(HttpServletRequest req) {

        Commodity commodity = new Commodity();
        commodity.setId(Integer.parseInt(req.getParameter("commodityId")));
        commodity.setPrice(Double.parseDouble(req.getParameter("price")));
        commodity.setName(req.getParameter("name"));
        commodity.setUnits(req.getParameter("units"));
        commodity.setSpecification(req.getParameter("specification"));
        commodity.setStock(Integer.parseInt(req.getParameter("stock")));
        supermarketService.inputCommodity(commodity);
        List<Commodity> list = new ArrayList<>();
        list.add(0, commodity);
        req.setAttribute("commodities", list);
        return "commodity";
    }

    /**
     * 删除商品库存
     *
     * @param id
     */
    @RequestMapping(path = "/deleteCommodity", method = RequestMethod.GET)
    public void deleteCommodity(int id) {
        supermarketService.deleteCommodity(id);
    }

    /**
     * 现金结账
     *
     * @param req
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/checkoutByCash", method = RequestMethod.POST)
    public String checkoutByCash(HttpServletRequest req) {
        /**
         * shoppingNum: 1132301181
         * commodityID:
         * count:
         * category: 2
         * total_cost: 10.0
         * cash_receive: 20
         * cash_balance: 10
         * memberID:
         */
        String orderNumber = req.getParameter("shoppingNum");
        String total = req.getParameter("total_cost");
       int a = supermarketService.checkoutByCash(orderNumber,total);

//        if (orderNumber != "" && orderNumber != null && total != "" && total != null) {
//            int orderNum = Integer.parseInt(orderNumber);
//            double totalCost = Double.parseDouble(total);
//            //更新订单信息
//            supermarketService.updateOrder(orderNum, totalCost);
//            //更新库存数量commdity表
//            List<OrderItem> orderItemList = supermarketService.getOrders(orderNum);
//            for (OrderItem item : orderItemList) {
//                int commodityID = item.getCommodityId();
//                Commodity commodity = supermarketService.getCommodity(commodityID);
//                int stock = commodity.getStock();
//                int count = item.getCount();
//                int newStock = stock - count;
//                if (newStock < 0) {
//                    newStock = 0;
//                }
//                supermarketService.updateCommodityChecked(commodityID, newStock);
//            }
//            //更新订单详情状态为已结账
//            int isCheck = 1;
//            int commodityId = 0;
//            supermarketService.updateOrderItem(orderNum, commodityId, isCheck);
//            req.setAttribute("shoppingNum", 0);
//            return "cashier";
//        } else {
//            return "参数不能为空";
//        }
        if (a==0) {
            req.setAttribute("shoppingNum", 0);
            return "cashier";
        }
        return null;
    }

    /**
     * 会员结账
     * @param req
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/checkoutByMember", method = RequestMethod.POST)
    public String checkoutByMember(HttpServletRequest req) {
        return "receipt";
    }
}
