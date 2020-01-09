<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Date" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>receipt</title>
</head>
<body style="text-align: center">
<div align="left" >
    <a href="/sale/back2cashier?role=2">返回</a>
</div>
<div align="center" style="width: 20%;margin:0 auto ;border: 1px solid #0f0f0f">

    <h2>利民超市</h2>

    <hr/>
    <table>
        <tbody>
        <tr>
            <td align="left"><label>收银员 : No.1001</label></td>
            <td align="right"><label>机号 : Admin</label></td>
        </tr>
        <tr>
            <td align="left"><label>时间</label></td>
            <td align="right"><label><fmt:formatDate value="<%=new Date() %>" pattern="yyyy-MM-dd HH:mm:ss"/></label></td>
        </tr>
        </tbody>
    </table>

    <hr/>
    <table>
        <thead>
        <tr>
            <th>商品名称</th>
            <th>单价</th>
            <th>数量</th>
            <th>金额</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${commodityList}" var="item">
            <tr>
                <td align="left">${item.commodityname}</td>
                <td align="left">${item.price}</td>
                <td align="left">${item.count}</td>
                <td align="left">${item.totalprice}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <hr/>
    <table>
        <tbody>
        <tr>
            <td align="left"><label>数量</label><label>${category} 件</label></td>
            <td align="right"><label>合计</label><label>${total_cost} 元</label></td>
        </tr>
        <tr>
            <td align="left"><label>收款</label></td>
            <td align="right"><label>${cash_receive} 元</label></td>
        </tr>
        <tr>
            <td align="left"><label>找零</label></td>
            <td align="right"><label>${cash_balance} 元</label></td>
        </tr>
        <tr <c:if test="${checkout_type == 0}"> hidden </c:if> </tr>
            <td align="left"><label>积分账号</label></td>
            <td align="right"><label> ${member_id}</label></td>
        </tr>
        <tr <c:if test="${checkout_type == 0}"> hidden </c:if> >
            <td align="left"><label>本次积分</label></td>
            <td align="right"><label>${member_current_points} </label></td>
        </tr>
        <tr <c:if test="${checkout_type == 0}"> hidden </c:if>}>
            <td align="left"><label>累计积分</label></td>
            <td align="right"><label>${member_points} </label></td>
        </tr>
        <tr>
            <td align="left"><label>小票号</label></td>
            <td align="right"><label>${shoppingNum} </label></td>
        </tr>
        <br>
        </tbody>
    </table>

    <hr />
    <br>
    <br>
    谢谢惠顾，欢迎下次光临！
    <br>
    <br>
    <br>
    <br>
</div>

</body>
</html>
