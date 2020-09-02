package com.data.operate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import com.data.entity.redisconfig;

import net.sf.json.JSONArray;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import spartacus_public.method.spartacus_debug;

/**
 * @author Song
 * @category redis的数据控制对象，运行依据redis.Properties配置文件
 * @serial
 *【2019年9月4日】	建立对象，支持单实例及集群模式的操作
 */
public class redis_dop 
{
	public static redisconfig redisconfig=new redisconfig();//redis的运行模式  false:单实例运行 true:集群模式运行
	//#region 初始化redis
	/**
	 * @author Song
	 * @category 初始化redis连接对象
	 * @serial
	 *【2019年9月4日】	系统会自动依据redis的运行模式，选择对应的单例或集群连接方式。
	 */
	public void Initialization()
	{
		try 
		{
			if(!redisconfig.isInitialization())
			{
				//#region 读取配置文件
				InputStream is = getClass().getClassLoader().getResourceAsStream("config/redis.Properties");
				Properties properties = new Properties();
				properties.load(is);
				redisconfig.setRedis_cluster(properties.getProperty("redis_cluster"));
				redisconfig.setRedis_host(properties.getProperty("redis_host"));
				redisconfig.setRedis_port(Integer.parseInt(properties.getProperty("redis_port")));
				redisconfig.setRedis_nodes(properties.getProperty("redis_nodes").replace("[", "").replace("]", ""));
				redisconfig.setRedis_pwd(properties.getProperty("redis_pwd"));
				redisconfig.setInitialization(true);
				//#endregion
			}
			if(redisconfig.getRedis_cluster().equals("true"))
			{
				//集群模式
				redisconfig.setCon_cluster(cluster_redis_connection());
			}
			else
			{
				//单实例模式
				redisconfig.setCon_single(single_redis_connection());
			}
		}
		catch (Exception e) 
		{
			if(spartacus_debug.isdebug)
			{
				//记录debug日志
				spartacus_debug.writelog_txt("spartacus_datacollection[syslog_service]:redis初始化失败，"+e.getMessage());
			}
			redisconfig = null;
		}
	}
	//#endregion
	//#region 连接单实例redis
	/**
	 * @author Song
	 * @category 连接单实例redis
	 * @serial 
	 * 【2019年9月4日】	创建此对象
	 * @throws Exception 连接失败时会抛出catch异常
	 * */
		private  Jedis single_redis_connection() throws Exception
		{		
			Jedis jedis = new Jedis(redisconfig.getRedis_host(),Integer.parseInt(redisconfig.getRedis_host()));
			jedis.auth(redisconfig.getRedis_pwd());
			return jedis;
		}
		//#endregion
	//#region 连接集群redis
	/**
	 * @author Song
	 * @category 连接集群redis
	 * @serial 
	 * 【2019年9月4日】	创建此对象
	 * @throws Exception 连接失败时会抛出catch异常
	 * */
	private JedisCluster cluster_redis_connection() throws Exception
	{
		String[] redis_nodes = redisconfig.getRedis_nodes().split(",");
		Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
		for (String nodeinfo : redis_nodes) 
		{
			//循环添加node节点
			String[] node= nodeinfo.split(":");
			nodes.add(new HostAndPort(node[0], Integer.parseInt(node[1])));
		}
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(99);//最大连接数
		jedisPoolConfig.setMaxWaitMillis(30000);//最大等待时间
		JedisCluster redisCluster = new JedisCluster(nodes,1000,1000,1,redisconfig.getRedis_pwd(), jedisPoolConfig);
		return redisCluster;
	}
	//#endregion

