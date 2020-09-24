package com.data.entity;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author Song
 * @category redis的配置对象
 * @serial
 *【2019年9月4日】	建立对象
 */
public class redisconfig 
{
	private boolean Initialization;
	private String redis_cluster;
	private String redis_host;
	private int redis_port;
	private String redis_nodes;
	private String redis_pwd;
	
	private Jedis con_single;
	private JedisCluster con_cluster;
	
	/**
	 * 获取redis的初始化状态
	 * @return true: 已经过初始化    false: 未经过初始化
	 **/
	public boolean isInitialization() {
		return Initialization;
	}
	/**
	 * 设置redis的初始化状态
	 * @param initialization true: 已经过初始化    false: 未经过初始化
	 **/
	public void setInitialization(boolean initialization) {
		Initialization = initialization;
	}
	/**
	 * 获取redis的集群模式开启状态
	 * @return true: 集群模式开启    false: 集群模式关闭
	 **/
	public String getRedis_cluster() {
		return redis_cluster;
	}
	/**
	 * 设置redis的集群模式开启状态
	 * @return true: 集群模式开启    false: 集群模式关闭
	 **/
	public void setRedis_cluster(String redis_cluster) {
		this.redis_cluster = redis_cluster;
	}
	/**
	 * 获取redis的单实例模式的服务器地址
	 **/
	public String getRedis_host() {
		return redis_host;
	}
	/**
	 * 设置redis的单实例模式的服务器地址
	 **/
	public void setRedis_host(String redis_host) {
		this.redis_host = redis_host;
	}
	/**
	 * 获取redis的单实例模式的服务器端口
	 **/
	public int getRedis_port() {
		return redis_port;
	}
	/**
	 * 设置redis的单实例模式的服务器端口
	 **/
	public void setRedis_port(int redis_port) {
		this.redis_port = redis_port;
	}
	/**
	 * 获取redis的集群模式下的nodes信息
	 **/
	public String getRedis_nodes() {
		return redis_nodes;
	}
	/**
	 * 设置redis的集群模式下的nodes信息
	 **/
	public void setRedis_nodes(String redis_nodes) {
		this.redis_nodes = redis_nodes;
	}
	/**
	 * 获取redis的连接密码
	 **/
	public String getRedis_pwd() {
		return redis_pwd;
	}
	/**
	 * 设置redis的连接密码
	 **/
	public void setRedis_pwd(String redis_pwd) {
		this.redis_pwd = redis_pwd;
	}
	/**
	 * 获取单实例模式的连接类
	 **/
	public Jedis getCon_single() {
		return con_single;
	}
	/**
	 * 设置单实例模式的连接类
	 **/
	public void setCon_single(Jedis con_single) {
		this.con_single = con_single;
	}
	/**
	 * 获取集群模式的连接类
	 **/
	public JedisCluster getCon_cluster() {
		return con_cluster;
	}
	/**
	 * 设置集群模式的连接类
	 **/
	public void setCon_cluster(JedisCluster con_cluster) {
		this.con_cluster = con_cluster;
	}
}
