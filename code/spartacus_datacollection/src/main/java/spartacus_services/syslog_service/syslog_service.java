package spartacus_services.syslog_service;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.data.operate.redis_dop;

import spartacus_public.entity.config.config;
import spartacus_public.method.spartacus_debug;
import spartacus_services.syslog_service.logic.syslog_logic;
import spartacus_services.syslog_service.threadclasslibrary.syslog_queuefactory;

/**
 * @author Song
 * @category spartacus 接收syslog协议的服务 数据采集模块
 * @serial
 *【2020年09月01日】	建立对象
 *【2020年09月02日】	仅测试udp并发接收（未入库），每秒50000无丢失 
 *									完成多线程入库模块，采用固定线程池实现
 *									redis采用长连接的形式，防止在多线程中重复开启连接造成的资源浪费
 *【2020年09月03日】	增加任务队列处理，防止数据库脏读问题，目前针对发现资产功能
 *【2020年09月04日】	在监听循环中添加了对redis的初始化，避免长时间无操作redis掉连接，具体情况需要再观察
 *【2020年09月18日】	将配置文件部分更改为通过spring上下文获取
 */

public class syslog_service extends Thread
{
	public static ConcurrentLinkedQueue syslog_queue = new ConcurrentLinkedQueue();//启用任务队列
	public void run()
	{
		final redis_dop redis = new redis_dop();
		redis.Initialization();//初始化redis连接
		
		syslog_queuefactory queuefactory = new syslog_queuefactory(redis);
		queuefactory.start();//启动队列处理工厂
		//int availProcessors = (Runtime.getRuntime().availableProcessors() * 2)+1;//计算cpu最大线程数
		int availProcessors =4;
		ExecutorService ThreadPool = Executors.newFixedThreadPool(availProcessors);;//创建一个固定线程池
		
		spartacus_debug.writelog_txt("spartacus_datacollection[syslog_service]:service start......");
		String bind_ip ="0.0.0.0"; //udp绑定的ip地址，默认0.0.0.0
		int bind_port =514; //udp绑定的端口，默认514
		DatagramChannel channel =null;
		DatagramSocket socket =null;
		Selector selector =null;
		ByteBuffer byteBuffer = ByteBuffer.allocate (102400000) ;//设定udp缓存区大小
		//#region 读取配置文件
		try 
		{
			bind_ip = config.content.getEnvironment().getProperty("syslog_ip");
			System.out.println(bind_ip);
			bind_port =  Integer.parseInt(config.content.getEnvironment().getProperty("syslog_port"));
		}
		catch (Exception e) 
		{
			spartacus_debug.writelog_txt("spartacus_datacollection[syslog_service]:读取系统配置文件时触发catch异常，"+e.getMessage());
			System.exit(1);//配置文件获取失败终止进程
		}
		//#endregion
		
		//#region 准备udp对象
		try 
		{
			channel = DatagramChannel.open();
			channel.configureBlocking(false);//将通道设置为非阻塞模式，考虑高并发
			socket = channel.socket();//创建socket监听器
			
			InetSocketAddress bindAddress = new InetSocketAddress(bind_ip,bind_port);
			socket.bind(bindAddress);
			
			selector = Selector.open(); //采用nio选择器 可支持多通道监听，方便以后扩展
			channel.register(selector, SelectionKey.OP_READ);
		}
		catch (Exception e)
		{
			spartacus_debug.writelog_txt("spartacus_datacollection[syslog_service]:绑定端口时触发catch异常，请检查端口是否已被占用，绑定ip:"+bind_ip+",端口号："+bind_port+"，"+e.getMessage());
			System.exit(1);//IP端口绑定失败终止进程
		}
		//#endregion
		
		//#region 开始监听
		int test_datacount =0;
		while(true)
		{
			try 
			{
				redis.Initialization();//初始化redis
				if(selector.select()>0)//如果存在0个以上的通道
				{
					Set selectedKeys = selector.selectedKeys();
					Iterator iterator = selectedKeys.iterator();
					while(iterator.hasNext())
					{
						test_datacount++;
						SelectionKey sk = (SelectionKey) iterator.next();
						if (sk.isReadable())
						{
							long starttime = System.currentTimeMillis();
							DatagramChannel datagramChannel = (DatagramChannel)sk.channel();
							byteBuffer.clear();//清空缓冲区
							final String sourceip= datagramChannel.receive(byteBuffer).toString().split(":")[0].replace("/", "");//获取发送者ip地址
							byteBuffer.flip();
							final String data = Charset.forName("UTF-8").decode(byteBuffer).toString();//获取日志内容
							//System.out.println(sourceip+"     "+data+"     "+test_datacount);
							ThreadPool.execute(new Runnable()
							{
								//启用多线程入库
								@Override
								public void run()
								{
									syslog_logic sysloglogic = new syslog_logic();
									sysloglogic.warehouse(sourceip,data,redis);
								}
							});
							iterator.remove();
							long endtime =  System.currentTimeMillis();
							System.out.println(endtime-starttime+"     "+test_datacount);
						}
					}
				}
			}
			catch (Exception e) 
			{
				spartacus_debug.writelog_txt("spartacus_datacollection[syslog_service]:数据接收时catch异常，"+e.getMessage());
				System.out.println(e.getMessage());
			}
		}
		//#endregion
	}
}
