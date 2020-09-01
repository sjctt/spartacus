package spartacus_controlls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;

import spartacus_public.entity.spartacus_debug;

@SpringBootApplication
public class App 
{
	public static void main(String[] args) 
	{
		spartacus_debug debug = new spartacus_debug();
		debug.init();//初始化debug状态
		
		SpringApplication.run(App.class, args);
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
