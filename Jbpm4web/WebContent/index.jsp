<%@page import="org.jbpm.api.task.*,org.jbpm.api.history .*"%>
<%@page import="org.jbpm.api.*,java.util.*"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的首页</title>
<script type="text/javascript">
function viewWorkFlow(taskId){
	var url="<%=request.getContextPath() %>/view.jsp?taskId="+taskId;
	window.open(url,'new','width=500,height=500,menubar=no,scrollbars=no,status=yes,resizable=yes,');
}
</script>
</head>
<body>
<%
String user=(String)request.getSession().getAttribute("user");
%>
<%
if(null!=user){
%>
欢迎你![用户:<%=user %>],<a href="<%=request.getContextPath()%>/login.jsp">重新登录</a>
<%
}else{
response.sendRedirect(request.getContextPath()+"/login.jsp");
}
%>
<hr>
<a href="<%=request.getContextPath() %>/business.jsp?action=deploy">Deploy</a>
<div align="center">
<%
//直接从默认的配置文件'jbpm.cfg.xml'里得到流程引擎
ProcessEngine processEngine=Configuration.getProcessEngine();

//管理发布资源服务
RepositoryService repositoryService=processEngine.getRepositoryService();

//管理流程实例服务
ExecutionService executionService=processEngine.getExecutionService();

//待办任务服务
TaskService taskService=processEngine.getTaskService();

//历史服务 
HistoryService historyService=processEngine.getHistoryService();

//historyService.createHistoryTaskQuery().
List<HistoryTask> list=historyService.createHistoryTaskQuery().assignee(user).list();   
List<HistoryTask> handledTasks=new ArrayList<HistoryTask>(); 
for (HistoryTask historyTask:list) {
   if(historyTask.getEndTime()!=null){ 
    handledTasks.add(historyTask);
   };
}

//得到发布的定义流程列表
List<ProcessDefinition> dfList=repositoryService.createProcessDefinitionQuery().list();

//得到流程实例列表
List<ProcessInstance> piList=executionService.createProcessInstanceQuery().list();

//得到本人的待办任务列表
List<Task> taskList=taskService.findPersonalTasks(user);
%>
<br/>
发布的流程列表
<br/>
<table border="1" width="60%" height="100%">
<tr>
<td>
流程定义ID
</td>
<td>
流程定义名称
</td>
<td>
流程版本
</td>
<td>
操作
</td>
</tr>
<%for(ProcessDefinition pd:dfList){ %>
<tr>
<td>
<%=pd.getId() %>
</td>
<td>
<%=pd.getName() %>
</td>
<td>
<%=pd.getVersion() %>
</td>
<td>
<a href="<%=request.getContextPath() %>/business.jsp?action=remove&id=<%=pd.getDeploymentId()%>">Remove</a>&nbsp;&nbsp;
<a href="<%=request.getContextPath() %>/business.jsp?action=startInstance&id=<%=pd.getId()%>">Start</a>
</td>
</tr>
<%} %>
</table>
<br/>
流程实例列表
<table border="1" width="60%" height="100%">
<tr>
<td>
流程实例ID
</td>
<td>
流程状态
</td>
<td>
流程节点名
</td>
<td>
操作
</td>
</tr>
<%for(ProcessInstance pi:piList){ %>
<tr>
<td>
<%=pi.getId() %>
</td>
<td>
<%=pi.getState() %>
</td>
<td>
<%=pi.findActiveActivityNames() %>
</td>
<td>
<a href="<%=request.getContextPath() %>/business.jsp?action=removeInstance&InstanceId=<%=pi.getId()%>">Remove</a>
</td>
</tr>
<%} %>
</table>
<br/>
用户待办列表
<table border="1" width="60%" height="100%">
<tr>
<td>
任务ID
</td>
<td>
流程节点名称
</td>
<td>
流程状态
</td>
<td>
操作
</td>
</tr>
<%for(Task task:taskList){ %>
<tr>
<td>
<%=task.getId() %>
</td>
<td>
<%=task.getActivityName() %>
</td>
<td>
<%=task.getName() %>
</td>
<td>
<a href="<%=request.getContextPath() %>/<%=task.getFormResourceName()%>?taskId=<%=task.getId() %>">查看任务</a>
<a href="javascript:viewWorkFlow('<%=task.getId()%>');">查看流程图</a>
</td>
</tr>
<%} %>
</table>
<br/>
用户已办列表
<table border="1" width="60%" height="100%">
<tr>
<td>
任务ID
</td>
<td>
流程状态
</td>
<td>
流程结束时间
</td>
<td>
流程完成人
</td>
</tr>
<%for(HistoryTask httask:handledTasks){ %>
<tr>
<td>
<%=httask.getId() %>
</td>
<td>
<%=httask.getState() %>
</td>
<td>
<%=httask.getEndTime() %>
</td>
<td>
<%=httask.getAssignee() %>
</td>
</tr>
<%} %>
</table>
</div>
</body>
</html>