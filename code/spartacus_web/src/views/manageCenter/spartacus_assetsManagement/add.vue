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
                <b-form-group :label="$t('layouts.assetsname')"  >
                  <b-form-input type="text" v-model="newItem.assetsname" :state="!$v.newItem.assetsname.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.char_number_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
              <b-colxx sm="6">
                 <b-form-group :label="$t('layouts.assetsip')">
                  <b-form-input type="text" v-model="newItem.assetsip" :state="!$v.newItem.assetsip.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.assetsip_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>

              <b-colxx sm="6">
                <b-form-group :label="$t('layouts.ref_node')">
                  <v-select  :options="nodes" v-model="newItem.ref_node"/>
                </b-form-group>
              </b-colxx>
              <b-colxx sm="6">
                <b-form-group :label="$t('layouts.securitydomain')">
                   <b-form-input type="text" v-model="newItem.securitydomain" :state="!$v.newItem.securitydomain.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.char_number_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
               <b-colxx sm="6">
                 <b-form-group :label="$t('layouts.securitydomain')">
                   <b-form-input type="text" v-model="newItem.securitydomain" :state="!$v.newItem.securitydomain.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.char_number_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
               <b-colxx sm="6">
                <b-form-group :label="$t('layouts.businessdomain')">
                   <b-form-input type="text" v-model="newItem.businessdomain" :state="!$v.newItem.businessdomain.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.char_number_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
               <b-colxx sm="6">
                <b-form-group :label="$t('layouts.physicalposition')">
                   <b-form-input type="text" v-model="newItem.physicalposition" :state="!$v.newItem.physicalposition.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.char_number_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
              <b-colxx sm="6">
                <b-form-group :label="$t('layouts.physicalposition')">
                   <b-form-input type="text" v-model="newItem.physicalposition" :state="!$v.newItem.physicalposition.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.char_number_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
              <b-colxx sm="6">
                <b-form-group :label="$t('layouts.remark')">
                   <b-textarea v-model="newItem.remark" :state="!$v.newItem.remark.$invalid"/>
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
import {ipRex,positiveNum} from "assets/js/my-validators.js"
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
         assetsname:"",
        assetsip:"",
        ref_node:"",
        securitydomain:"",
        businessdomain:"",
        physicalposition:"",
        remark:""
      },
      nodes: [],
    };
  },
  mixins: [validationMixin],
  validations: {
    newItem: {
      assetsname:{
        required,
        minLength: minLength(2),
      },
      assetsip:{
        required,
        ipRex
      },
      securitydomain:{          
        minLength: minLength(2),
      },
      businessdomain: {
         minLength: minLength(2),
      },
      physicalposition: {
         minLength: minLength(2),
      },
       remark: {
       minLength: minLength(2),
       }
    }
  },
  methods: {
    loadNodes(){
       this.axios({
          method: "get",
          url: "/static/json/assetsNodes.json",
          dataType: "json",
          crossDomain: true,
          cache: false
      }).then(resolve => {

       this.nodes=resolve.data.data;
      }),
      reject => {
        console.log("请求失败", reject);
      };
    },
    onValitadeFormSubmit() {
      console.log(JSON.stringify(this.newItem));
    },
    back(){
      this.$router.push("/assetsManagement")
    },
    addNewItem(){
      //console.log('adding item : ',this.newItem)
       const postobj=new URLSearchParams();
       for(const key in this.newItem){
           postobj.append(key,this.newItem[key]);        
       }
      this.axios.post("addAsset",postobj)
      .then((response) => {
        return response.data;
          })
      .then((res)=>{
        if(res.result==0){
            this.showAlert();
          }else{
            this.$router.push("/assetsManagement")
          }
        
      })
    },
    countDownChanged(dismissCountDown) {
        this.dismissCountDown = dismissCountDown
      },
      showAlert() {
        this.dismissCountDown = this.dismissSecs
      },
      mounted(){
       this.loadNodes();
      }
  },


};
</script>


