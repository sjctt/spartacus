package spartacus_public.entity.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix="")
@PropertySource(value= {"classpath:config/sysconf.Properties","file:config/sysconf.Properties"},ignoreResourceNotFound = true)
/**
* @author Song
* @category syconf配置文件
* @serial		【2020年9月18日】	建立函数
*/
public class sysconf
{
	private String syslog_ip;
	private String syslog_port;
	private String debug_log;
	
	public String getSyslog_ip() {
		return syslog_ip;
	}
	public void setSyslog_ip(String syslog_ip) {
		this.syslog_ip = syslog_ip;
	}
	public String getSyslog_port() {
		return syslog_port;
	}
	public void setSyslog_port(String syslog_port) {
		this.syslog_port = syslog_port;
	}
	public String getDebug_log() {
		return debug_log;
	}
	public void setDebug_log(String debug_log) {
		this.debug_log = debug_log;
	}
	
}
