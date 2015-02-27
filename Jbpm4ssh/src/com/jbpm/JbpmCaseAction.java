package com.jbpm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.task.Task;

import com.commons.CONSTANT;
import com.entity.leave.Leave;
import com.helper.beans.UserHelperBean;
import com.opensymphony.xwork2.ActionSupport;
import com.services.service.jbpm.JbpmService;

public class JbpmCaseAction extends ActionSupport implements RequestAware{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6232033631877291813L;
	
	private JbpmService jbpmService;
	private Map<String,Object> request;
	private UserHelperBean userHelperBean;
	
	public UserHelperBean getUserHelperBean() {
		return userHelperBean;
	}
	
	public void setUserHelperBean(UserHelperBean userHelperBean) {
		this.userHelperBean = userHelperBean;
	}
	
	public void setJbpmService(JbpmService jbpmService) {
		this.jbpmService = jbpmService;
	}
	
	public void setRequest(Map<String, Object> request) {
		this.request=request;
		
	}
	
	/**
	 * 发布流程定义
	 * @return
	 */
	public String DeployDefinition(){
		String wfzip="/leave3.zip";
		jbpmService.createProcessDefinition(wfzip);
		return "backPersonIndex";
	}
	/**
	 * 删除流程定义
	 * @return
	 */
	public String removeDefinition(){
		
		String deployId=userHelperBean.getDeployId();
		if(null!=deployId){
		jbpmService.removeProcessDefinition(deployId);
		}
		
		return "backPersonIndex";
	}
	
	/**
	 * 启动一个新流程实例
	 * @return
	 */
	public String createProcessInstance(){
		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		
		String userName=getSessionUser();
		
		Map<String,String> map =new HashMap();
		
		map.put("ower", userName);
		
		String definitionId=userHelperBean.getDefitionId();
		
		if(null!=definitionId){
			//把当前人作为申请人
			jbpmService.createProcessInstance(definitionId);
		}
		
		return "backPersonIndex";
	}
	
	/**
	 * 删除流程实例
	 * @return
	 */
	public String removeProcessInstance(){
		
		String instanceId=userHelperBean.getInstanceId();
		
		if(null!=instanceId){
			jbpmService.removeProcessInstance(instanceId);
			
		}
		return "backPersonIndex";
	}
	
	/**
	 * 用户登录
	 * @return
	 */
	public String userLogin(){
	//获得httpServletRequest
	HttpServletRequest httpRequest=ServletActionContext.getRequest();
	String userName=userHelperBean.getUserName();
	List list=null;
	if(null!=userName){
	list=jbpmService.findUser(userName);
	}
	//验证用户
	if(null==list || list.size()==0){
	request.put("message", "此用户名不存在");
	return "login";
	}else{
	//保存用户
	httpRequest.getSession().setAttribute("user",userName);
	}
	return "backPersonIndex";
	}
	
	/**
	 * 获得个人工作台
	 * @return
	 */
	public String showPersonDesk(){
		//发布的流程定义列表 
		List<ProcessDefinition> definitionList=jbpmService.findProcessDefinitionList();
		//流程实例列表 
		List<ProcessInstance> instanceList=jbpmService.findProcessInstanceList();
		//获得用户待办任务
		List<Task> taskList=null;
		String userName = getSessionUser();
		taskList=jbpmService.findPersonTasktoList(userName);
		request.put("userHelperBean", userHelperBean);
		request.put("dList", definitionList);
		request.put("iList", instanceList);
		request.put("tList", taskList);
		return "personIndex";
	}
	
	/**
	 * 显示任务详细
	 * @return
	 */
	public String viewPersonTask(){
		String taskId=userHelperBean.getTaskId();
	    Task task=jbpmService.findTaskById(taskId);
	    String activityName=task.getActivityName();
	    
	    if(activityName.equals(CONSTANT.ACTIVITYNAMEJL) || activityName.equals(CONSTANT.ACTIVITYNAMEBOSS)){
	    	
	    Map map=jbpmService.getVariablestoMap(taskId);
		
	    request.put("userMap", map);
			
		}
	    request.put("taskId", taskId);
		request.put("activityName", activityName);
		return "viewTask";
	}
	
	/**
	 * 处理任务
	 * @return
	 */
	public String processPersonTask(){
		
		jbpmService.processWfTask(userHelperBean);
		
		return "backPersonIndex";
		
	}
	
	/**
	 * 查看流程图
	 * @return
	 */
	public String viewWorkFlow(){
		String taskId=userHelperBean.getTaskId();
		Task task=jbpmService.findTaskById(taskId);
		ActivityCoordinates ac=jbpmService.findActivityCoordinates(taskId);
		request.put("ac", ac);
		request.put("eId", task.getExecutionId());
		return "viewWorkFlow";
	}
	
