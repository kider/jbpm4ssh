package com.jbpm.testCase;

import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;

public class StateStartListener implements EventListener {

	@Override
	public void notify(EventListenerExecution execution) throws Exception {
		// TODO Auto-generated method stub

		System.out.println(execution.getId()    +"  this is stateStartListener.......");
	}

}
