package com.entity.leave;

import java.io.Serializable;

public class Leave implements Serializable{

	private static final long serialVersionUID = 6228691558215034666L;
	
	public Leave(){
		
	}
	
	public Leave(String id,String workFlowId,String leaveDay,String leaveReason){
		this.id=id;
		this.workFlowId=workFlowId;
		this.leaveDay=leaveDay;
		this.leaveReason=leaveReason;
	}
	private String id;
	private String workFlowId;
	private String leaveDay;
	private String leaveReason;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWorkFlowId() {
		return workFlowId;
	}
	public void setWorkFlowId(String workFlowId) {
		this.workFlowId = workFlowId;
	}
	public String getLeaveDay() {
		return leaveDay;
	}
	public void setLeaveDay(String leaveDay) {
		this.leaveDay = leaveDay;
	}
	public String getLeaveReason() {
		return leaveReason;
	}
	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}
	
	

}
