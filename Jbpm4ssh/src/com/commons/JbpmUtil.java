package com.commons;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.task.Task;

import com.daos.dao.jbpm.JbpmDAO;

public class JbpmUtil {
	
	private static JbpmDAO jbpmDAO=(JbpmDAO) SpringBeanFactory.getBean("jbpmDAO");
	
	
	/**
	 * 发布的流程定义列表
	 * @return List<ProcessDefinition>
	 */
	public static List<ProcessDefinition> findProcessDefinitionList(){
		
	return jbpmDAO.findProcessDefinitionList();
		
	}
	
	/**
	 * 删除流程定义
	 * @param deployId 发布流程Id
	 */
	public static void removeProcessDefinition(String deployId){
		//使用级联删除  如果有流程实例也一并删除 
		jbpmDAO.removeProcessDefinition(deployId);
		
	}
	
	/**
	 * 发布流程定义
	 * @param wfzip zip路径
	 */
	public static void createProcessDefinition(String wfzip){
	//打成zip包 批量发布流程 (将流程png图片也发布到流程引擎中去)
		jbpmDAO.createProcessDefinition(wfzip);
	}
	
	
	/**
	 * 根据流程定义Id和活动名称 找到活动坐标
	 * @param processDefiniId 流程定义Id
	 * @param activityName 活动名称
	 * @return ActivityCoordinates
	 */
	public static ActivityCoordinates findActivityCoordinates(String processDefiniId,String activityName){
		
		 return jbpmDAO.findActivityCoordinates(processDefiniId, activityName);

	}
	
	
	/**
	 * 启动一个流程实例
	 * @param definitionId 流程定义Id
	 */
	public static ProcessInstance createProcessInstance(String definitionId){
		
		return jbpmDAO.createProcessInstance(definitionId);
	}
	
	/**
	 * 启动一个流程实例
	 * @param definitionId 流程定义Id
	 * @param map 流程变量
	 */
	public static ProcessInstance createProcessInstance(String definitionId,Map map){
		
		 
		 return jbpmDAO.createProcessInstance(definitionId, map);
	}
	
	/**
	 * 流程实例列表
	 * @return List<ProcessInstance>
	 */
	public static List<ProcessInstance> findProcessInstanceList(){
		
	return	jbpmDAO.findProcessInstanceList();
		
	}

	/**
	 * 删除实例
	 * @param instanceId 流程实例Id
	 */
	public static void removeProcessInstance(String instanceId){
		
		jbpmDAO.removeProcessInstance(instanceId);
		
	}
	
	/**
	 * 查找流程实例
	 * @param instanceId 流程实例Id
	 */
	public static ProcessInstance findProcessInstance(String instanceId){
		
		return jbpmDAO.findProcessInstance(instanceId);
		
	}
	
	/**
	 * 得到个人待办
	 * @param personId 
	 * @return List<Task>
	 */
	public static List<Task> findPersonTasktoList(String personId){
		
		return jbpmDAO.findPersonTasktoList(personId);
		
	}
	
	/**
	 * 根据taskId获取任务
	 * @param taskId 任务Id
	 * @return Task
	 */
	public static Task findTaskById(String taskId){
		
		return jbpmDAO.findTaskById(taskId);
		
	}
	
	/**
	 * 提交流程
	 * @param taskId 任务Id
	 * @param outcome 转移名
	 * @param map 流程变量
	 */
	public static void processTask(String taskId,String outcome,Map map){
		
		jbpmDAO.processTask(taskId, outcome,map);
		
	}
	
	/**
	 * 提交流程
	 * @param taskId 任务Id
	 * @param outcome 转移名
	 */
	public static void processTask(String taskId,String outcome){
		
		jbpmDAO.processTask(taskId, outcome);
	}
	
	/**
	 * 根据taskId获取流程实例中的变量
	 * @param taskId 任务Id
	 * @param setVariables 流程变量set集合
	 * @return Map
	 */
	public static Map getVariablesByTaskIdtoMap(String taskId,Set setVariables){
		
		
		return jbpmDAO.getVariablesByTaskIdtoMap(taskId, setVariables);
	}
	
	
	/**
	 * 根据taskId获取流程实例中的变量
	 * @param taskId 任务Id
	 * @return Set 
	 */
	public static Set getVariablesByTaskIdtoSet(String taskId){
		
		
		return jbpmDAO.getVariablesByTaskIdtoSet(taskId);
	}
	
	
	/**
	 * 根据流程实例Id和用户名 查找用户的当前任务
	 * @param assignee
	 * @param processInstanceId
	 * @return
	 */
	public static Task findUserTask(String assignee,String processInstanceId){
		
	return 	jbpmDAO.findUserTask(assignee, processInstanceId);
	
	}
	
	/**
	 * 根据executionId获取流程实例中的变量
	 * @param executionId 流程实例Id
	 * @param setVariables 流程变量set集合
	 * @return Map
	 */
	public static Map getVariablesByexeIdtoMap(String executionId,Set setVariables){
		
		Map mapVariable=jbpmDAO.getVariablesByexeIdtoMap(executionId, setVariables);
		
		return mapVariable;
	}
	
	/**
	 * 根据executionId获取流程实例中的变量
	 * @param executionId 流程实例Id
	 * @return Set
	 */
	public static Set getVariablesByexeIdtoSet(String executionId){
		
		return jbpmDAO.getVariablesByexeIdtoSet(executionId);
	}
	
	
	/**
	 * 根据executionId流程实例Id设置流程实例中的变量
	 * @param executionId 流程实例Id
	 * @param argumentName 变量名
	 * @param argumentValue 变量值
	 */
	public static void setVariablesByexeId(String executionId,String argumentName,Object argumentValue){
		
		jbpmDAO.setVariablesByexeId(executionId, argumentName, argumentValue);
	
	}
	
	/**
	 * 根据executionId流程实例Id设置流程实例中的变量
	 * @param executionId 流程实例Id
	 * @param map 流利变量map集合
	 */
	public static void setVariablesByexeId(String executionId,Map map){
		
		jbpmDAO.setVariablesByexeId(executionId, map);
	
	}
}
