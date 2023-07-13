<template>
  <b-row class="h-100">
    <b-colxx xxs="12" md=10  class="mx-auto my-auto">
      <b-card class="auth-card" no-body>
          <div class="position-relative image-side ">
            <p class=" text-white h2">{{ $t('dashboards.magic-is-in-the-details') }}</p>
              <p class="white mb-0">Please use your e-mail to reset your password. <br>If you are not a member, please
                <router-link tag="a" to="/user/register" class="white">register</router-link>.                  
              </p>
          </div>
          <div class="form-side">
            <router-link tag="a" to="/"><span class="logo-single"/></router-link>
            <h6 class="mb-4">{{ $t('user.forgot-password')}}</h6>
            <b-form @submit.prevent="formSubmit">
              <label class="form-group has-float-label mb-4">
                <input type="email" class="form-control" v-model="email">
                <span>{{ $t('user.email') }}</span>
              </label>
              <div class="d-flex justify-content-end align-items-center">
                  <b-button type="submit" variant="primary" size="lg" class="btn-shadow">{{ $t('user.reset-password-button')}}</b-button> 
              </div>
          </b-form>
        </div>
      </b-card>
    </b-colxx>
  </b-row>
</template>
<script>
export default {
  data(){
    return{
      fullname:"",
      email:"",
      password:""
    }
  },
  methods:{
    formSubmit(){
      console.log("forgot-password")
      this.$router.push("/")
    }
  }
};
</script>
