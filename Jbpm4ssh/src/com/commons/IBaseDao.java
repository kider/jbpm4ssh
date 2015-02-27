package com.commons;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;


public interface IBaseDao {
	 /**
     * 根据主键查找数据对象
     * 
     * @param key 主键
     * @return 数据库映射对象Model
     * @throws Exception
     */
    public abstract Object findByPrimaryKey(Serializable key) throws Exception;

    /**
     * 创建一个对象，数据表增加一条记录
     * 
     * @param obj
     * @throws Exception
     */
    public abstract void create(Object obj) throws Exception;

    /**
     * 更新对象，数据表修改一条记录
     * 
     * @param obj 要更新的数据对象model
     * @throws Exception
     */
    public abstract void update(Object obj) throws Exception;

    /**
     * 删除数据对象model，数据表删除一条记录
     * 
     * @param obj 要修改的数据对象model
     * @throws Exception
     */
    public abstract void remove(Object obj) throws Exception;

    public abstract void saveOrUpdate(Object obj) throws Exception;

    /**
     * 根据主键删除对象
     * 
     * @param key 主键
     * @throws Exception
     */
    public abstract void remove(Serializable key) throws Exception;

    /**
     * 保存一批数据
     * 
     * @param objects
     * @return
     * @throws DataAccessException
     */
    public abstract void batchSave(final Collection objects) throws DataAccessException;

    /**
     * 保存一批数据,返回保存对象的id
     * 
     * @param objects
     * @return 对象id的集合
     * @throws DataAccessException
     */
    public abstract List batchSaveReturnIds(final Collection objects) throws DataAccessException;

    /**
     * 更新一批数据
     * 
     * @param objects
     * @return
     * @throws DataAccessException
     */
    public abstract void batchUpdate(final Collection objects) throws DataAccessException;

    /**
     * 保存、更新、删除一批数据
     * 
     * @param objects 一批持久化对象，不为null
     * @param operations 对对象的相应操作，在类中定义，不为null
     * @return
     * @throws DataAccessException
     */
    public abstract void batchProcess(final Collection objects, final List operations) throws DataAccessException;

    /**
     * 使用HQL Update语句更新数据
     * 
     * @param hqlString HQL Update和Delete语句
     * @param params 更新参数
     * @return int 更新的记录数
     * @throws DataAccessException
     */
    public abstract int update(final String hqlString, final Object[] params) throws DataAccessException;

    /**
     * 清理Hibernate Session 缓存
     * 
     * @param entities
     * @throws DataAccessException
     */
    public abstract void evict(final Collection entities) throws DataAccessException;

    /**
     * 调用存储过程并返回执行结果
     * 
     * @param callStr 调用语句
     * @param inValues 调用存储过程传入的参数值，依次传入
     * @param outTypes 调用存储过程返回的结果类型，是sql 类型，见{@link java.sql.Types}.
     * @return 根据存储过程定义的out参数返回执行结果
     */
    public abstract Object callProcedure(final String callStr, final Object[] inValues, final int[] outTypes);

    /**
     * 调用函数并返回执行结果
     * 
     * @param callStr 调用语句
     * @param inValues 调用函数传入的参数值，依次传入
     * @param outTypes 调用函数返回的结果类型，是sql 类型，见{@link java.sql.Types}.
     * @return 根据函数定义的out参数返回执行结果
     */
    public abstract Object callFunction(final String callStr, final Object[] inValues, final int[] outTypes);

    /**
     * @return Returns the jdbcTemplate.
     */
    public abstract JdbcTemplate getJdbcTemplate();

    /**
     * @param jdbcTemplate The jdbcTemplate to set.
     */
    public abstract void setJdbcTemplate(JdbcTemplate jdbcTemplate);

    /**
     * 通用方法：更新某表的若干指定字段(字段名与更新及条件值须一一对应)
     * 
     * @author liubo
     * @param table 表名
     * @param properties 字段名数组
     * @param conditions 条件字段名数组
     * @param values 更新及条件值列表
     * @throws SystemRuleException
     */
    public abstract void updateProperty(String table, String[] properties, String[] conditions, List values)
            throws Exception;

    /**
     * 执行指定HQL语句返回的记录条数
     * 
     * @param fromAndWhere hql语句:from .....
     * @param params
     * @return 记录条数
     * @author yehailong
     */
    public abstract int getRowCount(String fromAndWhere, Object[] paramValues);

    /**
     * @param queryString hql语句
     * @param start: the first result, numbered from 0
     * @param maxResults:the maximum number of results
     * @return 结果集合List
     */
    public abstract List find(String queryString);
    
    public abstract List find(String queryString, int start, int maxResults);

    public abstract List find(String queryString, Object paramValue, int start, int maxResults);

    public abstract List find(String queryString, Object[] paramValues, int start, int maxResults);

    public abstract List findByNamedParam(String queryString, String[] paramNames, Object[] paramValues, int start,
            int maxResults);

}
