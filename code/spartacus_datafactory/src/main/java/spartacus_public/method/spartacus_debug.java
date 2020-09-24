package spartacus_public.method;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.helper.tools.iniHelper.iniOperate;
import org.helper.tools.txtHelper.txtOperate;

import spartacus_public.entity.config.config;

/**
* @author Song
* @category debug日志记录
* @serial		【2020年9月1日】	建立对象
*/
public class spartacus_debug 
{
	public static boolean isdebug = false;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //记录时间格式
	static SimpleDateFormat filename = new SimpleDateFormat("yyyyMMdd"); //文件名称格式
	
	//#region 初始化debug对象
	/**
	* @author Song
	* @category 初始化debug对象状态
	* @serial		【2020年9月1日】	建立函数
	*/
	public void init()
	{
		try 
		{
			String debug_log =config.content.getEnvironment().getProperty("debug_log");
			if(debug_log.equals("true"))
			{
				isdebug = true;
			}
			else
			{
				isdebug = false;
			}
		}
		catch (Exception e)
		{
			//初始化失败
			e.printStackTrace();
		}
	}
	//#endregion
	//#region 记录日志
	/**
	* @author Song
	* @category 向txt中记录debug日志
	* @serial		【2020年9月1日】	建立函数
	*/
	public static void writelog_txt(String event)
	{
		if(isdebug)
		{
			String logtime = sdf.format(new Date());
			txtOperate txt = new txtOperate();
			try 
			{
				txt.write("log/datafactory"+filename.format(new Date())+".log", logtime+"  "+event, true);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	//#endregion
}
