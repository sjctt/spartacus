package com.data.operate;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import spartacus_public.entity.config.config;
import spartacus_public.method.spartacus_debug;

import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;

/**
 * @author Song
 * @category
 * @serial
 *【2019年8月12日】	建立对象
 */
public class es_dop 
{
	//#region 连接搜索引擎
	public RestHighLevelClient es_connection()
	{
		try 
		{
			InputStream is = getClass().getClassLoader().getResourceAsStream("config/es.Properties");
			Properties properties = new Properties();
			properties.load(is);
			
			String es_host = config.content.getEnvironment().getProperty("es_host");
			int es_port = Integer.parseInt(config.content.getEnvironment().getProperty("es_port"));
			String es_schema = config.content.getEnvironment().getProperty("es_schema");
			final int es_connectTimeOut = Integer.parseInt(config.content.getEnvironment().getProperty("es_connectTimeOut"));
			final int es_socketTimeOut = Integer.parseInt(config.content.getEnvironment().getProperty("es_socketTimeOut"));
			final int es_connectionRequestTimeOut = Integer.parseInt(config.content.getEnvironment().getProperty("es_connectionRequestTimeOut"));
			final int es_maxConnectNum = Integer.parseInt(config.content.getEnvironment().getProperty("es_maxConnectNum"));
			final int es_maxConnectPerRoute = Integer.parseInt(config.content.getEnvironment().getProperty("es_maxConnectPerRoute"));
			
			ArrayList<HttpHost> hostList = new ArrayList<HttpHost>();
			String[] hosts = es_host.split(",");
			for (String host : hosts) 
			{
				hostList.add(new HttpHost(host,es_port,es_schema));
			}
			
			RestClientBuilder builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
			//异步httpclient连接延时配置
			builder.setRequestConfigCallback(new RequestConfigCallback() 
			{
				public Builder customizeRequestConfig(Builder requestConfigBuilder) 
				{
					requestConfigBuilder.setConnectTimeout(es_connectTimeOut);
					requestConfigBuilder.setSocketTimeout(es_socketTimeOut);
					requestConfigBuilder.setConnectionRequestTimeout(es_connectionRequestTimeOut);
					return requestConfigBuilder;
				}
			});
			// 异步httpclient连接数配置
			builder.setHttpClientConfigCallback(new HttpClientConfigCallback() 
			{
				public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) 
				{
					httpClientBuilder.setMaxConnTotal(es_maxConnectNum);
					httpClientBuilder.setMaxConnPerRoute(es_maxConnectPerRoute);
					return httpClientBuilder;
				}
			});
			RestHighLevelClient client = new RestHighLevelClient(builder);
			return client;
		} 
		catch (Exception e)
		{
			if(spartacus_debug.isdebug)
			{
				//记录debug日志
				spartacus_debug.writelog_txt("spartacus_datafactory[datafactory_service]:ES连接失败，"+e.getMessage());
			}
			return null;
		}
	}
	//#endregion
	//#region 单条创建索引
	public void createIndex(RestHighLevelClient client,String logJson)
	{
		try 
		{
			IndexRequest indexRequest = new IndexRequest();
			indexRequest.index("logsys_index");
			indexRequest.source(logJson,XContentType.JSON);
			client.index(indexRequest,RequestOptions.DEFAULT);
		} 
		catch (Exception e) 
		{
			System.out.println("触发catch："+e.getMessage());
			e.printStackTrace();
		}
	}
	//#endregion
	//#region 批量创建索引
	public void createIndex_many(RestHighLevelClient client,ArrayList<String> jsonlist)
	{
		try 
		{
			BulkRequest request = new BulkRequest();
			for (String jsonstr : jsonlist) 
			{
				IndexRequest indexRequest = new IndexRequest();
				indexRequest.index("logsys_index");
				indexRequest.source(jsonstr,XContentType.JSON);
				request.add(indexRequest);
			}
			client.bulk(request, RequestOptions.DEFAULT);
		}
		catch (Exception e) 
		{
			System.out.println("触发catch："+e.getMessage());
		}
	}
	//#endregion
}
