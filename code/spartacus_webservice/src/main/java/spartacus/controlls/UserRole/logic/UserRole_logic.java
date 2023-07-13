package spartacus.controlls.UserRole.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.data.operate.mysql_dop;

import spartacus.controlls.Account.entity.account_accountModel;
import spartacus.controlls.UserRole.model.userrole_userroleModel;
/**
 * @author Lix
 * @category	用户角色模块逻辑对象
 * @serial
 *【2019年7月8日】	建立对象
 *
 */
import spartacus_public.entity.return_resultModel;
import spartacus_public.entity.enums.CheckEnum;
import spartacus_public.method.check_parameter;
public class UserRole_logic 
{
	//#region 关于用户表的字段和表名初始化
	private String field = "roleName"; 
	private String tableName = "spartacus_userrole";
	//#region 查询用户角色信息
	/**
	 * @author Lix
	 * @category	查询用户角色信息
	 * @serial
	 *【2019年7月29日】	创建方法
	 *
	 */
	public ArrayList<userrole_userroleModel> selectUserRole(int idSpartacus_userrole,int NowPage,int ShowCount)
	{
		userrole_userroleModel userroleModel = new userrole_userroleModel();
		//判断是否传了用户id或者用户角色id，是的话组建sql
		String where = " where ";
		if(idSpartacus_userrole != 0) {
			where += "idSpartacus_userrole = " + idSpartacus_userrole;
		}else {
			where = "";
		}
		//整合分页sql limit
		String limit = "";
		//判断是否有分页参数，要是没有就查询全部得
		if(NowPage != 0 && ShowCount != 0) {
			limit += " limit (" + NowPage + "," + ShowCount + ")";
		}
		//组建sql
		String sqlstr=String.format("select * from %s  %s %s",tableName,where,limit);
		mysql_dop dop = new mysql_dop();
		Connection con = dop.conector();//获取数据库连接
		//创建用户list
		ArrayList<userrole_userroleModel> accountList =  new ArrayList <userrole_userroleModel>();
		try 
		{
			//得到数据
			ResultSet resultset = dop.get(sqlstr,con);
			if(resultset.next())
			{
				userroleModel.setId(resultset.getInt("idSpartacus_userrole"));
				userroleModel.setRoleName(resultset.getString("roleName"));
			}
			return accountList;
		}
		catch (Exception e) 
		{
			return accountList;
			//写入debug日志
		}
	}
	//#endregion
	/**
	 * @author lix
	 * @category 获取用户 角色总数方法
	 * @serial
	 * 【2019-07-30】 建立方法
	 * @param 
	 * @param idspartacus_useraccount用户id
	 * @return return_resultModel
	 */
	//#region
	public return_resultModel selectUserRoleCount(int idSpartacus_userrole,int NowPage,int ShowCount)
	{
		return_resultModel resultModel = new return_resultModel();
		//判断是否传了用户id或者用户角色id，是的话组建sql
		String where = " where ";
		if(idSpartacus_userrole != 0) {
			where += "idSpartacus_userrole = " + idSpartacus_userrole;
		}else {
			where = "";
		}
		//整合分页sql limit
		String limit = "";
		//判断是否有分页参数，要是没有就查询全部得
		if(NowPage != 0 && ShowCount != 0) {
			limit += " limit (" + NowPage + "," + ShowCount + ")";
		}
		//组建sql
		String sqlstr=String.format("select count(idSpartacus_userrole) as userroleCount from %s  %s",tableName, where);
		mysql_dop dop = new mysql_dop();
		Connection con = dop.conector();//获取数据库连接
		try 
		{
			//得到数据
			ResultSet resultset = dop.get(sqlstr,con);
			if(resultset.next())
			{
				resultModel.setResult(resultset.getInt("userroleCount"));
				resultModel.setData_message("获取用户总数成功");
			}
			return resultModel;
		}
		catch (Exception e) 
		{  
			resultModel.setResult(-1);
			resultModel.setData_message("获取用户总数失败。");
			return resultModel;
			//写入debug日志
		}
	}
	//#endregion
	
