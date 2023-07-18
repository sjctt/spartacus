package spartacus_services.nodemonitor_service;

import spartacus_public.bll.spartacus_debug;
import spartacus_public.entity.enums.LogLevelEnum;
import spartacus_services.nodemonitor_service.bll.nodemonitor_bll;

/**
 * @author Song
 * @category 每分钟检查子节点状态
 * @serial
 *【2019年9月17日】	建立对象
 */
public class nodemonitor_service extends Thread
{
	public void run()
	{
		while(true)
		{
			try 
			{
				nodemonitor_bll nodemonitor_bll = new nodemonitor_bll();
				nodemonitor_bll.get_node_info();
				Thread.sleep(60000);
			}
			catch (Exception e)
			{
				if(spartacus_debug.isdebug)
				{
					//记录debug日志
					spartacus_debug.writelog(LogLevelEnum.warning.getText()+" scService[10008]：节点监控服务运行时触发catch异常，"+e.getMessage());
				}
			}
		}
	}
}
