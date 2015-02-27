package com.commons;

/*******************************************************************************
 * $Header$
 * $Revision$
 * $Date$
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2006 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2010-5-5
 *******************************************************************************/

import java.io.Serializable;

import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateAccessor;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public class AbstractDaoSupport extends HibernateDaoSupport implements IBaseDao {

	private JdbcTemplate jdbcTemplate;

	protected Class modelClass = null;

	public AbstractDaoSupport() {

	}

	@SuppressWarnings("unchecked")
	public AbstractDaoSupport(Class modelClass) {
		super();
		this.modelClass = modelClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#findByPrimaryKey(java.io.Serializable)
	 */
	public Object findByPrimaryKey(Serializable key) throws Exception {
		Object obj = null;
		try {
			obj = getHibernateTemplate().load(modelClass, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#create(java.lang.Object)
	 */
	public void create(Object obj) throws Exception {
		
		System.out.println(getHibernateTemplate().getSessionFactory().isClosed()   +"**************************************************************");

		getHibernateTemplate().save(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#update(java.lang.Object)
	 */
	public void update(Object obj) throws Exception {
		getHibernateTemplate().update(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#remove(java.lang.Object)
	 */
	public void remove(Object obj) throws Exception {
		getHibernateTemplate().delete(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#saveOrUpdate(java.lang.Object)
	 */
	public void saveOrUpdate(Object obj) throws Exception {
		getHibernateTemplate().saveOrUpdate(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#remove(java.io.Serializable)
	 */
	public void remove(Serializable key) throws Exception {
		Object obj = findByPrimaryKey(key);
		remove(obj);
	}

	@SuppressWarnings("deprecation")
	protected void checkWriteOperationAllowed(Session session)
			throws InvalidDataAccessApiUsageException {
		if (getHibernateTemplate().isCheckWriteOperations()
				&& getHibernateTemplate().getFlushMode() != HibernateAccessor.FLUSH_EAGER
				&& FlushMode.NEVER.equals(session.getFlushMode())) {
			throw new InvalidDataAccessApiUsageException(
					"Write operations are not allowed in read-only mode (FlushMode.NEVER) - turn your Session "
							+ "into FlushMode.AUTO or remove 'readOnly' marker from transaction definition");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#batchSave(java.util.Collection)
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void batchSave(final Collection objects) throws DataAccessException {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				int count = 0;
				checkWriteOperationAllowed(session);
				for (Iterator it = objects.iterator(); it.hasNext();) {
					session.save(it.next());
					if ((++count) % getJDBCBatchSize() == 0) {
						// 将本批插入的对象立即写入数据库并释放内存
						session.flush();
						session.clear();
					}
				}
				return null;
			}
		}, true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#batchSaveReturnIds(java.util.Collection)
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List batchSaveReturnIds(final Collection objects)
			throws DataAccessException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				checkWriteOperationAllowed(session);
				int count = 0;
				List ids = new ArrayList();
				for (Iterator it = objects.iterator(); it.hasNext();) {
					Object id = session.save(it.next());
					ids.add(id);
					if ((++count) % getJDBCBatchSize() == 0) {
						// 将本批插入的对象立即写入数据库并释放内存
						session.flush();
						session.clear();
					}
				}
				return ids;
			}
		}, true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#batchUpdate(java.util.Collection)
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void batchUpdate(final Collection objects)
			throws DataAccessException {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				checkWriteOperationAllowed(session);
				int count = 0;
				for (Iterator it = objects.iterator(); it.hasNext();) {
					session.update(it.next());
					if ((++count) % getJDBCBatchSize() == 0) {
						// 将本批插入的对象立即写入数据库并释放内存
						session.flush();
						session.clear();
					}
				}
				return null;
			}
		}, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#batchProcess(java.util.Collection,
	 *      java.util.List)
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void batchProcess(final Collection objects, final List operations)
			throws DataAccessException {
		if (operations == null || objects == null
				|| operations.size() != objects.size()) {
			throw new IllegalArgumentException("传入方法的参数不正确");
		}
		getHibernateTemplate().execute(new HibernateCallback() {
			@SuppressWarnings("unchecked")
			public Object doInHibernate(Session session)
					throws HibernateException {
				checkWriteOperationAllowed(session);
				int count = 0;
				Iterator it = objects.iterator();
				for (int i = 0; i < operations.size(); i++) {
					if (((Number) operations.get(i)).intValue() == COMMON.OPERATE_UPDATE) {
						session.update(it.next());
					}
					if (((Number) operations.get(i)).intValue() == COMMON.OPERATE_CREATE) {
						session.save(it.next());
					}
					if (((Number) operations.get(i)).intValue() == COMMON.OPERATE_REMOVE) {
						session.delete(it.next());
					}
					if ((++count) % getJDBCBatchSize() == 0) {
						// 将本批插入的对象立即写入数据库并释放内存
						session.flush();
						session.clear();
					}
				}
				return null;
			}
		}, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#update(java.lang.String,
	 *      java.lang.Object[])
	 */
	@SuppressWarnings("deprecation")
	public int update(final String hqlString, final Object[] params)
			throws DataAccessException {
		Object result = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Query queryObject = session.createQuery(hqlString);
				prepareQuery(queryObject);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						queryObject.setParameter(i, params[i]);
					}
				}
				return new Integer(queryObject.executeUpdate());
			}
		}, true);
		int value = ((Integer) result).intValue();
		result = null;
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#evict(java.util.Collection)
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void evict(final Collection entities) throws DataAccessException {
		this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				for (Iterator iter = entities.iterator(); iter.hasNext();) {
					session.evict(iter.next());
				}
				return null;
			}
		}, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#callProcedure(java.lang.String,
	 *      java.lang.Object[], int[])
	 */
	public Object callProcedure(final String callStr, final Object[] inValues,
			final int[] outTypes) {
		return getJdbcTemplate().execute(callStr,
				new CallableStatementCallback() {
					@SuppressWarnings("unchecked")
					public Object doInCallableStatement(CallableStatement cs)
							throws SQLException {
						int i = 0;
						if (inValues != null) {
							for (int index = 0; index < inValues.length; index++) {
								i++;

								Object inValue = inValues[index];
								if (inValue instanceof StringBuffer
										|| inValue instanceof StringWriter) {
									cs.setString(i, inValue.toString());
								} else if ((inValue instanceof java.util.Date)
										&& !(inValue instanceof java.sql.Date
												|| inValue instanceof java.sql.Time || inValue instanceof java.sql.Timestamp)) {
									cs.setTimestamp(i, new java.sql.Timestamp(
											((java.util.Date) inValue)
													.getTime()));
								} else if (inValue instanceof Calendar) {
									Calendar cal = (Calendar) inValue;
									cs.setTimestamp(i, new java.sql.Timestamp(
											cal.getTime().getTime()));
								} else {
									// Fall back to generic setObject call
									// without SQL type specified.
									cs.setObject(i, inValue);
								}
							}
						}
						if (outTypes != null) {
							for (int index = 0; index < outTypes.length; index++) {
								i++;
								cs.registerOutParameter(i, outTypes[index]);
							}
						}

						boolean retVal = cs.execute();
						int updateCount = cs.getUpdateCount();

						if (retVal || updateCount != -1) {
							// Map returnedResults = new HashMap();
							// returnedResults.putAll(extractReturnedResultSets(cs,
							// declaredParameters, updateCount));
						}
						if (outTypes == null || outTypes.length <= 0) {
							return null;
						} else if (outTypes.length == 1) {
							return cs.getObject(i);
						} else {
							List results = new ArrayList();
							// 依次返回结果
							for (int index = 0; index < outTypes.length; index++) {
								results.add(cs.getObject(inValues.length
										+ index + 1));

							}
							return results;
						}

					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#callFunction(java.lang.String,
	 *      java.lang.Object[], int[])
	 */
	public Object callFunction(final String callStr, final Object[] inValues,
			final int[] outTypes) {
		return getJdbcTemplate().execute(callStr,
				new CallableStatementCallback() {
					@SuppressWarnings("unchecked")
					public Object doInCallableStatement(CallableStatement cs)
							throws SQLException {
						int i = 0;
						if (outTypes != null) {
							for (int index = 0; index < outTypes.length; index++) {
								i++;
								cs.registerOutParameter(i, outTypes[index]);
							}
						}

						if (inValues != null) {
							for (int index = 0; index < inValues.length; index++) {
								i++;
								// StatementCreatorUtils.setParameterValue(cs,
								// i,
								// Integer.MIN_VALUE, null,
								// inValues[index]);
								Object inValue = inValues[index];
								if (inValue instanceof StringBuffer
										|| inValue instanceof StringWriter) {
									cs.setString(i, inValue.toString());
								} else if ((inValue instanceof java.util.Date)
										&& !(inValue instanceof java.sql.Date
												|| inValue instanceof java.sql.Time || inValue instanceof java.sql.Timestamp)) {
									cs.setTimestamp(i, new java.sql.Timestamp(
											((java.util.Date) inValue)
													.getTime()));
								} else if (inValue instanceof Calendar) {
									Calendar cal = (Calendar) inValue;
									cs.setTimestamp(i, new java.sql.Timestamp(
											cal.getTime().getTime()));
								} else {
									// Fall back to generic setObject call
									// without SQL type specified.
									cs.setObject(i, inValue);
								}
							}
						}

						boolean retVal = cs.execute();
						int updateCount = cs.getUpdateCount();

						if (retVal || updateCount != -1) {
							// Map returnedResults = new HashMap();
							// returnedResults.putAll(extractReturnedResultSets(cs,
							// declaredParameters, updateCount));
						}
						if (outTypes == null || outTypes.length <= 0) {
							return null;
						} else if (outTypes.length == 1) {
							return cs.getObject(1);
						} else {
							List results = new ArrayList();
							// 依次返回结果
							for (int index = 0; index < outTypes.length; index++) {
								results.add(cs.getObject(index + 1));

							}
							return results;
						}

					}
				});
	}

	/**
	 * Prepare the given Query object, applying cache settings and/or a
	 * transaction timeout.
	 * 
	 * @param queryObject
	 *            the Query object to prepare
	 * @see #setCacheQueries
	 * @see #setQueryCacheRegion
	 * @see SessionFactoryUtils#applyTransactionTimeout
	 */
	protected void prepareQuery(Query queryObject) {
		if (getHibernateTemplate().isCacheQueries()) {
			queryObject.setCacheable(true);
			if (getHibernateTemplate().getQueryCacheRegion() != null) {
				queryObject.setCacheRegion(getHibernateTemplate()
						.getQueryCacheRegion());
			}
		}
		SessionFactoryUtils.applyTransactionTimeout(queryObject,
				getHibernateTemplate().getSessionFactory());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#getJdbcTemplate()
	 */
	public JdbcTemplate getJdbcTemplate() {
		if (jdbcTemplate == null) {
			throw new NoSuchBeanDefinitionException("jdbcTemplate",
					"没有为数据访问对象配置Spring JDBC Template Bean.");
		}
		return jdbcTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#setJdbcTemplate(org.springframework.jdbc.core.JdbcTemplate)
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	protected int getJDBCBatchSize() {
		String batchProp = getHibernateEnvirProp(Environment.STATEMENT_BATCH_SIZE);

		if (batchProp == null || batchProp.length() == 0) {
			return 30;
		} else {
			return new Integer(batchProp).intValue();
		}
	}

	private String getHibernateEnvirProp(String key) {
		return Environment.getProperties().getProperty(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#updateProperty(java.lang.String,
	 *      java.lang.String[], java.lang.String[], java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public void updateProperty(String table, String[] properties,
			String[] conditions, List values) throws Exception {
		if (COMMON.isEmpty(table) || properties == null || values == null
				|| conditions == null || properties.length < 1
				|| values.size() != (properties.length + conditions.length)) {
			throw new Exception("传入了空的表名，或空的字段名/值，或属性名和属性值的数量不相匹配！");
		}

		StringBuffer command = new StringBuffer(" update ");
		command.append(table);
		command.append(" set ");

		for (int i = 0; i < properties.length; i++) {
			if (i == properties.length - 1) {
				command.append(properties[i]);
				command.append(" = ? ");
				break;
			} else {
				command.append(properties[i]);
				command.append(" = ? ");
				command.append(" , ");
			}
		}

		command.append(" where ");

		for (int i = 0; i < conditions.length; i++) {
			if (i == conditions.length - 1) {
				command.append(conditions[i]);
				command.append(" = ? ");
				break;
			} else {
				command.append(conditions[i]);
				command.append(" = ? ");
				command.append(" and ");
			}
		}

		this.update(command.toString(), values.toArray());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#getRowCount(java.lang.String,
	 *      java.lang.Object[])
	 */
	public int getRowCount(String fromAndWhere, Object[] paramValues) {
		Object countObj = this.getHibernateTemplate().find(
				"select count(*) " + fromAndWhere, paramValues).get(0);
		return ((Long) countObj).intValue();
	}

	// 由于HibernateTemplate没有提供分页查询(从某条开始，并只返回指定条数)，所以写了几个公共函数，以供分页使用 add by
	// yehailong

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#find(java.lang.String, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List find(String queryString, int start, int maxResults) {
		return this.getHibernateTemplate().executeFind(
				new InnerHibernateCallback(queryString, start, maxResults) {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(getQueryString());
						prepareQuery(query);
						query.setFirstResult(this.getStart());
						query.setMaxResults(this.getMaxResults());
						return query.list();
					}
				});
	}

	@SuppressWarnings("unchecked")
	public List find(String queryString) {
		return this.getHibernateTemplate().executeFind(
				new InnerHibernateCallback(queryString) {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(getQueryString());
						prepareQuery(query);
						return query.list();
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#find(java.lang.String, java.lang.Object,
	 *      int, int)
	 */
	@SuppressWarnings("unchecked")
	public List find(String queryString, Object paramValue, int start,
			int maxResults) {
		Object[] paramValues = { paramValue };
		return find(queryString, paramValues, start, maxResults);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#find(java.lang.String, java.lang.Object[],
	 *      int, int)
	 */
	@SuppressWarnings("unchecked")
	public List find(String queryString, Object[] paramValues, int start,
			int maxResults) {
		return this.getHibernateTemplate().executeFind(
				new InnerHibernateCallback(queryString, paramValues, start,
						maxResults) {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(getQueryString());
						prepareQuery(query);
						Object[] paramValues = getParamValues();
						for (int i = 0; paramValues != null
								&& i < paramValues.length; i++) {
							if (paramValues[i] instanceof Date) {
								query.setDate(i, (Date) paramValues[i]);
							} else if (paramValues[i] instanceof Calendar) {
								query.setCalendar(i, (Calendar) paramValues[i]);
							} else {
								query.setParameter(i, paramValues[i]);
							}
						}
						query.setFirstResult(this.getStart());
						query.setMaxResults(this.getMaxResults());
						return query.list();
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basic.common.IBaseDao#findByNamedParam(java.lang.String,
	 *      java.lang.String[], java.lang.Object[], int, int)
	 */
	@SuppressWarnings("unchecked")
	public List findByNamedParam(String queryString, String[] paramNames,
			Object[] paramValues, int start, int maxResults) {
		return this.getHibernateTemplate().executeFind(
				new InnerHibernateCallback(queryString, paramNames,
						paramValues, start, maxResults) {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(getQueryString());
						prepareQuery(query);
						Object[] paramValues = getParamValues();
						String[] paramNames = getParamNames();
						for (int i = 0; paramNames != null
								&& i < paramNames.length; i++) {
							if (paramValues[i] instanceof Date) {
								query.setDate(paramNames[i],
										(Date) paramValues[i]);
							} else if (paramValues[i] instanceof Calendar) {
								query.setCalendar(paramNames[i],
										(Calendar) paramValues[i]);
							} else {
								query.setParameter(paramNames[i],
										paramValues[i]);
							}
						}
						query.setFirstResult(this.getStart());
						query.setMaxResults(this.getMaxResults());
						return query.list();
					}
				});

	}

	// ------------------------------
	abstract class InnerHibernateCallback implements HibernateCallback {
		private int start;

		private int maxResults;

		private String queryString;

		private String[] paramNames;

		private Object[] paramValues;

		public InnerHibernateCallback(String queryString) {
			this.queryString = queryString;
		}

		public InnerHibernateCallback(String queryString, int start,
				int maxResults) {
			this.queryString = queryString;
			this.start = start;
			this.maxResults = maxResults;
		}

		public InnerHibernateCallback(String queryString, Object[] paramValues,
				int start, int maxResults) {
			this.queryString = queryString;
			this.paramValues = paramValues;
			this.start = start;
			this.maxResults = maxResults;
		}

		public InnerHibernateCallback(String queryString, String[] paramNames,
				Object[] paramValues, int start, int maxResults) {
			this.queryString = queryString;
			this.paramNames = paramNames;
			this.paramValues = paramValues;
			this.start = start;
			this.maxResults = maxResults;
		}

		public int getStart() {
			return this.start;
		}

		public int getMaxResults() {
			return this.maxResults;
		}

		public String getQueryString() {
			return this.queryString;
		}

		public Object[] getParamValues() {
			return this.paramValues;
		}

		public String[] getParamNames() {
			return this.paramNames;
		}

		public abstract Object doInHibernate(Session session)
				throws HibernateException, SQLException;
	}

	/**
	 * 根据主键查找对象
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Object findObjectByKey(Serializable key) throws Exception {
		Object obj = null;
		try {
			obj = getHibernateTemplate().get(modelClass, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 根据sql查询结果集
	 * 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List findSQL(String sql) {
		List list = new ArrayList();
		Session session = null;
		SessionFactory sessionFactory = this.getHibernateTemplate()
				.getSessionFactory();
		try {
			session = sessionFactory.openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			list = sqlQuery.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	/**
	 * 根据属性名查找数据
	 * 
	 * @param obj
	 *            所查实体
	 * @param propertyName
	 * @param value
	 * @return
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	public List findByProperty(Object obj) throws Exception {

		List list = this.getHibernateTemplate().findByExample(obj);

		return list;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List findListRowMapBySQL(String sql) {
		List list = new ArrayList();
		SessionFactory sessionFactory = this.getHibernateTemplate()
				.getSessionFactory();
		Session session = sessionFactory.openSession();
		Connection con = session.connection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Map map = null;
		try {
			pstmt = con.prepareStatement(sql);
			rset = pstmt.executeQuery();
			ResultSetMetaData rsMetaData = rset.getMetaData();
			int columnCount = rsMetaData.getColumnCount();
			while ((rset != null) && (rset.next())) {
				map = new HashMap();
				for (int i = 1; i <= columnCount; ++i) {
					Object obj = null;
					int colType = rsMetaData.getColumnType(i);
					if ((colType == 93) || (colType == 91))
						obj = rset.getTimestamp(i);
					else {
						obj = rset.getObject(i);
					}
					map.put(rsMetaData.getColumnName(i).toUpperCase(), obj);
				}
				list.add(map);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			session.close();
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	public void executeBySQL(String sql) {
		SessionFactory sessionFactory = this.getHibernateTemplate().getSessionFactory();
		Session session = sessionFactory.openSession();
		Connection con = session.connection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			session.close();
		}
	}
		

}

