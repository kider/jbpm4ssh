<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>填写请假申请单</h2>
<hr>
<form action="<%=request.getContextPath() %>/jbpmAction!createLeaveWorkFlow.action" method="post">
<table>
<tr>
<td>
<s:textfield name="userHelperBean.userName" value="%{#session.user}" readonly="readonly" label="请假人"></s:textfield>
</td>
</tr>
<tr>
<td>
<s:textfield name="userHelperBean.day" label="请假天数"></s:textfield>
</td>
</tr>
<tr>
<td>
<s:textarea name="userHelperBean.reason" label="请假原因"></s:textarea>
</td>
</tr>
</table>
<input type="hidden" value="${param.definitionId }" name="userHelperBean.defitionId"/>
<input type="submit" value="提 交"/>&nbsp;&nbsp;<input type="button" value="返 回" onclick="goBack();"/>
</form>
</body>
</html>