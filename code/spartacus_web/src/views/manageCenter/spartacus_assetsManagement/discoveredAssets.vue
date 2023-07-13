<template>
<b-colxx class="disable-text-selection">
  <b-row>
    <b-colxx xxs="12">
      <h1>{{ $t('menu.data-list') }}</h1>
      <div class="float-sm-right">
        <b-button @click='openAdd()'  class="mb-1" variant="secondary" >{{ $t('layouts.add-in-batches') }}</b-button>
        <b-button @click='openDelete()' class="mb-1" variant="danger" >{{ $t('layouts.delete-in-batches') }}</b-button>
        <b-button-group>
        <b-dropdown  split right @click="selectAll(true)" class="check-button" variant="primary">
            <label class="custom-control custom-checkbox pl-4 mb-0 d-inline-block" slot="button-content">
              <input class="custom-control-input" type="checkbox" 
              :checked="isSelectedAll" 
              v-shortkey="{select: ['ctrl','a'], undo: ['ctrl','d']}" @shortkey="keymap"
              />
              <span :class="{ 
                'custom-control-label' :true,
                'indeterminate' : isAnyItemSelected
              }"/>
            </label>
          <b-dropdown-item>{{$t('layouts.delete')}}</b-dropdown-item>
          <b-dropdown-item>{{$t('layouts.add-to-assets')}}</b-dropdown-item>
        </b-dropdown>
        </b-button-group> 
      </div>
      <piaf-breadcrumb/>
      <div class="mb-2 mt-2">
        <b-button variant="empty" class="pt-0 pl-0 d-inline-block d-md-none" v-b-toggle.displayOptions>
          {{ $t('layouts.display-options') }} <i class="simple-icon-arrow-down align-middle" />
        </b-button>
        <b-collapse id="displayOptions" class="d-md-block">
          <span class="mr-3 mb-2 d-inline-block float-md-left">
            <a :class="{'mr-2 view-icon':true,'active': displayMode==='list'}" @click="changeDisplayMode('list')"><data-list-icon/></a>
            <a :class="{'mr-2 view-icon':true,'active': displayMode==='thumb'}" @click="changeDisplayMode('thumb')"><thumb-list-icon/></a>
            <a :class="{'mr-2 view-icon':true,'active': displayMode==='image'}" @click="changeDisplayMode('image')"><image-list-icon/></a>
          </span>
          <div class="d-block d-md-inline-block mb-2">
            <!-- <b-dropdown id="ddown1" :text="`${$t('layouts.orderby')} ${sort.label}`" variant="outline-dark" class="mr-1 float-md-left btn-group " size="xs">                
                <b-dropdown-item v-for="(order,index) in sortOptions" :key="index" @click="changeOrderBy(order)" >{{ order.label }}</b-dropdown-item>
            </b-dropdown> -->

            <!-- <div class="search-sm d-inline-block float-md-left mr-1 align-top">
              <b-input :placeholder="$t('menu.search')" v-model="search"/>
            </div> -->
          </div>
          <div class="float-md-right">
            <span class="text-muted text-small mr-1 mb-2">{{from}}-{{to}} of {{ total }}</span>
            <b-dropdown id="ddown2" right :text="`${perPage}`" variant="outline-dark" class="d-inline-block" size="xs">                
                <b-dropdown-item v-for="(size,index) in pageSizes" :key="index" @click="changePageSize(size)" >{{ size }}</b-dropdown-item>
            </b-dropdown>
          </div>
        </b-collapse>
      </div>
      <div class="separator mb-5"/>
    </b-colxx>
  </b-row>
  <template v-if="isLoad">
      <b-alert :show="dismissCountDown"
                      dismissible
                      variant="danger"
                      @dismissed="dismissCountDown=0"
                      @dismiss-count-down="countDownChanged">{{ $t('dashboards.status_update_error')}}</b-alert> 
  <b-row v-if="displayMode==='image'" key="image">
    
      <b-colxx  sm="6" lg="4" xl="3" class="mb-3" v-for="(item,index) in items"   :key="index"  :id="item.id">
        <image-list-item 
          :key="item.idspartacus_assets"
          :data="item" 
          :selected-items.sync="selectedItems"
          @toggle-item="toggleItem"
          v-contextmenu:contextmenu
        />
      </b-colxx>
    </b-row>
    <b-row v-else-if="displayMode==='thumb'" key="thumb">
      
      <b-colxx xxs="12" class="mb-3" v-for="(item,index) in items"   :key="index"  :id="item.id">
        <thumb-list-item 
          :key="item.id"
          :data="item" 
          :selected-items.sync="selectedItems"
          @toggle-item="toggleItem"
          v-contextmenu:contextmenu
        />
      </b-colxx>
    </b-row>
      <b-row v-else-if="displayMode==='list'" key="list">
        
      <b-colxx xxs="12" class="mb-3" v-for="(item,index) in items"   :key="index"  :id="item.id">
        <data-list-item 
          :key="item.id"
          :data="item" 
          :selected-items="selectedItems"
          @toggle-item="toggleItem"
          
          :linkgen="linkGen"
          v-contextmenu:contextmenu
        />
      </b-colxx>
    </b-row>
    <b-row v-if="lastPage>0">
      <b-colxx xxs="12">
        <b-pagination 
          :total-rows="total"
          v-model="page" 
          :per-page="perPage"
           first-text="First"
      prev-text="Prev"
      next-text="Next"
      last-text="Last"
          align="center"
           class="mt-4"
        />
      </b-colxx>
    </b-row>
   
    </template>
    <template v-else>
      <div class="loading"></div>
    </template>
     <b-modal id="delModal" :title="'Tips'"  @ok="onContextDelete">确定删除选中项？</b-modal>
    <v-contextmenu ref="contextmenu" @contextmenu="handleContextmenu">
      <v-contextmenu-item @click="openAdd()" ><i class="simple-icon-pencil"  /> <span>添加到资产</span></v-contextmenu-item>
      <!-- <v-contextmenu-item @click="onContextArchive()"><i class="simple-icon-drawer" /> <span>Move to archive</span></v-contextmenu-item> -->
      <v-contextmenu-item  v-b-modal.delModal  ><i class="simple-icon-trash" /> <span>删除</span></v-contextmenu-item>
    </v-contextmenu>
    </b-colxx>    
  </template>
