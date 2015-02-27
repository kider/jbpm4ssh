package com.services.serviceImpl.jbpm;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.task.Task;

import com.commons.COMMON;
import com.commons.CONSTANT;
import com.daos.dao.jbpm.JbpmDAO;
import com.daos.dao.jbpm.LeaveDAO;
import com.daos.dao.jbpm.SysUserDAO;
import com.entity.leave.Leave;
import com.entity.leave.User;
import com.helper.beans.UserHelperBean;
import com.services.service.jbpm.JbpmService;
public class JbpmServiceImpl implements JbpmService{
	
	private JbpmDAO jbpmDAO;
	private SysUserDAO sysUserDAO;
	private LeaveDAO leaveDAO;
	
	public void setSysUserDAO(SysUserDAO sysUserDAO) {
		this.sysUserDAO = sysUserDAO;
	}

	public void setLeaveDAO(LeaveDAO leaveDAO) {
		this.leaveDAO = leaveDAO;
	}
	
	public void setJbpmDAO(JbpmDAO jbpmDAO) {
		this.jbpmDAO = jbpmDAO;
	}
	
	/**
	 * 发布的流程定义列表
	 * @return List<ProcessDefinition>
	 */
	public List<ProcessDefinition> findProcessDefinitionList(){
		
	return jbpmDAO.findProcessDefinitionList();
		
	}
	
	/**
	 * 删除流程定义
	 * @param deployId 发布流程Id
	 */
	public void removeProcessDefinition(String deployId){
		//使用级联删除  如果有流程实例也一并删除 
		jbpmDAO.removeProcessDefinition(deployId);
		
	}
	
	/**
	 * 发布流程定义
	 * @param wfzip zip路径
	 */
	public void createProcessDefinition(String wfzip){
	//打成zip包 批量发布流程 (将流程png图片也发布到流程引擎中去)
	jbpmDAO.createProcessDefinition(wfzip);
	}
	
	
	
	/**
	 * 启动一个流程实例
	 * @param definitionId 流程定义Id
	 */
	public ProcessInstance createProcessInstance(String definitionId){
		
		 ProcessInstance processInstance=jbpmDAO.createProcessInstance(definitionId);
		 
		 return processInstance;
	}
	
	/**
	 * 启动一个流程实例
	 * @param definitionId 流程定义Id
	 * @param map 流程变量
	 */
	public ProcessInstance createProcessInstance(String definitionId,Map map){
		
		
		 return jbpmDAO.createProcessInstance(definitionId,map);
		
	}
	
	
	/**
	 * 流程实例列表
	 * @return List<ProcessInstance>
	 */
	public List<ProcessInstance> findProcessInstanceList(){
		
	return	jbpmDAO.findProcessInstanceList();
		
	}

	/**
	 * 删除实例
	 * @param instanceId 流程实例Id
	 */
	public void removeProcessInstance(String instanceId){
		
		jbpmDAO.removeProcessInstance(instanceId);
		
	}
	
	/**
	 * 查找流程实例
	 * @param instanceId 流程实例Id
	 */
	public ProcessInstance findProcessInstance(String instanceId){
		
		return jbpmDAO.findProcessInstance(instanceId);
		
	}
	
	/**
	 * 得到个人待办
	 * @param personId 
	 * @return List<Task>
	 */
	public List<Task> findPersonTasktoList(String personId){
		
		return jbpmDAO.findPersonTasktoList(personId);
		
	}
	
	/**
	 * 根据taskId获取任务
	 * @param taskId 任务Id
	 * @return Task
	 */
	public Task findTaskById(String taskId){
		
		return jbpmDAO.findTaskById(taskId);
		
	}
	
	/**
	 * 根据任务Id得到流程变量
	 * @param taskId
	 * @return Map
	 */
	public Map getVariablestoMap(String taskId){
		
		Set valSet=jbpmDAO.getVariablesByTaskIdtoSet(taskId);
		
		return jbpmDAO.getVariablesByTaskIdtoMap(taskId, valSet);
	}
	
	
	
	/**
	 * 提交流程
	 * @param taskId 任务Id
	 * @param outcome 转移名
	 * @param map 流程变量
	 */
	public void processTask(String taskId,String outcome,Map map){
		
		jbpmDAO.processTask(taskId, outcome, map);
		
	}
	