	/**
	 * @author lix
	 * @category 新增用户角色得逻辑代码 
	 * @serial
	 * 【2019-08-16】创建方法
	 * @param userrole_userroleModel用户角色得model类
	 * @return return_resultModel 特定得返回类
	 */
	//#region
	public return_resultModel addUserRole(userrole_userroleModel userroleModel) {
		return_resultModel resultModel = new return_resultModel();
		//组建添加得值
		String values = userroleModel.getRoleName();
		//组建sql
		String sqlstr=String.format("insert into %s ( %s ) values ( %s )",tableName,field,values);
		mysql_dop dop = new mysql_dop();
		Connection con = dop.conector();//获取数据库连接
		try 
		{
			//得到数据
			int resultset = dop.edit(sqlstr,con);
			if(resultset>0)
			{
				resultModel.setResult(1);
				resultModel.setData_message("添加成功。");
			}
			return resultModel;
		}
		catch (Exception e) 
		{
			resultModel.setResult(0);
			resultModel.setData_message("添加失败。");
			return resultModel;
			//写入debug日志
		}
	}
	//#endregion
	
	/**
	 * @author lix
	 * @category 编辑用户角色得逻辑代码 
	 * @serial
	 * 【2019-07-30】创建方法
	 * 【2019-08-15】完善方法--增加了判断，以防止传null.增加了验证到底修改的是什么值的方法类。为log做准备
	 * @param account_accountModel用户得model类
	 * @return return_resultModel 特定得返回类
	 */
	//#region
	public return_resultModel editUserRole(userrole_userroleModel userroleModel){
		//调用返回值model
		return_resultModel resultModel = new return_resultModel();
		check_parameter check = new check_parameter();
		try {
			mysql_dop dop = new mysql_dop();
			Connection con = dop.conector();//获取数据库连接 
			//这里循环model。把整个数据都进行一次修改
			System.out.println(tableName);
			String sqlstr = String.format("update %s set roleName='%s' where idSpartacus_userrole=%s",tableName,
					userroleModel.getRoleName(),userroleModel.getId());
			//去保存数据
			int resultset = dop.edit(sqlstr,con);
			//直接调用公共类，区别出到底都修改了什么值
			check.check_parameter(CheckEnum.userole,userroleModel);
			if(resultset >0)
			{
				resultModel.setResult(1);
				resultModel.setData_message("保存成功！");
				return resultModel;
			}
		} catch (Exception e) {
			resultModel.setResult(0);
			resultModel.setData_message("保存失败！");
			return resultModel;
		}
		return resultModel;
	}
	//#endregion
	
	/**
	 * @author lix
	 * @category 删除用户角色得逻辑代码 
	 * @serial
	 * 【2019-08-16】创建方法，采用真实删除，一经删除，没有找回
	 * 【2019-08-16】新增一处判断，防止用户角色下存在用户，应该提示，请先将用户移除后再进行删除
	 * @param userAccountID 用户id	增加判断，防止前端不给传id或瞎传，导致全部删除或者报错。
	 * @return return_resultModel 特定得返回类
	 */
	//#region
	
	public return_resultModel deleteUserRole(String userRoleID){
		//调用返回值model
		return_resultModel resultModel = new return_resultModel();
		mysql_dop dop = new mysql_dop();
		Connection con = dop.conector();//获取数据库连接
		try {
			//首先判断用户角色下是否存在用户
			String ifsql = "select count(iduseraccount_userrole) as count from useraccount_userrole where ref_userrole = " + userRoleID;
			//去查询数据
			ResultSet ifresiltset = dop.get(ifsql,con);
			if(ifresiltset.next()) {
				//判断是否查到了数据，如果为0表示没查到
				int Count = ifresiltset.getInt("count");
				if(Count > 0) {
					resultModel.setResult(-1);
					resultModel.setData_message("请先将用户移除后再进行删除！");
					return resultModel;
				}
			}
			//组建删除sql
			String sqlstr = String.format("delete from %s where idSpartacus_userrole=%s",tableName,userRoleID!=null?userRoleID:0);
			//去保存数据
			int resultset = dop.edit(sqlstr,con);
			if(resultset>0)
			{
				resultModel.setResult(1);
				resultModel.setData_message("删除成功！");
				return resultModel;
			}
		} catch (Exception e) {
			resultModel.setResult(0);
			resultModel.setData_message("删除失败！");
			return resultModel;
		}
		return resultModel;
	}
	//#endregion
}
