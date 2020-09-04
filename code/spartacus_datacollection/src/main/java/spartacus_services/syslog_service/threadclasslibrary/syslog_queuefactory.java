package spartacus_services.syslog_service.threadclasslibrary;

import com.data.operate.redis_dop;

import spartacus_services.syslog_service.syslog_service;
import spartacus_services.syslog_service.logic.syslog_logic;

/**
 * @author Song
 * @category syslog模块队列处理工厂
 * @serial
 *【2020年09月02日】	建立对象
 *									处理发现资产队列任务
 */
public class syslog_queuefactory  extends Thread
{
	redis_dop redis=null;
	public syslog_queuefactory(redis_dop redis)
	{
		this.redis = redis;
	}
	public void run() 
	{
		redis_dop redis = new redis_dop();
		//redis.Initialization();
		try 
		{
			while(true)
			{
				Object queue = syslog_service.syslog_queue.poll();
				if(queue!=null)
				{
					syslog_logic logic = new syslog_logic();
					logic.set_assetsdiscover(queue.toString(), redis);
				}
				Thread.sleep(1);
			}
		}
		catch (Exception e) 
		{
			
		}
	}
}
