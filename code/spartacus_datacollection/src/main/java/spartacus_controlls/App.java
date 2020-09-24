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

@SpringBootApplication
@EnableConfigurationProperties({sysconf.class,redisconf.class})//加载系统配置文件
public class App 
{
	public static void main(String[] args) 
	{
		config.content = SpringApplication.run(App.class, args);//获取spring上下文
		
		syslog_service syslog = new syslog_service();
		syslog.start();//启动syslog采集模块
		
		spartacus_debug debug = new spartacus_debug();
		debug.init();//初始化debug状态
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
