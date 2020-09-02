package interactive_test;

import spartacus_controlls.spartacus_nodeinteractive.logic.interactive_logic;
import spartacus_public.entity.return_resultModel;

public class test {

	public static void main(String[] args) 
	{
		interactive_logic logic = new interactive_logic();
		
		return_resultModel  result = logic.execute("get_sysinfo");//服务中心通信接口
		System.out.println(result.getData_message());

	}
}
