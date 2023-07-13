package spartacus_webservice;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.elasticsearch.action.admin.cluster.settings.ClusterGetSettingsRequest;
import org.elasticsearch.action.admin.cluster.settings.ClusterGetSettingsResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;

import com.data.operate.es_dop;

import net.sf.json.JSONObject;
import spartacus.controlls.LogAudit.logic.datasearch_logic;

/**
 * @author Song
 * @category
 * @serial
 *【2019年10月24日】	建立对象
 */
public class dataquery {

	public static void main(String[] args) throws IOException
	{
		String message = "<30>Oct 9 22:33:20 <spartacus>hlfedora</spartacus> auditd[1787]: The audit daemon is exiting. <spartacus>这是</spartacus><spartacus>第</spartacus> 992 条<spartacus>信息</spartacus>。";
		get_light_value("aa",message);
	}
	
	
	public static void get_light_value(String field,String message)
	{
		 int startindex = message.indexOf("<spartacus>");
		 int endindex = message.indexOf("</spartacus>");
		 if(startindex!=-1)
		 {
			 System.out.println(startindex+"    "+endindex);
			 String value = message.substring((startindex+11),endindex);
			 System.out.println(value);
			 
			 String new_message = message.substring(endindex+12);
			 get_light_value("aa",new_message);
		 }
	}
	public static void getsetting() throws IOException
	{
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("logsys_index");
		String json1 = "{\"properties\":{\"message\":{\"type\":\"text\",\"analyzer\":\"ik_max_word\"}}}";
		createIndexRequest.mapping("doc",json1,XContentType.JSON);
		
		es_dop dop = new es_dop();
		//RestHighLevelClient  client = dop.es_connection();
        //IndicesClient indices = client.indices();
//        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest,RequestOptions.DEFAULT);
//        boolean acknowledged = createIndexResponse.isAcknowledged();
	} 
	public static void query()
	{
		es_dop dop = new es_dop();
		String searchscript = "* | sort by receivetime asc";
		try 
		{
			ArrayList<String> stringlist = dop.query_index_spl("logsys_index", searchscript,0,100);
			for (String string : stringlist) {
				System.out.println(string);
			}
		} 
		catch (Exception e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
