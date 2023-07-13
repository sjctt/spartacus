package spartacus.controlls.Account;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spartacus.controlls.Account.entity.account_accountModel;
import spartacus.controlls.Account.logic.Account_logic;
import spartacus_public.entity.return_resultModel;

/**
 * @author Lix
 * @category 用于控制用户信息的模块对象
 * @serial
 *【2019年7月5日】	建立对象
 *
 */
@RestController
@CrossOrigin
public class AccountControll 
{	
	//#region 查询用户信息接口
	/**
	 * @author Lix
	 * @category 用于查询用户信息的接口
	 * @serial
	 *【2019年7月29日】	建立接口
	 *
	 *@param idspartacus_useraccount/ref_utype	 用户id/角色id
	 *@param 
	 *
	 *@return jsonstr
	 */
	@RequestMapping(value="/selectAccount",method = RequestMethod.POST)
	public String selectAccount(HttpServletRequest request,HttpServletResponse response)
	{
			//可选条件
			int idspartacus_useraccount = Integer.valueOf(request.getParameter("idspartacus_useraccount")!=null?request.getParameter("idspartacus_useraccount"):"0");//获取用户条件
			int ref_utype = Integer.valueOf(request.getParameter("ref_utype")!=null?request.getParameter("ref_utype"):"0");//获取角色条件
			//分页参数
			int NowPage = Integer.valueOf(request.getParameter("NowPage")!=null?request.getParameter("NowPage"):"0");//获取当前页码
			int ShowCount = Integer.valueOf(request.getParameter("ShowCount")!=null?request.getParameter("ShowCount"):"0");//获取每页显示数量
			Account_logic logic = new Account_logic();
			ArrayList<account_accountModel> result = logic.selectAccount(idspartacus_useraccount,ref_utype,NowPage,ShowCount);
			
			String jsonstr = JSONArray.fromObject(result).toString();
			return jsonstr;
	}
	//#endregion
	
	
	//#region 用于分页使用得查询总数功能
	/**
	 * @author lix
	 * @category 用于分页使用得查询总数功能
	 * @serial
	 * 【2019-07-30】建立接口
	 * @param idspartacus_useraccount用户id	ref_utype角色id
	 * @return	jsonstr
	 */
	@RequestMapping(value="/selectAccountCount",method = RequestMethod.POST)
	public String selectAccountCount(HttpServletRequest request,HttpServletResponse response) {
		//可选条件
		int idspartacus_useraccount = Integer.valueOf(request.getParameter("idspartacus_useraccount")!=null?request.getParameter("idspartacus_useraccount"):"0");//获取用户条件
		int ref_utype = Integer.valueOf(request.getParameter("ref_utype")!=null?request.getParameter("ref_utype"):"0");//获取角色条件
		Account_logic logic = new Account_logic();
		return_resultModel result = logic.selectAccountCount(idspartacus_useraccount,ref_utype);
		//转化json
		String jsonstr = JSONObject.fromObject(result).toString();
		return jsonstr;
	}
	//#endregion
	
	//#region 用于添加用户接口
	/**
	 * @author lix
	 * @category 用于添加用户接口
	 * @serial
	 * 【2019-07-30】建立接口
	 * @param 
	 * @return	jsonstr
	 */
	@RequestMapping(value="/addAccount",method = RequestMethod.POST)
	public String addAccount(account_accountModel accountModel,HttpServletRequest request,HttpServletResponse response) {
		Account_logic logic = new Account_logic();
		return_resultModel result = logic.addAccount(accountModel);
		//转化json
		String jsonstr = JSONObject.fromObject(result).toString();
		return jsonstr;
	}
	//#endregion
	
	//#region	用于编辑用户接口
	/**
	 * @author lix
	 * @category 用于编辑用户接口
	 * @serial
	 * 【2019-08-15】建立接口，并增加了判断当前编辑了什么值的功能。
	 * @param 
	 * @return	jsonstr
	 */
	@RequestMapping(value="/editAccount",method = RequestMethod.POST)
	public String editAccount(account_accountModel accountModel,HttpServletRequest request,HttpServletResponse response) {
		Account_logic logic = new Account_logic();
		return_resultModel result = logic.editAccount(accountModel);
		//转化json
		String jsonstr = JSONObject.fromObject(result).toString();
		return jsonstr;
	}
	//#endregion
	
	//#region 用于删除用户接口
	/**
	 * @author lix
	 * @category 用于删除用户接口
	 * @serial
	 * 【2019-08-15】建立接口
	 * @param 
	 * @return	jsonstr
	 */
	@RequestMapping(value="/deleteAccount",method = RequestMethod.POST)
	public String deleteAccount(HttpServletRequest request,HttpServletResponse response) {
		Account_logic logic = new Account_logic();
		String UserAccountID=request.getParameter("idspartacus_useraccount");
		return_resultModel result = logic.deleteAccount(UserAccountID);
		//转化json
		String jsonstr = JSONObject.fromObject(result).toString();
		return jsonstr;
	}
	//#endregion
	
	//#region 用于编辑用户某一个值得接口
	/**
	 * @author lix
	 * @category 用于编辑用户某一个值得接口
	 * @serial
	 * 【2019-08-22】建立接口--
	 * @param 
	 * @return	jsonstr
	 */
	@RequestMapping(value="/editAccountAlone",method = RequestMethod.POST)
	public String editAccountAlone(account_accountModel accountModel,HttpServletRequest request,HttpServletResponse response) {
		Account_logic logic = new Account_logic();
		return_resultModel result = logic.editAccount(accountModel);
		//转化json
		String jsonstr = JSONObject.fromObject(result).toString();
		return jsonstr;
	}
	//#endregion
}
