package com.daos.daoImpl.jbpm;

import com.commons.AbstractDaoSupport;

import com.daos.dao.jbpm.SysRoleDAO;
import com.entity.leave.Group;

public class SysRoleDaoImpl  extends AbstractDaoSupport implements SysRoleDAO {

	public SysRoleDaoImpl(){
		super(Group.class);
	}
	
}