	/**
	 * 提交流程
	 * @param taskId 任务Id
	 * @param outcome 转移名
	 */
	public void processTask(String taskId,String outcome){
		
		jbpmDAO.processTask(taskId, outcome);
	}
	
	

	/**
	 * 找到流程活动节点坐标
	 * @param taskId
	 * @return ActivityCoordinates
	 */
	public ActivityCoordinates findActivityCoordinates(String taskId){
		
		
		
		Task task = jbpmDAO.findTaskById(taskId);
		
		ProcessInstance processInstance = jbpmDAO.findProcessInstance(task.getExecutionId());
		
		Set<String> activityNames = processInstance.findActiveActivityNames();
		
		return jbpmDAO.findActivityCoordinates(processInstance.getProcessDefinitionId(), activityNames.iterator().next());
	}
	
	/**
	 * 填写请假单并发起流程
	 * @param userHelperBean
	 * @param username
	 * @return
	 */
	public void createLeaveAndStartWf(UserHelperBean userHelperBean,String userName){
		User sysUser=(User)findUser(userName).iterator().next();
		
		Map<String,Object> userMap=new HashMap<String,Object>();
		
		userMap.put("ower", userName);
		userMap.put("day", Integer.parseInt(userHelperBean.getDay()));
		
		ProcessInstance processInstance=createProcessInstance(userHelperBean.getDefitionId(), userMap);
		
		Task task=findUserTask(userName, processInstance.getId());
		
		//保存流程实例Id
		userHelperBean.setExecutionId(processInstance.getId());
		
		
		//保存请假表数据
		saveLeaveInfo(userHelperBean);
		
		//获得角色ID
		String roleId=sysUser.getGroup().getGroupId();
		//设置流程参数
		Map<String,Object> roleMap=new HashMap<String,Object>();
		roleMap.put("ismanager",roleId);
		//如果当前人为经理 则直接送给boss
		if(roleId.equals("2")){
		roleMap.put("boss", "boss");
		roleMap.put("fboss", "fboss");
		}else{
		roleMap.put("manager", "liyan");
		}
		//完成第一个节点
		processTask(task.getId(), CONSTANT.OUTCOMETJ,roleMap);
	}
	
	/**
	 * 处理用户提交任务
	 * @param userHelperBean
	 */
	public void processWfTask(UserHelperBean userHelperBean){
		
		if(userHelperBean.getActivityName().equals(CONSTANT.ACTIVITYNAMESQ)){
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("day", Integer.parseInt(userHelperBean.getDay()));
			map.put("reason", userHelperBean.getReason());
			processTask(userHelperBean.getTaskId(),CONSTANT.OUTCOMEPZ, map);
			}else if(userHelperBean.getActivityName().equals(CONSTANT.ACTIVITYNAMEJL)){
			if(userHelperBean.getApproveName().equals(CONSTANT.OUTCOMEPZ)){
			processTask(userHelperBean.getTaskId(), CONSTANT.OUTCOMEPZ);
			}else if(userHelperBean.getApproveName().equals(CONSTANT.OUTCOMEBH)){
			processTask(userHelperBean.getTaskId(), CONSTANT.OUTCOMEBH);
			}
			}else if(userHelperBean.getActivityName().equals(CONSTANT.ACTIVITYNAMEBOSS)){
			processTask(userHelperBean.getTaskId(), CONSTANT.OUTCOMEJS);	
			}
	}
	
	/**
	 * 根据taskId获取流程实例中的变量
	 * @param taskId 任务Id
	 * @param setVariables 流程变量set集合
	 * @return Map
	 */
	public Map getVariablesByTaskIdtoMap(String taskId,Set setVariables){
		
		Map mapVariable=jbpmDAO.getVariablesByTaskIdtoMap(taskId, setVariables);
		
		return mapVariable;
	}
	
	
	/**
	 * 根据taskId获取流程实例中的变量
	 * @param taskId 任务Id
	 * @return Set 
	 */
	public Set getVariablesByTaskIdtoSet(String taskId){
		
		Set setVariables=jbpmDAO.getVariablesByTaskIdtoSet(taskId);
		
		return setVariables;
	}
	
	
	/**
	 * 根据流程实例Id和用户名 查找用户的当前任务
	 * @param assignee
	 * @param processInstanceId
	 * @return
	 */
	public Task findUserTask(String assignee,String processInstanceId){
		
	return 	jbpmDAO.findUserTask(assignee, processInstanceId);
	
	}
	
