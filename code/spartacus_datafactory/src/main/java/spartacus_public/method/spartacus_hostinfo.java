package spartacus_public.method;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Map;

/**
* @author Song
* @category 封装获取计算机属性的类
* @serial		【2020年9月1日】	建立函数
* 												新增计算机名称获取函数 get_hostname
*/
public class spartacus_hostinfo 
{
	//#region 获取计算机的名称
	/**
	* @author Song
	* @category 获取计算机的名称
	* @serial		【2020年9月1日】	建立函数
	* 												新增windows、linux判断，未来可能跨平台
	* @return		String
	*/
	public String get_hostname()
	{
		try 
		{
			Map<String,String> map = System.getenv();//取计算机信息
			String osName = System.getProperties().getProperty("os.name").toLowerCase();
			if(osName.indexOf("windows") > -1) 
			{
				//windows的
				return map.get("COMPUTERNAME");
			}
			else
			{
				//按linux处理
				return map.get("HOSTNAME");
			}
		}
		catch (Exception e) 
		{
			spartacus_debug.writelog_txt("spartacus_datacollection[get_hostname]:获取计算机名称时触发catch异常，"+e.getMessage());//记录debug日志
			return "";
		}
	}
	//#endregion
	//#region 获取计算机IP地址
	/**
	 * @deprecated
	 **/
	public void get_hostaddress()
	{
		String clientip = "192.168.1.188";
		long starttime = System.currentTimeMillis();
		try 
		{
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) 
			{
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) 
				{
			          continue;
				}
				else
				{
					 Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
					 while (addresses.hasMoreElements()) 
					 {
						 if(addresses.nextElement() instanceof Inet4Address)
						 {
							 String ip = addresses.nextElement().getHostAddress();
						 }
					 }
				}
			}
		}
		catch (Exception e) 
		{
			
		}
		long endtime = System.currentTimeMillis();
		System.out.println(endtime-starttime);
	}
	//#endregion
}
