package com.daos.daoImpl.jbpm;

import com.commons.AbstractDaoSupport;
import com.daos.dao.jbpm.LeaveDAO;
import com.entity.leave.Leave;

public class LeaveDaoImpl extends AbstractDaoSupport implements LeaveDAO {

	public LeaveDaoImpl(){
		super(Leave.class);
	}
}
