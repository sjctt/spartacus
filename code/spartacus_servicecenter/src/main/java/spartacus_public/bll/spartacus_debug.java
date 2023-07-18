package spartacus_public.bll;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.helper.tools.txtHelper.txtOperate;

/**
 * @author Song
 * @category debug日志记录
 * @serial
 *【2019年9月9日】	建立对象
 */

public class spartacus_debug 
{
	public static boolean isdebug = false;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat filename = new SimpleDateFormat("yyyyMMdd");
	//#region 初始化配置
	public void Initialization()
	{
		try 
		{
			InputStream is = getClass().getClassLoader().getResourceAsStream("config/sysconf.Properties");
			Properties properties = new Properties();
			properties.load(is);
			String debug = properties.getProperty("debug_log");
			if(debug.equals("true"))
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
			isdebug = false;
		}
	}
	//#endregion
	//#region 记录debug日志
	public static void writelog(String EventLog)
	{
		if(isdebug)
		{
			try 
			{
				String logtime = sdf.format(new Date());
				txtOperate to = new txtOperate();
				to.write("log\\sc_"+filename.format(new Date())+".log", logtime+" "+EventLog, true);
			} 
			catch (Exception e) 
			{
				
			}
		}
	}
	//#endregion
}
