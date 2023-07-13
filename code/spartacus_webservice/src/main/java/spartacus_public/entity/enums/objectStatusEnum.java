package spartacus_public.entity.enums;
/**
 * @author Song
 * @category 所有对象状态的枚举类
 * @serial
 *【2019年7月8日】	建立对象
 */
public enum objectStatusEnum 
{
	/**
	 * 禁用对象
	 **/
	disable(0,"已禁用"),
	/**
	 * 正常状态
	 **/
	normal(1,"正常"),
	/**
	 * 锁定账号
	 **/
	locked(2,"已锁定")
	;
	
	int code;
	String val;
	private objectStatusEnum(int code,String val)
	{
		this.code = code;
		this.val = val;
	}
	/**
	 * 获取对象val值
	 **/
	public String get_val()
	{
		return val;
	}
	/**
	 * 获取对象code值
	 **/
	public int get_code()
	{
		return code;
	}
	/**
	 * 根据code获取枚举对象
	 **/
	public static objectStatusEnum get_status_by_code(int code) 
	{
		for(objectStatusEnum status : objectStatusEnum.values())
		{
			if(status.get_code() == code)
			{
				return status;
			}
		}
		return null;
	}
}
