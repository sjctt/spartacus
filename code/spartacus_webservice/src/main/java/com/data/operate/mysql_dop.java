package com.data.operate;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;



/**
 * @author Song
 * @category mysql的数据操作类
 * @serial 
 * 【2019年7月5日】 建立对象,此对象下所有catch异常均抛出
 */
public class mysql_dop 
{
	//#region 连接数据库
	public Connection conector()
	{
		try 
		{
			InputStream is = getClass().getClassLoader().getResourceAsStream("dbconfig.Properties");
			Properties properties = new Properties();
			properties.load(is);
			String mysql_driver = properties.getProperty("mysql_driver");
			String mysql_constr = properties.getProperty("mysql_constr");
			String mysql_user = properties.getProperty("mysql_user");
			String mysql_pwd = properties.getProperty("mysql_pwd");
			
			Class.forName(mysql_driver);
			Connection con = DriverManager.getConnection(mysql_constr,mysql_user,mysql_pwd);
			return con;
		} 
		catch (Exception e)
		{
			//记录debug日志
			return null;
		}
		
	}
	//#endregion
	//#region 查询数据
	public ResultSet get(String sqlstr,Connection con) throws Exception
	{
		Statement statement = con.createStatement();
		ResultSet resultset = statement.executeQuery(sqlstr);
		return resultset;
	}
	//#endregion
	//#region 编辑数据
	public int edit(String sqlstr,Connection con) throws Exception
	{
		PreparedStatement pst = con.prepareStatement(sqlstr);
		int result = pst.executeUpdate();
		return result;
	}
	//#endregion
}
