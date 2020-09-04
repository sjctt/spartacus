package spartacus_services.syslog_service.logic;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.data.operate.redis_dop;

import net.sf.json.JSONObject;
import spartacus_public.method.spartacus_debug;
import spartacus_public.method.spartacus_hostinfo;
import spartacus_services.syslog_service.syslog_service;
import spartacus_services.syslog_service.entity.spartacus_assets_discover;
import spartacus_services.syslog_service.entity.spartacus_receive_data;

/**
 * @author Song
 * @category syslog模块逻辑层
 * @serial
 *【2020年09月02日】	建立对象
 *【2020年09月03日】	增加任务队列处理，防止数据库脏读问题，目前针对发现资产功能
 */
public class syslog_logic 
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	//#region redis入库
	/**
	 * @author Song
	 * @category redis入库函数
	 * @serial
	 *【2020年09月02日】	建立对象
	 *									首先检查资产是否存在，如果存在则将资产数据整理到入库数据中
	 *									如果不存在，则放入资产发现表 spartacus_assets_discover，
	 *									数据整理完毕后放入数据表spartacus_COMPANY_PC_data中
	 */
	public void warehouse(String sourceip,String data,redis_dop redis)
	{
		spartacus_receive_data receivedata = new spartacus_receive_data();
		//#region 确定资产是否存在
		String json = isexist(sourceip,redis);
		if(json == null)
		{
			//资产不存在，需要写入资产发现列表
			syslog_service.syslog_queue.offer(sourceip);//加入任务队列
		}
		else
		{
			//资产存在，整理到入库数据中
			JSONObject assets = JSONObject.fromObject(json);//序列化资产对象
			receivedata.setHostip(assets.getString("assetsip"));
			receivedata.setReceivetime(System.currentTimeMillis());
			receivedata.setReceivesource("syslog");
			receivedata.setIdspartacus_assets(assets.getInt("idspartacus_assets"));
			receivedata.setAssetsname(assets.getString("assetsname"));
			receivedata.setSecuritydomain(assets.getString("securitydomain"));
			receivedata.setBusinessdomain(assets.getString("businessdomain"));
			receivedata.setPhysicalposition(assets.getString("physicalposition"));
			receivedata.setSubnodename(assets.getString("subnodename"));
			receivedata.setSubnodeip(assets.getString("subnodeip"));
		}
		//#endregion
		
		//#region 组建数据并入库
		receivedata.setDatacontent(data);
		ArrayList<spartacus_receive_data> discoverlist = new ArrayList<spartacus_receive_data>();
		discoverlist.add(receivedata);
		spartacus_hostinfo hostinfo = new spartacus_hostinfo();
		String host_name = hostinfo.get_hostname();
		redis.redis_set_list("spartacus_"+host_name+"_data", discoverlist);
		//#endregion
	}
	//#endregion
	//#region 查询资产是否存在
	/**
	 * @author Song
	 * @category 检测资产是否存在
	 * @serial
	 *【2020年09月02日】	建立对象
	 *									检测资产是否已存在，存在则返回资产对象，不存在则返回null
	 */
	private String isexist(String client_ip,redis_dop redis)
	{
		ArrayList<String> assetslist = redis.redis_get_list("spartacus_assets", -1);
		String assetip = "\"assetsip\":\""+ client_ip +"\"";//查询条件
		for (String asset : assetslist)
		{
			if(asset.contains(assetip))
			{
				return asset;
			}
		}
		return null;
	}
	//#endregion
	//#region 发现资产入库
	/**
	 * @author Song
	 * @category 发现资产入库
	 * @serial
	 *【2020年09月02日】	建立对象
	 *                                  将clientip添加到发现列表，如果发现则表已存在则不添加
	 */
	public void set_assetsdiscover(String sourceip,redis_dop redis)
	{
		try 
		{
			ArrayList<String> assetslist = redis.redis_get_list("spartacus_assets_discover", -1);
			String assetip = "\"sourceip\":\""+ sourceip +"\"";//查询条件
			for (String asset : assetslist)
			{
				if(asset.contains(assetip))
				{
					return;
				}
			}
			spartacus_assets_discover discover = new spartacus_assets_discover();
			discover.setNodeip(InetAddress.getLocalHost().getHostAddress());
			discover.setSourceip(sourceip);
			discover.setCreatetime(sdf.format(new Date()));
			ArrayList<spartacus_assets_discover> discoverlist = new ArrayList<spartacus_assets_discover>();
			discoverlist.add(discover);
			redis.redis_set_list("spartacus_assets_discover", discoverlist);
		
		}
		catch (Exception e)
		{
			spartacus_debug.writelog_txt("spartacus_datacollection[syslog_service]:发现资产入库时catch异常，"+e.getMessage());
		}
	}
	//#endregion
}
