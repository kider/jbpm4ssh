package com.services.service.jbpm;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.task.Task;

import com.entity.leave.Leave;
import com.helper.beans.UserHelperBean;


public interface JbpmService {
	/**
	 * 发布流程定义
	 * @param wfzip zip路径
	 */
	public void createProcessDefinition(String wfzip);
	
	/**
	 * 删除流程定义
	 * @param deployId 发布流程Id
	 */
	public void removeProcessDefinition(String deployId);
	
	/**
	 * 启动一个流程实例
	 * @param definitionId 流程定义Id
	 */
	public ProcessInstance createProcessInstance(String definitionId);
	
	/**
	 * 删除实例
	 * @param instanceId 流程实例Id
	 */
	public void removeProcessInstance(String instanceId);
	
	/**
	 * 根据用户名查找用户
	 * @param userName
	 * @return List
	 */
	public List findUser(String userName);
	
	/**
	 * 流程实例列表
	 * @return List<ProcessInstance>
	 */
	public List<ProcessInstance> findProcessInstanceList();
	
	/**
	 * 发布的流程定义列表
	 * @return List<ProcessDefinition>
	 */
	public List<ProcessDefinition> findProcessDefinitionList();
	
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
	 * 根据任务Id得到流程变量
	 * @param taskId
	 * @return Map
	 */
	public Map getVariablestoMap(String taskId);
	
	/**
	 * 提交流程
	 * @param taskId 任务Id
	 * @param outcome 转移名
	 */
	public void processTask(String taskId,String outcome);
	
	/**
	 * 提交流程
	 * @param taskId 任务Id
	 * @param outcome 转移名
	 * @param map 变量集合
	 */
	public void processTask(String taskId,String outcome,Map map);
	
	/**
	 * 找到流程活动节点坐标
	 * @param taskId
	 * @return ActivityCoordinates
	 */
	public ActivityCoordinates findActivityCoordinates(String taskId);
	
	/**
	 * 启动一个流程实例
	 * @param definitionId 流程定义Id
	 * @param map 流程变量
	 */
	public ProcessInstance createProcessInstance(String definitionId,Map map);
	
	
	/**
	 * 填写请假单并发起流程
	 * @param userHelperBean
	 * @param username
	 * @return
	 */
	public void createLeaveAndStartWf(UserHelperBean userHelperBean,String userName);
	
	/**
	 * 处理用户提交任务
	 * @param userHelperBean
	 */
	public void processWfTask(UserHelperBean userHelperBean);
	
	/**
	 * 根据保存在流程变量中的业务ID获得请假条数据
	 * @param ID
	 * @return Leave
	 */
	public Leave processViewPersonTaskbyId(String ID);
	
	/**
	 * 根据taskId获取流程实例中的变量
	 * @param taskId 任务Id
	 * @return Set 
	 */
	public Set getVariablesByTaskIdtoSet(String taskId);
	
	/**
	 * 根据taskId获取流程实例中的变量
	 * @param taskId 任务Id
	 * @param setVariables 流程变量set集合
	 * @return Map
	 */
	public Map getVariablesByTaskIdtoMap(String taskId,Set setVariables);
	
	/**
	 * 根据保存在流程变量中的业务ID修改请假条数据
	 * @param userHelperBean
	 * @return
	 */
	public void updateViewPersonTaskbyId(UserHelperBean userHelperBean);
	
}
