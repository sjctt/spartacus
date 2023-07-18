package spartacus_public.entity.enums;
/**
 * @author Song
 * @category
 * @serial
 *【2019年9月9日】	建立对象
 */
public enum LogLevelEnum {
	/*
	事件级别枚举
	三个参数 String String int
	参数1： name 前端页面展示文本
	参数2： text 索引中储存的文本
	参数3：code 值所对应的数值
	*/
	debug("调试#debug","debug",7),
	info("信息#info","info",6),
	notice("通知#notice","notice",5),
	warning("警告#warning","warning",4),
	error("错误#err","err",3),
	critical("关键#crit","crit",2),
	alert("警示#alert","alert",1),
	emergency("紧急#emerg","emerg",0),
	other("其他#other","other",-1);
	private LogLevelEnum()
	{
		
	}
	private LogLevelEnum(String name ,String text,int nCode) {
		this.name=name;
		this.nCode = nCode;
		this.text = text;
	}
	
	private String name ;
	private int nCode;
	private String text;
	public String getName() {
		return name;
	}
	public String getText()
	{
		return text;
	}
	public int getnCode() {
		return nCode;
	}
	public void setnCode(int nCode) {
		this.nCode = nCode;
	}
	
	public static String getName(int nCode) 
	{  
		for (LogLevelEnum log : LogLevelEnum.values()) 
		{
			if (log.getnCode() == nCode) 
			{  
	               return log.name;  
			} 
		}
		return null;
	}
	public static String getText(int nCode) 
	{  
		for (LogLevelEnum log : LogLevelEnum.values()) 
		{
			if (log.getnCode() == nCode) 
			{  
	               return log.text;  
			} 
		}
		return null;
	}
}
