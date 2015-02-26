<%@page import="org.jbpm.api.*,java.util.*,org.jbpm.api.model.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看流程跟踪图</title>
</head>
<body>
<%

String taskId=request.getParameter("taskId");

ProcessEngine processEngine=Configuration.getProcessEngine();

RepositoryService repositoryService=processEngine.getRepositoryService();

ExecutionService executionService=processEngine.getExecutionService();

TaskService taskService=processEngine.getTaskService();
//根据taskId来找到流程实例
ProcessInstance processInstance = executionService.findProcessInstanceById(taskService.getTask(taskId).getExecutionId());

Set<String> activityNames = processInstance.findActiveActivityNames();

ActivityCoordinates ac=repositoryService.getActivityCoordinates(processInstance.getProcessDefinitionId(), activityNames.iterator().next());

%>
<img alt="流程图" src="pic.jsp?id=<%=taskService.getTask(taskId).getExecutionId() %>" style="position:absolute;left:0px;top:0px;"/>
<div style="position:absolute;border:1px solid red;left:<%=ac.getX() %>px;top:<%=ac.getY() %>px;width:<%=ac.getWidth() %>px;height:<%=ac.getHeight()%>px;"></div>
</body>
</html>