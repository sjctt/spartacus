import axios from 'axios'
import des from 'assets/js/js-des/tripledes.js';

/**
 * 用户登录认证
 * @param {*} account 用户名
 * @param {*} password 密码
 * @param {*} router 用于控制页面跳转
 * @param {*} notify 用于控制错误提示框
 * 2019年7月23日    增加了des加密，以防止文明传输时被截取
 */
var verify = function(account,password,router,notify)
{
    //对密码进行des加密
    des.des_encryp(password,function(val){
        password = val;
    });
    const params = new URLSearchParams();
    params.append('account',account);
    params.append('password',password);
    //调用后台服务
    axios.post('/verify',params).then((res)=>{
    if(res.data.result == 1)
    {
        //认证通过
        var loginUser_inf = {"account":'"'+account+'"',"token":'"'+res.data.data_message+'"'};
        sessionStorage.setItem("loginuser_inf",JSON.stringify(loginUser_inf));
        router.push('/search');
    }
    else
    {
        //认证失败
        notify('error', '登录失败', res.data.data_message, { duration: 3000, permanent: false });
    }
    }).catch((err)=>{
        //出现异常
        notify('error', '登录失败', res.data.data_message, { duration: 3000, permanent: false });
    })
}
export default
{
    verify
}