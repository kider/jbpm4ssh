<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>申请人填写申请单</title>
</head>
<body>
<h2>填写请假申请单</h2>
<hr>
<form action="<%=request.getContextPath() %>/ower_submit.jsp" method="post">
<table>
<tr>
<td>
请假人：
</td>
<td>
<input type="text" value="${sessionScope['user'] }" name="name"/>
</td>
</tr>
<tr>
<td>
请假天数：
</td>
<td>
<input type="text" value="" name="day"/>
</td>
</tr>
<tr>
<td>
请假原因:
</td>
<td>
<textarea rows="" cols="" name="reason"></textarea>
</td>
</tr>
</table>
<input type="hidden" value="${param.taskId }" name="taskId"/>
<input type="submit" value="提 交"/>
</form>
</body>
</html>