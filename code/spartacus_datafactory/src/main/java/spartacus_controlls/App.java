package spartacus_controlls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import spartacus_public.entity.config.config;
import spartacus_public.entity.config.esconf;
import spartacus_public.entity.config.redisconf;
import spartacus_public.entity.config.sysconf;
import spartacus_public.method.spartacus_debug;
import spartacus_services.datafactory_service.datafactory_service;

@SpringBootApplication
@EnableConfigurationProperties({sysconf.class,redisconf.class,esconf.class})//加载系统配置文件
public class App 
{
	public static void main(String[] args) 
	{
		config.content = SpringApplication.run(App.class, args);
		
		spartacus_debug debug = new spartacus_debug();
		debug.init();//初始化debug状态
		
		datafactory_service service = new datafactory_service();
		service.start();
	}
	
	@Bean	
	public EmbeddedServletContainerCustomizer containerCustomizer()
	{
			return new EmbeddedServletContainerCustomizer()
			{
				@Override
				public void customize(ConfigurableEmbeddedServletContainer container) 
				{
					container.setPort(8004);//修改tomcat端口
				}
			};
	}
}
