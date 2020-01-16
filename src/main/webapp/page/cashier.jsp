<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.sale.supermarket.utils.OrderItemVO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<html>
<%
    String shopNum = request.getAttribute("shoppingNum").toString();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>cashier</title>
</head>
<script type="text/javascript" src="${pageContext.request.contextPath}/component/jquery-3.3.1.min.js"></script>
<script type="text/javascript">


    function addCommodity() {

        var commodityID = $("#commodity_id_txt").val();
        var commodityCount = $("#commodity_count_txt").val();
        if (commodityID == '' || commodityID == undefined) {
            alert("请输入商品码");
            return;
        }
        if (commodityCount == '' || commodityCount == undefined) {
            alert("请输入商品数量");
            return;
        }

        $("#cashier_fm").attr('action', '/supermarket/addCommodity.htm');
        $("#cashier_fm").submit();
    };

    function removeCommodity() {

        var commodityID = $("#commodity_id_txt").val();
        if (commodityID == '' || commodityID == undefined) {
            alert("请输入商品码");
            return;
        }

        $("#cashier_fm").attr('action', '/supermarket/removeCommodity.htm');
        $("#cashier_fm").submit();
    };

    function calculate() {
        var cash_receive = $("#receive_txt").val();
        var cash_cost = $("#total_cost_txt").text();
        var balance = cash_receive - cash_cost;
        if (balance < 0) {
            balance = 0;
        }
        $("#cash_balance_lbl").text(balance);

        $("#cash_receive").val(cash_receive);
        $("#cash_balance").val(balance);


    };

    function checkoutByCash() {
        var balance = $("#receive_txt").val() - $("#total_cost_txt").text();
        if (balance < 0) {
            alert("收到现金额度有误，请查验");
            return;
        }
        $("#cashier_fm").attr('action', '/supermarket/checkoutByCash.htm');
        $("#cashier_fm").submit();
    }

    function checkoutByMember() {
        var memberName = $("#member_name_lbl").text();
        var balance = $("#member_balance_lbl").text() - $("#total_cost_txt").text();

        if (memberName == "" || memberName == undefined) {
            alert("请输入有效的会员号");
            return;
        }
        if (balance < 0) {
            alert("余额不足，请及时充值");
            return;
        }
        $("#cashier_fm").attr('action', '/supermarket/checkoutByMember.htm');
        $("#cashier_fm").submit();
    }

    function showMember() {
        var memberID = $("#member_id_txt").val();
        if (memberID == "" || memberID == undefined) {
            alert("请输入有效的会员卡号");
            return;
        }
        var shopNum = <%=shopNum%>;
            $.ajax({
            type: "POST",
            url: "getMember.htm",
            data:  {memberID: memberID
            },
            dataType: "json",
            success: function (data) {
                $("#member_balance_lbl").text(data.total);
                $("#member_points_lbl").text(data.points);
                $("#member_name_lbl").text(data.name);
            },
                error:function(data){
                    alert("error");

                }

        })
    }


</script>
<body>
<%
    String shoppingNum = request.getAttribute("shoppingNum").toString();
    List<OrderItemVO> orderItemList = (ArrayList) request.getAttribute("orderItemList");
    Integer category = null == request.getAttribute("category") ? 0 : Integer.parseInt(request.getAttribute("category").toString());
    Double totalCost = null == request.getAttribute("totalCost") ? 0.00 : Double.parseDouble(request.getAttribute("totalCost").toString());
%>

<div align="center">
    <h1>收银管理</h1>
    <hr>
</div>
<div align="right">
    登录时间 ：<label><fmt:formatDate value="<%=new Date() %>" pattern="yyyy-MM-dd HH:mm:ss"/></label>
    <hr>
</div>
<form action="" id="cashier_fm" method="post">
    <input type="hidden" id="shopping_num_txt" name="shoppingNum" value="<%= shoppingNum%>">
    <div>
        小票流水号 ： <%=shoppingNum%>  输入商品条码：<input type="text" id="commodity_id_txt" name="commodityID"> 数量：<input
            type="text" id="commodity_count_txt" name="count">
        <input type="button" id="add_btn" value="查询商品" onclick="addCommodity()">
        <input type="button" id="delete_btn" value="删除商品" onclick="removeCommodity()">
    </div>
    <br>
    <hr>
    <table width="80%" border="1px" cellpadding="0" cellspacing="0" align="center">
        <thead>
        <tr>
            <th>商品条码</th>
            <th>商品名称</th>
            <th>规格等级</th>
            <th>单位</th>
            <th>当前库存</th>
            <th>会员价</th>
            <th>零售价</th>
            <th>数量</th>
            <th>金额</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="<%=orderItemList%>" var="item">
            <tr>
                <td align="center">${item.commodityId}</td>
                <td align="center">${item.commodityName}</td>
                <td align="center">${item.specification}</td>
                <td align="center">${item.units}</td>
                <td align="center">${item.stock}</td>
                <td align="center">${item.price}</td>
                <td align="center">${item.price}</td>
                <td align="center">${item.count}</td>
                <td align="center">${item.total}</td>
            </tr>
        </c:forEach>
        </tbody>

    </table>
    <br>
    <hr>
    <div>共有：<label id="3"><%=category%>
    </label> 种商品 共计：￥<label id="total_cost_txt"><%=totalCost%>
    </label> 元
    </div>
    <input type="hidden" name="category" value="<%=category%>">
    <input type="hidden" name="total_cost" value="<%=totalCost%>">
    <hr>
    <div>实收：￥ <input type="text" onblur="calculate()" id="receive_txt" value='0.00'/> 元 找零：￥ <label id="cash_balance_lbl">0.0</label> 元
    </div>
    <input type="hidden" name="cash_receive" id="cash_receive">
    <input type="hidden" name="cash_balance" id="cash_balance">
    <br>
    <div><input onclick="checkoutByCash()" type="button" id="cash_btn" value="现金结账"></div>
    <br>
    <hr>
    <div>会员卡号：<input onblur="showMember()" type="text" id="member_id_txt" name="memberID"> 姓名：<label id="member_name_lbl"></label>
    </div>
    <div>积分：<label id="member_points_lbl"></label>分 余额：￥ <label id="member_balance_lbl"></label> 元</div>
    <br>
    <div><input type="button" onclick="checkoutByMember()" id="balance_btn" value="余额结账"></div>
</form>
</body>
</html>
