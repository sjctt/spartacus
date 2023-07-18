package com.data.operate;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import com.data.entity.mysqlconfig;

import spartacus_public.bll.spartacus_debug;
import spartacus_public.entity.enums.LogLevelEnum;



/**
 * @author Song
 * @category mysql的数据操作类
 * @serial 
 * 【2019年7月5日】 建立对象,此对象下所有catch异常均抛出
 */
public class mysql_dop 
{
	public static mysqlconfig mysqlconfig = new mysqlconfig();
	//#region 初始化数据库连接
	public void Initialization()
	{
		try 
		{
			InputStream is = getClass().getClassLoader().getResourceAsStream("config/mysql.Properties");
			Properties properties = new Properties();
			properties.load(is);
			mysqlconfig.setMysql_driver(properties.getProperty("mysql_driver"));
			mysqlconfig.setMysql_constr(properties.getProperty("mysql_constr"));
			mysqlconfig.setMysql_user(properties.getProperty("mysql_user"));
			mysqlconfig.setMysql_pwd(properties.getProperty("mysql_pwd"));
			
			Class.forName(mysqlconfig.getMysql_driver());
			Connection con = DriverManager.getConnection(mysqlconfig.getMysql_constr(),mysqlconfig.getMysql_user(),mysqlconfig.getMysql_pwd());
			mysqlconfig.setMysql_con(con);
		} 
		catch (Exception e) 
		{
			if(spartacus_debug.isdebug)
			{
				//记录debug日志
				spartacus_debug.writelog(LogLevelEnum.emergency.getText()+" scService[10004]：初始化mysql数据库连接时触发catch异常，"+e.getMessage());
			}
		}
	}
	//#endregion
	//#region 查询数据
	public ResultSet get(String sqlstr)
	{
		try 
		{
			Statement statement = mysqlconfig.getMysql_con().createStatement();
			ResultSet resultset = statement.executeQuery(sqlstr);
			return resultset;
		} 
		catch (Exception e) 
		{
			if(spartacus_debug.isdebug)
			{
				//记录debug日志
				spartacus_debug.writelog(LogLevelEnum.emergency.getText()+" scService[10005]：执行SQL语句时触发catch异常，"+sqlstr+"，"+e.getMessage());
			}
			return null;
		}
	}
	//#endregion
	//#region 编辑数据
	public int edit(String sqlstr)
	{
		try 
		{
			PreparedStatement pst = mysqlconfig.getMysql_con().prepareStatement(sqlstr);
			int result = pst.executeUpdate();
			return result;
		} 
		catch (Exception e) 
		{
			if(spartacus_debug.isdebug)
			{
				//记录debug日志
				spartacus_debug.writelog(LogLevelEnum.emergency.getText()+" scService[10005]：执行SQL语句时触发catch异常，"+sqlstr+"，"+e.getMessage());
			}
			return 0;
		}
	}
	//#endregion
	//#region 关闭数据库连接
	public void close()
	{
		try 
		{
			if(!mysqlconfig.getMysql_con().isClosed())
			{
				mysqlconfig.getMysql_con().close();
				mysqlconfig = new mysqlconfig();
			}
		} 
		catch (Exception e) 
		{
			if(spartacus_debug.isdebug)
			{
				//记录debug日志
				spartacus_debug.writelog(LogLevelEnum.notice.getText()+" scService[10006]：关闭mysql数据库连接时触发catch异常，"+e.getMessage());
			}
		}
	}
	//#endregion
}
