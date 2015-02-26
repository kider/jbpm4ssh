package com.jbpm.testCase;
import java.util.*;

import junit.framework.TestCase;

import org.jbpm.api.*;
import org.jbpm.api.task.Task;
public class GroupCase extends TestCase{
	private ProcessEngine processEngine;
	
	public GroupCase(){
		
		processEngine=Configuration.getProcessEngine();
		IdentityService identityService=processEngine.getIdentityService();
		identityService.createGroup("dev");
		identityService.createUser("user1", "user1", "user1");
		identityService.createUser("user2", "user2", "user2");
		identityService.createMembership("user1", "dev");
		identityService.createMembership("user2", "dev");
		
	}
	
	
	public void testGroup(){
		
		RepositoryService repositoryService=processEngine.getRepositoryService();
		
		TaskService taskService=processEngine.getTaskService();
		
		ExecutionService executionService=processEngine.getExecutionService();
		
		repositoryService.createDeployment().addResourceFromClasspath("groupTask.jpdl.xml").deploy();
		
		ProcessInstance processInstance=executionService.startProcessInstanceByKey("groupTask");
		
		
		assertEquals(1, taskService.findGroupTasks("user1").size());
		assertEquals(1, taskService.findGroupTasks("user2").size());
		
		List<Task> list=taskService.findGroupTasks("user1");
		
		Iterator<Task> taskIt=list.iterator();
		
		
		while(taskIt.hasNext()){
		Task task=taskIt.next();
		taskService.takeTask(task.getId(),"user1");
		taskService.completeTask(task.getId());
			
		}
		
		
		
		
	}
	

}
