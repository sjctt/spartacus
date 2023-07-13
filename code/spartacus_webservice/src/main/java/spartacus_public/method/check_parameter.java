package spartacus_public.method;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.data.operate.mysql_dop;

import spartacus.controlls.Account.entity.account_accountModel;
import spartacus.controlls.UserRole.model.userrole_userroleModel;
import spartacus_public.entity.enums.CheckEnum;

/**
 * @author Lix
 * @category	校验参数类，主要目的是解决前台传回来model无法得到所修改得字段得问题
 * @serial
 *【2019年7月23日】	建立对象
 */
public class check_parameter 
{
	/**
	 * @author lix
	 * @category 用于所有功能项在编辑时判断用户编辑了什么值。
     * @param model 枚举类。用来区分当前是保存什么模块。
     * @param newData 前端传过来的数据。用于和数据库中旧数据比对。
     * @return 
     * @throws Exception	返回字符串，当前一共编辑了什么值。并写日志。
     */
    public static String check_parameter(CheckEnum model,Object newData) throws Exception
    {
		mysql_dop dop = new mysql_dop();
		Connection con = dop.conector();//获取数据库连接
		//初始化返回值
		String message = "";
		//判断，当前为什么值。根据枚举
    	switch (model.get_code())
    	{
			case 0:
				//如果为0则是比对处理用户数据
				account_accountModel data = (account_accountModel)newData;
				
				String sqlstr = "select * from " + model.get_tableName() + " where id" + model.get_tableName() + 
						"=" + data.getId(); 
				//得到数据 
				ResultSet resultset = dop.get(sqlstr,con);
				//循环去做比对
				 while (resultset.next()) {
		             if(!resultset.getString("account").equals(data.getAccount())){
		            	 message += "账户、";
		             }
		             if(!resultset.getString("password").equals(data.getPassword())) {
		            	 message += "密码、";
		             }
		             if(!resultset.getString("name").equals(data.getName())) {
		            	 message += "昵称、";
		             }
		             if(!resultset.getString("jobNumber").equals(data.getJobNumber())) {
		            	 message += "工号、";
		             }
		             if(!resultset.getString("accountStatus").equals(data.getAccountStatus())) {
		            	 message += "状态、";
		             }
		             if(resultset.getString("phoneNumber").equals(data.getPhoneNumber())) {
		            	 message += "电话、";
		             }
		             if(!resultset.getString("email").equals(data.getEmail())) {
		            	 message += "邮箱、";
		             }
		             if(!resultset.getString("remark").equals(data.getRemark())) {
		            	 message += "备注、";
		             }
				 }
				break;
			case 1:
				//如果为1则是比对处理用户角色数据
				userrole_userroleModel userroledata = (userrole_userroleModel)newData;
				
				String userrolesqlstr = "select * from " + model.get_tableName() + "where id" + model.get_tableName() + 
						"=" + userroledata.getId(); 
				//得到数据 
				ResultSet userroleresultset = dop.get(userrolesqlstr,con);
				//循环去做比对
				 while (userroleresultset.next()) {
					 //---现在用户角色是一个值，但是为了今后做准备
		             if(!userroleresultset.getString("roleName").equals(userroledata.getRoleName())){
		            	 message += "角色名称、";
		             }
				 }
				break;
			default:
				break;
		}
    	//去掉message多出来的最后一个标点符号（、）。
    	message.substring(0,message.length()-1);
    	return message;
    } 
}
