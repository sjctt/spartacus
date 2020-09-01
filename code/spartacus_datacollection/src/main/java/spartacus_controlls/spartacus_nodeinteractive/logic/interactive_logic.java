package spartacus_controlls.spartacus_nodeinteractive.logic;

import net.sf.json.JSONObject;
import spartacus_controlls.spartacus_nodeinteractive.entity.sysinfo;
import spartacus_public.entity.return_resultModel;
import spartacus_public.entity.spartacus_debug;
import spartacus_public.method.spartacus_hostinfo;

/**
* @author Song
* @category 服务中心通信接口的逻辑处理层
* @serial		【2020年9月1日】	建立函数
*/
public class interactive_logic 
{
	/**
	* @author Song
	* @category 执行命令
	* @serial		【2020年9月1日】	建立函数
	* @param command 	服务中心发送来的命令信息
	* 									命令内容参照博客：https://blog.csdn.net/FormatWindowsXP/article/details/108279929
	* @return		return_resultModel
	*/
	public return_resultModel execute(String command)
	{
		return_resultModel result = new return_resultModel();
		if(command.equals("get_sysinfo"))
		{
			spartacus_hostinfo hostinfo = new spartacus_hostinfo();
			String hostname = hostinfo.get_hostname();//获取计算机名称
			
			sysinfo sys = new sysinfo();
			sys.setNodename(hostname);
			sys.setRunstatus(1);//当前正常调用就表示节点运行正常
			
			result.setResult(1);
			result.setData_message(JSONObject.fromObject(sys).toString());
		}
		else
		{
			spartacus_debug.writelog_txt("spartacus_datacollection[interactive]:接口调用失败,请检查命令格式或重试。");//记录debug日志
		}
		return result;
	}
}
