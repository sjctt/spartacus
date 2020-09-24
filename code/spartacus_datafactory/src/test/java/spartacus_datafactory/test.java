package spartacus_datafactory;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.data.operate.redis_dop;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spartacus_services.datafactory_service.entity.key_value;
import spartacus_services.datafactory_service.logic.datafactory_logic;

public class test {

	public static void main(String[] args) 
	{
		String data = "<134>192.168.7.111 - - [25/May/2018:17:41:58 +0800] \"GET /index.php/Home/Api/test HTTP/1.1\" 200 2981";
		//                    <134>2018-04-13 23:59:59 10.0.0.2 GET /favicon.ico - 80 - 10.0.0.188 Mozilla/5.0+(Windows+NT+10.0;+WOW64)+AppleWebKit/537.36+(KHTML,+like+Gecko)+Chrome/55.0.2883.87+Safari/537.36 404 0 2 41
		redis_dop dop = new redis_dop();

		dop.redisconfig.setRedis_cluster("true");
		dop.redisconfig.setRedis_host("");
		dop.redisconfig.setRedis_port(0);
		dop.redisconfig.setRedis_nodes("10.0.0.6:7001,10.0.0.6:7002,10.0.0.5:7001,10.0.0.5:7002,10.0.0.4:7001,10.0.0.4:7002");
		dop.redisconfig.setRedis_pwd("abcd1234!");
		dop.redisconfig.setInitialization(true);
		
		dop.Initialization();
		datafactory_logic logic = new datafactory_logic();
		logic.basisparsing(data, dop);
	}
}
