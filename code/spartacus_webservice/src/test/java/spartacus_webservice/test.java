package spartacus_webservice;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import spartacus.controlls.Account.entity.account_accountModel;
import spartacus.controlls.Account.logic.Account_logic;
import spartacus_public.entity.return_resultModel;
import spartacus_public.method.js_des;

public class test 
{
	public static void main(String[] args) throws Exception 
	{
		Account_logic logic = new Account_logic();
		account_accountModel ac = new account_accountModel();
		//js_des des = new js_des();
		
		ac.setAccountStatus(1);
		ac.setName("太66了！~!!");
		ac.setJobNumber("1234-1234");
		ac.setPassword("123456789");
		ac.setAccount("admin2");
		ac.setEmail("email123");
		ac.setJobNumber("jon");
		ac.setPhoneNumber("phone");
		
		ac.setId(11);
		
		return_resultModel result = logic.editAccount(ac);
		String jsonstr = JSONArray.fromObject(result).toString();
		System.out.println(jsonstr);
	}
}
