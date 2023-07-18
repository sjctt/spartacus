package spartacus_public.bll;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Song
 * @category 后台调用webapi
 * @serial
 *【2019年9月10日】	建立对象
 */
public class webapi 
{
	/**
	 * @author Song
	 * @category 发送调用请求并获取返回值
	 * @param api_url api的调用地址
	 * @param param api需要的参数
	 * @param method POST or GET，必须全部大写
	 * @throws Exception 
	 * @serial
	 *【2019年9月10日】	建立对象
	 */
	public static String send(String api_url,String param,String method) throws Exception
	{

			URL url = new URL(api_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        PrintWriter out = null;
	        conn.setRequestProperty("accept", "*/*");
	        conn.setRequestProperty("connection", "Keep-Alive");
	        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

	        conn.setDoOutput(true);
	        conn.setDoInput(true);
	        
	        conn.setRequestMethod(method);//GET和POST必须全大写
	        conn.connect(); 
	        
	        
	        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
	        dos.writeBytes(param);
	        
	      //获取URLConnection对象对应的输入流
	        InputStream is = conn.getInputStream();
	        //构造一个字符流缓存
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        String resultJson = "";
		    while ((resultJson = br.readLine()) != null) 
		    {
		    	resultJson=new String(resultJson.getBytes(),"UTF-8");//解决中文乱码问题
		    	break;
		    }
	        //关闭流
	        is.close();
	        //断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
	        //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
	        conn.disconnect();
	        return resultJson;

	}
}
