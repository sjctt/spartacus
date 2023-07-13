package spartacus_public.entity.enums;
/**
 * @author Song
 * @category 系统模块枚举
 * @serial
 *【2019年7月8日】	建立对象
 */
public enum CheckEnum 
{
	/**
	 * 账号管理模块
	 **/
	account(0,"spartacus_useraccount"),//用户
	userole(1,"spartacus_userrole"),//用户角色
	;
	
	
	int code;
	String tableName;
	private CheckEnum(int code,String tableName)
	{
		this.code = code;
		this.tableName = tableName;
	}
	/**
	 * 获取对象code值
	 **/
	public int get_code()
	{
		return code;
	}
	/**
	 * 获取表名
	 **/
	public String get_tableName()
	{
		return tableName;
	}
	/**
	 * 根据code获取枚举对象
	 **/
	public static CheckEnum get_status_by_code(int code) 
	{
		for(CheckEnum status : CheckEnum.values())
		{
			if(status.get_code() == code)
			{
				return status;
			}
		}
		return null;
	}
}
