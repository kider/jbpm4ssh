<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,org.jbpm.api.*,java.util.zip.*" %>
<% 
//直接从默认的配置文件'jbpm.cfg.xml'里得到流程引擎
ProcessEngine processEngine=Configuration.getProcessEngine();

//管理发布资源服务
RepositoryService repositoryService=processEngine.getRepositoryService();

//管理流程实例服务
ExecutionService executionService=processEngine.getExecutionService();

//待办任务服务
String action = request.getParameter("action");

if(null!=action && action.equals("deploy")){
	//发布定义的流程
	//发布单条流程
	/*
	repositoryService.createDeployment().addResourceFromClasspath("leave.jpdl.xml").deploy();
	*/
	//leave.zip  打成zip包 批量发布流程 (将流程png图片也发布到流程引擎中去)
	ZipInputStream zipInputStream=new ZipInputStream(this.getClass().getResourceAsStream("/leave.zip"));
	repositoryService.createDeployment().addResourcesFromZipInputStream(zipInputStream).deploy();
	
}else if(action.equals("remove")){
	// 根据发布流程ID删除定义流程
	repositoryService.deleteDeploymentCascade(request.getParameter("id"));
}else if(action.equals("startInstance")){
	//把当们人作为申请人发起流程实例
	String userName=(String)request.getSession().getAttribute("user");
	Map map=new HashMap();
	map.put("ower", userName);
	ProcessInstance processInstance=executionService.startProcessInstanceById(request.getParameter("id"),map);
	//executionService.createVariables(processInstance.getId(), map, true);
}else if(action.equals("removeInstance")){
	executionService.deleteProcessInstanceCascade(request.getParameter("InstanceId"));
}
response.sendRedirect(request.getContextPath()+"/index.jsp");

%>