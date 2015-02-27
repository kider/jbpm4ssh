package com.daos.daoImpl.jbpm;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.IdentityService;
import org.jbpm.api.ManagementService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.task.Task;

import com.daos.dao.jbpm.JbpmDAO;

public class JbpmDaoImpl implements JbpmDAO{
	private RepositoryService repositoryService;
	private ExecutionService executionService;
	private HistoryService historyService;
	private ManagementService managementService;
	private TaskService taskService;
	private IdentityService identityService;

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	public ExecutionService getExecutionService() {
		return executionService;
	}
	public void setExecutionService(ExecutionService executionService) {
		this.executionService = executionService;
	}
	public HistoryService getHistoryService() {
		return historyService;
	}
	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}
	public ManagementService getManagementService() {
		return managementService;
	}
	public void setManagementService(ManagementService managementService) {
		this.managementService = managementService;
	}
	public TaskService getTaskService() {
		return taskService;
	}
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
	public IdentityService getIdentityService() {
		return identityService;
	}
	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}
	
	
	/**
	 * 发布的流程定义列表
	 * @return List<ProcessDefinition>
	 */
	public List<ProcessDefinition> findProcessDefinitionList(){
		
	return repositoryService.createProcessDefinitionQuery().list();
		
	}
	
	/**
	 * 删除流程定义
	 * @param deployId 发布流程Id
	 */
	public void removeProcessDefinition(String deployId){
		//使用级联删除  如果有流程实例也一并删除 
		repositoryService.deleteDeploymentCascade(deployId);
		
	}
	
	/**
	 * 发布流程定义
	 * @param wfzip zip路径
	 */
	public void createProcessDefinition(String wfzip){
	//打成zip包 批量发布流程 (将流程png图片也发布到流程引擎中去)
	ZipInputStream zipInputStream=new ZipInputStream(this.getClass().getResourceAsStream(wfzip));
	repositoryService.createDeployment().addResourcesFromZipInputStream(zipInputStream).deploy();
	}
	
	
	/**
	 * 根据流程定义Id和活动名称 找到活动坐标
	 * @param processDefiniId 流程定义Id
	 * @param activityName 活动名称
	 * @return ActivityCoordinates
	 */
	public ActivityCoordinates findActivityCoordinates(String processDefiniId,String activityName){
		
		 return repositoryService.getActivityCoordinates(processDefiniId,activityName);

	}
	
	
	/**
	 * 启动一个流程实例
	 * @param definitionId 流程定义Id
	 */
	public ProcessInstance createProcessInstance(String definitionId){
		
		 ProcessInstance processInstance=executionService.startProcessInstanceById(definitionId);
		 
		 return processInstance;
	}
	
	/**
	 * 启动一个流程实例
	 * @param definitionId 流程定义Id
	 * @param map 流程变量
	 */
	public ProcessInstance createProcessInstance(String definitionId,Map map){
		
		 ProcessInstance processInstance=executionService.startProcessInstanceById(definitionId, map);
		 
		 return processInstance;
	}
	
	/**
	 * 流程实例列表
	 * @return List<ProcessInstance>
	 */
	public List<ProcessInstance> findProcessInstanceList(){
		
	return	executionService.createProcessInstanceQuery().list();
		
	}

	/**
	 * 删除实例
	 * @param instanceId 流程实例Id
	 */
	public void removeProcessInstance(String instanceId){
		
		executionService.deleteProcessInstanceCascade(instanceId);
		
	}
	
	/**
	 * 查找流程实例
	 * @param instanceId 流程实例Id
	 */
	public ProcessInstance findProcessInstance(String instanceId){
		
		return executionService.findProcessInstanceById(instanceId);
		
	}
	
	/**
	 * 得到个人待办
	 * @param personId 
	 * @return List<Task>
	 */
	public List<Task> findPersonTasktoList(String personId){
		
		return taskService.findPersonalTasks(personId);
		
	}
	
	/**
	 * 根据taskId获取任务
	 * @param taskId 任务Id
	 * @return Task
	 */
	public Task findTaskById(String taskId){
		
		return taskService.getTask(taskId);
		
	}
	
	/**
	 * 提交流程
	 * @param taskId 任务Id
	 * @param outcome 转移名
	 * @param map 流程变量
	 */
	public void processTask(String taskId,String outcome,Map map){
		
		taskService.completeTask(taskId, outcome, map);
		
	}
	
	/**
	 * 提交流程
	 * @param taskId 任务Id
	 * @param outcome 转移名
	 */
	public void processTask(String taskId,String outcome){
		
		taskService.completeTask(taskId, outcome);
	}
	
	/**
	 * 根据taskId获取流程实例中的变量
	 * @param taskId 任务Id
	 * @param setVariables 流程变量set集合
	 * @return Map
	 */
	public Map getVariablesByTaskIdtoMap(String taskId,Set setVariables){
		
		Map mapVariable=taskService.getVariables(taskId, setVariables);
		
		return mapVariable;
	}
	
	
	/**
	 * 根据taskId获取流程实例中的变量
	 * @param taskId 任务Id
	 * @return Set 
	 */
	public Set getVariablesByTaskIdtoSet(String taskId){
		
		Set setVariables=taskService.getVariableNames(taskId);
		
		return setVariables;
	}
	
	
	/**
	 * 根据流程实例Id和用户名 查找用户的当前任务
	 * @param assignee
	 * @param processInstanceId
	 * @return
	 */
	public Task findUserTask(String assignee,String processInstanceId){
		
	return 	taskService.createTaskQuery().processInstanceId(processInstanceId).assignee(assignee).uniqueResult();
	
	}
	
	/**
	 * 根据executionId获取流程实例中的变量
	 * @param executionId 流程实例Id
	 * @param setVariables 流程变量set集合
	 * @return Map
	 */
	public Map getVariablesByexeIdtoMap(String executionId,Set setVariables){
		
		Map mapVariable=executionService.getVariables(executionId, setVariables);
		
		return mapVariable;
	}
	
	/**
	 * 根据executionId获取流程实例中的变量
	 * @param executionId 流程实例Id
	 * @return Set
	 */
	public Set getVariablesByexeIdtoSet(String executionId){
		
		return executionService.getVariableNames(executionId);
	}
	
	
	/**
	 * 根据executionId流程实例Id设置流程实例中的变量
	 * @param executionId 流程实例Id
	 * @param argumentName 变量名
	 * @param argumentValue 变量值
	 */
	public void setVariablesByexeId(String executionId,String argumentName,Object argumentValue){
		
		executionService.setVariable(executionId, argumentName, argumentValue);
	
	}
	
	/**
	 * 根据executionId流程实例Id设置流程实例中的变量
	 * @param executionId 流程实例Id
	 * @param map 流利变量map集合
	 */
	public void setVariablesByexeId(String executionId,Map map){
		
		executionService.setVariables(executionId, map);
	
	}
}