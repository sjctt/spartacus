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
                  <b-form-input type="text" v-model="selectedItem.assetsname" :state="!$v.selectedItem.assetsname.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.char_number_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
              <b-colxx sm="6">
                <b-form-group :label="$t('layouts.assetsip')">
                  <b-form-input type="text" v-model="selectedItem.assetsip" :state="!$v.selectedItem.assetsip.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.assetsip_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>

              <b-colxx sm="6">
                <b-form-group :label="$t('layouts.ref_node')">
                  <v-select  :options="nodes" v-model="selectedItem.ref_node"/>
                  <b-form-invalid-feedback>{{ $t('layouts.ref_node_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
              <b-colxx sm="6">
                <b-form-group :label="$t('layouts.securitydomain')">
                   <b-form-input type="text" v-model="selectedItem.securitydomain" :state="!$v.selectedItem.securitydomain.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.char_number_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
               <b-colxx sm="6">
                <b-form-group :label="$t('layouts.businessdomain')">
                   <b-form-input type="text" v-model="selectedItem.businessdomain" :state="!$v.selectedItem.businessdomain.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.char_number_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
               <b-colxx sm="6">
                <b-form-group :label="$t('layouts.physicalposition')">
                   <b-form-input type="text" v-model="selectedItem.physicalposition" :state="!$v.selectedItem.physicalposition.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('layouts.char_number_error_message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
               <b-colxx sm="6">
                <b-form-group :label="$t('layouts.remark')">
                   <b-textarea v-model="selectedItem.remark" :state="!$v.selectedItem.remark.$invalid"/>
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
      selectedItem:{
         assetsname:"",
        assetsip:"",
        ref_node:"",
        securitydomain:"",
        businessdomain:"",
        physicalposition:"",
        remark:""
      },
      nodes:  [],
    };
  },
  mixins: [validationMixin],
  validations: {
    selectedItem: {
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
    //获取节点数据用于下拉框
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
      console.log(JSON.stringify(this.selectedItem));
    },
    back(){
      this.$router.push("/assetsManagement")
    },
    loadItems(){
      //查询用户数据---在页面加载时调用（mounted方法中调用）
      const postobj=new URLSearchParams();
      postobj.append('id',this.$route.query.id);
      this.axios.post("selectAssets",postobj)
      .then((response) => {
        return response.data;
          })
      .then((res)=>{
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
      
      this.axios.post("editAsset",postobj)
      .then((response) => {
        return response;
          })
      .then((res)=>{
          if(res.result==0){
            this.showAlert();
          }else{
            this.$router.push("/assetsMangement")
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
    this.loadNodes();
    this.loadItems();
  }
};
</script>


