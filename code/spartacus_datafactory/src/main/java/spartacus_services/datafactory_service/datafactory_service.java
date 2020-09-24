package spartacus_services.datafactory_service;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.elasticsearch.client.RestHighLevelClient;

import com.data.operate.es_dop;
import com.data.operate.redis_dop;

import spartacus_public.method.spartacus_debug;
import spartacus_public.method.spartacus_hostinfo;
import spartacus_services.datafactory_service.logic.datafactory_logic;

/**
 * @author Song
 * @category 数据处理工厂，从这里进行解析、补全、索引等操作
 * @serial
 *【2020年9月5日】	建立对象
 *								多线程处理索引入库，每次每线程最大处理10000条数据
 *								reids es 采用长连接方式，减少线程频繁开关造成了连接用尽和资源浪费
 */
public class datafactory_service extends Thread
{
	public void run()
	{
		final redis_dop rdop = new redis_dop();
		rdop.Initialization();//初始化redis连接
		
		es_dop es_dop = new es_dop();//创建索引链接 
		final RestHighLevelClient client = es_dop.es_connection();//初始化es连接
		
		//int availProcessors = Runtime.getRuntime().availableProcessors() *2;//获取cpu核心数
		int availProcessors =4;
		ExecutorService ThreadPool = Executors.newFixedThreadPool(availProcessors);//创建一个固定线程池
		
		spartacus_hostinfo hostinfo = new spartacus_hostinfo();
		String hostname = hostinfo.get_hostname();//获取计算机名
		String redis_key_name = "spartacus_"+hostname+"_data";//生成redis key名
		
		while(true)
		{
			try 
			{
				rdop.Initialization();//初始化redis
				final ArrayList<String> data_list = rdop.redis_get_list(redis_key_name,10000);//获取redis日志数据
				if(data_list.size() > 0)
				{
					ThreadPool.execute(new Runnable() {
						@Override
						public void run() 
						{
							datafactory_logic logic = new datafactory_logic();
							logic.warehouse(data_list, client,rdop);
						}
					});
					rdop.redis_del_list(redis_key_name, data_list.size());
				}
			} 
			catch (Exception e) 
			{
				spartacus_debug.writelog_txt("spartacus_datafactory[datafactory_service]:datafactory模块运行过程中触发catch异常，"+e.getMessage());
			}
			try 
			{
				Thread.sleep(10);//防止cpu空转
			}
			catch (InterruptedException e) {}
		}
	}
}
