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
                  <b-form-input type="text" v-model="newItem.account" :state="!$v.newItem.account.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.char_number_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
              <b-colxx sm="6">
                <b-form-group :label="$t('layouts.password')">
                  <b-form-input type="password" v-model="newItem.password" :state="!$v.newItem.password.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.password_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>

              <b-colxx sm="6">
                <b-form-group :label="$t('layouts.email')">
                  <b-form-input type="text" v-model="newItem.email" :state="!$v.newItem.email.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.email_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
              <b-colxx sm="6">
                <b-form-group :label="$t('layouts.jobNumber')">
                   <b-form-input type="number" v-model="newItem.jobNumber" :state="!$v.newItem.jobNumber.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.jobNumber_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
               <b-colxx sm="6">
                <b-form-group :label="$t('layouts.phoneNumber')">
                   <b-form-input type="number" v-model="newItem.phoneNumber" :state="!$v.newItem.phoneNumber.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.phoneNumber_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
               <b-colxx sm="6">
                <b-form-group :label="$t('layouts.name')">
                   <b-form-input type="text" v-model="newItem.name" :state="!$v.newItem.name.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.char_number_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
            </b-row>  
            <b-alert :show="dismissCountDown"
                      dismissible
                      variant="danger"
                      @dismissed="dismissCountDown=0"
                      @dismiss-count-down="countDownChanged">{{ $t('layouts.submit_error')}}</b-alert>                     
            <b-button type="submit" variant="primary" class="mt-4" :disabled="$v.newItem.$invalid" @click="addNewItem()">{{ $t('layouts.submit') }}</b-button> 
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
      newItem:{
        account:"",
        password:"",
        name:"",
        phoneNumber:"",
        jobNumber:"",
        email:"",
        accountStatus:1
      },
    };
  },
  mixins: [validationMixin],
  validations: {
    newItem: {
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
      console.log(JSON.stringify(this.newItem));
    },
    back(){
      this.$router.push("/userAccount")
    },
    randomNum (){ 
        return parseInt(Math.random()*(99999999-10000000+1)+10000000,10); 
    }, 
    addNewItem(){
      //console.log('adding item : ',this.newItem)
       const postobj=new URLSearchParams();
       const key=this.randomNum();
        postobj.append('pwdkey',key );
       for(const key in this.newItem){
         
         if(key=="password"){
            var pwd=this.newItem.password;
            des.des_encryp(pwd,function(val){
              pwd = val;
            });
            postobj.append('password',pwd);
         }else{
           postobj.append(key,this.newItem[key]);
         }
         
       }
       
      this.axios.post("addAccount",postobj)
      .then((response) => {
        return response.data;
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


};
</script>


