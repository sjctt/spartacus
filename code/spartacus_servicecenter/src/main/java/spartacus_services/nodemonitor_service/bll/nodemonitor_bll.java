package spartacus_services.nodemonitor_service.bll;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.sf.json.JSONObject;
import spartacus_public.bll.spartacus_debug;
import spartacus_public.bll.spartacus_nodes_bll;
import spartacus_public.bll.webapi;
import spartacus_public.entity.spartacus_nodes;
import spartacus_public.entity.enums.LogLevelEnum;

/**
 * @author Song
 * @category
 * @serial
 *【2019年9月17日】	建立对象
 */
public class nodemonitor_bll 
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//#region 获取所有子节点的状态
	/**
	 * 获取子节点的状态信息
	 **/
	public void get_node_info()
	{
		spartacus_nodes_bll nodebll = new spartacus_nodes_bll();
		String sqlstr="select * from spartacus_nodes";
		ArrayList<spartacus_nodes>nodelist = nodebll.mysql_get_nodes(sqlstr);
		for (spartacus_nodes spartacus_nodes : nodelist) 
		{
			String nodeip = spartacus_nodes.getNodeip();
			String dc_url = "http://"+nodeip+":8003/interactive";//数据采集器接口
			String df_url = "http://"+nodeip+":8004/interactive";//数据工厂接口
			String method = "POST";
			//#region 获取sysinfo
			//#region 数据采集器部分
			try
			{
				String param = "command=" + URLEncoder.encode("sysinfo", "UTF-8");
				try 
				{
					String dc_jsonstr = webapi.send(dc_url,param, method);//数据采集器
					JSONObject dc_jsonobj = JSONObject.fromObject(dc_jsonstr);
					spartacus_nodes.setNodedcsstatus(dc_jsonobj.getInt("runstatus"));
					spartacus_nodes.setNodename(dc_jsonobj.getString("nodename"));
				} 
				catch (Exception e) 
				{
					//超时或异常
					spartacus_nodes.setNodedcsstatus(0);
					if(spartacus_debug.isdebug)
					{
						//记录debug日志
						spartacus_debug.writelog(LogLevelEnum.warning.getText()+" scService[10009]：连接采集器时触发catch异常，可能由于服务未运行或其他问题，"+e.getMessage());
					}
				}
				//#endregion
			//#region 数据工厂部分
			try 
			{
				String df_jsonstr = webapi.send(df_url,param, method);//数据工厂
				JSONObject df_jsonobj = JSONObject.fromObject(df_jsonstr);
				spartacus_nodes.setNodedfsstatus(df_jsonobj.getInt("runstatus"));
			} 
			catch (Exception e) 
			{
				//超时或异常
				spartacus_nodes.setNodedfsstatus(0);
				if(spartacus_debug.isdebug)
				{
					//记录debug日志
					spartacus_debug.writelog(LogLevelEnum.warning.getText()+" scService[10009]：连接数据工厂时触发catch异常，可能由于服务未运行或其他问题，"+e.getMessage());
				}
			}
		}
		catch (Exception e) 
		{
			//定义为超时，状态异常
			spartacus_nodes.setNodedcsstatus(0);
			spartacus_nodes.setNodedfsstatus(0);
			if(spartacus_debug.isdebug)
			{
				//记录debug日志
				spartacus_debug.writelog(LogLevelEnum.warning.getText()+" scService[10008]：节点监控服务运行时触发catch异常，"+e.getMessage());
			}
		}
		//#endregion
			spartacus_nodes.setLastactivetime(sdf.format(new Date()));
			//节点已存在，更新信息
			String update_str = String.format("update spartacus_nodes set "
																	+ "nodename='%s',"
																	+ "nodedcsstatus=%s,"
																	+ "nodedfsstatus=%s,"
																	+ "lastactivetime='%s'"
																	+ " where nodeip='%s'", 
																	spartacus_nodes.getNodename(),
																	spartacus_nodes.getNodedcsstatus(),
																	spartacus_nodes.getNodedfsstatus(),
																	spartacus_nodes.getLastactivetime(),
																	spartacus_nodes.getNodeip());
			nodebll.mysql_update_nodes(update_str);
			//#endregion
		}
	}
	//#endregion
}
