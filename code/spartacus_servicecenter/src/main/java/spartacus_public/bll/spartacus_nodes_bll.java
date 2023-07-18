package spartacus_public.bll;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.data.operate.mysql_dop;

import spartacus_public.entity.spartacus_nodes;

/**
 * @author Song
 * @category
 * @serial
 *【2019年9月17日】	建立对象
 */
public class spartacus_nodes_bll 
{
	//#region 取出节点信息
	public ArrayList<spartacus_nodes> mysql_get_nodes(String sqlstr)
	{
		ArrayList<spartacus_nodes> nodelist = new ArrayList<spartacus_nodes>();
		mysql_dop mysql_dop = new mysql_dop();
		mysql_dop.Initialization();//初始化mysql连接
		ResultSet  result = mysql_dop.get(sqlstr);
		if(result!=null)
		{
			while(true)
			{
				try 
				{
					if(result.next())
					{
						spartacus_nodes node = new spartacus_nodes();
						node.setIdspartacus_nodes(result.getInt("idspartacus_nodes"));
						node.setNodename(result.getString("nodename"));
						node.setNodeip(result.getString("nodeip"));
						node.setNodedcsstatus(result.getInt("nodedcsstatus"));
						node.setNodedfsstatus(result.getInt("nodedfsstatus"));
						node.setNodedayflow(result.getInt("nodedayflow"));
						node.setNodesumflow(result.getInt("nodesumflow"));
						node.setLastactivetime(result.getString("lastactivetime"));
						node.setLastflowtime(result.getString("lastflowtime"));
						nodelist.add(node);
					}
					else
					{
						break;
					}
				}
				catch (Exception e){}
			}
		}
		mysql_dop.close();
		return nodelist;
	}
	//#endregion
	
	//#region 新增、编辑节点信息
		public int mysql_update_nodes(String sqlstr)
		{
			mysql_dop mysql_dop = new mysql_dop();
			mysql_dop.Initialization();//初始化mysql连接
			int result = mysql_dop.edit(sqlstr);
			mysql_dop.close();
			return result;
		}
		//#endregion
}
