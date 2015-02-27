<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>
</head>
<body>
<h2>用户登录</h2>
<hr>
<s:form action="jbpmAction!userLogin.action" method="post">
<s:textfield label="用户名" name="userHelperBean.userName"></s:textfield>
<s:if test="#request.message != ''">
<s:label value="%{#request.message}"></s:label>
</s:if>
<s:submit label="提 交"></s:submit>
</s:form>
</body>
</html>