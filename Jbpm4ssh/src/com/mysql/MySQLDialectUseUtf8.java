package com.mysql;

import org.hibernate.dialect.MySQL5InnoDBDialect;

public class MySQLDialectUseUtf8 extends MySQL5InnoDBDialect {
	
	/**
	 * 使hibernate 建表时用UTF-8字符集
	* <p>Title: getTableTypeString</p> 
	* @see org.hibernate.dialect.MySQL5InnoDBDialect#getTableTypeString()
	 */
	@Override
	public String getTableTypeString() {
		return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
	}

	
	
}
