package com.entity.leave;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7103543782668937613L;
	
	public User(){
		
	}
	
	public User(String userId,String userName,Group group){
		this.userId=userId;
		this.userName=userName;
		this.group=group;
	}
	
	private String userId;
	private String userName;
	private Group group;

	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	
}