	//#region 添加字符串对象
	/**
	 * @author Song
	 * @category 向redis中添加String对象
	 * @param key String对象的key值，相当于id
	 * @param value 要存入的字符串内容
	 * @serial 
	 * 【2019年9月4日】	创建此对象
	 * */
	public boolean redis_set_string(String key,String value)
	{
		String result = " fail";
		if(redisconfig.getRedis_cluster().equals("true"))
		{
			JedisCluster  cluster = redisconfig.getCon_cluster();
			result = cluster.set(key, value);
		}
		else if(redisconfig.getRedis_cluster().equals("false"))
		{
			Jedis single = redisconfig.getCon_single();
			result = single.set(key, value);
		}
		if(result.equals("OK"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	//#endregion
	//#region 获取字符串对象
	/**
	 * @author Song
	 * @category 获取redis中的String对象
	 * @param key 要获取的String对象的key值
	 * @serial 
	 * 【2019年9月4日】	创建此对象
	 * */
	public String redis_get_string(String key)
	{
		String result = "";
		if(redisconfig.getRedis_cluster().equals("true"))
		{
			JedisCluster  cluster = redisconfig.getCon_cluster();
			result = cluster.get(key);
		}
		else if(redisconfig.getRedis_cluster().equals("false"))
		{
			Jedis single = redisconfig.getCon_single();
			result = single.get(key);
		}
		return result;
	}
	//#endregion
	//#region 删除字符串对象
	/**
	 * @author Song
	 * @category 删除redis中的String对象
	 * @param key 要删除的String对象的key值
	 * @serial 
	 * 【2019年9月4日】	创建此对象
	 * */
	public boolean redis_del_string(String key)
	{
		Long result = 0L;
		if(redisconfig.getRedis_cluster().equals("true"))
		{
			JedisCluster  cluster = redisconfig.getCon_cluster();
			result = cluster.del(key);
		}
		else if(redisconfig.getRedis_cluster().equals("false"))
		{
			Jedis single = redisconfig.getCon_single();
			result = single.del(key);
		}
		if(result>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	//#endregion
	//#region 修改字符串对象
	/**
	 * @author Song
	 * @category 编辑redis中的String对象
	 * @param key 要编辑的String对象的key值
	 * @param newValue 新的value值
	 * @serial 
	 * 【2019年9月4日】	创建此对象
	 * */
	public boolean redis_edit_string(String key,String newValue)
	{
		String result = "";
		if(redisconfig.getRedis_cluster().equals("true"))
		{
			JedisCluster  cluster = redisconfig.getCon_cluster();
			result = cluster.getSet(key,newValue);
		}
		else  if(redisconfig.getRedis_cluster().equals("false"))
		{
			Jedis single = redisconfig.getCon_single();
			result = single.getSet(key,newValue);
		}
		if(!result.equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	//#endregion
	//#region 字符串模糊查询
	public boolean redis_contains_string(String key,String query)
	{
		boolean result = false;
		if(redisconfig.getRedis_cluster().equals("true"))
		{
			JedisCluster  cluster = redisconfig.getCon_cluster();
			Boolean iskey =  cluster.exists(key);
			if(iskey) 
			{
				result = cluster.get(key).contains(query);
			}else {
				result = false;
			}
		}
		else  if(redisconfig.getRedis_cluster().equals("false"))
		{
			Jedis single = redisconfig.getCon_single();
			Boolean iskey =  single.exists(key);
			if(iskey) 
			{
				result = single.get(key).contains(query);
			}else {
				result = false;
			}
		}
		return result;
	}
	//#endregion
	
	//#region 添加list对象
	/**
	 * @author Song
	 * @category 向redis中添加List对象
	 * @param key List对象的key值，相当于表名
	 * @param objectList list对象，可接收一个泛型集合
	 * @serial 
	 * 【2019年9月4日】	创建此对象
	 * */
	public <T> void redis_set_list(String key,ArrayList<T> objectList)
	{
		if(redisconfig.getRedis_cluster().equals("true"))
		{
			JedisCluster  cluster = redisconfig.getCon_cluster();
			String jsonstr = JSONArray.fromObject(objectList).toString();
			cluster.rpush(key, jsonstr);
		}
		else if(redisconfig.getRedis_cluster().equals("false"))
		{
			Jedis single = redisconfig.getCon_single();
			String jsonstr = JSONArray.fromObject(objectList).toString();
			single.rpush(key, jsonstr);
		}
	}
	//#endregion
	//#region 移除list中指定的对象
	/**
	 * @author Song
	 * @category 从list中移除指定的条目
	 * @param key List对象的key值，相当于表名
	 * @param count 需要移除的条目数
	 * @serial 
	 * 【2019年9月4日】	创建此对象
	 * */
	public <T> void redis_del_list(String key,int count)
	{
		if(redisconfig.getRedis_cluster().equals("true"))
		{
			JedisCluster  cluster = redisconfig.getCon_cluster();
			cluster.ltrim(key, count,-1);//设置保留从x到末尾的对象，范围外的对象将被删除
		}
		else if(redisconfig.getRedis_cluster().equals("false"))
		{
			Jedis single = redisconfig.getCon_single();
			single.ltrim(key, count,-1);//设置保留从x到末尾的对象，范围外的对象将被删除
		}
	}
	//#endregion
	//#region 获取list中的对象
	/**
	 * @author Song
	 * @category 从list中获取指定数量的对象
	 * @param key List对象的key值，相当于表名
	 * @param count 需要获取的条目数，这里为下标，如获取10条，count应为9  -1为获取所有
	 * @serial 
	 * 【2019年9月4日】	创建此对象
	 * */
	public ArrayList<String> redis_get_list(String key,int count)
	{
		ArrayList<String> datalist = new ArrayList<String>();
		if(redisconfig.getRedis_cluster().equals("true"))
		{
			JedisCluster  cluster = redisconfig.getCon_cluster();
			datalist = (ArrayList<String>) cluster.lrange(key, 0, count);
		}
		else if(redisconfig.getRedis_cluster().equals("false"))
		{
			Jedis single = redisconfig.getCon_single();
			datalist = (ArrayList<String>) single.lrange(key, 0, count);
		}
		return datalist;
	}
	//#endregion
	//#region 关闭连接
	public void close()
	{
		if(redisconfig.getCon_cluster()!=null)
		{
			redisconfig.getCon_cluster().close();
		}
		if(redisconfig.getCon_single()!=null)
		{
			redisconfig.getCon_single().close();
		}
		redisconfig=new redisconfig();
	}
	//#endregion
}
