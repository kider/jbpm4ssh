package com.daos.daoImpl.jbpm;

import com.commons.AbstractDaoSupport;

import com.daos.dao.jbpm.SysUserDAO;
import com.entity.leave.User;

public class SysUserDaoImpl extends AbstractDaoSupport implements SysUserDAO {

	public SysUserDaoImpl(){
		super(User.class);
	}
	
}
