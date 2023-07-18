package spartacus_servicecenter.test_spartacus_analyserule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.helper.tools.dataParsingHelper.dataParse;
import org.helper.tools.dataParsingHelper.model.key_value_Parsing;

import com.data.operate.mysql_dop;

import net.sf.json.JSONObject;
import spartacus_servicecenter.test_spartacus_analyserule.entity.key_value;
import spartacus_servicecenter.test_spartacus_analyserule.entity.rulecontent;
import spartacus_servicecenter.test_spartacus_analyserule.entity.spartacus_analyserule;

/**
 * @author Song
 * @category
 * @serial
 *【2019年9月27日】	建立对象
 */
public class test_spartacus_analyserule 
{
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static void main(String[] args) 
	{
		//split();
		windows();
		linux();
		IIS();
		Apache();
	}
	//#region split
	public static void split()
	{
		String windows_event="<10>Jul 26 10:48:39 XINCHENI-1B4AC5 Service Control Manager: 7036: Windows Installer 服务处于 停止 状态。";
		dataParse dp = new dataParse();
		ArrayList<key_value_Parsing> kvplist = dp.BasisParsing(windows_event);
		for (key_value_Parsing key_value_Parsing : kvplist) 
		{
			System.out.println(key_value_Parsing.getKey()+"   "+key_value_Parsing.getValue());
		}
	}
	//#endregion
	//#region windows
	public static void windows()
	{
		//"<30>Jul 26 10:48:39 XINCHENI-1B4AC5 Service Control Manager: 7036: Windows Installer 服务处于 停止 状态。";
		String regular = "[<][\\\\d{1,9}]+[>](Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) [0-9][0-9] [0-9][0-9]:[0-9][0-9]:[0-9][0-9] [\\\\W\\\\w]*[:] \\\\d{1,9}[:] [\\\\W\\\\w]*";
		spartacus_analyserule analyserule  = new spartacus_analyserule();
		analyserule.setIdspartacus_analyserule(1);
		analyserule.setRulename("类Windows解析规则");
		analyserule.setRuletype(1);
		analyserule.setAnalyzers(1);
		analyserule.setRulestatus(1);
		
		rulecontent rc = new rulecontent();
		rc.setRegular(regular);
		rc.setSplitchar("");
		rc.setEventtype("eventtype_a");
		
		key_value kv=null;
		ArrayList<key_value> kvlist = new ArrayList<key_value>();
		
		kv = new key_value();
		kv.setKey("0");
		kv.setValue("month");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("1");
		kv.setValue("day");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("2");
		kv.setValue("hour");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("3");
		kv.setValue("min");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("4");
		kv.setValue("sec");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("5");
		kv.setValue("hostname");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("6");
		kv.setValue("application");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("7");
		kv.setValue("pid");
		kvlist.add(kv);
		
		rc.setMapping(kvlist);
		String jsonstr = JSONObject.fromObject(rc).toString();
		analyserule.setRulecontent(jsonstr);
		analyserule.setCreatetime(sdf.format(new Date()));
		

		String json = JSONObject.fromObject(analyserule).toString();
		System.out.println(json);
		insert(analyserule);
		
		
//		redis_dop rdop = new redis_dop();
//		rdop.Initialization();
//		ArrayList<spartacus_analyserule> analyserulelist = new ArrayList<spartacus_analyserule>();
//		analyserulelist.add(analyserule);
//		rdop.redis_set_list("spartacus_analyserule", analyserulelist);
	}
	//#endregion
	//#region linux
	public static void linux()
	{
		//<11>Jul 26 11:48:15 localhost pulseaudio[2615]: alsa-sink.c: ALSA 提醒我们在该设备中写入新数据，但实际上没有什么可以写入的！
		String regular ="[<][\\\\d{1,9}]+[>](Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) [0-9]+ [0-9][0-9]:[0-9][0-9]:[0-9][0-9] [\\\\W\\\\w]*[\\\\[]\\\\d{1,9}[\\\\]][:][\\\\W\\\\w]*";
		
		spartacus_analyserule analyserule  = new spartacus_analyserule();
		analyserule.setIdspartacus_analyserule(2);
		analyserule.setRulename("类Linux解析规则");
		analyserule.setRuletype(1);
		analyserule.setAnalyzers(1);
		analyserule.setRulestatus(1);
		
		rulecontent rc = new rulecontent();
		rc.setRegular(regular);
		rc.setSplitchar("");
		rc.setEventtype("eventtype_b");
		
		key_value kv=null;
		ArrayList<key_value> kvlist = new ArrayList<key_value>();
		
		kv = new key_value();
		kv.setKey("0");
		kv.setValue("month");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("1");
		kv.setValue("day");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("2");
		kv.setValue("hour");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("3");
		kv.setValue("min");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("4");
		kv.setValue("sec");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("5");
		kv.setValue("hostname");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("6");
		kv.setValue("application");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("7");
		kv.setValue("pid");
		kvlist.add(kv);
		
		rc.setMapping(kvlist);
		String jsonstr = JSONObject.fromObject(rc).toString();
		analyserule.setRulecontent(jsonstr);
		analyserule.setCreatetime(sdf.format(new Date()));
		
		insert(analyserule);
	}
	//#endregion
	//#region IIS
	public static void IIS()
	{
		//<134>2018-04-13 23:59:59 10.0.0.2 GET /favicon.ico - 80 - 10.0.0.188 Mozilla/5.0+(Windows+NT+10.0;+WOW64)+AppleWebKit/537.36+(KHTML,+like+Gecko)+Chrome/55.0.2883.87+Safari/537.36 404 0 2 41
		String regular = "[<][\\\\d{1,9}]+[>]([0-9]{4})[-]([0-9]{2})[-]([0-9]{2}) ([0-9]{2})[:]([0-9]{2})[:]([0-9]{2}) [\\\\W\\\\w]+ ((25[0-5]|2[0-4]\\\\d|((1\\\\d{2})|([1-9]?\\\\d)))\\\\.){3}(25[0-5]|2[0-4]\\\\d|((1\\\\d{2})|([1-9]?\\\\d)))+ [\\\\W\\\\w]+";
		
		spartacus_analyserule analyserule  = new spartacus_analyserule();
		analyserule.setIdspartacus_analyserule(3);
		analyserule.setRulename("类IIS解析规则");
		analyserule.setRuletype(1);
		analyserule.setAnalyzers(1);
		analyserule.setRulestatus(1);
		
		rulecontent rc = new rulecontent();
		rc.setRegular(regular);
		rc.setSplitchar("");
		rc.setEventtype("eventtype_c");
		
		key_value kv=null;
		ArrayList<key_value> kvlist = new ArrayList<key_value>();
		
		kv = new key_value();
		kv.setKey("0");
		kv.setValue("year");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("1");
		kv.setValue("month");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("2");
		kv.setValue("day");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("3");
		kv.setValue("hour");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("4");
		kv.setValue("min");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("5");
		kv.setValue("sec");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("6");
		kv.setValue("server-ip");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("7");
		kv.setValue("method");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("8");
		kv.setValue("url");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("9");
		kv.setValue("request");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("10");
		kv.setValue("port");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("11");
		kv.setValue("username");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("12");
		kv.setValue("client-ip");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("13");
		kv.setValue("client-info");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("14");
		kv.setValue("status");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("15");
		kv.setValue("substatus");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("16");
		kv.setValue("win-status");
		kvlist.add(kv);//windows状态码
		 
		kv = new key_value();
		kv.setKey("17");
		kv.setValue("times");
		kvlist.add(kv);
		
		rc.setMapping(kvlist);
		String jsonstr = JSONObject.fromObject(rc).toString();
		analyserule.setRulecontent(jsonstr);
		analyserule.setCreatetime(sdf.format(new Date()));
		
		insert(analyserule);
	}
	//#endregion
	//#region Apache
	public static void Apache()
	{
		String regular = "[<][\\\\d{1,9}]+[>]((25[0-5]|2[0-4]\\\\d|((1\\\\d{2})|([1-9]?\\\\d)))\\\\.){3}(25[0-5]|2[0-4]\\\\d|((1\\\\d{2})|([1-9]?\\\\d))) [\\\\W\\\\w]+ [\\\\[][\\\\W\\\\w]+[\\\\]] [\\\\W\\\\w]+";
		
		spartacus_analyserule analyserule  = new spartacus_analyserule();
		analyserule.setIdspartacus_analyserule(4);
		analyserule.setRulename("类Apache解析规则");
		analyserule.setRuletype(1);
		analyserule.setAnalyzers(1);
		analyserule.setRulestatus(1);
		
		rulecontent rc = new rulecontent();
		rc.setRegular(regular);
		rc.setSplitchar("");
		rc.setEventtype("eventtype_d");
		
		key_value kv=null;
		ArrayList<key_value> kvlist = new ArrayList<key_value>();
		
		kv = new key_value();
		kv.setKey("0");
		kv.setValue("client-ip");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("1");
		kv.setValue("remote-login-user");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("2");
		kv.setValue("remote-user");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("3");
		kv.setValue("month");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("4");
		kv.setValue("day");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("5");
		kv.setValue("year");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("6");
		kv.setValue("hour");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("7");
		kv.setValue("min");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("8");
		kv.setValue("sec");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("9");
		kv.setValue("method");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("10");
		kv.setValue("uri");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("11");
		kv.setValue("protocol");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("12");
		kv.setValue("status");
		kvlist.add(kv);
		
		kv = new key_value();
		kv.setKey("13");
		kv.setValue("times");
		kvlist.add(kv);
		
		rc.setMapping(kvlist);
		String jsonstr = JSONObject.fromObject(rc).toString();
		analyserule.setRulecontent(jsonstr);
		analyserule.setCreatetime(sdf.format(new Date()));
		
		insert(analyserule);
	}
	//#endregion
	public static void insert(spartacus_analyserule analyserule)
	{
		mysql_dop dop = new mysql_dop();
		dop.Initialization();
		String sql=String.format("insert into spartacus_analyserule("
												+ "rulename,"
												+ "ruletype,"
												+ "analyzers,"
												+ "rulestatus,"
												+ "rulecontent,"
												+ "createtime) "
												+ "values('%s',%s,%s,%s,'%s','%s')", 
												analyserule.getRulename(),
												analyserule.getRuletype(),
												analyserule.getAnalyzers(),
												analyserule.getRulestatus(),
												analyserule.getRulecontent(),
												sdf.format(new Date()));
		dop.edit(sql);
	}
}
