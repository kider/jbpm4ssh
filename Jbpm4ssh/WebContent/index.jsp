<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户界面 </title>
</head>
<body>
欢迎你！[用户：<s:property value="#request.userHelperBean.userName"/>]
<a href="<%=request.getContextPath() %>/login.jsp">重新登录</a>
<hr>
流程定义列表<a href="<%=request.getContextPath() %>/jbpmAction!DeployDefinition.action">deploy</a>
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
<s:iterator value="#request.dList" id="Processdefinition">
<tr>
<td>
<s:property value="#Processdefinition.id"/>
</td>
<td>
<s:property value="#Processdefinition.name"/>
</td>
<td>
<s:property value="#Processdefinition.version"/>
</td>
<td>
<a href="<%=request.getContextPath() %>/jbpmAction!removeDefinition.action?userHelperBean.deployId=<s:property value="#Processdefinition.deploymentId"/>">remove</a>
&nbsp;&nbsp;
<!-- 方式一    先分配任务然后在填写申请单
<a href="<%=request.getContextPath() %>/jbpmAction!createProcessInstance.action?userHelperBean.defitionId=<s:property value="#Processdefinition.id"/>">start</a>
 -->
<!--方式二  直接填写申请单   -->
<a href="<%=request.getContextPath() %>/writeLeave.jsp?definitionId=<s:property value="#Processdefinition.id"/>">start</a>
</td>
</tr>
</s:iterator>
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
<s:iterator value="#request.iList" id="ProcessInstance">
<tr>
<td>
<s:property value="#ProcessInstance.id"/>
</td>
<td>
<s:property value="#ProcessInstance.state"/>
</td>
<td>
<s:property value="#ProcessInstance.findActiveActivityNames()"/>
</td>
<td>
<a href="<%=request.getContextPath() %>/jbpmAction!removeProcessInstance.action?userHelperBean.instanceId=<s:property value="#ProcessInstance.id"/>">remove</a>
</td>
</tr>
</s:iterator>
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
<s:iterator value="#request.tList" id="Task">
<tr>
<td>
<s:property value="#Task.id"/>
</td>
<td>
<s:property value="#Task.activityName"/>
</td>
<td>
<s:property value="#Task.state"/>
</td>
<td>
<!--方式一
<a href="<%=request.getContextPath() %>/jbpmAction!viewPersonTask.action?userHelperBean.taskId=<s:property value="#Task.id"/>">view</a>
-->
<!-- 方式二 -->
<a href="<%=request.getContextPath() %>/jbpmAction!viewpersonTaskOther.action?userHelperBean.taskId=<s:property value="#Task.id"/>">view</a>
</td>
</tr>
</s:iterator>
</table>
</body>
</html>