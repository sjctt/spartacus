package spartacus.controlls.LogAudit;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import spartacus.controlls.LogAudit.entity.searchconditionentity;
import spartacus.controlls.LogAudit.logic.datasearch_logic;


/**
 * @author Song
 * @category 数据查询模块控制器
 * @serial
 *【2019年10月16日】	建立对象
 *【2019年10月23日】	添加了查询数据总量的接口
 *【2019年10月23日】	添加了查询当页数据接口
 *【2019年10月23日】	添加了柱状图区域数据查询接口
 */
@RestController
@CrossOrigin
public class datasearch 
{
	//#region 查询数据总量
	/**
	 * @author Song
	 * @category 提供前端获取数据总量的接口
	 * @serial
	 *【2019年7月19日】	建立接口
	 *
	 *@param searchconditionentity condition 查询参数对象
	 *
	 *@return return_resultModel
	 */
	@RequestMapping(value = "/get_datacount",method = RequestMethod.POST)
	public void get_datacount(searchconditionentity condition,HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		datasearch_logic logic = new datasearch_logic();
		String jsonstr = logic.get_datacount(condition);
		
		System.out.println(jsonstr);
		PrintWriter out = null;
		response.setCharacterEncoding("UTF-8"); //设置编码格式
		response.setContentType("application/json");
		out=response.getWriter();
		out.write(jsonstr);
	}
	//#endregion
	
	//#region 查询当页数据
	/**
	 * @author Song
	 * @category 提供前端获取指定页数据的接口
	 * @serial
	 *【2019年7月19日】	建立接口
	 *
	 *@param searchconditionentity condition 查询参数对象
	 *
	 *@return return_resultModel
	 */
	@RequestMapping(value = "/get_pagedata",method = RequestMethod.POST)
	public void get_pagedata(searchconditionentity condition,HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		
		datasearch_logic logic = new datasearch_logic();
		String jsonstr = logic.get_pagedata(condition);
		
		System.out.println(jsonstr);
		PrintWriter out = null;
		response.setCharacterEncoding("UTF-8"); //设置编码格式
		response.setContentType("application/json");
		out=response.getWriter();
		out.write(jsonstr);
	}
	//#endregion
	
	//#region 柱状图区域数据查询
	/**
	 * @author Song
	 * @category 提供前端获取指定页数据的接口
	 * @serial
	 *【2019年7月19日】	建立接口
	 *
	 *@param searchconditionentity condition 查询参数对象
	 *
	 *@return return_resultModel
	 * @throws IOException 
	 */
	@RequestMapping(value = "/get_histogramdata",method = RequestMethod.POST)
	public void get_histogramdata(searchconditionentity condition,HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		datasearch_logic logic = new datasearch_logic();
		String jsonstr = logic.get_histogramdata(condition);
		//System.out.println(jsonstr);
		
		PrintWriter out = null;
		response.setCharacterEncoding("UTF-8"); //设置编码格式
		response.setContentType("application/json");
		out=response.getWriter();
		out.write(jsonstr);
	}
	//#endregion
}
