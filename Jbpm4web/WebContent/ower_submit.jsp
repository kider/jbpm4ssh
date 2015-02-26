<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,org.jbpm.api.*" %>
<%
//直接从默认的配置文件'jbpm.cfg.xml'里得到流程引擎
ProcessEngine processEngine=Configuration.getProcessEngine();
TaskService taskService=processEngine.getTaskService();

String owerName=request.getParameter("name");
int day=Integer.parseInt(request.getParameter("day"));
String reason=request.getParameter("reason");
String taskId=request.getParameter("taskId");
Map map=new HashMap();
map.put("name", owerName);
map.put("day", day);
map.put("reason", reason);
taskService.setVariables(taskId, map);
taskService.completeTask(taskId);
//JBPM4 No unnamed transitions were found for the task Exception
//taskService.completeTask(taskId, map);
response.sendRedirect(request.getContextPath()+"/index.jsp");


%>
