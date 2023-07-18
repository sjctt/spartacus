package spartacus_controlls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import spartacus_public.entity.config.config;
import spartacus_public.entity.config.redisconf;
import spartacus_public.entity.config.sysconf;
import spartacus_public.method.spartacus_debug;
import spartacus_services.syslog_service.syslog_service;

/**
 * @author Song
 * @category spartacus 数据采集服务
 * =================================================================================
 * 该模块主要负责spartacus系统的数据采集工作，其主要功能如下：
 * 1、监听指定端口的UDP数据。
 * 2、将数据塞入redis中。
 * 3、自动发现新设备。
 * 4、接收服务中心命令执行并反馈。
 * =================================================================================
 */

@SpringBootApplication
@EnableConfigurationProperties({sysconf.class,redisconf.class})//加载系统配置文件
public class App 
{
	public static void main(String[] args) 
	{
		config.content = SpringApplication.run(App.class, args);//获取spring上下文
		
		spartacus_debug debug = new spartacus_debug();
		debug.init();//初始化debug状态
		
		syslog_service syslog = new syslog_service();
		syslog.start();//启动syslog采集模块
	}
	@Bean	
	public EmbeddedServletContainerCustomizer containerCustomizer()
	{
			return new EmbeddedServletContainerCustomizer()
			{
				@Override
				public void customize(ConfigurableEmbeddedServletContainer container) 
				{
					container.setPort(8003);//修改tomcat端口
				}
			};
	}
}
