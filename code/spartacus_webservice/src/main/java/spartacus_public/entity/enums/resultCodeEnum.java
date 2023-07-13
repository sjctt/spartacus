package spartacus_public.entity.enums;
/**
 * @author Song
 * @category 返回状态码枚举
 * @serial
 *【2019年7月9日】	建立对象
 */
public enum resultCodeEnum 
{
	/** 函数执行失败**/
	fail(0),
	/** 函数执行成功**/
	success(1),
	/** 用户登录超时**/
	login_timeOut(2)
	;
	
	
	int code;
	private resultCodeEnum(int code)
	{
		this.code = code;
	}
	public int getcode()
	{
		return code;
	}
}
