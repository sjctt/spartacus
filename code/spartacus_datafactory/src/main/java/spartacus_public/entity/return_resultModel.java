package spartacus_public.entity;
/**
 * @author Song
 * @category 数据反馈model
 * @serial
 *【2019年7月8日】	建立对象
 */
public class return_resultModel 
{
	/**
	 * 执行结果，返回代码
	 * 0：失败
	 * 1：成功
	 * */
	private int result;
	/**
	 * 数据消息，为json数据或消息提示
	 * */
	private String data_message;
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getData_message() {
		return data_message;
	}
	public void setData_message(String data_message) {
		this.data_message = data_message;
	}
	
}
