package com.sale.supermarket.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import com.sale.supermarket.pojo.Commodity;
import com.sale.supermarket.pojo.Member;
import com.sale.supermarket.pojo.User;
import com.sale.supermarket.service.SupermarketService;
import com.sale.supermarket.utils.OrderItemVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.swing.StringUIClientPropertyKey;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @RequestMapping(path ="/login", produces = {"text/html;charset=UTF-8"})
    public String login(HttpServletRequest req) {
        logger.info("----进入login方法");
        String username = req.getParameter("username");
        if (StringUtil.isEmpty(username)) {
            req.setAttribute("message", "username can not be empty");
            return "error";
        }
        String password = req.getParameter("password");
        if (StringUtil.isEmpty(password)) {
            req.setAttribute("message", "password can not be empty");
            return "error";
        }
        String role = req.getParameter("role");
        if(StringUtil.isEmpty(role)){
            req.setAttribute("message", "role can not be empty");
            return "error";
        }
        User user = supermarketService.getUser(username, password);
        if (user == null) {
            req.setAttribute("message", "The user does not exist");
            return "error";
        }
        if ( user.getRole() ==1 && user.getRole() == Integer.parseInt(role)) {
            List<Member> list = supermarketService.getAllMembers();
            req.setAttribute("members", list);
            return "manager";
        } else if (user.getRole() ==2 && user.getRole() == Integer.parseInt(role)) {
            req.setAttribute("shoppingNum", 0);
            return "cashier";
        }
        req.setAttribute("message", "选择正确的角色进入");
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
    public String addMember(HttpServletRequest req) throws Exception {
        Integer flag = new Integer(Integer.parseInt(req.getParameter("flag")));
        HttpSession session = req.getSession();
        //如果flag不一致，返回原来页面
        if (!flag.equals(session.getAttribute("flag"))) {
            List<Member> list = supermarketService.getAllMembers();
            req.setAttribute("members", list);
            return "manager";
        }
        String id = req.getParameter("id");
        if (StringUtil.isEmpty(id)) {
            req.setAttribute("message", "id can not be empty ");
            return "error";
        }
        Member mem = supermarketService.getMember(Integer.parseInt(id));
        if (mem != null) {
            req.setAttribute("message", "The id exists");
            return "error";
        }
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String total = req.getParameter("total");
        Member member = new Member();
        member.setId(Integer.parseInt(id));
        member.setName(name);
        member.setPhone(phone);
        if (StringUtil.isEmpty(total)) {
            total = "0.00";
        }
        member.setPoints(0);
        member.setTotal(Double.parseDouble(total));
        supermarketService.addMember(member);
        List<Member> list = supermarketService.getAllMembers();
        req.setAttribute("members", list);
        session.removeAttribute("flag");
        return "manager";
    }

    /**
     * 查询会员
     * ajax请求
     *
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/getMember", produces = {"text/html;charset=UTF-8"})
    @ResponseBody
    public String getMember(@RequestParam("memberID") String memberID) {
        /**
         * memberID: 22
         * shopNum: 115160118
         */
        if ("".equals(memberID)) {
            return null;
        }

        Member mem = supermarketService.getMember(Integer.parseInt(memberID));
        String member = JSON.toJSONString(mem);
        return member;
    }

    /**
     * 返回收银页面
     *
     * @param req
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/back2cashier", method = RequestMethod.GET)
    public String back2cashier(HttpServletRequest req) {
        req.setAttribute("shoppingNum", 0);
        return "cashier";
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
        if (StringUtil.isEmpty(commodityID) || StringUtil.isEmpty(count) || StringUtil.isEmpty(shoppingNumStr)) {
            req.setAttribute("message", "Empty parameter exists");
            return "error";
        }
        int shopNumber = supermarketService.addCommodity(commodityID, count, shoppingNumStr);
        if (shopNumber == 0) {
            req.setAttribute("message", "The commodity does not exist or the stock is 0");
            return "error";
        }
        Double totalCost = 0.0;
        int category = 0;
        //回显页面,先查该订单号的所有信息，
        List<OrderItemVO> ord = supermarketService.getAllUncheck(shopNumber);
        for (OrderItemVO item1 : ord) {
            totalCost += item1.getTotal();
        }
        category = ord.size();
        req.setAttribute("shoppingNum", String.valueOf(shopNumber));
        req.setAttribute("orderItemList", ord);
        req.setAttribute("totalCost", String.valueOf(totalCost));
        req.setAttribute("category", String.valueOf(category));
        return "cashier";
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

        if (StringUtil.isEmpty(commodityId) || StringUtil.isEmpty(shoppingNumStr)) {
            req.setAttribute("message", "Empty parameter exists");
            return "error";
        }
        int ischeck = 2;
        supermarketService.updateOrderItem(Integer.parseInt(shoppingNumStr), Integer.parseInt(commodityId), ischeck);
        double totalCost = 0.00;
        //回显页面
        //先查该订单号的所有信息，
        List<OrderItemVO> ord = supermarketService.getAllUncheck(Integer.parseInt(shoppingNumStr));
        for (OrderItemVO item1 : ord) {
            totalCost += item1.getTotal();
        }
        int category = ord.size();
        req.setAttribute("shoppingNum", shoppingNumStr);
        req.setAttribute("orderItemList", ord);
        req.setAttribute("totalCost", totalCost);
        req.setAttribute("category", category);
        return "cashier";
    }

    /**
     * 进入进货页面
     *
     * @param req
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/getToCommodities", method = RequestMethod.POST)
    public String getToCommodities(HttpServletRequest req) {
        String username = req.getParameter("username");
        if (StringUtil.isEmpty(username)) {
            req.setAttribute("message", "username can not be empty");
            return "error";
        }
        String password = req.getParameter("password");
        if (StringUtil.isEmpty(password)) {
            req.setAttribute("message", "password can not be empty");
            return "error";
        }
        String role = req.getParameter("role");
        if(StringUtil.isEmpty(role)){
            req.setAttribute("message", "role can not be empty");
            return "error";
        }
        User user = supermarketService.getUser(username, password);
        if (user == null) {
            req.setAttribute("message", "The user does not exist");
            return "error";
        }
        if ( user.getRole() ==1 && user.getRole() == Integer.parseInt(role)) {
            List<Commodity> commodity = supermarketService.getCommodities();
            req.setAttribute("commodities", commodity);
            return "commodity";
        }
        req.setAttribute("message", "选择正确的角色进入");
        return "error";
    }

    /**
     * 根据Id查询商品
     *
     * @param req
     * @return
     */
    @RequestMapping(path = "/getCommodity", method = RequestMethod.POST)
    public String getCommodity(HttpServletRequest req) {
        String id = req.getParameter("commodityId").trim();
        if (StringUtil.isEmpty(id)) {
            req.setAttribute("message", "commodityId can not be empty");
            return "error";
        }
        int Id = Integer.parseInt(id);
        Commodity commodity = supermarketService.getCommodity(Id);
        List<Commodity> list = new ArrayList<>();
        list.add(commodity);
        req.setAttribute("commodityList", list);
        return "commodity";
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
        Integer flag = new Integer(Integer.parseInt(req.getParameter("flag")));
        HttpSession session = req.getSession();
        if (!flag.equals(session.getAttribute("flag"))) {
            List<Commodity> commodityList = supermarketService.getCommodities();
            req.setAttribute("commodities", commodityList);
            return "commodity";
        }
        String id = req.getParameter("commodityId");
        String price = req.getParameter("price");
        String name = req.getParameter("name");
        String units = req.getParameter("units");
        String sp = req.getParameter("specification");
        String stock = req.getParameter("stock");
        if (StringUtil.isEmpty(id) || StringUtil.isEmpty(price) || StringUtil.isEmpty(name) || StringUtil.isEmpty(sp) || StringUtil.isEmpty(stock) || StringUtil.isEmpty(units)) {
            req.setAttribute("message", "Empty parameter exists");
            return "error";
        }
        //判断商品是否已经存在
        Commodity comm = supermarketService.getCommodity(Integer.parseInt(id));
        if (comm != null){
            req.setAttribute("message", "The commodity is exists");
            return "error";
        }
        Commodity commodity = new Commodity();
        commodity.setId(Integer.parseInt(id));
        commodity.setPrice(Double.parseDouble(price));
        commodity.setName(name);
        commodity.setUnits(units);
        commodity.setSpecification(sp);
        commodity.setStock(Integer.parseInt(stock));
        supermarketService.inputCommodity(commodity);
        List<Commodity> commodityList = supermarketService.getCommodities();
        req.setAttribute("commodities", commodityList);
        session.removeAttribute("flag");
        return "commodity";

    }

    /**
     * 删除商品库存
     *
     * @param req
     */
    @RequestMapping(path = "/deleteCommodity", method = RequestMethod.GET)
    public String deleteCommodity(HttpServletRequest req) {
        String id = req.getParameter("id");
        if (StringUtil.isEmpty(id)) {
            req.setAttribute("message", "id can not be empty");
            return "error";
        }
        supermarketService.deleteCommodity(Integer.parseInt(id));
        List<Commodity> commodities = supermarketService.getCommodities();
        req.setAttribute("commodities", commodities);
        return "commodity";
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
        String cashReceive = req.getParameter("cash_receive");
        String cashBalance = req.getParameter("cash_balance");
        String shopNumber = req.getParameter("shoppingNum");
        String total = req.getParameter("total_cost");
        if (StringUtil.isEmpty(shopNumber) || StringUtil.isEmpty(total)) {
            req.setAttribute("message", "Empty parameter exists");
            return "error";
        }
        int a = supermarketService.checkoutByCash(shopNumber, total);
        if (a != 0) {
            req.setAttribute("message", "checkoutByCash service problem");
            return "error";
        }
        Double totalCost = 0.0;
        int category = 0;
        //回显页面,先查该订单号的所有信息，
        List<OrderItemVO> ord = supermarketService.getAllChecked(Integer.parseInt(shopNumber));
        for (OrderItemVO item1 : ord) {
            totalCost += item1.getTotal();
        }
        category = ord.size();
        req.setAttribute("shoppingNum", shopNumber);
        req.setAttribute("orderItemList", ord);
        req.setAttribute("total_cost", String.valueOf(totalCost));
        req.setAttribute("category", String.valueOf(category));
        req.setAttribute("checkout_type", 0);
        req.setAttribute("cash_receive", cashReceive);
        req.setAttribute("cash_balance", cashBalance);
        return "receipt";

    }

    /**
     * 会员结账
     *
     * @param req
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(path = "/checkoutByMember", method = RequestMethod.POST)
    public String checkoutByMember(HttpServletRequest req) {
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
        String shopNumber = req.getParameter("shoppingNum");
        String total = req.getParameter("total_cost");
        String memberId = req.getParameter("memberID");

        if (StringUtil.isEmpty(shopNumber) && StringUtil.isEmpty(total) && StringUtil.isEmpty(memberId)) {
            req.setAttribute("message", "Empty parameter exists");
            return "error";
        }
        Map<String, Object> map = supermarketService.checkoutByMember(shopNumber, total, memberId);
        if (map.get("error").equals(99999999)) {
            req.setAttribute("message", "shopNumber OR  memberId is 0 ");
            return "error";
        }
        Double totalCost = 0.0;
        int category = 0;
        //回显页面,先查该订单号的所有信息，
        List<OrderItemVO> ord = supermarketService.getAllChecked(Integer.parseInt(shopNumber));
        for (OrderItemVO item1 : ord) {
            totalCost += item1.getTotal();
        }
        int totalMember = new Double(totalCost).intValue();
        category = ord.size();
        req.setAttribute("shoppingNum", shopNumber);
        req.setAttribute("orderItemList", ord);
        req.setAttribute("total_cost", String.valueOf(totalCost));
        req.setAttribute("category", String.valueOf(category));
        req.setAttribute("checkout_type", 1);
        req.setAttribute("cash_balance", map.get("total"));
        req.setAttribute("member_id", memberId);
        req.setAttribute("member_current_points", totalMember);
        req.setAttribute("member_points", map.get("points"));
        return "receipt";
    }
}