	/**
	 * 填写请假单并发起流程
	 * @return
	 */
	public String createLeaveWorkFlow(){
		
		String userName=getSessionUser();
		try{
		jbpmService.createLeaveAndStartWf(userHelperBean, userName);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "backPersonIndex";
	}
	
	/**
	 * 获得当前用户
	 * @return
	 */
	private String getSessionUser(){
		//获得httpServletRequest
		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		return (String)httpRequest.getSession().getAttribute("user");
	}
	
	/**
	 * 显示任务
	 * @return
	 */
	public String viewpersonTaskOther(){
		String taskId=userHelperBean.getTaskId();
		try{
		//得到此任务
		Task task=jbpmService.findTaskById(taskId);
		//得到此节点名称
	    String activityName=task.getActivityName();
	    
	    Map map=jbpmService.getVariablesByTaskIdtoMap(taskId, jbpmService.getVariablesByTaskIdtoSet(taskId));
	    
	    String Lid=(String)map.get("LID");
	    
	    //获取请假条数据
	    //Leave leave=jbpmService.processViewPersonTask(task.getExecutionId());
	    
	    Leave leave=jbpmService.processViewPersonTaskbyId(Lid);
	   
	    request.put("leave", leave);
	    request.put("map", map);
	    request.put("activityName", activityName);
	    request.put("taskId", taskId);
	    
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "viewTaskOther";
	}
	
	/**
	 * 处理任务
	 * @return
	 */
	public String processPersonTaskOther(){
		
		String taskId=userHelperBean.getTaskId();
		String activityName=userHelperBean.getActivityName();
		String approveName=userHelperBean.getApproveName();
		
		if(activityName.equals(CONSTANT.ACTIVITYNAMEJLO)){
		if(approveName.equals(CONSTANT.OUTCOMEPZ)){
		//大于3的情况下走boss
		if(Integer.parseInt(userHelperBean.getDay())>3){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("boss", "boss");
		map.put("fboss", "fboss");
		jbpmService.processTask(taskId, CONSTANT.OUTCOMEPZ,map);
		}else{
		jbpmService.processTask(taskId, CONSTANT.OUTCOMEPZ);
		}
		}else if(approveName.equals(CONSTANT.OUTCOMEBH)){
			jbpmService.processTask(taskId, CONSTANT.OUTCOMEBH);
		}else if(approveName.equals(CONSTANT.OUTCOMEBPZ)){
			jbpmService.processTask(taskId, CONSTANT.OUTCOMEBPZ);
		}
		}else if(activityName.equals(CONSTANT.ACTIVITYNAMEBOSS)){
			
			/*
			if(approveName.equals(CONSTANT.OUTCOMEBPZ)){
				jbpmService.processTask(taskId, CONSTANT.OUTCOMEBPZ);	
			}else if(approveName.equals(CONSTANT.OUTCOMEPZ)){
				jbpmService.processTask(taskId, CONSTANT.OUTCOMEPZ);	
			}else if(approveName.equals(CONSTANT.OUTCOMEBH)){
				jbpmService.processTask(taskId, CONSTANT.OUTCOMEBH);	
			}
			*/
			
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("approveBoss", approveName);
			jbpmService.processTask(taskId, CONSTANT.OUTCOMESP,map);
			
			
		}else if(activityName.equals(CONSTANT.ACTIVITYNAMEFBOSS)){
			
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("approvefBoss", approveName);
			jbpmService.processTask(taskId, CONSTANT.OUTCOMESP,map);
			
		}
		return "backPersonIndex";
		
	}
	
	/**
	 * 经办人修改过后提交
	 * @return
	 */
	public String processPersonTaskModify(){
		
		String taskId=userHelperBean.getTaskId();
		//得到此任务
		Task task=jbpmService.findTaskById(taskId);
		
		 //获取流程变量
		Map map=jbpmService.getVariablesByTaskIdtoMap(taskId, jbpmService.getVariablesByTaskIdtoSet(taskId));
		
		//修改流程天数
		map.put("day", userHelperBean.getDay());
	    userHelperBean.setLeaveId((String)map.get("LID"));
		//userHelperBean.setExecutionId(task.getExecutionId());
		
		//修改请假条数据
	   //jbpmService.updateViewPersonTask(userHelperBean);
	    
	    jbpmService.updateViewPersonTaskbyId(userHelperBean);
	    
		//完成任务
		jbpmService.processTask(taskId, CONSTANT.OUTCOMETJ,map);
	    
		return "backPersonIndex";
	}
}
