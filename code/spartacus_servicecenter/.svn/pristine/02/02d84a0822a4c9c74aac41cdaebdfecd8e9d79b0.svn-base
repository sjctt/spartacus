package spartacus_servicecenter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import spartacus_public.bll.spartacus_debug;

/**
 * @author Song
 * @category
 * @serial
 *【2019年9月9日】	建立对象
 */
public class test_license {

	public static String main(String[] args) throws Exception 
	{
		spartacus_debug debug = new spartacus_debug();
		debug.Initialization();//debug日志初始化
		
//		license_bll bll = new license_bll();
//		bll.check_license();
		
		URL url = new URL("http://localhost:8002/get_licenseinfo");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        PrintWriter out = null;
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

        conn.setDoOutput(true);
        conn.setDoInput(true);
        
        conn.setRequestMethod("GET");//GET和POST必须全大写
        conn.connect(); 
        
      //获取URLConnection对象对应的输入流
        InputStream is = conn.getInputStream();
        //构造一个字符流缓存
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String str = "";
        while ((str = br.readLine()) != null) 
        {
        	str=new String(str.getBytes(),"UTF-8");//解决中文乱码问题
        }
        //关闭流
        is.close();
        //断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
        //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
        conn.disconnect();
        System.out.println("完整结束");
        return str;

	}

}
