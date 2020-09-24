package spartacus_public.entity.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="")
@PropertySource(value= {"classpath:config/redis.Properties","file:config/redis.Properties"},ignoreResourceNotFound = true)
/**
* @author Song
* @category redis配置文件
* @serial		【2020年9月18日】	建立函数
*/
public class redisconf 
{
	private String redis_cluster;
	private String redis_host;
	private String redis_port;
	private String redis_nodes;
	private String redis_pwd;
	
	public String getRedis_cluster() {
		return redis_cluster;
	}
	public void setRedis_cluster(String redis_cluster) {
		this.redis_cluster = redis_cluster;
	}
	public String getRedis_host() {
		return redis_host;
	}
	public void setRedis_host(String redis_host) {
		this.redis_host = redis_host;
	}
	public String getRedis_port() {
		return redis_port;
	}
	public void setRedis_port(String redis_port) {
		this.redis_port = redis_port;
	}
	public String getRedis_nodes() {
		return redis_nodes;
	}
	public void setRedis_nodes(String redis_nodes) {
		this.redis_nodes = redis_nodes;
	}
	public String getRedis_pwd() {
		return redis_pwd;
	}
	public void setRedis_pwd(String redis_pwd) {
		this.redis_pwd = redis_pwd;
	}
}
