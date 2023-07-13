package spartacus_public.entity;


/**
 * @author Song
 * @category 柱状图数据对象
 * @serial
 *【2019年10月24日】	
 */
public class histogramdata_entity 
{
	private String key;//显示在x轴坐标上的数据
	private long value;//每个柱状体的数据
	private String Content;//用户前台鼠标指向柱状图时的显示内容
	private String histogram_startTime;//柱子对应的开始时间
	private String histogram_endTime;//主子对应的结束时间
	private boolean search_isend;//直方图查询是否结束
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getHistogram_startTime() {
		return histogram_startTime;
	}
	public void setHistogram_startTime(String histogram_startTime) {
		this.histogram_startTime = histogram_startTime;
	}
	public String getHistogram_endTime() {
		return histogram_endTime;
	}
	public void setHistogram_endTime(String histogram_endTime) {
		this.histogram_endTime = histogram_endTime;
	}
	public boolean isSearch_isend() {
		return search_isend;
	}
	public void setSearch_isend(boolean search_isend) {
		this.search_isend = search_isend;
	}
}
