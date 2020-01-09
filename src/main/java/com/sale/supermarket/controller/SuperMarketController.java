package com.sale.supermarket.controller;

import com.sale.supermarket.pojo.Commodity;
import com.sale.supermarket.pojo.Member;
import com.sale.supermarket.pojo.User;
import com.sale.supermarket.service.SupermarketService;
import com.sale.supermarket.utils.CommodityVO;
import com.sale.supermarket.utils.DateUtil;
import com.sale.supermarket.utils.IDUtil;
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
            }else if("3".equals(role)){
                return "commodity";
            }
        } else {
            return "error";
        }
        return "error";
    }

    /**
     * 添加会员
     * @param req
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/addMember")
    public String addMember(HttpServletRequest req)  {

        Member member = new Member();
        member.setId(Integer.parseInt(req.getParameter("id")));
        member.setName(req.getParameter("name"));
        member.setPhone(req.getParameter("phone"));
        member.setPoints(Integer.parseInt(req.getParameter("points")));
        member.setTotal(Integer.parseInt(req.getParameter("total")));
//        member.setRegisterTime(DateUtil.getFormatString(6,new Date()));
        supermarketService.addMember(member);
        List<Member> list = supermarketService.getMember(Integer.parseInt(req.getParameter("id")));
        req.setAttribute("members", list);
        return "manager";
    }

    /**
     * 查询会员
     * @param req
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/getMembers", method = RequestMethod.GET)
    public String getMembers(HttpServletRequest req)  {
        List<Member> list =  supermarketService.getMember(Integer.parseInt(req.getParameter("id")));
        req.setAttribute("members",list);
        return "manager";
    }

    /**
     * 返回收银页面
     * @param req
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/back2cashier", method = RequestMethod.GET)
    public void back2cashier(HttpServletRequest req)  {

    }

    /**
     * 买商品，添加订单详情
     * @param req
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/addCommodity", method = RequestMethod.POST)
    private void addCOrder(HttpServletRequest req)  {
        String commodityID = req.getParameter("commodityID");
        String count = req.getParameter("count");
        String shoppingNumStr = req.getParameter("shoppingNum");
        Double totalCost = 0.0;
        int category = 0;
        int shoppingNumber = 0;

        if (shoppingNumStr != null && shoppingNumStr != "") {
            shoppingNumber = Integer.parseInt(shoppingNumStr);
            if (shoppingNumber == 0) {
                shoppingNumber = IDUtil.getId();
            }
        } else {
            shoppingNumber = IDUtil.getId();
        }

        req.setAttribute("shoppingNum", shoppingNumber);
        //根据Id查商品详情
        Commodity commodity = supermarketService.getCommodity(Integer.parseInt(commodityID));
        if (commodity != null) {
            //转换实体，CommodityOV
            CommodityVO commodityVO = new CommodityVO();
            BeanUtils.copyProperties(commodity, commodityVO);
            commodityVO.setCount(Integer.parseInt(count));

        }

//        List<CommodityVO> commodityList;
        //添加订单
//        supermarketService.addOrder(shoppingNumber, commodity);
//
//        for (CommodityVO item : commodityList) {
//            totalCost += item.getTotalPrice();
//        }
//        //回显页面
//        category = commodityList.size();
//
//        req.setAttribute("commodityList", commodityList);
//        req.setAttribute("totalCost", totalCost);
//        req.setAttribute("category", category);
//
//        String forwardPage = cashierPage;
//        RequestDispatcher view = req.getRequestDispatcher(forwardPage);
//        view.forward(req, resp);

    }

    @RequestMapping(path = "/removeBoughtCommodity", method = RequestMethod.POST)
    public void removeBoughtCommodity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    /**
     * 查询商品信息
     * @param req
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/getCommodities", method = RequestMethod.GET)
    public String getCommodities(HttpServletRequest req) {
        String id = req.getParameter("commodityId");
        if (id != "" && id != null) {
            List<Commodity> commodity = supermarketService.get(Integer.parseInt(id));
            req.setAttribute("commodityList", commodity);
            return "commodity";
        }
        return null;
    }

    /**
     * 添加商品
     * @param req
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/inputCommodity", method = RequestMethod.POST)
    public String inputCommodity(HttpServletRequest req) throws ServletException, IOException {

        Commodity commodity = new Commodity();
        commodity.setId(Integer.parseInt(req.getParameter("commodityId")));
        commodity.setPrice(Double.parseDouble(req.getParameter("price")));
        commodity.setName(req.getParameter("name"));
        commodity.setUnits(req.getParameter("units"));
        commodity.setSpecification(req.getParameter("specification"));
        commodity.setStock(Integer.parseInt(req.getParameter("stock")));
       supermarketService.inputCommodity(commodity);
        List<Commodity> list = new ArrayList<>();
        list.add(0,commodity);
        req.setAttribute("commodities",list );
        return "commodity";
    }

    /**
     * 删除商品
     * @param id
     */
    @RequestMapping(path = "/deleteCommodity",method = RequestMethod.GET)
    public void deleteCommodity(int id){
        supermarketService.deleteCommodity(id);
    }

    /**
     * 现金结账
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/checkoutByCash", method = RequestMethod.GET)
    public void checkoutByCash(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    /**
     *会员结账
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/checkoutByMember", method = RequestMethod.GET)
    public void checkoutByMember(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
