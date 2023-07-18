package spartacus_services.datacache_service;

import java.util.ArrayList;

import spartacus_public.bll.spartacus_debug;
import spartacus_public.entity.spartacus_assets;
import spartacus_public.entity.enums.LogLevelEnum;
import spartacus_services.datacache_service.bll.datacache_bll;

/**
 * @author Song
 * @category 用于后台数据的redis自动缓存
 * @serial
 *【2019年9月17日】	建立对象 每分钟进行一次数据缓存
 *【2019年9月25日】	对资产列表进行缓存
 *【2019年9月30日】	对规则列表进行缓存
 */
public class datacache_service extends Thread
{
	public void run()
	{
		while(true)
		{
			try
			{
				ArrayList<spartacus_assets> assetslist = new ArrayList<spartacus_assets>();
				datacache_bll datacache_bll = new datacache_bll();
				datacache_bll.cachedata();
				Thread.sleep(60000);
			} 
			catch (Exception e)
			{
				if(spartacus_debug.isdebug)
				{
					//记录debug日志
					spartacus_debug.writelog(LogLevelEnum.warning.getText()+" scService[10011]：进行数据缓存时触发catch异常，"+e.getMessage());
				}
				try 
				{
					Thread.sleep(100);
				} 
				catch (InterruptedException e1) 
				{
					e1.printStackTrace();
				}
			}
		}
	}
}
