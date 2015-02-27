package com.daos.dao.jbpm;

import java.io.Serializable;
import java.util.List;

public interface SysRoleDAO {
	 /**
     * 根据主键查找数据对象
     * 
     * @param key 主键
     * @return 数据库映射对象Model
     * @throws Exception
     */
    public Object findByPrimaryKey(Serializable key) throws Exception;

    /**
     * 创建一个对象，数据表增加一条记录
     * 
     * @param obj
     * @throws Exception
     */
    public void create(Object obj) throws Exception;

    /**
     * 更新对象，数据表修改一条记录
     * 
     * @param obj 要更新的数据对象model
     * @throws Exception
     */
    public void update(Object obj) throws Exception;

    /**
     * 删除数据对象model，数据表删除一条记录
     * 
     * @param obj 要修改的数据对象model
     * @throws Exception
     */
    public void remove(Object obj) throws Exception;

    /**
     * 根据主键删除对象
     * 
     * @param key 主键
     * @throws Exception
     */
    public void remove(Serializable key) throws Exception;
    
    /**
     * 根据hql查找
     * @param queryString
     * @return
     */
    public List find(String queryString);

}
