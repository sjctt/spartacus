package spartacus_servicecenter;

import com.data.operate.redis_dop;

/**
 * @author Song
 * @category
 * @serial
 *【2019年9月25日】	建立对象
 */
public class testredis
{
	public static void main(String[] args) 
	{
		redis_dop rdop = new redis_dop();
		rdop.Initialization();
		boolean result = rdop.redis_contains_string("spartacus_assets", "\"assetsip\":\"10.0.0.7\"");
		System.out.println(result);
	}
}
