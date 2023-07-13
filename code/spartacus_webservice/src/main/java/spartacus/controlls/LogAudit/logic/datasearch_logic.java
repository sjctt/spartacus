package spartacus.controlls.LogAudit.logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import com.data.operate.es_dop;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spartacus.controlls.LogAudit.entity.searchconditionentity;
import spartacus_public.entity.histogramdata_entity;
import spartacus_public.entity.return_resultModel;
import spartacus_public.entity.enums.histogramtypeenum;

/**
 * @author Song
 * @category 数据检索逻辑代码
 * @serial
 *【2019年10月17日】	建立对象
 */
public class datasearch_logic 
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	//#region 获取ES数据总量
	public String get_datacount(searchconditionentity condition)
	{
		long[] timelist = arrange_Time(condition);//处理查询条件
		String search_script=String.format("receivetime:{%s TO %s}", timelist[0],timelist[1]);
		condition.setSearchscript(search_script+" AND ("+condition.getSearchscript()+")");
		es_dop es_dop = new es_dop();
		long datacount = es_dop.get_es_datacount("logsys_index", condition.getSearchscript());
		return_resultModel result = new return_resultModel();
		result.setResult(1);
		result.setData_message(String.valueOf(datacount));
		String jsonstr=JSONObject.fromObject(result).toString();
		return jsonstr;
	}
	//#endregion
	//#region 获取分页数据方法
	public String get_pagedata(searchconditionentity condition)
	{
		long[] timelist = arrange_Time(condition);//处理查询条件
		String search_script=String.format("receivetime:{%s TO %s}", timelist[0],timelist[1]);
		condition.setSearchscript(search_script+" AND ("+condition.getSearchscript()+")");
		
		es_dop es_dop = new es_dop();
		int from = (condition.getSelectedpage()-1)*condition.getPagecount();

		ArrayList<String> datalist = es_dop.query_index_spl("logsys_index", condition.getSearchscript(), from, condition.getPagecount());
		
		String listjsonstr = JSONArray.fromObject(datalist).toString();
		
		return_resultModel result = new return_resultModel();
		result.setResult(1);
		result.setData_message(listjsonstr);
		
		String jsonstr=JSONObject.fromObject(result).toString();
		return jsonstr;
	}
	//#endregion
	//#region 处理时间条件
	/**
	 * @author Song
	 * 作用：整理查询时间范围，计算出查询开始结束时间点
	 * 修改记录：
	 * 2018年10月23日 建立并完成这个函数 @author Song
	 * 2018年10月24日 应为前端的时间选择器只能精准到分钟，所以我需要在这里把
	 * 							   时间查询条件的秒和分钟取出，然后补充为从本分钟的0.000秒
	 * 							   到本分钟的59.9999秒 @author Song
	 **/
	private long[] arrange_Time(searchconditionentity condition)
	{
		long[] timelist = new long[2];
		Long endtime = 0L;//查询结束时间
		Long starttime = 0L;//查询开始时间
		Date nowdate = new Date();//系统当前时间
		
		Calendar start_time_cal = Calendar.getInstance();
		start_time_cal.setTime(nowdate);//开始时间计算对象

		Calendar end_time_cal = Calendar.getInstance();
		end_time_cal.setTime(nowdate);//结束时间计算对象
		
		switch(condition.getTimerange())
		{
			case 0://15分钟
				start_time_cal.add(Calendar.MINUTE, -15);
			break;
			case 1://30分钟
				start_time_cal.add(Calendar.MINUTE, -30);
			break;
			case 2://1小时
				start_time_cal.add(Calendar.HOUR_OF_DAY, -1);
			break;	
			case 3://3小时
				start_time_cal.add(Calendar.HOUR_OF_DAY, -3);//向前3小时
			break;	
			case 4://6小时
				start_time_cal.add(Calendar.HOUR_OF_DAY, -6);//向前6小时
			break;	
			case 5://1天
				start_time_cal.add(Calendar.DAY_OF_MONTH, -1);//向前1天
			break;	
			case 6://2天
				start_time_cal.add(Calendar.DAY_OF_MONTH, -2);//向前2天
			break;	
			case 7://7天
				start_time_cal.add(Calendar.DAY_OF_MONTH, -7);//向前7天
			break;	
			case 8://15天
				start_time_cal.add(Calendar.DAY_OF_MONTH, -15);//向前15天
			break;	
			case 9://1个月
				start_time_cal.add(Calendar.MONTH, -1);//向前1月
			break;	
			case 10://3个月
				start_time_cal.add(Calendar.MONTH, -3);//向前3月
			break;
			case 11://自定义
				try 
				{
					start_time_cal.setTime(sdf.parse(condition.getFromtime()));
					end_time_cal.setTime(sdf.parse(condition.getTotime()));
				} 
				catch (Exception e) 
				{
					
				}
			break;
		}
		starttime = start_time_cal.getTimeInMillis()/(1000*60)*(1000*60);//去掉秒和毫秒部分
		endtime = end_time_cal.getTimeInMillis()/(1000*60)*(1000*60);//去掉秒和毫秒部分
		
		//给结束时间补充秒和毫秒部分到59.9999
		
		Calendar count_end = Calendar.getInstance();
		count_end.setTime(new Date(endtime));
		count_end.set(Calendar.SECOND, 59);
		count_end.set(Calendar.MILLISECOND,999);
		endtime = count_end.getTimeInMillis();
		
		timelist[0] = starttime;
		timelist[1] = endtime;
		return timelist;
	}
	//#endregion
	//#region 构造柱状图数据对象
	/**
	 * @author Song
	 * 这个函数目的是汇总数据总量、构造柱状图数据
	 * 作用：
	 * 			1. 根据条件汇总数据总数
	 * 			2. 构造柱状图数据对象
	 * 参数：
	 * 			condition：前端提交的查询条件
	 * 返回值：
	 * 			 histogramdata_entity 数据交互对象
	 * 修改记录：
	 * 2018年10月24日 建立并完成这个函数 @author Song
	 **/
	public String get_histogramdata(searchconditionentity condition)
	{
		histogramdata_entity histogramdata = new histogramdata_entity();
		long[] timelist = arrange_Time(condition);//处理查询条件,拿到开始时间和结束时间
		boolean isfirst = false;
		if(condition.getHistogram_lasttime()==0)
		{
			//首次查询，使用开始时间
			condition.setHistogram_lasttime(timelist[0]);
			isfirst = true;
		}
		long ms = timelist[1] - timelist[0];//计算时间跨度，毫秒
		//#region 确定柱状图显示类别和时间
		long[] histogram_timelist = new long[2];//柱状图的查询开始时间和结束时间
		histogram_timelist[0] = condition.getHistogram_lasttime();
		if((ms/1000/60)<=1)//小于等于1分钟
		{
			if(!isfirst)
			{
				histogram_timelist[0] +=1;
			}
			histogram_timelist[1] = histogram_timelist[0]+999;//计算结束时间
			
			SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			SimpleDateFormat hms = new SimpleDateFormat("mm分ss秒");
			String key=hms.format(histogram_timelist[0]);
			histogramdata.setKey(key);
			String content = ymdhms.format(histogram_timelist[0]);
			histogramdata.setContent(content);
			histogramdata.setHistogram_startTime(ymdhms.format(histogram_timelist[0]));
			histogramdata.setHistogram_endTime(ymdhms.format(histogram_timelist[1]));
			condition.setHistogramtype(histogramtypeenum.sec);//直方图显示内容
		}
		else if((ms/1000/60/60)<=1)//小于等于1小时
		{
			if(!isfirst)
			{
				histogram_timelist[0] = histogram_timelist[0]+1;
			}
			histogram_timelist[1] = histogram_timelist[0]+59999;
			
			SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			SimpleDateFormat hm = new SimpleDateFormat("HH时mm分");
			
			String key=hm.format(histogram_timelist[0]);
			histogramdata.setKey(key);
			String content = ymdhm.format(histogram_timelist[0]);
			histogramdata.setContent(content);
			histogramdata.setHistogram_startTime(ymdhms.format(histogram_timelist[0]));
			histogramdata.setHistogram_endTime(ymdhms.format(histogram_timelist[1]));
			condition.setHistogramtype(histogramtypeenum.min);
		}
	    else if((ms/1000/60/60/24)<=1)//小于等于1天
	    {
	    	//需要注意处理分钟、秒、毫秒
	    	if(!isfirst)
			{
		    	histogram_timelist[0] = histogram_timelist[0]+1;
			}
	    	Calendar count_cal = Calendar.getInstance();
	    	count_cal.setTime(new Date(histogram_timelist[0]));
	    	count_cal.set(Calendar.MINUTE,59);
	    	count_cal.set(Calendar.SECOND, 59);
	    	count_cal.set(Calendar.MILLISECOND, 999);
	    	histogram_timelist[1] = count_cal.getTimeInMillis();
	    	
	    	SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    	SimpleDateFormat ymdh = new SimpleDateFormat("yyyy/MM/dd HH");
			SimpleDateFormat dh = new SimpleDateFormat("dd日HH时");
			
	    	String key=dh.format(histogram_timelist[0]);
			histogramdata.setKey(key);
			String content = ymdh.format(histogram_timelist[0]);
			histogramdata.setContent(content);
			histogramdata.setHistogram_startTime(ymdhms.format(histogram_timelist[0]));
			histogramdata.setHistogram_endTime(ymdhms.format(histogram_timelist[1]));
			condition.setHistogramtype(histogramtypeenum.hour);
	    }
	    else
	    {
	    	//以下是按天、月、年的显示处理
	    	Calendar condition_starttime = Calendar.getInstance();
	    	condition_starttime.setTime(new Date(timelist[0]));
	    	
	    	Calendar condition_endtime = Calendar.getInstance();
	    	condition_endtime.setTime(new Date(timelist[1]));
	    	
	    	int result = condition_endtime.get(Calendar.MONTH) - condition_starttime.get(Calendar.MONTH);
	    	int month = (condition_endtime.get(Calendar.YEAR) - condition_starttime.get(Calendar.YEAR)) * 12;
	 		int mon = Math.abs(month + result);//首先我要计算查询条件中的月差
	 		if(mon<=1)//小于等于1月，则按天来进行查询
	 		{
	 			condition.setHistogramtype(histogramtypeenum.day);
	 			
	 			if(!isfirst)
				{
	 				histogram_timelist[0] = condition.getHistogram_lasttime()+1;
				}

	 			Calendar count_cal = Calendar.getInstance();
		    	count_cal.setTime(new Date(histogram_timelist[0]));
		    	count_cal.set(Calendar.HOUR_OF_DAY,23);
		    	count_cal.set(Calendar.MINUTE,59);
		    	count_cal.set(Calendar.SECOND, 59);
		    	count_cal.set(Calendar.MILLISECOND, 999);
		    	histogram_timelist[1] = count_cal.getTimeInMillis();
		    	
		    	SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				SimpleDateFormat hms = new SimpleDateFormat("MM月dd日");
				SimpleDateFormat ymd = new SimpleDateFormat("yyyy年MM月dd日");
				
				String key=hms.format(histogram_timelist[0]);
				histogramdata.setKey(key);
				String content = ymd.format(histogram_timelist[0]);
				histogramdata.setContent(content);
				histogramdata.setHistogram_startTime(ymdhms.format(histogram_timelist[0]));
				histogramdata.setHistogram_endTime(ymdhms.format(histogram_timelist[1]));
	 		}
	 		else if(mon>1&&mon<=12)//大于一个月 小于12个月，则按月来查询
	 		{
	 			condition.setHistogramtype(histogramtypeenum.month);
	 			
	 			if(!isfirst)
				{
	 				histogram_timelist[0] = condition.getHistogram_lasttime()+1;
				}
	 			
	 			Calendar count_cal = Calendar.getInstance();
		    	count_cal.setTime(new Date(histogram_timelist[0]));
		    	count_cal.set(Calendar.DAY_OF_MONTH,count_cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		    	count_cal.set(Calendar.HOUR_OF_DAY,23);
		    	count_cal.set(Calendar.MINUTE,59);
		    	count_cal.set(Calendar.SECOND, 59);
		    	count_cal.set(Calendar.MILLISECOND, 999);
		    	histogram_timelist[1] = count_cal.getTimeInMillis();
		    	
		    	SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				SimpleDateFormat hms = new SimpleDateFormat("MM月");
				SimpleDateFormat ym = new SimpleDateFormat("yyyy年MM月");
				
				String key=hms.format(histogram_timelist[0]);
				histogramdata.setKey(key);
				String content = ym.format(histogram_timelist[0]);
				histogramdata.setContent(content);
				histogramdata.setHistogram_startTime(ymdhms.format(histogram_timelist[0]));
				histogramdata.setHistogram_endTime(ymdhms.format(histogram_timelist[1]));
	 		}
	 		if(mon>12)//大于1年，按年来查询
	 		{
	 			condition.setHistogramtype(histogramtypeenum.year);
	 			if(!isfirst)
				{
	 				histogram_timelist[0] = condition.getHistogram_lasttime()+1;
				}
	 			
	 			Calendar count_cal = Calendar.getInstance();
		    	count_cal.setTime(new Date(histogram_timelist[0]));
		    	count_cal.set(Calendar.MONTH,11);
		    	count_cal.set(Calendar.DAY_OF_MONTH,31);
		    	count_cal.set(Calendar.HOUR_OF_DAY,23);
		    	count_cal.set(Calendar.MINUTE,59);
		    	count_cal.set(Calendar.SECOND, 59);
		    	count_cal.set(Calendar.MILLISECOND, 999);
		    	histogram_timelist[1] = count_cal.getTimeInMillis();
		    	
		    	SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		    	SimpleDateFormat hms = new SimpleDateFormat("yyyy年");
		    	
		    	String key=hms.format(histogram_timelist[0]);
				histogramdata.setKey(key);
				String content = hms.format(histogram_timelist[0]);
				histogramdata.setContent(content);
				histogramdata.setHistogram_startTime(ymdhms.format(histogram_timelist[0]));
				histogramdata.setHistogram_endTime(ymdhms.format(histogram_timelist[1]));
	 		}
	    }
		condition.setHistogram_lasttime(histogram_timelist[1]);//记录本次查询时间，供下次使用
		//#endregion
		/*
		 * 至此，我得到了本次直方图查询需要的开始时间和结束时间：histogram_timelist
		 * 还有查询定义的开始时间和结束时间：timelist
		 */
		
		if(histogram_timelist[1]>=timelist[1])
		{
			//如果本次查询时间大于条件定义的时间，这表示已经达到查询最后,则使用条件定义的结束时间
			histogram_timelist[1] = timelist[1];
			SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			histogramdata.setHistogram_endTime(ymdhms.format(histogram_timelist[1]));
			histogramdata.setSearch_isend(true);//标识查询结束，通知前台不需再次提交了
		}
		String search_script=String.format("receivetime:{%s TO %s}", histogram_timelist[0],histogram_timelist[1]);
		condition.setSearchscript(search_script+" AND ("+condition.getSearchscript()+")");
		es_dop es_dop = new es_dop();
		long datacount = es_dop.get_es_datacount("logsys_index", condition.getSearchscript());
		histogramdata.setValue(datacount);
		
		String datajson = JSONObject.fromObject(histogramdata).put("histogram_lasttime", condition.getHistogram_lasttime()).toString();
		return_resultModel result = new return_resultModel();
		result.setResult(1);
		result.setData_message(datajson);
		
		String jsonstr=JSONObject.fromObject(result).toString();
		return jsonstr;
	}
	//#endregion
	
}