<script>
import { DataListIcon, ThumbListIcon, ImageListIcon} from 'components/Svg'
import InputTag from "components/Form/InputTag"
import vSelect from "vue-select"
import { validationMixin } from "vuelidate"
const { required, minLength, maxLength,between } = require("vuelidate/lib/validators")


import ImageListItem from 'components/Listing/ImageListItem'
import ThumbListItem from 'components/Listing/ThumbListItem'
import DataListItem from 'components/Listing/DataListItem'
import { apiUrl } from "constants/config"



export default {
  components:{
    DataListIcon,
    ThumbListIcon, 
    ImageListIcon,
    InputTag,
    vSelect,
    ImageListItem,
    ThumbListItem,
    DataListItem,
  },
  data(){
   return{
      dismissSecs: 5,
      dismissCountDown: 0,
      isLoad:false,
     // apiBase :apiUrl+"/cakes/fordatatable",
     // apiBase :"",
      displayMode:'list',
      sort:{column: "assetsname",label: "资产名称"},
      sortOptions:[
        {column: "assetsip",label: "IP"},
        {column: "createTime",label: "创建时间"},
        {column: "ref_node",label: "所属节点"},
        {column: "physicalposition",label: "物理位置"}
      ],
       
      page:1,
      perPage:10,
      search:"",
      from:0,
      to:0,
      total:0,
      lastPage:0,
      items:[],
      pageSizes: [5, 10, 15],
      selectedItems:[],
      
   }
  },
  methods:{
    loadItems(){
      //查询用户数据---在页面加载时调用（mounted方法中调用）
      this.isLoad=false;
      const postobj=new URLSearchParams();
         postobj.append("NowPage",this.page);
         postobj.append("ShowCount",this.perPage);
         // postobj.append("search",this.searchKeyword);
      //this.axios.post(this.apiUrl,postobj)
      // this.axios.get("/static/json/assetsManagement.json")
      // .then((response) => {
      //   return response.data;
      //     })
      // .then((res)=>{
      //   this.items=res;
      //   this.isLoad=true;
      // })
      this.axios({
          method: "get",
          url: "/static/json/assetsManagement.json",
          dataType: "json",
          crossDomain: true,
          cache: false
      }).then(resolve => {

       this.items=resolve.data.data;
         this.isLoad=true;
      }),
      reject => {
        console.log("请求失败", reject);
      };
    },
    loadPages(){
     //查询分页信息
      this.axios.post("selectAssetsCount")
      .then((response) => {
        return response.data;
          })
      .then((res)=>{
        this.total=res.result;
        this.perPage = 10;
        this.lastPage=Math.ceil(res.result/ this.perPage);
      })
    },
    hideModal(refname){
      this.$refs[refname].hide()
    },
    changeDisplayMode(displayType){
      this.displayMode=displayType;
    },
    changePageSize(perPage){
      this.perPage=perPage;
    },
    linkGen(pageNum) {
        return pageNum === 1 ? '?' : `?page=${pageNum}`
      },
    changeOrderBy(sort){
      this.sort=sort;
    },
    //批量删除
    openDelete(){

    },
    //添加到资产（批量和单个的都用这个方法）
    openAdd(){
     // this.$router.push("/assetsManagement/asset_add")
    },
    selectAll(isToggle){
      if(this.selectedItems.length>= this.items.length)
      {
        if(isToggle)
          this.selectedItems=[]
      }else{
        this.selectedItems=this.items.map(x => x.id)
      }
    },
    keymap (event) {
      switch (event.srcKey) {
        case 'select':
          this.selectAll(false)
          break;
        case 'undo':
          this.selectedItems=[]
        break;
      }
    },
    getIndex(value, arr, prop) {
      for (var i = 0; i < arr.length; i++) {
        if (arr[i][prop] === value) {
          return i;
        }
      }
      return -1;
    },
    toggleItem(event,itemId){
     // alert(itemId)
      if(event.shiftKey && this.selectedItems.length>0)
      {
        let itemsForToggle=this.items;
        var start = this.getIndex(itemId, itemsForToggle, "idspartacus_assets");
        var end = this.getIndex(this.selectedItems[this.selectedItems.length-1], itemsForToggle, "idspartacus_assets");
        itemsForToggle=itemsForToggle.slice(Math.min(start, end), Math.max(start, end) + 1);
        this.selectedItems.push(
          ...itemsForToggle.map(item => {
            return item.idspartacus_assets;
          })
        )
      }else{
        if(this.selectedItems.includes(itemId)){
          this.selectedItems=this.selectedItems.filter(x=>x!=itemId)
        }else
          this.selectedItems.push(itemId)
        }
    },
    countDownChanged(dismissCountDown) {
        this.dismissCountDown = dismissCountDown
      },
      showAlert() {
        this.dismissCountDown = this.dismissSecs
      },
    handleContextmenu(vnode){
     //alert(JSON.stringify(vnode));
      if(!this.selectedItems.includes(vnode.key)){
        this.selectedItems=[vnode.key]
      }
    },
    
    // onContextArchive(){
    //   console.log("context menu item clicked - Move to Archive Items: ",this.selectedItems)
    // },
    onContextDelete(){
      const postobj=new URLSearchParams();
      postobj.append("id",this.selectedItems[0]);   
         
      this.axios.post("deleteAsset",postobj)
      .then((response) => {
        return response.data;
          })
      .then((res)=>{
        //this.items.push(res);
        alert('删除成功');
      })
      
    }
  },
  computed:{
    isSelectedAll(){
      return  this.selectedItems.length >= this.items.length;
    },
    isAnyItemSelected(){
      return  this.selectedItems.length > 0 && this.selectedItems.length < this.items.length;
    },
    apiUrl(){
     // return  `${this.apiBase}?sort=${this.sort.column}&page=${this.page}&per_page=${this.perPage}&search=${this.search}`
     return  `selectAssets?page=${this.page}&per_page=${this.perPage}&search=${this.searchKeyword}`
    },
  },
  watch:{
    search(){
      this.page=1;
    },
    apiUrl(){
      this.loadItems();
    }
  },
  mounted(){
    this.loadItems();
  //  this.loadPages();
  }
};
</script>
