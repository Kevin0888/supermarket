<%@ page import="java.util.Random" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>manager</title>
</head>
<script type="text/javascript" src="${pageContext.request.contextPath}/component/jquery-3.3.1.min.js"></script>
<script type="text/javascript">

    function to_add_member() {
        $("#manager_fm").attr('action', '/supermarket/addMember.htm');
        $("#manager_fm").submit();
    }
</script>
<body>
<%
    Random random = new Random();
    //生成随机flag，
    Integer   flag=new   Integer(random.nextInt());
    session.setAttribute("flag",flag);
%>
<div align="center">
    <h1>会员管理</h1>
    <hr>
</div>
<form id="manager_fm"  method="post">
    <div id="add_member" align="center">
        <label>会员卡号</label><input type="text" id="member_id" name="id"/>
        <label>姓&#12288&#12288名</label><input type="text" id="member_name" name="name"/>
        <br>
        <label>电&#12288&#12288话</label><input type="text" id="phone" name="phone"/>
        <label>积&#12288&#12288分</label><input type="text" id="points" name="points"/>
        <br>
        <label>余&#12288&#12288额</label><input type="text" id="total" name="total"/>
        <br>
        <br>
        <input type="button" id="add_btn" value="添加" onclick="to_add_member()"/>
        <input type="button" id="cancel_btn" value="取消" onclick=""/>
        <input   type=hidden   name="flag"   value="<%=flag%>">
        <br>
        <hr>
    </div>
    <table align="center" width="80%" border="1px" cellpadding="0" cellspacing="0">
        <thead>
        <tr>
        <tr>
            <th>会员卡号</th>
            <th>姓名</th>
            <th>电话</th>
            <th>积分</th>
            <th>余额</th>
        </tr>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${members}" var="item">
            <tr>
                <td align="center">${item.id}</td>
                <td align="center">${item.name}</td>
                <td align="center">${item.phone}</td>
                <td align="center">${item.points}</td>
                <td align="center">${item.total}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</form>
</body>
</html>
