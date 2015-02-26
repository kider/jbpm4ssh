<%@ page import="java.io.*,org.jbpm.api.*" %>
<%
String id=request.getParameter("id");
ProcessEngine processEngine=Configuration.getProcessEngine();

RepositoryService repositoryService=processEngine.getRepositoryService();

ExecutionService executionService=processEngine.getExecutionService();
//流程实例
ProcessInstance processInstance=executionService.findProcessInstanceById(id);
//获得流程定义id
String processDefinitionId=processInstance.getProcessDefinitionId();
//获得流程定义
ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().processDefinitionId((processDefinitionId)).uniqueResult();
//获得输入流
InputStream inputStream=repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), "leave.png");

byte[] b=new byte[1024];
int len=-1;
OutputStream os=response.getOutputStream();
//如果不等于-1则至少读取了1字节到b中
while((len=inputStream.read(b, 0, 1024))!=-1){
	//将b数组中0到len个字节写入到此输出流
	os.write(b, 0, len);
}
os.flush();
os.close();
out.clear();
out = pageContext.pushBody();
%>



