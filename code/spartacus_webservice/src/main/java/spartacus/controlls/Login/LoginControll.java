package spartacus.controlls.Login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONObject;
import spartacus.controlls.Login.logic.login_logic;
import spartacus_public.entity.return_resultModel;
import spartacus_public.entity.enums.resultCodeEnum;

/**
 * @author Song
 * @category 用于控制用户登录的模块对象
 * @serial
 *【2019年7月5日】	建立对象
 *【2019年7月9日】增加了当前用户登录验证函数
 */
@RestController
@CrossOrigin
public class LoginControll 
{	
	//#region 验证用户身份
	/**
	 * @author Song
	 * @category 用于验证用户身的接口
	 * @serial
	 *【2019年7月8日】	建立接口
	 *【2019年7月9日】尝试vue前端与springboot通信，解决了跨域问题。
	 *								 CrossOrigin解决跨域问题，要注意注释顺序，@CrossOrigin需要在后端  @author Song
	 *
	 *@param account 用户账号
	 *@param password 用户密码
	 *
	 *@return return_resultModel
	 */
	@RequestMapping(value="/verify",method = RequestMethod.POST)
	public String verify(String account,String password,HttpServletRequest request,HttpServletResponse response)
	{
			String clientIP = request.getRemoteAddr();
			login_logic logic = new login_logic();
			return_resultModel result = logic.verify(account, password);
			if(result.getResult()==resultCodeEnum.success.getcode())//登录成功
			{
				request.getSession().setAttribute(clientIP, result.getData_message());//将登录用户写入session
			}
			String jsonstr = JSONObject.fromObject(result).toString();
			return jsonstr;
	}
	//#endregion
	//#region 验证用户是否登录
	/**
	 * @author Song
	 * @category 用于验证当前token是否登录
	 * @serial
	 *【2019年7月8日】	建立接口，通过token+请求端ip地址进行是否登录验证 @author Song
	 *
	 *@param token 登录用户的token特征码
	 *
	 *@return return_resultModel  result=1:用户处于登录状态  result=2:用户登录已超时
	 */
	@RequestMapping(value="/islogin")
	public String isLogin(String token,HttpServletRequest request,HttpServletResponse response)
	{
		String clientIP = request.getRemoteAddr();
		String session_token = request.getSession().getAttribute(clientIP)==null?"":request.getSession().getAttribute(clientIP).toString();
		login_logic logic = new login_logic();
		return_resultModel result =logic.isLogin(token, clientIP, session_token);
		String jsonstr = JSONObject.fromObject(result).toString();
		return jsonstr;
	}
	//#endregion
	//#region 登出方法
		/**
		 * @author Lix
		 * @category 用于退出登录，清除session
		 * @serial
		 *【2019年7月29日】	建立接口，做了清除session用户信息的操作  @author Lix
		 *
		 *@param 
		 *
		 *@return return_resultModel result=0:登出失败  result=1:登出成功
		 */
		@RequestMapping(value="/loginOut")
		public String loginOut(HttpServletRequest request,HttpServletResponse response)
		{
			return_resultModel result = new return_resultModel();
			//去处理session清除操作
			try 
			{
				//销毁session
				request.getSession().invalidate();
			} 
			catch (Exception e) 
			{
				result.setResult(resultCodeEnum.login_timeOut.getcode());
				result.setData_message("清除session信息失败，等返回重试。");
			}
			
			String jsonstr = JSONObject.fromObject(result).toString();
			return jsonstr;
		}
		//#endregion
}
