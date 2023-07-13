<template>
    <div class="login_container">
        <div class="right"></div>
        <div class="left">
            <div class="background"></div>
                <div class="header">
                    <h2 class="animation a1">Spartacus 数据智能分析系统 beta v 1.0.0
                    </h2>
                    <h4 class="animation a2">您可在下方登录您的账户</h4>
                </div>
                <form  :model="loginForm"   ref="loginForm" class="form" >
                    <input type="text" v-model="loginForm.account" class="form-field animation a3" placeholder="邮箱账号">
                    <input type="password"  v-model="loginForm.password" class="form-field animation a4" placeholder="密码">
                    <p/>
                    <button class="animation a6 animation.active" @click="login();" type="button">登录</button>
                </form>         
        </div>
    </div>
</template>
<script>
    import loginJS from 'views/Login/js/login.js';
    export default {
      name:'login',
      data(){
      return { 
        loginForm: {
            account: '',
            password: '',
        },
      }
    },
    methods:
    {
        login()
        {
           // loginJS.verify(this.loginForm.account,this.loginForm.password,this.$router,this.$notify);
             var loginUser_inf = {"account":'"'+this.loginForm.account+'"',"token":'"'+"111"+'"'};
            sessionStorage.setItem("loginuser_inf",JSON.stringify(loginUser_inf));
            this.$router.push('/search');
        },
     },
     watch: {

    },
    }
</script>
<style scoped>
   @import "css/login.css";
   /* @import "@css/login.css"; */
</style>