<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*,org.jbpm.api.*" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>老板审批</title>
</head>
<%
ProcessEngine processEngine = Configuration.getProcessEngine();
TaskService taskService=processEngine.getTaskService();
Set setVariables=taskService.getVariableNames(request.getParameter("taskId"));
Map mapVariable=taskService.getVariables(request.getParameter("taskId"), setVariables);
%>
<body>
<h2>请假申请单</h2>
<hr>
<form action="<%=request.getContextPath() %>/boss_submit.jsp" method="post">
<table>
<tr>
<td>
请假人：
</td>
<td>
<%=mapVariable.get("name") %>
</td>
</tr>
<tr>
<td>
请假天数：
</td>
<td>
<%=mapVariable.get("day") %>
</td>
</tr>
<tr>
<td>
请假原因:
</td>
<td>
<%=mapVariable.get("reason") %>
</td>
</tr>
</table>
<input type="hidden" value="${param.taskId }" name="taskId"/>
<br/>
<input type="submit" value="批 准"/>
</form>
</body>
</html>