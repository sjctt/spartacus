package spartacus.controlls.Login.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.helper.tools.encryptDecryptHelper.DESClass;
import org.helper.tools.encryptDecryptHelper.MD5Class;

import com.data.operate.mysql_dop;

import spartacus_public.entity.return_resultModel;
import spartacus_public.entity.enums.objectStatusEnum;
import spartacus_public.entity.enums.resultCodeEnum;
import spartacus_public.method.js_des;

/**
 * @author Song
 * @category	登录模块逻辑对象
 * @serial
 *【2019年7月8日】	建立对象
 *【2019年7月8日】	完成登录认证函数 verify(String account,String password) @author Song
 */
public class login_logic 
{
	//#region 验证用户名密码
	/**
	 * @author Song
	 * @category	验证用户名及密码
	 * @serial
	 *【2019年7月8日】	账号密码是否正确、账号是否被禁用、账号是否被锁定、锁定后是否达到解锁条件 @author Song
	 *【2019年7月23日】	增加前端加解密，防止明文传输时被截取	@author Song
	 */
	public return_resultModel verify(String account,String password)
	{
		return_resultModel resultModel = new return_resultModel();
		String sqlstr=String.format("select * from spartacus_useraccount where account='%s'", account);
		mysql_dop dop = new mysql_dop();
		Connection con = dop.conector();//获取数据库连接
		try 
		{
			password = js_des.decryption(password, "");
			ResultSet resultset = dop.get(sqlstr,con);
			if(resultset.next())
			{
				//根据账号密码查询到了对应账号
				int accountStatus = resultset.getInt("accountStatus");//获取账号状态
		        objectStatusEnum status = objectStatusEnum.get_status_by_code(accountStatus);//通过状态获取枚举对象
				switch(status)
				{
					case disable:
						resultModel.setResult(resultCodeEnum.fail.getcode());
						resultModel.setData_message("认证失败，账号已被禁用。");
						break;
					case locked:
						long lockTime = resultset.getLong("lockTime");//账号被锁定的时间
						long unlockTime = lockTime+(6000*10*10);//计算解锁时间，10分钟之后
						long systemTime = System.currentTimeMillis();//系统当前时间
						if(systemTime < unlockTime)
						{
							//未达到解锁时间
							resultModel.setResult(resultCodeEnum.fail.getcode());
							resultModel.setData_message("认证失败，密码错误次数过多，账号已被锁定。");
							break;
						}
						else
						{
							//已达到解锁时间，重置账号状态，正常登录
							sqlstr = String.format("update spartacus_useraccount set pwdErrorTimes=%s,lockTime=%s,accountStatus=%s where account='%s'", 0,0,objectStatusEnum.normal.get_code(),account);
							dop.edit(sqlstr,con);//重置用户状态
							String token = DESClass.encrypt(account,"");
							resultModel.setResult(resultCodeEnum.success.getcode());
							resultModel.setData_message(token);
							
							break;
						}
					case normal:
					{
						//从数据库中拿key出来。
						String pwdkey = resultset.getString("pwdkey");
						password = DESClass.encrypt(password, pwdkey);
						if(password.equals(resultset.getString("password")))
						{
							//账号状态正常，登录成功，清空密码错误次数和锁定时间
							sqlstr = String.format("update spartacus_useraccount set pwdErrorTimes=0,lockTime=0 where account='%s'", account);
							dop.edit(sqlstr,con);
							String token = DESClass.encrypt(account,"");
							resultModel.setResult(resultCodeEnum.success.getcode());
							resultModel.setData_message(token);
						}
						else
						{
							int pwdErrorTimes = (resultset.getInt("pwdErrorTimes")+1);//获取密码错误次数
							if(pwdErrorTimes>=3)
							{
								sqlstr = String.format("update spartacus_useraccount set pwdErrorTimes=%s,lockTime=%s,accountStatus=%s where account='%s'", pwdErrorTimes,System.currentTimeMillis(),objectStatusEnum.locked.get_code(),account);
								dop.edit(sqlstr,con);
								
								resultModel.setResult(resultCodeEnum.fail.getcode());
								resultModel.setData_message("认证失败，密码错误次数过多，账号已被锁定。");
							}
							else
							{
								sqlstr = String.format("update spartacus_useraccount set pwdErrorTimes=%s where account='%s'", pwdErrorTimes,account);
								dop.edit(sqlstr,con);
								resultModel.setResult(resultCodeEnum.fail.getcode());
								resultModel.setData_message("认证失败，账号或密码错误。");
							}
						}
						break;
					}
					default:
						resultModel.setResult(resultCodeEnum.fail.getcode());
						resultModel.setData_message("认证失败，账号或密码错误。");
						break;
				}
			}
			else
			{
				resultModel.setResult(resultCodeEnum.fail.getcode());
				resultModel.setData_message("认证失败，账号或密码错误。");
			}
			return resultModel;
		}
		catch (Exception e) 
		{
			resultModel.setResult(resultCodeEnum.fail.getcode());
			resultModel.setData_message("认证失败，账号或密码错误。");
			e.printStackTrace();
			return resultModel;
			//写入debug日志
		}
		finally 
		{
			if(con != null)
			{
				try
				{
					con.close();
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	//#endregion
	//#region 验证用户是否登录
	public return_resultModel isLogin(String token,String clientIP,String session_token)
	{
		return_resultModel result = new return_resultModel();
		try 
		{
			if(session_token.equals(token))
			{
				result.setResult(resultCodeEnum.success.getcode());
				result.setData_message("");
			}
			else
			{
				result.setResult(resultCodeEnum.login_timeOut.getcode());
				result.setData_message("当前身份验证已超时，请返回重新登录。");
			}
		} 
		catch (Exception e) 
		{
			result.setResult(resultCodeEnum.login_timeOut.getcode());
			result.setData_message("当前身份验证已超时，等返回重新登录。");
		}
		return result;
	}
	//#endregion
}
