import {store} from '../store'

export default (to, from, next) => {
  var loginuser_inf = sessionStorage.getItem("loginuser_inf");
  if(loginuser_inf!=null&&loginuser_inf.length>0)
  {
    next();
  }
  else
  {
    localStorage.removeItem("loginuser_inf")
    next('/login');
  }
}