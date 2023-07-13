<template>
<div>
  <b-row>
    <b-colxx xxs="12">
      <piaf-breadcrumb :heading="$t('menu.forms')"/>
      <div class="separator mb-5"></div>
    </b-colxx>
  </b-row>
 <b-row>
    <b-colxx xxs="12">
        <b-card class="mb-4" :title="$t('forms.validation')">
          <b-form @submit.prevent="onValitadeFormSubmit">
            <b-row>

              <b-colxx sm="6">
                <b-form-group :label="$t('layouts.account')"  >
                  <b-form-input type="text" v-model="selectedItem.account" :state="!$v.selectedItem.account.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.char_number_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
              <b-colxx sm="6">
                <b-form-group :label="$t('layouts.password')">
                  <b-form-input type="password" v-model="selectedItem.password" :state="!$v.selectedItem.password.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.password_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>

              <b-colxx sm="6">
                <b-form-group :label="$t('layouts.email')">
                  <b-form-input type="text" v-model="selectedItem.email" :state="!$v.selectedItem.email.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.email_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
              <b-colxx sm="6">
                <b-form-group :label="$t('layouts.jobNumber')">
                   <b-form-input type="number" v-model="selectedItem.jobNumber" :state="!$v.selectedItem.jobNumber.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.jobNumber_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
               <b-colxx sm="6">
                <b-form-group :label="$t('layouts.phoneNumber')">
                   <b-form-input type="number" v-model="selectedItem.phoneNumber" :state="!$v.selectedItem.phoneNumber.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.phoneNumber_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
               <b-colxx sm="6">
                <b-form-group :label="$t('layouts.name')">
                   <b-form-input type="text" v-model="selectedItem.name" :state="!$v.selectedItem.name.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.char_number_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
            </b-row>  
             <b-alert :show="dismissCountDown"
                      dismissible
                      variant="danger"
                      @dismissed="dismissCountDown=0"
                      @dismiss-count-down="countDownChanged">{{ $t('layouts.submit_error')}}</b-alert>       
            <b-button type="submit" variant="primary" class="mt-4" :disabled="$v.selectedItem.$invalid" @click="editItem()">{{ $t('layouts.submit') }}</b-button> 
            <b-button type="button"  class="mt-4" @click="back();">{{ $t('layouts.cancel') }}</b-button>
          </b-form>
        </b-card>
    </b-colxx>
  </b-row>
  </div>
</template>
<script>
import des from 'assets/js/js-des/tripledes.js';
import InputTag from "components/Form/InputTag";
import vSelect from "vue-select";
import { validationMixin } from "vuelidate";
import {passwordRex,positiveNum,phoneNumRex} from "assets/js/my-validators.js"
const { required, minLength, between,email } = require("vuelidate/lib/validators")
export default {
  components: {
    InputTag,
    vSelect
  },
  data() {
    return {
       dismissSecs: 5,
        dismissCountDown: 0,
      selectedItem:{
        account:"",
        password:"",
        name:"",
        phoneNumber:"",
        jobNumber:"",
        email:""
      },
    };
  },
  mixins: [validationMixin],
  validations: {
    selectedItem: {
      account:{
        required,
        minLength: minLength(2),
      },
      password:{
        required,
        passwordRex
      },
      name:{          
        minLength: minLength(2),
      },
      phoneNumber: {
        phoneNumRex
      },
      jobNumber: {
        positiveNum
      },
       email: {
        email
       }
    }
  },
  methods: {
    onValitadeFormSubmit() {
      console.log(JSON.stringify(this.selectedItem));
    },
    back(){
      this.$router.push("/userAccount")
    },
    loadItems(){
      //查询用户数据---在页面加载时调用（mounted方法中调用）
      const postobj=new URLSearchParams();
      postobj.append('id',this.$route.query.id);
      this.axios.post("selectAccount",postobj)
      .then((response) => {
        return response.data;
          })
      .then((res)=>{
        var password='';
        des.encryp(res[0].password,function(val){
          res[0].password = val;
        });
        this.selectedItem=res[0];
           
      })
    },
     editItem(){
      const postobj=new URLSearchParams();
      //alert(JSON.stringify(this.selectedItem))
       for(var key in this.selectedItem){
          //alert(key+this.selectedItem[key]);
         if(key=='passowrd') {
            postobj.append(key, des.des_encryp(this.selectedItem[key],function(val){
              this.selectedItem[key]= val;
            }));
         }else{
            postobj.append(key,this.selectedItem[key]);
         }
        
       }
      
      this.axios.post("editAccount",postobj)
      .then((response) => {
        return response;
          })
      .then((res)=>{
          if(res.result==0){
            this.showAlert();
          }else{
            this.$router.push("/userAccount")
          }
      })
    },
     countDownChanged(dismissCountDown) {
        this.dismissCountDown = dismissCountDown
      },
      showAlert() {
        this.dismissCountDown = this.dismissSecs
      }
  },
  mounted(){
     this.loadItems();
  }
};
</script>


