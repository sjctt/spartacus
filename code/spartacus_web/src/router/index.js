import Vue from "vue";
import Router from "vue-router";
import AuthRequired from './authRequired'
Vue.use(Router);
import Error from "pages/Error.vue";

import manageCenter from "views/manageCenter";

import spartacus_datasearchManagement from "views/manageCenter/spartacus_datasearch"
import datasearchManagement from "views/manageCenter/spartacus_datasearch/datasearch"

import login from "views/Login/Login";


const routes = [
  {
    path:"/",
    component:manageCenter,
    redirect:"/search",
    beforeEnter:AuthRequired,
    children:[
      {
        path:"/search",
        component:spartacus_datasearchManagement,
        redirect:"/search",
        children:[
          {
            path:"/search",
            component:datasearchManagement,
          },
        ]
      },
    ]
  },
  //管理中心路由 end

  //错误界面路由
  {path: "/error",component: Error},
  { path:"*",component: Error},
  //错误界面路由 end

  //登录界面路由
  {
    path:"/login",
    component:login
  },
  //登录界面路由 end
];

const router =new Router({
  linkActiveClass: "active",
  routes,
  mode: "history"
});

export default router
