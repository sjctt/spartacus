package spartacus_public.bll;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.data.operate.mysql_dop;

import spartacus_public.entity.spartacus_assets;

/**
 * @author Song
 * @category
 * @serial
 *【2019年9月25日】	建立对象
 */
public class spartacus_assets_bll 
{
	//#region 查询资产列表
	public ArrayList<spartacus_assets> mysql_get_assets()
	{
		ArrayList<spartacus_assets> assetslist = new ArrayList<spartacus_assets>();
		mysql_dop mdop = new mysql_dop();
		mdop.Initialization();
		String sqlstr = "SELECT assets.*,"
							+ "node.nodename as subnodename,"
							+ "node.nodeip as subnodeip "
							+ "from spartacus_assets assets "
							+ "left join spartacus_nodes node "
							+ "on node.idspartacus_nodes = assets.ref_node";
		ResultSet rs = mdop.get(sqlstr);
		if(rs!=null)
		{
			while(true)
			{
				try 
				{
					if(rs.next())
					{
						spartacus_assets assets = new spartacus_assets();
						assets.setIdspartacus_assets(rs.getInt("idspartacus_assets"));
						assets.setAssetsname(rs.getString("assetsname"));
						assets.setAssetsip(rs.getString("assetsip"));
						assets.setSecuritydomain(rs.getString("securitydomain"));
						assets.setBusinessdomain(rs.getString("businessdomain"));
						assets.setPhysicalposition(rs.getString("physicalposition"));
						assets.setRef_node(rs.getInt("ref_node"));
						assets.setRemark(rs.getString("remark"));
						assets.setCreatetime(rs.getString("createtime"));
						assets.setSubnodename(rs.getString("subnodename"));
						assets.setSubnodeip(rs.getString("subnodeip"));
						assetslist.add(assets);
					}
					else
					{
						break;
					}
				}
				catch (Exception e) {}
			}
		}
		return assetslist;
	}
	//#endregion
}
