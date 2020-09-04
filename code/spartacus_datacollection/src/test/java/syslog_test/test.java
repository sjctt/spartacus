package syslog_test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class test {

	public static void main(String[] args) throws UnknownHostException {
		//spartacus_hostinfo host = new spartacus_hostinfo();
		//host.get_hostaddress();
		System.out.println(InetAddress.getLocalHost().getHostAddress());
	}

}
