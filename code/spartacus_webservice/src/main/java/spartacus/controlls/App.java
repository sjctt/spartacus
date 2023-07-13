package spartacus.controlls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * @author Song
 * @category 项目启动入口
 * @serial
 *【2019年7月8日】	建立对象
 */
@SpringBootApplication
public class App
{
	public static void main(String[] args) 
	{
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
					container.setSessionTimeout(60);// 设置session有效期
					container.setPort(8001);//修改tomcat端口
				}
			};
		}
}
