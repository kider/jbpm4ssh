package com.entity.leave;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Group implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7102568373911777260L;

	private String groupId;
	private String groupName;
	private Set users=new HashSet(0);
	
	public Set getUsers() {
		return users;
	}
	public void setUsers(Set users) {
		this.users = users;
	}
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public  Group(){
		
	}
	
	public Group(String groupId,String groupName,Set users){
		this.groupId=groupId;
		this.groupName=groupName;
		this.users=users;
	}
}
