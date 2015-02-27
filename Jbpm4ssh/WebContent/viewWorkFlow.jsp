<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看流程图</title>
</head>
<body>
<img alt="流程图" src="<%=request.getContextPath() %>/pic.jsp?executionId=<s:property value="#request.eId"/>" style="position:absolute;left:0px;top:0px;"/>
<div style="position:absolute;border:1px solid red;left:<s:property value="#request.ac.x"/>px;top:<s:property value="#request.ac.y"/>px;width:<s:property value="#request.ac.width"/>px;height:<s:property value="#request.ac.height"/>px;">
</body>
</html>