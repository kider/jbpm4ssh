package com.services.jbpmDecisionImpl;

import java.util.Map;

import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.OpenExecution;

import com.commons.CONSTANT;
import com.commons.JbpmUtil;

public class BossDecisionImpl implements DecisionHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5145998550594362009L;


	@Override
	public String decide(OpenExecution execution) {
		// TODO Auto-generated method stub
		
		System.out.println(execution.getId()+"************************************");
		
		
		Map map=JbpmUtil.getVariablesByexeIdtoMap(execution.getId(), JbpmUtil.getVariablesByexeIdtoSet(execution.getId()));
		
		
		String appboss=(String)map.get("approveBoss");
		
		String appfboss=(String)map.get("approvefBoss");
		
		if(appboss.equals(CONSTANT.OUTCOMEPZ) && appfboss.equals(CONSTANT.OUTCOMEPZ)){
			
			return CONSTANT.OUTCOMEPZ;
			
		}else if(appboss.equals(CONSTANT.OUTCOMEBH) || appfboss.equals(CONSTANT.OUTCOMEBH)){
			
		    return CONSTANT.OUTCOMEBH;
		    
		}else if(appboss.equals(CONSTANT.OUTCOMEBPZ) || appfboss.equals(CONSTANT.OUTCOMEBPZ)){
			
			return CONSTANT.OUTCOMEBPZ;
		}
		
		
		return CONSTANT.OUTCOMEPZ;
	}

}
