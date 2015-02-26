<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>
</head>
<body>
<h2>用户登录</h2>
<hr>
<form action="<%=request.getContextPath() %>/doLogin.jsp">
用户名：<input type="text" name="userName" id="userName"/>
<input type="submit" value="提  交"/>
</form>
</body>
</html>