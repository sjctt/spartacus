package spartacus_services.syslog_service;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import org.helper.tools.iniHelper.iniOperate;

import spartacus_public.method.spartacus_debug;

/**
 * @author Song
 * @category spartacus 接收syslog协议的服务 数据采集模块
 * @serial
 *【2020年09月01日】	建立对象
 */
public class syslog_service extends Thread
{
	public void run()
	{
		spartacus_debug.writelog_txt("spartacus_datacollection[syslog_service]:service start......");
		String bind_ip ="0.0.0.0"; //udp绑定的ip地址
		int bind_port =514; //udp绑定的端口
		DatagramChannel channel =null;
		DatagramSocket socket =null;
		Selector selector =null;
		ByteBuffer byteBuffer = ByteBuffer.allocate (102400000) ;//设定udp缓存区大小
		//#region 读取配置文件
		try 
		{
			URL sysconf = getClass().getClassLoader().getResource("config/sysconf.ini");//获取配置文件
			iniOperate ini = new iniOperate(sysconf.getPath());
			bind_ip = ini.getValue("syslog_config", "IP");
			bind_port = Integer.parseInt(ini.getValue("syslog_config", "Port"));
		}
		catch (Exception e) 
		{
			spartacus_debug.writelog_txt("spartacus_datacollection[syslog_service]:读取系统配置文件时触发catch异常，"+e.getMessage());
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
		}
		//#endregion
		
		//#region 开始监听
		while(true)
		{
			try 
			{
				if(selector.select()>0)//如果存在0个以上的通道
				{
					Set selectedKeys = selector.selectedKeys();
					Iterator iterator = selectedKeys.iterator();
					while(iterator.hasNext())
					{
						SelectionKey sk = (SelectionKey) iterator.next() ;
						if (sk.isReadable())
						{
							DatagramChannel datagramChannel = (DatagramChannel)sk.channel();
							byteBuffer.clear();//清空缓冲区
							String server_ip= datagramChannel.receive(byteBuffer).toString().split(":")[0].replace("/", "");//获取发送者ip地址
							byteBuffer.flip();
							String data = Charset.forName("UTF-8").decode(byteBuffer).toString();//获取日志内容
							System.out.println(server_ip+"     "+data);
							iterator.remove();
						}
					}
				}
			}
			catch (Exception e) 
			{
				spartacus_debug.writelog_txt("spartacus_datacollection[syslog_service]:数据接收时catch异常，"+e.getMessage());
			}
		}
		//#endregion

	}
}
