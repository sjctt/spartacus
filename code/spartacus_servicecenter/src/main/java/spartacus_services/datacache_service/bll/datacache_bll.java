package spartacus_services.datacache_service.bll;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.data.operate.mysql_dop;
import com.data.operate.redis_dop;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spartacus_public.bll.spartacus_assets_bll;
import spartacus_public.entity.spartacus_assets;
import spartacus_services.datacache_service.entity.spartacus_analyserule;

/**
 * @author Song
 * @category
 * @serial
 *【2019年9月25日】	建立对象
 *【2019年9月30日】	增加规则缓存
 */
public class datacache_bll 
{
	/**
	 * 将数据缓存至redis
	 **/
	public void cachedata()
	{
		redis_dop rdop = new redis_dop();
		rdop.Initialization();
		//#region 资产列表缓存
		spartacus_assets_bll assets_bll = new spartacus_assets_bll();
		ArrayList<spartacus_assets> assetslist = assets_bll.mysql_get_assets();//获取资产列表
		if(rdop.isexist("spartacus_assets"))
		{
			//如果存在则删除
			rdop.redis_del_string("spartacus_assets");
		}
		rdop.redis_set_list("spartacus_assets", assetslist);//将资产写入redis
		//#endregion
		//#region 解析规则缓存
		ArrayList<spartacus_analyserule> analyserulelist =  mysql_get_analyserule();
		if(rdop.isexist("spartacus_analyserule"))
		{
			//如果存在则删除
			rdop.redis_del_string("spartacus_analyserule");
		}
		rdop.redis_set_list("spartacus_analyserule", analyserulelist);
		//#endregion
		rdop.close();
	}
	//#region 获取规则列表
	/**
	 * 查询规则列表
	 **/
	public ArrayList<spartacus_analyserule> mysql_get_analyserule()
	{
		ArrayList<spartacus_analyserule> analyserulelist = new ArrayList<spartacus_analyserule>();
		mysql_dop mdop = new mysql_dop();
		mdop.Initialization();
		String sqlstr = "select * from spartacus_analyserule where rulestatus=1";
		ResultSet rs = mdop.get(sqlstr);
		if(rs != null)
		{
			try 
			{
				while(rs.next())
				{
					spartacus_analyserule analyserule = new spartacus_analyserule();
					analyserule.setIdspartacus_analyserule(rs.getInt("idspartacus_analyserule"));
					analyserule.setRulename(rs.getString("rulename"));
					analyserule.setRuletype(rs.getInt("ruletype"));
					analyserule.setAnalyzers(rs.getInt("analyzers"));
					analyserule.setRulestatus(rs.getInt("rulestatus"));
					analyserule.setRulecontent(rs.getString("rulecontent"));
					analyserule.setCreatetime(rs.getString("createtime"));
					analyserulelist.add(analyserule);
					System.out.println(analyserule.getRulecontent());
				}
			}
			catch (Exception e) {}
		}
		mdop.close();
		return analyserulelist;
	}
	//#endregion
}
