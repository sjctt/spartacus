package spartacus_controlls.spartacus_nodeinteractive.bll;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.helper.tools.licenseHelper.License_LOGSYS;
import org.helper.tools.licenseHelper.PublicKeyTool;

import com.data.operate.mysql_dop;

import net.sf.json.JSONObject;
import spartacus_controlls.spartacus_nodeinteractive.entity.spartacus_licenseinfo;
import spartacus_public.bll.spartacus_debug;
import spartacus_public.bll.spartacus_nodes_bll;
import spartacus_public.entity.spartacus_nodes;
import spartacus_public.entity.enums.LogLevelEnum;

/**
 * @author Song
 * @category
 * @serial
 *【2019年9月12日】	建立对象
 *【2019年9月12日】	新增流量消息处理、授权请求处理
 *【2019年9月17日】	新增子节点状态消息处理
 */
public class nodeinteractive_bll 
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//#region 命令执行方法
	/**
	 * @author Song
	 * @category 执行请求命令
	 * @param command:命令内容
	 * @return json字符串
	 * @serial
	 *【2019年9月12日】	建立对象
	 *【2019年9月24日】	增加子节点启动消息
	 */
	public String execute(String command)
	{
		System.out.println(command);
		spartacus_nodes_bll nodebll = new spartacus_nodes_bll();
		String[] comlist = command.split(">");
		//#region 请求消息处理
		if(comlist[0].equals("R"))
		{
			//请求消息
			if(comlist[1].equals("license"))
			{
				//检查授权信息
				String licenseJson = check_license();
				return licenseJson;
			}
		}
		//#endregion
		//#region 通知消息处理
		else if(comlist[0].equals("N"))
		{
			//通知消息
			JSONObject jsonObject = JSONObject.fromObject(comlist[1]);
			String comtype = jsonObject.getString("comtype");
			//#region 流量统计
			if(comtype.equals("nodeflow"))
			{
				//节点流量操作
				String querystr =String.format("select * from spartacus_nodes where nodeip='%s'", jsonObject.getString("nodeip"));
				ArrayList<spartacus_nodes> nodelist = nodebll.mysql_get_nodes(querystr);
				if(nodelist.size() > 0)
				{
					int nowflow = jsonObject.getInt("nodeflow");//拿出子节点发送来的数据长度
					
					spartacus_nodes node = nodelist.get(0);//数据库当前节点信息
					int nodedayflow = 0;
					int nodesumflow = node.getNodesumflow()+(nowflow/1024);
					
					String lasttimestr = node.getLastflowtime().equals("")?sdf.format(new Date()):node.getLastflowtime();
					Date lasttime;
					try 
					{
						lasttime = sdf.parse(lasttimestr);
					} 
					catch (ParseException e) 
					{
						lasttime = new Date();
					}
					//上次流量统计时间
					Date nowtime = new Date();//系统当前时间
					if(nowtime.getDay()>lasttime.getDay())
						//当前天大于上次天，表示跨天
						nodedayflow = nowflow;
					else
						nodedayflow = node.getNodedayflow()+nowflow;
					
					String sqlstr = String.format("update spartacus_nodes set "
							+ "nodedayflow=%s,"
							+ "nodesumflow=%s,"
							+ "lastflowtime='%s' "
							+ "where nodeip='%s'",
							nodedayflow,
							nodesumflow,
							sdf.format(new Date()),
							node.getNodeip()
							);
					nodebll.mysql_update_nodes(sqlstr);//更新节点信息
					return "{\"restule\":\"OK\"}";
				}
			}
			//#endregion
			//#region 子节点启动消息
			else if(comtype.equals("nodestart"))
			{
				 String nodename = jsonObject.getString("nodename"); 
				 String nodeip =jsonObject.getString("nodeip"); 
				 String query_str =String.format("select * from spartacus_nodes where nodeip='%s'", nodeip);
				 ArrayList<spartacus_nodes> nodelist = nodebll.mysql_get_nodes(query_str);
				 
				if(nodelist.size() <= 0)
				{
					//节点不存在，新建节点
					String insert_str = String.format("insert into spartacus_nodes("
							+ "nodename,"
							+ "nodeip,"
							+ "lastactivetime,"
							+ "lastflowtime) "
							+ "values('%s','%s','%s','%s')",
							nodename,
							nodeip,
							sdf.format(new Date()),
							"");
					nodebll.mysql_update_nodes(insert_str);
					return "{\"restule\":\"OK\"}";
				}
				else
				{
					return "{\"restule\":\"OK\"}";
				}
			}
			//#endregion
		}
		//#endregion
		return "{\"restule\":\"Fail\"}";
	}
	//#endregion
	
	//#region 检查当前系统授权
	private String check_license()
	{
		spartacus_nodes_bll nodebll = new spartacus_nodes_bll();
		String licenseJson ="";
		mysql_dop mysql_dop = new mysql_dop();
		mysql_dop.Initialization();//初始化mysql连接
		try 
		{
			String sqlstr = "";
			//#region 获取数据库授权信息
			sqlstr = "select * from spartacus_license";
			String localhardwarecode = "";
			String license = "";
			ResultSet rs = mysql_dop.get(sqlstr);//获取数据库中的授权数据
			if(rs.next())
			{
				//获取到授权数据
				localhardwarecode = rs.getString("hardwarecode");//获取数据库中的硬件吗
				license = rs.getString("license");
			}
			else
			{
				//没有获取倒授权数据
				if(spartacus_debug.isdebug)
				{
					//记录debug日志
					spartacus_debug.writelog(LogLevelEnum.warning.getText()+" scService[10002]：未获取到任何授权数据，请检查系统授权。");
				}
			}
			//#endregion
			//#region 硬件码监控
			String hardwarecode = PublicKeyTool.GetPublicKeyTool();//获取本地硬件码
			if(!localhardwarecode.equals(hardwarecode))
			{
				//硬件码发生变化,更新数据库中的硬件码
				sqlstr = String.format("update spartacus_license set hardwarecode= '%s'", hardwarecode);
				mysql_dop.edit(sqlstr);
				if(spartacus_debug.isdebug)
				{
					//记录debug日志
					spartacus_debug.writelog(LogLevelEnum.warning.getText()+" scService[10001]：服务器硬件码发生变化，这可能会影响到Spartacus系统的正常运行");
				}
			}
			//#endregion
			//#region 授权信息分解
			if(license!=null&&!license.equals(""))
			{
				String[] licensearr = License_LOGSYS.GetLicenseInfo(license);
				spartacus_licenseinfo licenseinfo = new spartacus_licenseinfo();
				licenseinfo.setLicensestatus(licensearr[0]);
				licenseinfo.setUnitname(licensearr[1]);
				licenseinfo.setLicensetype(licensearr[2]);
				licenseinfo.setLicenseclass(licensearr[3]);
				licenseinfo.setExpirytime(licensearr[4]);
				licenseinfo.setFlow(licensearr[5]==""?0L:Long.parseLong(licensearr[5]));
				licenseinfo.setErrormessage(licensearr[6]);

				if(licenseinfo.getLicensestatus().equals("Fail"))
				{
					if(spartacus_debug.isdebug)
					{
						//记录debug日志
						spartacus_debug.writelog(LogLevelEnum.warning.getText()+" scService[10003]：授权检测失败，请更新授权信息。");
					}
				}
				else
				{
					//授权验证通过，检查当天可用流量剩余
					long license_flow = licenseinfo.getFlow();
					String query_nodes_sql = "select * from spartacus_nodes";
					ArrayList<spartacus_nodes> nodelist = nodebll.mysql_get_nodes(query_nodes_sql);
					long day_use_flow = 0;
					for (spartacus_nodes node : nodelist) 
					{
						day_use_flow +=node.getNodedayflow();
					}
					if(day_use_flow>=license_flow)
					{
						//当天处理量超过授权量，授权状态失败
						licenseinfo.setLicensestatus("Fail");
						licenseinfo.setErrormessage("当日流量已用尽，如需增加授权流量，请联系厂商。");
						if(spartacus_debug.isdebug)
						{
							//记录debug日志
							spartacus_debug.writelog(LogLevelEnum.info.getText()+" scService[10007]：当日流量已用尽，如需增加授权流量，请联系厂商。");
						}
					}
				}
				licenseJson = JSONObject.fromObject(licenseinfo).toString();
			}
			else
			{
				spartacus_licenseinfo licenseinfo = new spartacus_licenseinfo();
				licenseinfo.setLicensestatus("Fail");
				licenseinfo.setUnitname("--");
				licenseinfo.setLicensetype("--");
				licenseinfo.setLicenseclass("--");
				licenseinfo.setExpirytime("--");
				licenseinfo.setFlow(0L);
				licenseinfo.setErrormessage("授权检测失败，请更新授权信息。");
				licenseJson = JSONObject.fromObject(licenseinfo).toString();
				if(spartacus_debug.isdebug)
				{
					//记录debug日志
					spartacus_debug.writelog(LogLevelEnum.warning.getText()+" scService[10003]：授权检测失败，请更新授权信息。");
				}
				return licenseJson;
			}
			//#endregion
			return licenseJson;
		}
		catch (Exception e) 
		{
			if(spartacus_debug.isdebug)
			{
				//记录debug日志
				spartacus_debug.writelog(LogLevelEnum.warning.getText()+" scService[10003]：授权检测失败，请更新授权信息。");
			}
			spartacus_licenseinfo licenseinfo = new spartacus_licenseinfo();
			licenseinfo.setLicensestatus("Fail");
			licenseinfo.setUnitname("--");
			licenseinfo.setLicensetype("--");
			licenseinfo.setLicenseclass("--");
			licenseinfo.setExpirytime("--");
			licenseinfo.setFlow(0L);
			licenseinfo.setErrormessage("授权检测失败，请更新授权信息。");
			licenseJson = JSONObject.fromObject(licenseinfo).toString();
			return licenseJson;//关闭mysql数据库连接
		}
		finally
		{
			try 
			{
				 mysql_dop.close();
			} 
			catch (Exception e2) {}
		}
	}
	//#endregion
	
	
	
	

}
