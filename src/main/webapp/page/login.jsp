<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>登录</title>
</head>
<script type="text/javascript" src="${pageContext.request.contextPath}/component/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
    function to_manage_commodities() {
        $("#login_frm").attr('action', '/supermarket/getToCommodities.htm');
        $("#login_frm").submit();
    }

    function to_login() {
        $("#login_frm").attr('action', '/supermarket/login.htm');
        $("#login_frm").submit();
    }
</script>
<body>
<form id="login_frm" method="post">
    <div align="center">
        <h1>利民超市</h1>
        <hr>
    </div>
    <div align="center" style="width: 20%;margin:0 auto ;border: 1px solid #0f0f0f;position: absolute;top:30%;left:40%">
        <input type="hidden" name="submitFlag" value="login"/>
        <br/>
        用户名:
        <input type="text" name="username"/>
        <br/>
        密&#12288码:
        <input type="password" name="password"/>
        <br/>
        <br/>
        身&#12288份:
        <input type="radio" name="role" value=1>管理员
        <input type="radio" name="role" value=2>收银员
        <br/>
        <br/>
        <div align="center">
            <input type="button" value="登 录" onclick="to_login()"/>
            <input type="button" value="进 货" onclick="to_manage_commodities()"/></div>
    </div>
    <br/>
</form>
</body>
</html>