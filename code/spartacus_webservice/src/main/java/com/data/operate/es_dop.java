package com.data.operate;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpHost;
//import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse.FieldMappingMetadata;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.wltea.analyzer.lucene.IKAnalyzer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spartacus.controlls.LogAudit.entity.datahightlightentity;

/**
 * @author Song
 * @category
 * @serial
 *【2019年10月18日】	建立对象
 */
public class es_dop 
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//#region 连接搜索引擎
	private RestHighLevelClient es_connection()
	{
		try 
		{
			InputStream is = getClass().getClassLoader().getResourceAsStream("config/es.Properties");
			Properties properties = new Properties();
			properties.load(is);
			String es_host = properties.getProperty("es_host");
			int es_port = Integer.parseInt(properties.getProperty("es_port"));
			String es_schema = properties.getProperty("es_schema");
			final int es_connectTimeOut = Integer.parseInt(properties.getProperty("es_connectTimeOut"));
			final int es_socketTimeOut = Integer.parseInt(properties.getProperty("es_socketTimeOut"));
			final int es_connectionRequestTimeOut = Integer.parseInt(properties.getProperty("es_connectionRequestTimeOut"));
			final int es_maxConnectNum = Integer.parseInt(properties.getProperty("es_maxConnectNum"));
			final int es_maxConnectPerRoute = Integer.parseInt(properties.getProperty("es_maxConnectPerRoute"));
			
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
			System.out.println(e.getMessage());
			return null;
		}
	}
	//#endregion
	//#region sql方式查询
	public void query_index_sql(String sqlstr)
	{
		String address = "jdbc:es://http://10.0.0.5:9200";
		Properties properties = new Properties();
		try
		{
			Connection connection = DriverManager.getConnection(address, properties);
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(sqlstr);
			
			//ResultSetMetaData rsmd = results.getMetaData();//获取总列数
//			while(true)
//			{
//				if(results.next())
//				{
//					System.out.println(results.getString("eventmessage"));
//				}
//				else
//				{
//					System.out.println("跳出循环");
//					break;
//				}
//			}
			connection.close();
		} 
		catch (Exception e) 
		{
			System.out.println("catch："+e.getMessage());
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
		}
	}
	//#endregion
	//#region spl标准数据查询
	/**
	 * @author Song
	 * @category es的spl查询方式
	 * @param indexname 索引名
	 * @param searchscript 搜索脚本
	 * @param from 搜索的起始位置，用于分页查询
	 * @param pagecount 搜索的结束位置，用于分页查询
	 * @return 
	 * @serial
	 *【2019年10月18日】	建立对象
	 *【2019年11月5日】json数据格式
	 *								assetsname				//资产名称
									hostip						//资产IP
									subnodeip				//所属节点ip
									subnodename			//所属节点名
									physicalposition		//资产物理位置
									securitydomain		//资产所属安全域
									businessdomain		//资产所属业务域
									receivesource			//信息接收源
									message					//消息内容
									receivetime				//接收时间戳
									id								//模拟id用于前端下拉菜单
									keys							//所有字段的名
									......							//其他动态解析的字段
									es_word					//数据分词结果
									es_light					//数据高亮结果
		【2019年11月12日】	这几天把检索方法完善了一下，增加了高亮、分词处理等部分
											优选字段预留功能，提供前台去定义在原始日志下优先显示的字段
	 */
	public ArrayList<String> query_index_spl(String indexname,String searchscript,int from,int to)
	{
		ArrayList<String> datalist = new ArrayList<String>();
		try 
		{
			RestHighLevelClient client = es_connection();
			SearchRequest searchRequest = new SearchRequest(); 
			searchRequest.indices(indexname);//指定查询的索引
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			
			searchSourceBuilder.query(QueryBuilders.queryStringQuery(searchscript).analyzer("ik_max_word"));//采用分词查询
			searchSourceBuilder.sort("receivetime", SortOrder.DESC);
			searchSourceBuilder.from(from);
			searchSourceBuilder.size(to);
			//#region 处理高亮条件
			ArrayList<String> fieldlist = get_es_fiedname(indexname,client);//获取索引下所有字段
			ArrayList<HighlightBuilder.Field> hight_fieldlist = new ArrayList<HighlightBuilder.Field>();//配置高亮字段
			for (String fieldname : fieldlist) 
			{
				hight_fieldlist.add(new Field(fieldname).preTags("<spartacus>").postTags("</spartacus>"));
			}
			
			HighlightBuilder highlightBuilder = new HighlightBuilder();
			highlightBuilder.fields().addAll(hight_fieldlist);
			
			searchSourceBuilder.highlighter(highlightBuilder);
			//#endregion
			
			searchRequest.source(searchSourceBuilder);
			SearchResponse sr = client.search(searchRequest,RequestOptions.DEFAULT);
			
			SearchHits hits = sr.getHits();
			Iterator<SearchHit> iterator = hits.iterator();
			
			int index=0;
			while (iterator.hasNext())
			{
				SearchHit searchHit = iterator.next(); // 每个查询对象
				JSONObject jsonobj = JSONObject.fromObject(searchHit.getSourceAsString());
				//#region 常规字段处理
				long timestamp = Long.parseLong(jsonobj.get("receivetime").toString());//处理一下时间戳
				jsonobj.remove("receivetime");
				jsonobj.put("receivetime", sdf.format(new Date(timestamp)));//转换时间格式
				jsonobj.put("id", index);//模拟ID，用于下拉
				//#endregion

				//#region 进行分词处理
				ArrayList<String> wordlist = new ArrayList<String>();
				Request request = new Request("GET", "_analyze");
				JSONObject entity = new JSONObject();
				entity.put("analyzer", "ik_max_word");
			    entity.put("text", jsonobj.getString("datacontent"));
			    request.setJsonEntity(entity.toString());
			    Response response = client.getLowLevelClient().performRequest(request);
			    JSONObject tokens = JSONObject.fromObject(EntityUtils.toString(response.getEntity()));
			    JSONArray arrays = tokens.getJSONArray("tokens");
			    for (int i = 0; i < arrays.length(); i++)
			    {
			      JSONObject obj = JSONObject.fromObject(arrays.getString(i));
			      if(!obj.getString("token").substring(obj.getString("token").length()-1).equals("."))
			    	  wordlist.add(obj.getString("token"));
			    }
				jsonobj.put("es_word", wordlist);//将分词添加到json

				//#endregion
				
				//#region 优选字段处理
				ArrayList<String> selected_word = new ArrayList<String>();
				selected_word.add("hostip");
				selected_word.add("hostname");
				selected_word.add("assetsname");
				selected_word.add("physicalposition");
				selected_word.add("receivesource");
				jsonobj.put("selected_word", selected_word);
				//selected_word.add();
				//#endregion
				
				//#region  高亮字段处理
				ArrayList<datahightlightentity> es_lightlist = new ArrayList<datahightlightentity>();
				Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
				for (String key : highlightFields.keySet()) 
				{
					HighlightField highlight = highlightFields.get(key);
				    Text[] fragments = highlight.fragments(); // 多值的字段会有多个值
				    String fragmentString = fragments[0].string();
				    ArrayList<datahightlightentity> lightlist = get_light_value(key,fragmentString,null);
				    es_lightlist.addAll(lightlist);
				}
				jsonobj.put("es_light", es_lightlist);
				//#endregion

				index++;
				
				datalist.add(jsonobj.toString());
			}
			client.close();
		}
		catch (Exception e) 
		{
			System.out.println("出错"+e.getMessage());
		}
		return datalist;
	}
	//#endregion
	//#region 查询数据总量
	public long get_es_datacount(String indexname,String searchscript)
	{
		long datacount=0;
		try 
		{
			RestHighLevelClient client = es_connection();
			SearchRequest searchRequest = new SearchRequest(); 
			searchRequest.indices(indexname);//指定查询的索引
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
			searchSourceBuilder.query(QueryBuilders.queryStringQuery(searchscript)); 
			searchRequest.source(searchSourceBuilder);
			searchSourceBuilder.from(0);
			searchSourceBuilder.size(1);
			
			SearchResponse sr = client.search(searchRequest,RequestOptions.DEFAULT);

			SearchHits hits = sr.getHits();
			datacount = hits.getTotalHits().value;
			client.close();
		}
		catch (Exception e)
		{
			datacount=0;
		}
		return datacount;
	}
	//#endregion
	//#region 获取索引下所有fied名
	private ArrayList<String> get_es_fiedname(String indexname,RestHighLevelClient client) throws IOException
	{
		ArrayList<String> fieldlist = new ArrayList<String>();
		try 
		{
			GetFieldMappingsRequest request = new GetFieldMappingsRequest(); 
			request.indices(indexname);
			request.fields("*");
			GetFieldMappingsResponse response =client.indices().getFieldMapping(request, RequestOptions.DEFAULT);
			Map<String, Map<String, Map<String, FieldMappingMetadata>>> mappings =response.mappings();
			

			Map<String, Map<String, FieldMappingMetadata>> fieldMappings =mappings.get(indexname);
		
			Collection<Map<String, FieldMappingMetadata>> test = fieldMappings.values();
			for (Map<String, FieldMappingMetadata> map : test) 
			{
				for (FieldMappingMetadata obj : map.values()) 
				{
					String fieldname = obj.fullName();
					if(fieldname.indexOf('_')!=0 && 
						!fieldname.contains(".keyword") &&
						!fieldname.equals("keys") &&
						!fieldname.equals("receivetime") &&
						!fieldname.equals("idspartacus_assets"))
					{
						//排除了一下无必要的字段
						fieldlist.add(fieldname);
					}
				}
			}
		}
		catch (Exception e)
		{
			fieldlist.add("message");
		}

		return fieldlist;
	}
	//#endregion
	//#region 递归所有高亮字段
	private ArrayList<datahightlightentity> get_light_value(String field,String message,ArrayList<datahightlightentity> lightlist)
	{
		if(lightlist==null)
		{
			lightlist = new ArrayList<datahightlightentity>();
		}
		int startindex = message.indexOf("<spartacus>");
		int endindex = message.indexOf("</spartacus>");
		if(startindex!=-1)
		{
			String value = message.substring((startindex+11),endindex);
			datahightlightentity light = new datahightlightentity(); 
			light.setFieldname(field);
			light.setValue(value);
			lightlist.add(light);
			String new_message = message.substring(endindex+12);
			get_light_value(field,new_message,lightlist);
		 }
		return lightlist;
	}
	//#endregion
}
