package spartacus.controlls;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

/**
 * @author Song
 * @category web过滤器 主要解决vue与springboot跨域导致的session丢失问题
 * @serial
 *【2020年8月27日】	建立对象
 */
@Component
public class CorsFilter implements Filter
{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		HttpServletResponse res = (HttpServletResponse) response;
		//res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:9000");
		//res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		//res.setHeader("Access-Control-Max-Age", "3600");
		//res.setHeader("Access-Control-Allow-Headers", "Content-type");
		//res.setHeader("Access-Control-Allow-Credentials","true");
        chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
