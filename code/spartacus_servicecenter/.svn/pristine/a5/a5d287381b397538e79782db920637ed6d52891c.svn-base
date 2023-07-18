package spartacus_servicecenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Song
 * @category
 * @serial
 *【2019年9月12日】	建立对象
 */
public class testtcp {

	public static void main(String[] args) throws UnknownHostException, IOException 
	{
		Socket sock = new Socket("192.168.1.100", 8999);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintStream ps = new PrintStream(sock.getOutputStream());
		ps.println(in.readLine());
	}

}
