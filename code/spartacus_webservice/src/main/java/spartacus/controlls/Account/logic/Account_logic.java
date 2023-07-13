package spartacus.controlls.Account.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.helper.tools.encryptDecryptHelper.DESClass;

import com.data.operate.mysql_dop;

import spartacus.controlls.Account.entity.account_accountModel;
import spartacus_public.entity.return_resultModel;
import spartacus_public.entity.enums.CheckEnum;
import spartacus_public.method.check_parameter;
import spartacus_public.method.js_des;
/**
 * @author Lix
 * @category	用户信息模块逻辑对象
 * @serial
 *【2019年7月8日】	建立对象
 *
 */
public class Account_logic 
{
	//#region 关于用户表的字段和表名初始化
	private String field = "account,password,pwdkey,name,jobNumber,accountStatus,phoneNumber,email,createTime,remark"; 
	private String tableName = "spartacus_useraccount";
	//#endregion
	
	//#region 查询用户基础信息
	/**
	 * @author Lix
	 * @category	查询用户基础信息
	 * @serial
	 *【2019年7月29日】	创建方法
	 *
	 */
	public ArrayList<account_accountModel> selectAccount(int idspartacus_useraccount,int ref_utype,int NowPage,int ShowCount)
	{
		js_des des = new js_des();
		//判断是否传了用户id或者用户角色id，是的话组建sql
		String where = " where ";
		if(idspartacus_useraccount != 0 && ref_utype != 0) {
			where += "idspartacus_useraccount = " + idspartacus_useraccount + " or "+"ref_utype = " + ref_utype;
		}else if(idspartacus_useraccount != 0) {
			where += "idspartacus_useraccount = " + idspartacus_useraccount;
		}else if(ref_utype != 0) {
			where += "ref_utype = " + ref_utype;
		}else {
			where = "";
		}
		//整合分页sql limit
		String limit = "";
		//判断是否有分页参数，要是没有就查询全部得
		if(NowPage != 0 && ShowCount != 0) {
			//处理跳过几条
			NowPage = (NowPage * ShowCount)-ShowCount;
			limit += " limit " + NowPage + "," + ShowCount + "";
		}
		//组建sql
		String sqlstr=String.format("select * from %s  %s %s",tableName,where,limit);
		mysql_dop dop = new mysql_dop();
		Connection con = dop.conector();//获取数据库连接
		//创建用户list
		ArrayList<account_accountModel> accountList =  new ArrayList <account_accountModel>();
		try 
		{
			//得到数据
			ResultSet resultset = dop.get(sqlstr,con);
			while(true) 
			{
				if(resultset.next())
				{
					account_accountModel accountModel = new account_accountModel();
					accountModel.setId(resultset.getInt("idspartacus_useraccount"));
					accountModel.setAccount(resultset.getString("account"));
					//处理密码
					String resultpwd = resultset.getString("password");
					String pwdkey = resultset.getString("pwdkey");
					String despwd;
					despwd = DESClass.decrypt(resultpwd,pwdkey);//后端解密
					String pwd = des.encryption(despwd, des.GetKey());//前端加密
					accountModel.setPassword(pwd);
					accountModel.setName(resultset.getString("name"));
					accountModel.setJobNumber(resultset.getString("jobNumber"));
					accountModel.setAccountStatus(resultset.getInt("accountStatus"));
					accountModel.setPhoneNumber(resultset.getString("phoneNumber"));
					accountModel.setEmail(resultset.getString("email"));
					accountModel.setPwdErrorTimes(resultset.getInt("pwdErrorTimes"));
					accountModel.setLockTime(resultset.getInt("lockTime"));
					accountModel.setLastLoginTime(resultset.getString("lastLoginTime"));
					accountModel.setRemark(resultset.getString("remark"));
					accountModel.setCreateTime(resultset.getString("createTime"));
					accountList.add(accountModel);
				}else
				{
					break;
				}
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
	
	
	//#region	获取用户总数方法
	/**
	 * @author lix
	 * @category 获取用户总数方法
	 * @serial
	 * 【2019-07-30】 建立方法
	 * @param 
	 * @param idspartacus_useraccount用户id	ref_utype角色id
	 * @return account_accountModel
	 */
	public return_resultModel selectAccountCount(int idspartacus_useraccount,int ref_utype)
	{
		return_resultModel resultModel = new return_resultModel();
		//判断是否传了用户id或者用户角色id，是的话组建sql
		String where = " where ";
		if(idspartacus_useraccount != 0 && ref_utype != 0) {
			where += "idspartacus_useraccount = " + idspartacus_useraccount + " or "+"ref_utype = " + ref_utype;
		}else if(idspartacus_useraccount != 0) {
			where += "idspartacus_useraccount = " + idspartacus_useraccount;
		}else if(ref_utype != 0) {
			where += "ref_utype = " + ref_utype;
		}else {
			where = "";
		}
		//组建sql
		String sqlstr=String.format("select count(idspartacus_useraccount) as accountCount from %s  %s",tableName, where);
		mysql_dop dop = new mysql_dop();
		Connection con = dop.conector();//获取数据库连接
		try 
		{
			//得到数据
			ResultSet resultset = dop.get(sqlstr,con);
			if(resultset.next())
			{
				resultModel.setResult(resultset.getInt("accountCount"));
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
	
	
	//#region 新增用户得逻辑代码 
	/**
	 * @author lix
	 * @category 新增用户得逻辑代码 
	 * @serial
	 * 【2019-07-30】创建方法
	 * @param account_accountModel用户得model类
	 * @return return_resultModel 特定得返回类
	 */
	public return_resultModel addAccount(account_accountModel accountModel) {
		return_resultModel resultModel = new return_resultModel();
		//组建添加得字段
		//String field = "account,password,name,jobNumber,accountStatus,phoneNumber,email,ref_utype,createTime";
		//这里处理密码
		String password = accountModel.getPassword();
		js_des des = new js_des();
		String despassword = "";
		//这里生成一个随机得8位数key
		StringBuilder keybuil = new StringBuilder();//定义变长字符串
		Random random=new Random();
		//随机生成数字，并添加到字符串
		for(int i=0;i<8;i++){
			keybuil.append(random.nextInt(10));
		}
		String key = ""+keybuil;
		try {
			//这里把前端得加密后密码进行解密
			//String newpassword = des.decryption(password,des.GetKey());
			//再进行后端加密
			despassword = DESClass.encrypt(password, key);
		} catch (Exception e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		//初始化时间格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//组建添加得值
		String values = (accountModel.getAccount()==null?null:("'"+accountModel.getAccount()+"'"))+","
		+ (despassword==null?null: ("'"+despassword+"'"))+ "," + (key==null?null: ("'"+key+"'"))
		+ "," + (accountModel.getName()==null?null: ("'"+accountModel.getName()+"'")) + "," + (accountModel.getJobNumber()==null?null: ("'"+accountModel.getJobNumber()+"'"))
		+ "," + (String.valueOf(accountModel.getAccountStatus()))+ "," + (accountModel.getPhoneNumber()==null?null:("'"+accountModel.getPhoneNumber())+"'")
		+ "," + (accountModel.getEmail()==null?null:("'"+accountModel.getEmail()+"'"))+  ",'" + dateFormat.format(new Date())
		+"'," +(accountModel.getRemark()==null?null:("'"+accountModel.getRemark()+"'"));
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
	
	
	//#region  编辑用户得逻辑代码 
	/**
	 * @author lix
	 * @category 编辑用户得逻辑代码 
	 * @serial
	 * 【2019-07-30】创建方法
	 * 【2019-08-15】完善方法--增加了判断，以防止传null.增加了验证到底修改的是什么值的方法类。为log做准备
	 * 【2019-08-22】完善方法--最初为修改用户状态创建接口。后考虑到之后可能会单独修改某一项得情况。故增加接口复用性。复用性提升，但是稍微有些冗余，今后想到好的办法在做优化。
	 * 【2019-08-23】优化方法--当前方法较于冗余，进行一次优化，代码上未优化，只能说性能上更优了一些。减少了会判断得次数。
	 * @param account_accountModel用户得model类
	 * @return return_resultModel 特定得返回类
	 */
	public return_resultModel editAccount(account_accountModel accountModel){
		//调用返回值model
		return_resultModel resultModel = new return_resultModel();
		check_parameter check = new check_parameter();
		try {
			mysql_dop dop = new mysql_dop();
			Connection con = dop.conector();//获取数据库连接
			//处理用户
			String account = "";
			if(accountModel.getAccount() != null) {
				account = String.format("account='%s'",accountModel.getAccount());
			}
			//这里处理密码--密码需要加密解密操作，所以需要单独去处理
			String despassword = "";
			if(accountModel.getPassword() != null) {
				String password = accountModel.getPassword();
				js_des des = new js_des();
				//这里把key取出来。
				String selectsql = String.format("select pwdkey from %s where idspartacus_useraccount =%s",tableName,accountModel.getId());
				ResultSet resultget = dop.get(selectsql,con);
				String key = "";
				if(resultget.next()) {
					key = resultget.getString("pwdkey");
				}			
				try {
					//这里把前端得加密后密码进行解密
					//String newpassword = des.decryption(password,des.GetKey());
					//再进行后端加密
					String despasswordold = DESClass.encrypt(password, key);
					//赋值一个没有逗号得
					despassword = String.format("password='%s'",despasswordold);
					if(account != ""){
						despassword = String.format(",password='%s'",despasswordold);//重新赋值，因为sql语句前面有一个值了，要加逗号了
					}
				} catch (Exception e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
			//处理name
			String name = "";
			if(accountModel.getName()!=null){
				name = String.format("name = '%s'",accountModel.getName());
				if(account != "" || despassword != "") {
					name = ","+name;
				}
			}
			//处理jobnumber
			String jobnumber = "";
			if(accountModel.getJobNumber()!=null){
				jobnumber = String.format("jobNumber = '%s'",accountModel.getJobNumber());
				if(account != "" || despassword != "" || name != "") {
					jobnumber = ","+jobnumber;
				}
			}
			//处理状态Status
			String status = "";
			if(""+accountModel.getAccountStatus()!=null){
				status = String.format("accountStatus = %s",accountModel.getAccountStatus());
				if(account != "" || despassword != "" || name != "" || jobnumber != "") {
					status = ","+status;
				}
			}
			//处理PhoneNumber
			String phonenumber = "";
			if(accountModel.getPhoneNumber()!=null){
				phonenumber = String.format("phoneNumber = '%s'",accountModel.getPhoneNumber());
				if(account != "" || despassword != "" || name != "" || jobnumber != "" || status != "") {
					phonenumber = ","+phonenumber;
				}
			}
			//处理邮箱
			String email = "";
			if(accountModel.getEmail()!=null){
				email = String.format("phoneNumber = '%s'",accountModel.getEmail());
				if(account != "" || despassword != "" || name != "" || jobnumber != "" || status != "" || phonenumber != "") {
					email = ","+email;
				}
			}
			//处理角色关系
			String remark = "";
			if(accountModel.getRemark()!=null){
				remark = String.format("remark = %s",accountModel.getRemark());
				if(account != "" || despassword != "" || name != "" || jobnumber != "" || status != "" || phonenumber != "" || email != "") {
					remark = ","+remark;
				}
			}
			//这里循环model。把整个数据都进行一次修改	-- 层层判断，得到最后到底要添加得值--id不需要判断了
			String sqlstr = String.format("update %s set %s %s %s %s %s %s %s %s where idspartacus_useraccount=%s"
					,tableName
					,account
					,despassword
					,name
					,jobnumber
					,status
					,phonenumber
					,email
					,remark
					,accountModel.getId());
			//去保存数据
			int resultset = dop.edit(sqlstr,con);
			//直接调用公共类，区别出到底都修改了什么值
			accountModel.setPassword(despassword);//修改一下密码，防止检测不到是不是修改了密码
			check.check_parameter(CheckEnum.account,accountModel);
			if(resultset >0) 
			{
				resultModel.setResult(1);
				resultModel.setData_message("保存成功！");
				return resultModel;
			}
		} catch (Exception e) {
			System.out.println("catch:"+e.getMessage());
			resultModel.setResult(0);
			resultModel.setData_message("保存失败！");
			return resultModel;
		}
		return resultModel;
	}
	//#endregion
	
	
	//#region	删除用户得逻辑代码 
	/**
	 * @author lix	
	 * @category 删除用户得逻辑代码 
	 * @serial
	 * 【2019-07-30】创建方法，采用真实删除，一经删除，没有找回
	 * @param userAccountID 用户id	增加判断，防止前端不给传id，导致全部删除或者报错。
	 * @return return_resultModel 特定得返回类
	 */
	public return_resultModel deleteAccount(String userAccountID){
		//调用返回值model
		return_resultModel resultModel = new return_resultModel();
		try {
			mysql_dop dop = new mysql_dop();
			Connection con = dop.conector();//获取数据库连接 
			String sqlstr = String.format("delete from %s where idspartacus_useraccount=%s",tableName,userAccountID!=null?userAccountID:0);
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
