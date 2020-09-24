package spartacus_public.entity.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="")
@PropertySource(value= {"classpath:config/es.Properties","file:config/es.Properties"},ignoreResourceNotFound = true)
public class esconf 
{
	private String es_host;
	private int es_port;
	private String es_schema;
	private int es_connectTimeOut;
	private int es_socketTimeOut;
	private int es_connectionRequestTimeOut;
	private int es_maxConnectNum;
	private int es_maxConnectPerRoute;
	public String getEs_host() {
		return es_host;
	}
	public void setEs_host(String es_host) {
		this.es_host = es_host;
	}
	public int getEs_port() {
		return es_port;
	}
	public void setEs_port(int es_port) {
		this.es_port = es_port;
	}
	public String getEs_schema() {
		return es_schema;
	}
	public void setEs_schema(String es_schema) {
		this.es_schema = es_schema;
	}
	public int getEs_connectTimeOut() {
		return es_connectTimeOut;
	}
	public void setEs_connectTimeOut(int es_connectTimeOut) {
		this.es_connectTimeOut = es_connectTimeOut;
	}
	public int getEs_socketTimeOut() {
		return es_socketTimeOut;
	}
	public void setEs_socketTimeOut(int es_socketTimeOut) {
		this.es_socketTimeOut = es_socketTimeOut;
	}
	public int getEs_connectionRequestTimeOut() {
		return es_connectionRequestTimeOut;
	}
	public void setEs_connectionRequestTimeOut(int es_connectionRequestTimeOut) {
		this.es_connectionRequestTimeOut = es_connectionRequestTimeOut;
	}
	public int getEs_maxConnectNum() {
		return es_maxConnectNum;
	}
	public void setEs_maxConnectNum(int es_maxConnectNum) {
		this.es_maxConnectNum = es_maxConnectNum;
	}
	public int getEs_maxConnectPerRoute() {
		return es_maxConnectPerRoute;
	}
	public void setEs_maxConnectPerRoute(int es_maxConnectPerRoute) {
		this.es_maxConnectPerRoute = es_maxConnectPerRoute;
	}
}
