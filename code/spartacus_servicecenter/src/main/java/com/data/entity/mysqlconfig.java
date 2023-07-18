package com.data.entity;

import java.sql.Connection;

/**
 * @author Song
 * @category
 * @serial
 *【2019年9月9日】	建立对象
 */
public class mysqlconfig 
{
	private boolean Initialization;
	private String mysql_driver;
	private String mysql_constr;
	private String mysql_user;
	private String mysql_pwd;
	
	public boolean isInitialization() {
		return Initialization;
	}
	public void setInitialization(boolean initialization) {
		Initialization = initialization;
	}
	public String getMysql_driver() {
		return mysql_driver;
	}
	public void setMysql_driver(String mysql_driver) {
		this.mysql_driver = mysql_driver;
	}
	
	public String getMysql_constr() {
		return mysql_constr;
	}
	public void setMysql_constr(String mysql_constr) {
		this.mysql_constr = mysql_constr;
	}
	public String getMysql_user() {
		return mysql_user;
	}
	public void setMysql_user(String mysql_user) {
		this.mysql_user = mysql_user;
	}
	public String getMysql_pwd() {
		return mysql_pwd;
	}
	public void setMysql_pwd(String mysql_pwd) {
		this.mysql_pwd = mysql_pwd;
	}
	public Connection getMysql_con() {
		return mysql_con;
	}
	public void setMysql_con(Connection mysql_con) {
		this.mysql_con = mysql_con;
	}
	private Connection mysql_con;
}
