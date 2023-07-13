package spartacus_public.entity.enums;
/**
 * @author Song
 * @category
 * @serial
 *【2019年10月24日】	建立对象
 */
public enum histogramtypeenum 
{
	/**
	 * 指示柱状图以年为单位
	 **/
	year(0),
	/**
	 * 指示柱状图以月为单位
	 **/
	month(1),
	/**
	 * 指示柱状图以日为单位
	 **/
	day(2),
	/**
	 * 指示柱状图以时为单位
	 **/
	hour(3),
	/**
	 * 指示柱状图以分为单位
	 **/
	min(4),
	/**
	 * 指示柱状图以秒为单位
	 **/
	sec(5);
	
	private int nCode;
	private histogramtypeenum()
	{
		
	}
	private histogramtypeenum(int nCode) 
	{
		this.nCode = nCode;
	}
	public int getnCode() {
		return nCode;
	}
	public void setnCode(int nCode) {
		this.nCode = nCode;
	}
}
