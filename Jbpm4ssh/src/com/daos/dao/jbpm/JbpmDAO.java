package com.daos.dao.jbpm;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

public interface JbpmDAO {
	/**
	 * 获取jbpm repositoryService服务
	 * @return
	 */
	public RepositoryService getRepositoryService();
	
	/**
	 * 获取jbpm executionService服务
	 * @return
	 */
	public ExecutionService getExecutionService();
	
	/**
	 * 获取jbpm HistoryService服务
	 * @return
	 */
	public HistoryService getHistoryService();
	
	/**
	 * 获取jbpm ManagementService服务
	 * @return
	 */
	public ManagementService getManagementService();
	
	/**
	 * 获取jbpm IdentityService服务
	 * @return
	 */
	public IdentityService getIdentityService();
	
	/**
	 * 获取jbpm taskService服务
	 * @return
	 */
	public TaskService getTaskService();
	
	/**
	 * 发布的流程定义列表
	 * @return List<ProcessDefinition>
	 */
	public List<ProcessDefinition> findProcessDefinitionList();
	
	/**
	 * 删除流程定义
	 * @param deployId 发布流程Id
	 */
	public void removeProcessDefinition(String deployId);
	
	/**
	 * 发布流程定义
	 * @param wfzip zip路径
	 */
	public void createProcessDefinition(String wfzip);
	
	/**
	 * 根据流程定义Id和活动名称 找到活动坐标
	 * @param processDefiniId 流程定义Id
	 * @param activityName 活动名称
	 * @return ActivityCoordinates
	 */
	public ActivityCoordinates findActivityCoordinates(String processDefiniId,String activityName);
	
	/**
	 * 启动一个流程实例
	 * @param definitionId 流程定义Id
	 */
	public ProcessInstance createProcessInstance(String definitionId);
	
	/**
	 * 流程实例列表
	 * @return List<ProcessInstance>
	 */
	public List<ProcessInstance> findProcessInstanceList();

	/**
	 * 删除实例
	 * @param instanceId 流程实例Id
	 */
	public void removeProcessInstance(String instanceId);
	
	/**
	 * 查找流程实例
	 * @param instanceId 流程实例Id
	 */
	public ProcessInstance findProcessInstance(String instanceId);
	
	/**
	 * 得到个人待办
	 * @param personId 
	 * @return List<Task>
	 */
	public List<Task> findPersonTasktoList(String personId);
	
	/**
	 * 根据taskId获取任务
	 * @param taskId 任务Id
	 * @return Task
	 */
	public Task findTaskById(String taskId);
	
	/**
	 * 提交流程
	 * @param taskId 任务Id
	 * @param outcome 转移名
	 * @param map 流程变量
	 */
	public void processTask(String taskId,String outcome,Map map);
	
	/**
	 * 提交流程
	 * @param taskId 任务Id
	 * @param outcome 转移名
	 */
	public void processTask(String taskId,String outcome);
	
	/**
	 * 根据taskId获取流程实例中的变量
	 * @param taskId 任务Id
	 * @param setVariables 流程变量set集合
	 * @return Map
	 */
	public Map getVariablesByTaskIdtoMap(String taskId,Set setVariables);
	
	/**
	 * 根据taskId获取流程实例中的变量
	 * @param taskId 任务Id
	 * @return Set 
	 */
	public Set getVariablesByTaskIdtoSet(String taskId);
	
	/**
	 * 根据流程实例Id和用户名 查找用户的当前任务
	 * @param assignee
	 * @param processInstanceId
	 * @return
	 */
	public Task findUserTask(String assignee,String processInstanceId);
	
	/**
	 * 根据executionId获取流程实例中的变量
	 * @param executionId 流程实例Id
	 * @param setVariables 流程变量set集合
	 * @return Map
	 */
	public Map getVariablesByexeIdtoMap(String executionId,Set setVariables);
	
	/**
	 * 根据executionId获取流程实例中的变量
	 * @param executionId 流程实例Id
	 * @return Set
	 */
	public Set getVariablesByexeIdtoSet(String executionId);
	
	/**
	 * 根据executionId流程实例Id设置流程实例中的变量
	 * @param executionId 流程实例Id
	 * @param argumentName 变量名
	 * @param argumentValue 变量值
	 */
	public void setVariablesByexeId(String executionId,String argumentName,Object argumentValue);
	
	/**
	 * 根据executionId流程实例Id设置流程实例中的变量
	 * @param executionId 流程实例Id
	 * @param map 流利变量map集合
	 */
	public void setVariablesByexeId(String executionId,Map map);
	
	/**
	 * 启动一个流程实例
	 * @param definitionId 流程定义Id
	 * @param map 流程变量
	 */
	public ProcessInstance createProcessInstance(String definitionId,Map map);
}
