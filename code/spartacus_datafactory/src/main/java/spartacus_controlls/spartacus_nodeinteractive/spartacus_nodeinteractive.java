package spartacus_controlls.spartacus_nodeinteractive;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONObject;
import spartacus_controlls.spartacus_nodeinteractive.logic.interactive_logic;
import spartacus_public.entity.return_resultModel;

@RestController
@CrossOrigin
public class spartacus_nodeinteractive 
{
	//#region 服务中心通信接口
	/**
	* @author Song
	* @category 服务中心通信接口，接收服务中心发送的的命令求情并处理
	* @serial		【2020年9月1日】	建立函数
	* @param command 	服务中心发送来的命令信息
	* @return		return_resultModel 的 json
	*/
	@RequestMapping(value="/interactive",method = RequestMethod.POST)
	private String interactive(HttpServletRequest request,HttpServletResponse response) 
	{
		String command = request.getParameter("command"); //获取请求命令
		interactive_logic logic = new interactive_logic();
		return_resultModel result = logic.execute(command);
		String jsonstr = JSONObject.fromObject(result).toString();
		return jsonstr;
	}
	//#endregion
}
