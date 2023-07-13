package spartacus.controlls.LogAudit.entity;

import spartacus_public.entity.enums.histogramtypeenum;

/**
 * @author Song
 * @category 数据查询条件封装
 * @serial
 *【2019年10月17日】	建立对象
 */
public class searchconditionentity 
{
	//#region 通用参数
	private int timerange;//时间范围选择器
	private String fromtime;//开始时间
	private String totime;//截止时间
	private String searchscript;//检索脚本
	private int pagecount;//每页显示数量
	private int selectedpage;//当前选中的页
	//#endregion
	
	//#region 柱状图参数
	private histogramtypeenum histogramtype;//柱状图的显示类型
	private long histogram_lasttime;//上次异步查询的时间节点
	//#endregion
	
	public int getTimerange() {
		return timerange;
	}
	public void setTimerange(int timerange) {
		this.timerange = timerange;
	}
	public String getFromtime() {
		return fromtime;
	}
	public void setFromtime(String fromtime) {
		this.fromtime = fromtime;
	}
	public String getTotime() {
		return totime;
	}
	public void setTotime(String totime) {
		this.totime = totime;
	}
	public String getSearchscript() {
		return searchscript;
	}
	public void setSearchscript(String searchscript) {
		this.searchscript = searchscript;
	}
	public int getPagecount() {
		return pagecount;
	}
	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}
	public int getSelectedpage() {
		return selectedpage;
	}
	public void setSelectedpage(int selectedpage) {
		this.selectedpage = selectedpage;
	}
	public histogramtypeenum getHistogramtype() {
		return histogramtype;
	}
	public void setHistogramtype(histogramtypeenum histogramtype) {
		this.histogramtype = histogramtype;
	}
	public long getHistogram_lasttime() {
		return histogram_lasttime;
	}
	public void setHistogram_lasttime(long histogram_lasttime) {
		this.histogram_lasttime = histogram_lasttime;
	}

	
}
