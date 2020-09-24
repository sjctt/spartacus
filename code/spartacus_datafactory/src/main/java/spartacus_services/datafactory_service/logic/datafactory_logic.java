package spartacus_services.datafactory_service.logic;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.elasticsearch.client.RestHighLevelClient;

import com.data.operate.es_dop;
import com.data.operate.redis_dop;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spartacus_public.method.spartacus_debug;
import spartacus_services.datafactory_service.entity.key_value;

/**
 * @author Song
 * @category	数据工厂逻辑层
 * @serial
 *【2020年9月5日】	建立对象
 *【2020年9月16日】	增加技术分析仪，基础分析仪中对类windows日志采用单独处理，因为windows中对于触发模块的记录有时会有多个空格
 *								通过递归分解数据，可支持多个分隔符
 */
public class datafactory_logic 
{
	//#region 创建elasticsearch索引
	/**
	 * @author Song
	 * @category	创建elasticsearch索引
	 *@param	data_list	data列表
	 *@param	client	es连接对象
	 *@param	rdop		redis连接对象
	 *@serial
	 *【2020年9月5日】	多线程处理方法，每个线程每次最大处理10000条数据
	 */
	public void warehouse(ArrayList<String> data_list,RestHighLevelClient client,redis_dop rdop)
	{
		try 
		{
			Long starttime = System.currentTimeMillis();
			ArrayList<String> json_list = new ArrayList<String>();
			for (String data : data_list) 
			{
				if(data.charAt(0)=='[')
				{
					data = data.substring(1, data.length() -1);//去除掉json最外层的[]
				}
				JSONObject json = JSONObject.fromObject(data);//json反序列化
				String datacontent = json.getString("datacontent");//获取日志体
				//ArrayList<key_value> kvlist = new  ArrayList<key_value>();
				ArrayList<key_value> kvlist = basisparsing(datacontent,rdop);//对数据进行基础分析
				String keys ="";
				for (key_value kv : kvlist) 
				{
					json.put(kv.getKey(), kv.getValue());
					keys+= kv.getKey()+"|";
				}
				keys = "assetsname|hostip|subnodeip|subnodename|physicalposition|securitydomain|businessdomain|receivesource|"+keys;
				json.put("keys", keys);
				json_list.add(json.toString());
			}
			es_dop es_dop = new es_dop();
			es_dop.createIndex_many(client, json_list);//去创建索引
			Long endtime = System.currentTimeMillis();
			System.out.println(data_list.size()+"条数据，共用时："+(endtime-starttime));
		} 
		catch (Exception e) 
		{
			spartacus_debug.writelog_txt("spartacus_datafactory[datafactory_service]:数据接处理是触发catch异常，"+e.getMessage());
		}
	}
	//#endregion
	//#region 基础分析仪
	/**
	 * @author Song
	 * @category	spartacus基础分析仪，从数据中解析出基础信息
	 * @serial
	 *【2020年9月5日】	多线程处理方法，每个线程每次最大处理10000条数据
	 */
	public ArrayList<key_value> basisparsing(String data,redis_dop rdop)
	{
		ArrayList<key_value> parskvlist = new ArrayList<key_value>();
		try 
		{
			key_value kv = null;
			if(data.charAt(0) !='<')
			{
				data = "<30>"+data;//如果非典型syslog格式，则以info级别存储
			}
			//#region pri部分解析
			String pri = data.substring(0,data.indexOf(">")).replace("<", "").replace(">", "");//获取pri部分内容
			String pri_t = Integer.toBinaryString(Integer.valueOf(pri));
			while (pri_t.length() < 8)
	        {
	            pri_t = "0" + pri_t;
	        }
			 //低位的3个bit表示Severity
	        String Severity_t = pri_t.substring(pri_t.length() - 3);
	        int Severity = Integer.valueOf(Severity_t,2);
	        //高位的部分右移3位，表示Facility的值
	        pri_t = pri_t.substring(0,pri_t.length() - 3);
	        int Facility = Integer.valueOf(pri_t,2);
	        
	        kv = new key_value();
	        kv.setKey("severity");
	        kv.setValue(Integer.toString(Severity));
	        parskvlist.add(kv);
	        
	        kv = new key_value();
	        kv.setKey("facility");
	        kv.setValue(Integer.toString(Facility));
	        parskvlist.add(kv);
	        //#endregion
	        
	        //#region 匹配到规则
	        ArrayList<String> rule_list = rdop.redis_get_list("spartacus_analyserule",-1);//获取规则数据
	        for (String rule : rule_list) 
			{
	        	JSONObject rule_json = JSONObject.fromObject(rule);//反序列化json
				String  rulecontent = rule_json.getString("rulecontent");
				
				JSONObject rulecontent_json = JSONObject.fromObject(rulecontent);//取出正则表达式
				String regular = rulecontent_json.getString("regular");//拿到正则
				
				Pattern p=Pattern.compile(regular.trim());
				Matcher m=p.matcher(data.trim());
				if(m.matches())//匹配到正则
				{
					data = data.substring(data.indexOf(">")+1,data.length());//去除尖括号内容
					//#region 类windows数据处理
					if(rulecontent_json.getString("eventtype").equals("eventtype_a"))
					{
						//<30>Jul 26 10:48:39 XINCHENI-1B4AC5 Service Control Manager: 7036: Windows Installer 服务处于 停止 状态。
						String[] datalist = data.split(" ");
						
						//#region 月 日 时 分 秒 名
						kv = new key_value();
						kv.setKey("month");
						kv.setValue(datalist[0]);
						parskvlist.add(kv);//月
						
						kv = new key_value();
						kv.setKey("day");
						kv.setValue(datalist[1]);
						parskvlist.add(kv);//日
						
						String[] hms = datalist[2].split(":");
						kv = new key_value();
						kv.setKey("hour");
						kv.setValue(hms[0]);
						parskvlist.add(kv);//时
						
						kv = new key_value();
						kv.setKey("min");
						kv.setValue(hms[1]);
						parskvlist.add(kv);//分
						
						kv = new key_value();
						kv.setKey("sec");
						kv.setValue(hms[2]);
						parskvlist.add(kv);//秒
						
						kv = new key_value();
						kv.setKey("hostname");
						kv.setValue(datalist[3]);
						parskvlist.add(kv);//名
						//#endregion
						
						//#region 应用 pid
						String application ="";
						String pid = "";
						for(int i=4;i<datalist.length;i++)
						{
							application+=datalist[i]+" ";
							if(datalist[i].contains(":"))
							{
								application = application.replace(":", "").trim();
								pid = datalist[i+1].replace(":", "").trim();
								break;
							}
						}
						kv = new key_value();
						kv.setKey("application");
						kv.setValue(application);
						parskvlist.add(kv);//application
						
						kv = new key_value();
						kv.setKey("pid");
						kv.setValue(pid);
						parskvlist.add(kv);//application
						//#endregion
					}
					//#endregion
					
					//#region 类linux数据处理
					else if(rulecontent_json.getString("eventtype").equals("eventtype_b"))
					{
						//<11>Jul 26 11:48:15 localhost pulseaudio[2615]: alsa-sink.c: ALSA 提醒我们在该设备中写入新数据，但实际上没有什么可以写入的！
						String[] datalist = data.split(" ");
						
						//#region 月 日 时 分 秒 名
						kv = new key_value();
						kv.setKey("month");
						kv.setValue(datalist[0]);
						parskvlist.add(kv);//月
						
						kv = new key_value();
						kv.setKey("day");
						kv.setValue(datalist[1]);
						parskvlist.add(kv);//日
						
						String[] hms = datalist[2].split(":");
						kv = new key_value();
						kv.setKey("hour");
						kv.setValue(hms[0]);
						parskvlist.add(kv);//时
						
						kv = new key_value();
						kv.setKey("min");
						kv.setValue(hms[1]);
						parskvlist.add(kv);//分
						
						kv = new key_value();
						kv.setKey("sec");
						kv.setValue(hms[2]);
						parskvlist.add(kv);//秒
						
						kv = new key_value();
						kv.setKey("hostname");
						kv.setValue(datalist[3]);
						parskvlist.add(kv);//名
						//#endregion
						
						//#region 应用  pid
						String application ="";
						String pid = "";
						for(int i=4;i<datalist.length;i++)
						{
							application+=datalist[i]+" ";
							if(datalist[i].contains("["))
							{
								application = application.split("\\[")[0].trim();
								pid = datalist[i].split("\\[")[1].split(":")[0].replace("]", "").trim();
								break;
							}
						}
						kv = new key_value();
						kv.setKey("application");
						kv.setValue(application);
						parskvlist.add(kv);//应用
						
						kv = new key_value();
						kv.setKey("pid");
						kv.setValue(pid);
						parskvlist.add(kv);//pid
						//#endregion
					}
					//#endregion
					
					//#region 类IIS数据处理
					else if(rulecontent_json.getString("eventtype").equals("eventtype_c"))
					{
						//2020-09-24 08:13:40 127.0.0.1 GET /css/animate.css 8077 127.0.0.1 Mozilla/5.0+(Windows+NT+10.0;+Win64;+x64;+rv:80.0)+Gecko/20100101+Firefox/80.0 http://127.0.0.1:8077/ 200 0 0 22
						String[] datalist = data.split(" ");
						
						//#region 年月日时分秒
						String[] ymd = datalist[0].split("-");
						
						kv = new key_value();
						kv.setKey("year");
						kv.setValue(ymd[0]);
						parskvlist.add(kv);//年
						
						kv = new key_value();
						kv.setKey("month");
						kv.setValue(ymd[1]);
						parskvlist.add(kv);//月
						
						kv = new key_value();
						kv.setKey("day");
						kv.setValue(ymd[2]);
						parskvlist.add(kv);//日
						
						String[] hms = datalist[1].split(":");
						
						kv = new key_value();
						kv.setKey("hour");
						kv.setValue(hms[0]);
						parskvlist.add(kv);//时
						
						kv = new key_value();
						kv.setKey("min");
						kv.setValue(hms[1]);
						parskvlist.add(kv);//分
						
						kv = new key_value();
						kv.setKey("sec");
						kv.setValue(hms[2]);
						parskvlist.add(kv);//秒
						//#endregion
						
						kv = new key_value();
						kv.setKey("server-ip");
						kv.setValue(datalist[2]);
						parskvlist.add(kv);//server-ip
						
						kv = new key_value();
						kv.setKey("method");
						kv.setValue(datalist[3]);
						parskvlist.add(kv);//method
						
						kv = new key_value();
						kv.setKey("stem");
						kv.setValue(datalist[4]);
						parskvlist.add(kv);//stem
						
						kv = new key_value();
						kv.setKey("port");
						kv.setValue(datalist[5]);
						parskvlist.add(kv);//port
						
						kv = new key_value();
						kv.setKey("client-ip");
						kv.setValue(datalist[6]);
						parskvlist.add(kv);//client-ip
						
						kv = new key_value();
						kv.setKey("client-info");
						kv.setValue(datalist[7]);
						parskvlist.add(kv);//client-info
						
						kv = new key_value();
						kv.setKey("url");
						kv.setValue(datalist[8]);
						parskvlist.add(kv);//url
						
						kv = new key_value();
						kv.setKey("status");
						kv.setValue(datalist[9]);
						parskvlist.add(kv);//status
						
						kv = new key_value();
						kv.setKey("substatus");
						kv.setValue(datalist[10]);
						parskvlist.add(kv);//substatus
						
						kv = new key_value();
						kv.setKey("win-status");
						kv.setValue(datalist[11]);
						parskvlist.add(kv);//win-status
						
						kv = new key_value();
						kv.setKey("times");
						kv.setValue(datalist[12]);
						parskvlist.add(kv);//win-status
					}
					//#endregion
					
					//#region 类apache数据处理
					else if(rulecontent_json.getString("eventtype").equals("eventtype_d"))
					{
						//<134>192.168.7.111 - - [25/May/2018:17:42:00 +0800] "GET /index.php/Home/Api/test HTTP/1.1" 200 2981
						String[] datalist = data.split(" ");
						
						kv = new key_value();
						kv.setKey("client-ip");
						kv.setValue(datalist[0]);
						parskvlist.add(kv);//client-ip
						
						kv = new key_value();
						kv.setKey("remote-login-user");
						kv.setValue(datalist[1]);
						parskvlist.add(kv);//remote-login-user
						
						kv = new key_value();
						kv.setKey("remote-user");
						kv.setValue(datalist[2]);
						parskvlist.add(kv);//remote-user
						
						String[] ymdlist = datalist[3].split("/");
						
						kv = new key_value();
						kv.setKey("month");
						kv.setValue(ymdlist[1]);
						parskvlist.add(kv);//月
						
						kv = new key_value();
						kv.setKey("day");
						kv.setValue(ymdlist[0].replace("[", ""));
						parskvlist.add(kv);//日
						
						
						String[] yhms = ymdlist[2].split(":");
						kv = new key_value();
						kv.setKey("year");
						kv.setValue(yhms[0]);
						parskvlist.add(kv);//年
						
						kv = new key_value();
						kv.setKey("hour");
						kv.setValue(yhms[1]);
						parskvlist.add(kv);//时
						
						kv = new key_value();
						kv.setKey("min");
						kv.setValue(yhms[2]);
						parskvlist.add(kv);//分
						
						kv = new key_value();
						kv.setKey("sec");
						kv.setValue(yhms[3]);
						parskvlist.add(kv);//秒
						
						kv = new key_value();
						kv.setKey("method");
						kv.setValue(datalist[5].replace("\"", ""));
						parskvlist.add(kv);//method
						
						kv = new key_value();
						kv.setKey("uri");
						kv.setValue(datalist[6]);
						parskvlist.add(kv);//uri
						
						kv = new key_value();
						kv.setKey("protocol");
						kv.setValue(datalist[7].replace("\"", ""));
						parskvlist.add(kv);//protocol
						
						kv = new key_value();
						kv.setKey("status");
						kv.setValue(datalist[8]);
						parskvlist.add(kv);//status
						
						kv = new key_value();
						kv.setKey("times");
						kv.setValue(datalist[9]);
						parskvlist.add(kv);//times
					}
					//#endregion
				}
			}
	        //#endregion
	        return parskvlist;
		} 
		catch (Exception e) 
		{
			return parskvlist;
		}
	}
	//#region 递归解析 弃用
//	public ArrayList<key_value> basisparsing(String data,redis_dop rdop)
//	{
//		ArrayList<key_value> parskvlist = new ArrayList<key_value>();
//		key_value kv = null;
//		if(data.charAt(0) !='<')
//		{
//			data = "<30>"+data;//如果非典型syslog格式，则以info级别存储
//		}
//		//#region pri部分解析
//		int[] Pri_p = new int[2];
//		String pri = data.substring(0,data.indexOf(">")).replace("<", "").replace(">", "");//获取pri部分内容
//		String pri_t = Integer.toBinaryString(Integer.valueOf(pri));
//		while (pri_t.length() < 8)
//        {
//            pri_t = "0" + pri_t;
//        }
//		 //低位的3个bit表示Severity
//        String Severity_t = pri_t.substring(pri_t.length() - 3);
//        int Severity = Integer.valueOf(Severity_t,2);
//        //高位的部分右移3位，表示Facility的值
//        pri_t = pri_t.substring(0,pri_t.length() - 3);
//        int Facility = Integer.valueOf(pri_t,2);
//        Pri_p[0] = Severity;
//        Pri_p[1] = Facility;
//        
//        kv = new key_value();
//        kv.setKey("severity");
//        kv.setValue(Integer.toString(Pri_p[0]));
//        parskvlist.add(kv);
//        
//        kv = new key_value();
//        kv.setKey("facility");
//        kv.setValue(Integer.toString(Pri_p[1]));
//        parskvlist.add(kv);
//		//#endregion
//		//#region 匹配到规则
//		ArrayList<String> rule_list = rdop.redis_get_list("spartacus_analyserule",-1);//获取规则数据
//		for (String rule : rule_list) 
//		{
//			JSONObject rule_json = JSONObject.fromObject(rule);//反序列化json
//			String  rulecontent = rule_json.getString("rulecontent");
//			
//			JSONObject rulecontent_json = JSONObject.fromObject(rulecontent);//取出正则表达式
//			String regular = rulecontent_json.getString("regular");//拿到正则
//			
//			Pattern p=Pattern.compile(regular.trim());
//			Matcher m=p.matcher(data.trim());
//			if(m.matches())//匹配到正则
//			{
//				data = data.substring(data.indexOf(">")+1,data.length());//去除尖括号内容
//				
//				JSONArray mapping_json = JSONArray.fromObject(rulecontent_json.getString("mapping"));
//				ArrayList<key_value> kvlist = (ArrayList<key_value>)JSONArray.toList(mapping_json, key_value.class);
//				
//				rec_split(data,rulecontent_json.getString("splitchar"),null);//将数据递归分解
//				for(int i=0;i<kvlist.size();i++)
//				{
//					//#region  单独处理类windows日志 0915
//					if(rulecontent_json.getString("eventtype").equals("eventtype_a"))
//					{
//						if(kvlist.get(i).getKey().equals("6"))//处理下标6位置时
//						{
//							String application="";
//							for(int j=6;j<data_list.size();j++)
//							{
//								try 
//								{
//									Integer.parseInt(data_list.get(j));
//									kv = new key_value();
//									kv.setKey(kvlist.get(i+1).getValue());
//									kv.setValue(data_list.get(j));
//									parskvlist.add(kv);
//									break;
//								}
//								catch (Exception e) 
//								{
//									//触发catch表示不是int类型
//									application+=data_list.get(j)+" ";
//								}
//							}
//							kv = new key_value();
//							kv.setKey(kvlist.get(i).getValue());
//							kv.setValue(application);
//							parskvlist.add(kv);
//							break;
//						}
//						else
//						{
//							kv = new key_value();
//							kv.setKey(kvlist.get(i).getValue());
//							kv.setValue(data_list.get(Integer.parseInt(kvlist.get(i).getKey())));
//							parskvlist.add(kv);
//						}
//					}
//					else
//					{
//						kv = new key_value();
//						kv.setKey(kvlist.get(i).getValue());
//						kv.setValue(data_list.get(Integer.parseInt(kvlist.get(i).getKey())));
//						parskvlist.add(kv);
//					}
//					//#endregion
//				}
//			}
//		}
//		//#endregion
//		return parskvlist;
//	}
	//#endregion
	//#endregion
	//#region 递归分解数据
	ArrayList<String> data_list = new ArrayList<String>();
	private void rec_split(String data,String splitchar,String[] data_split)
	{
		if(data!=null)
		{
			data_split = data.split(String.valueOf(splitchar.charAt(0)));
		}
		for (String item : data_split) 
		{
			for(int i=0;i<splitchar.length();i++)
			{
				String splitstr = String.valueOf(splitchar.charAt(i));
				String[] freeList = item.split("\\"+splitstr);
				if(freeList.length==0)
				{
					//表示截取后没有任何信息了，不用记录
					break;
				}
				else if(freeList.length==1)
				{
					item =freeList[0];
					if(i==splitchar.length()-1)
					{
						if(!freeList[0].equals(""))
						{
							System.out.println(freeList[0]);
							data_list.add(freeList[0].trim());
						}
					}
				}
				else if(freeList.length>1)
				{
					rec_split(null,splitchar,freeList);
					break;
				}
			}
		}
	}
	//#endregion
}
