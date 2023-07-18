package spartacus_controlls.spartacus_nodeinteractive;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import spartacus_controlls.spartacus_nodeinteractive.bll.nodeinteractive_bll;

/**
* @author Song
* @category 子节点与服务中心信息交互模块
*					子节点消息分为两类，<通知消息>及<请求消息>
*						<通知消息>用于子节点发现新资产、发送子节点状态信息
*						<请求消息>用于子节点向服务中心发送请求，获取需要的信息。如：授权信息获取
*						通知消息格式：N>json
*						请求消息格式：R>command
*						命令集详见《概要设计文档》
*
*
* @serial
*【2019年9月12日】	建立对象
*/
@RestController
@CrossOrigin
public class spartacus_nodeinteractive 
{
	@RequestMapping(value="/interactive",method = RequestMethod.POST)
	private String interactive(HttpServletRequest request,HttpServletResponse response)
	{
		String command = request.getParameter("command");//获取子节点发送的消息内容
		System.out.println(command);
		nodeinteractive_bll bll = new nodeinteractive_bll();
		String resultjson = bll.execute(command);
		System.out.println(resultjson);
		return resultjson;
	}
}
