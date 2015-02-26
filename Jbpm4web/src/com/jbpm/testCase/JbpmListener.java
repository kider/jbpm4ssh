package com.jbpm.testCase;

import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;

public class JbpmListener implements EventListener {

	@Override
	public void notify(EventListenerExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(execution+" execution********************");
		System.out.println(execution.getName()+" execution.getName********************");
		System.out.println(execution.getState()+" execution.getState********************");
	}

}
