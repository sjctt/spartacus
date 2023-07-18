package spartacus_controlls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;

import spartacus_public.bll.spartacus_debug;
import spartacus_public.entity.enums.LogLevelEnum;
import spartacus_services.datacache_service.datacache_service;
import spartacus_services.nodemonitor_service.nodemonitor_service;


/**
 * @author Song
 * @category
 * @serial
 *【2019年9月9日】	建立对象
 */
@SpringBootApplication
public class App 
{
	public static void main(String[] args)
	{
		spartacus_debug debug = new spartacus_debug();
		debug.Initialization();//初始化debug日志类
		
		datacache_service datacache_service = new datacache_service();
		datacache_service.start();//启动数据缓存服务
		
		nodemonitor_service nodemonitor_service = new nodemonitor_service();
		nodemonitor_service.start();//节点状态监听服务
		
		SpringApplication.run(App.class, args);//启动webapi
	}
	@Bean	
	public EmbeddedServletContainerCustomizer containerCustomizer()
	{
			return new EmbeddedServletContainerCustomizer()
			{
				@Override
				public void customize(ConfigurableEmbeddedServletContainer container) 
				{				
					container.setSessionTimeout(60);// 设置session有效期
					container.setPort(8002);//修改tomcat端口
				}
			};
	}
}
