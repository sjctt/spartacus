package spartacus.controlls.UserRole;

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
import spartacus.controlls.UserRole.logic.UserRole_logic;
import spartacus.controlls.UserRole.model.userrole_userroleModel;
import spartacus_public.entity.return_resultModel;

/**
 * @author Lix
 * @category 用户规则接口类
 * @serial
 *【2019年08月15日】	建立对象
 *【2019年08月16日】 	构建关于用户规则增删改接口
 */
@RestController
@CrossOrigin
public class UserRoleControll 
{	
	//#region 查询用户规则接口
	/**
	 * @author Lix
	 * @category 用于查询用户规则的接口
	 * @serial
	 *【2019年7月29日】	建立接口
	 *
	 *@param idSpartacus_userrole	角色id
	 *@param 
	 *
	 *@return jsonstr
	 */
	@RequestMapping(value="/selectUserRole",method = RequestMethod.POST)
	public String selectUserRole(HttpServletRequest request,HttpServletResponse response)
	{
			//可选条件
			int UserRoleID = Integer.valueOf(request.getParameter("idSpartacus_userrole")!=null?request.getParameter("idSpartacus_userrole"):"0");//获取角色条件
			//分页参数
			int NowPage = Integer.valueOf(request.getParameter("NowPage")!=null?request.getParameter("NowPage"):"0");//获取当前页码
			int ShowCount = Integer.valueOf(request.getParameter("ShowCount")!=null?request.getParameter("ShowCount"):"0");//获取每页显示数量
			UserRole_logic logic = new UserRole_logic();
			ArrayList<userrole_userroleModel> result = logic.selectUserRole(UserRoleID,NowPage,ShowCount);
			
			String jsonstr = JSONArray.fromObject(result).toString();
			return jsonstr;
	}
	//#endregion
	
	//#region 查询用户规则总数接口（分页使用）
		/**
		 * @author Lix
		 * @category 用于查询用户规则总数的接口
		 * @serial
		 *【2019年7月29日】	建立接口
		 *
		 *@param idSpartacus_userrole	角色id
		 *@param 
		 *
		 *@return jsonstr
		 */
		@RequestMapping(value="/selectUserRoleCount",method = RequestMethod.POST)
		public String selectUserRoleCount(HttpServletRequest request,HttpServletResponse response)
		{
				//可选条件
				int UserRoleID = Integer.valueOf(request.getParameter("idSpartacus_userrole")!=null?request.getParameter("idSpartacus_userrole"):"0");//获取角色条件
				//分页参数
				int NowPage = Integer.valueOf(request.getParameter("NowPage")!=null?request.getParameter("NowPage"):"0");//获取当前页码
				int ShowCount = Integer.valueOf(request.getParameter("ShowCount")!=null?request.getParameter("ShowCount"):"0");//获取每页显示数量
				UserRole_logic logic = new UserRole_logic();
				return_resultModel result = logic.selectUserRoleCount(UserRoleID,NowPage,ShowCount);
				
				String jsonstr = JSONArray.fromObject(result).toString();
				return jsonstr;
		}
		//#endregion
		
		/**
		 * @author lix
		 * @category 用于添加用户角色接口
		 * @serial
		 * 【2019-08-16】建立接口
		 * @param 
		 * @return	jsonstr
		 */
		//#region
		@RequestMapping(value="/addUserRole",method = RequestMethod.POST)
		public String addUserRole(userrole_userroleModel userroleModel,HttpServletRequest request,HttpServletResponse response) {
			UserRole_logic logic = new UserRole_logic();
			return_resultModel result = logic.addUserRole(userroleModel);
			//转化json
			String jsonstr = JSONObject.fromObject(result).toString();
			return jsonstr;
		}
		//#endregion
		
		/**
		 * @author lix
		 * @category 用于编辑用户角色接口
		 * @serial
		 * 【2019-08-16】建立接口
		 * @param 
		 * @return	jsonstr
		 */
		//#region
		@RequestMapping(value="/editUserRole",method = RequestMethod.POST)
		public String editUserRole(userrole_userroleModel userroleModel,HttpServletRequest request,HttpServletResponse response) {
			UserRole_logic logic = new UserRole_logic();
			return_resultModel result = logic.editUserRole(userroleModel);
			//转化json
			String jsonstr = JSONObject.fromObject(result).toString();
			return jsonstr;
		}
		//#endregion
		
		/**
		 * @author lix
		 * @category 用于删除用户角色接口
		 * @serial
		 * 【2019-08-16】建立接口
		 * 【2019-08-16】增加判断，如果当前角色下存在用户，则提示请先将用户移除后再进行删除
		 * @param 
		 * @return	jsonstr
		 */
		//#region
		@RequestMapping(value="/deleteUserRole",method = RequestMethod.POST)
		public String deleteUserRole(HttpServletRequest request,HttpServletResponse response) {
			UserRole_logic logic = new UserRole_logic();
			String UserRoleID=request.getParameter("idSpartacus_userrole");
			return_resultModel result = logic.deleteUserRole(UserRoleID);
			//转化json
			String jsonstr = JSONObject.fromObject(result).toString();
			return jsonstr;
		}
		//#endregion
}
