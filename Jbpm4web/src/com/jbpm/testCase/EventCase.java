package com.jbpm.testCase;

import junit.framework.TestCase;
import org.jbpm.api.*;

public class EventCase extends TestCase {
	private ProcessEngine processEngine;
	
	public EventCase(){
		
		processEngine=Configuration.getProcessEngine();
		
	}
	public void testEvent(){
		RepositoryService repositoryService=processEngine.getRepositoryService();
		ExecutionService executionService=processEngine.getExecutionService();
		repositoryService.createDeployment().addResourceFromClasspath("event.jpdl.xml").deploy();
		executionService.startProcessInstanceByKey("event");
	}
	
	
	
	

}