	/**
	 * 根据executionId获取流程实例中的变量
	 * @param executionId 流程实例Id
	 * @param setVariables 流程变量set集合
	 * @return Map
	 */
	public Map getVariablesByexeIdtoMap(String executionId,Set setVariables){
		
		Map mapVariable=jbpmDAO.getVariablesByexeIdtoMap(executionId, setVariables);
		
		return mapVariable;
	}
	
	/**
	 * 根据executionId获取流程实例中的变量
	 * @param executionId 流程实例Id
	 * @return Set
	 */
	public Set getVariablesByexeIdtoSet(String executionId){
		
		return jbpmDAO.getVariablesByexeIdtoSet(executionId);
	}
	
	
	/**
	 * 根据executionId流程实例Id设置流程实例中的变量
	 * @param executionId 流程实例Id
	 * @param argumentName 变量名
	 * @param argumentValue 变量值
	 */
	public void setVariablesByexeId(String executionId,String argumentName,Object argumentValue){
		
		jbpmDAO.setVariablesByexeId(executionId, argumentName, argumentValue);
	
	}
	
	/**
	 * 根据executionId流程实例Id设置流程实例中的变量
	 * @param executionId 流程实例Id
	 * @param map 流利变量map集合
	 */
	public void setVariablesByexeId(String executionId,Map map){
		
		jbpmDAO.setVariablesByexeId(executionId, map);
	
	}

	
	
	
	/**
	 * 根据用户名查找用户
	 * @param userName
	 * @return List
	 */
	public List findUser(String userName){
		
		return sysUserDAO.find("from User where userName ='"+userName+"'");
		
	}
	
	
	/**
	 * 保存请假条数据
	 * @param userHelperBean
	 */
	public void saveLeaveInfo(UserHelperBean userHelperBean){
		
		String leaveId=COMMON.getUUID();
		Leave leave=new Leave();
		leave.setId(leaveId);
		leave.setLeaveDay(userHelperBean.getDay());
		leave.setLeaveReason(userHelperBean.getReason());
		leave.setWorkFlowId(userHelperBean.getExecutionId());
		
		//把业务Id设置到流程变量中去
		setVariablesByexeId(userHelperBean.getExecutionId(), "LID", leaveId);
		
		try {
			leaveDAO.create(leave);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
			
		}
		
	}
	
	/**
	 * 根据executionId 获得请假条数据
	 * @param executionId
	 * @return
	 */
	public Leave processViewPersonTask(String executionId){
		
		String hql="from Leave where workFlowId='"+executionId+"'";
		
		List<Leave> list=leaveDAO.find(hql);
		
		Leave leave=null;
		if(!COMMON.isEmpty(list)){
			
			leave=list.iterator().next();
			
		}
	
		return leave;
	
	}
	
	
	/**
	 * 根据保存在流程变量中的业务ID获得请假条数据
	 * @param ID
	 * @return Leave
	 */
	public Leave processViewPersonTaskbyId(String ID){
		
		Leave leave=null;
		try {
			leave = (Leave) leaveDAO.findByPrimaryKey(ID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return leave;
	
	}
	
	/**
	 * 根据executionId 修改请假条数据
	 * @param userHelperBean
	 * @return
	 */
	public void updateViewPersonTask(UserHelperBean userHelperBean){
		
		String hql="from Leave where workFlowId='"+userHelperBean.getExecutionId()+"'";
		
		List<Leave> list=leaveDAO.find(hql);
		
		Leave leave=null;
		if(!COMMON.isEmpty(list)){
			
			leave=list.iterator().next();
			
			leave.setLeaveDay(userHelperBean.getDay());
			leave.setLeaveReason(userHelperBean.getReason());
			
			try {
				leaveDAO.update(leave);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
	
	
	}
	
	/**
	 * 根据保存在流程变量中的业务ID修改请假条数据
	 * @param userHelperBean
	 * @return
	 */
	public void updateViewPersonTaskbyId(UserHelperBean userHelperBean){
			
			Leave leave=null;
			try {
				leave = (Leave) leaveDAO.findByPrimaryKey(userHelperBean.getLeaveId());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(!COMMON.isEmpty(leave)){
			leave.setLeaveDay(userHelperBean.getDay());
			leave.setLeaveReason(userHelperBean.getReason());
			try {
				leaveDAO.update(leave);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException();
			}
			}
	}
	
	
}
