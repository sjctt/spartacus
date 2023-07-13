<template>
  <div>
    <b-row>
      <b-colxx xxs="12">
        <piaf-breadcrumb :heading="$t('menu.content')"/>
        <div class="separator mb-5"></div>
      </b-colxx>
    </b-row>
    <b-row>
      <b-colxx xl="6" lg="12">
        <div class="icon-cards-row">
          <swiper :options="swiperIconsOption" ref="swiperIcons" >
            <swiper-slide>
              <icon-card :title="$t('dashboards.pending-orders')" icon="iconsmind-Alarm" :value=14 />
            </swiper-slide>
            <swiper-slide>
              <icon-card :title="$t('dashboards.completed-orders')" icon="iconsmind-Basket-Coins" :value=32 />
            </swiper-slide>
            <swiper-slide>
              <icon-card :title="$t('dashboards.refund-requests')" icon="iconsmind-Arrow-Refresh" :value=74 />
            </swiper-slide>
            <swiper-slide>
              <icon-card :title="$t('dashboards.new-comments')" icon="iconsmind-Mail-Read" :value=25 />
            </swiper-slide>
          </swiper>
          <resize-observer @notify="handleSwipersResize"/>
          </div>
          <b-row>
            <b-colxx md="12">
             <b-card class="mb-4 dashboard-quick-post" :title="$t('dashboards.quick-post')">
               <b-refresh-button @click="refreshButtonClick"/>
              <b-form @submit.prevent="quickPostSubmit">
                <b-form-group horizontal label-cols="3" breakpoint="sm" :label="$t('dashboards.title')">
                  <b-form-input v-model="quickPost.title" :placeholder="$t('dashboards.title')"></b-form-input>
                </b-form-group>
                <b-form-group horizontal label-cols="3" breakpoint="sm" :label="$t('dashboards.content')">
                  <b-textarea  v-model="quickPost.content" :placeholder="$t('dashboards.content')" :rows="4" :max-rows="4"/>
                </b-form-group>
                <b-form-group horizontal label-cols="3" breakpoint="sm" :label="$t('dashboards.category')">
                    <v-select  :options="selectData"/>
                </b-form-group>
                <b-button type="submit" variant="primary" class="float-right">{{ $t('dashboards.save-and-publish') }}</b-button> 
              </b-form>
            </b-card>
            </b-colxx>
          </b-row>
      </b-colxx>

      <b-colxx lg="12" xl="6">
        <b-card :title="$t('dashboards.top-viewed-posts')">
           <vuetable ref="vuetable"
            :api-url="bestsellers.apiUrl"
            :fields="bestsellers.fields"
            :per-page="6"
            pagination-path=""
            @vuetable:pagination-data="onPaginationData"
          ></vuetable>
          <vuetable-pagination-bootstrap ref="pagination"
            @vuetable-pagination:change-page="onChangePage"
          ></vuetable-pagination-bootstrap>
        </b-card>
      </b-colxx>
    </b-row>


    <b-row>
      <b-colxx lg="4" md="6" class="mb-4">
       <b-card :title="$t('dashboards.cakes')" class="dashboard-link-list">
           <two-column-list :data="cakes"/>
        </b-card>
      </b-colxx>

      <b-colxx lg="8" md="12" class="mb-4">
        <b-card :title="$t('dashboards.new-comments')" class="dashboard-link-list">
           <vue-perfect-scrollbar class="scroll dashboard-list-with-user" :settings="{ suppressScrollX: true, wheelPropagation: false }">
              <list-with-user-item v-for="(item, index) in comments" :data="item" detail-path="/app/layouts/details" :key="index"/>
            </vue-perfect-scrollbar>
        </b-card>
      </b-colxx>
    </b-row>

  <b-row>
    <b-colxx sm="12" md="6" class="mb-4">
      <b-card class="dashboard-filled-line-chart" no-body>
        <b-card-body>
          <div class="float-left float-none-xs">
          <div class="d-inline-block">
            <h5 class="d-inline">{{ $t('dashboards.website-visits') }}</h5>
            <span class="text-muted text-small d-block">{{ $t('dashboards.unique-visitors') }}</span>
          </div>
        </div>
          <b-dropdown id="ddown5" :text="$t('dashboards.this-week')" size="xs" variant="outline-primary" class="float-right float-none-xs mt-2">
              <b-dropdown-item>{{ $t('dashboards.last-week') }}</b-dropdown-item>
              <b-dropdown-item>{{ $t('dashboards.this-month') }}</b-dropdown-item>
          </b-dropdown>
        </b-card-body>
        <div class="chart card-body pt-0">
          <area-shadow-chart :data="areaChartData" :height="195"/>
        </div>
      </b-card>
    </b-colxx>
    <b-colxx sm="12" md="6" class="mb-4">
      <b-card class="dashboard-filled-line-chart" no-body>
        <b-card-body>
          <div class="float-left float-none-xs">
          <div class="d-inline-block">
            <h5 class="d-inline">{{ $t('dashboards.conversion-rates') }}</h5>
            <span class="text-muted text-small d-block">{{ $t('dashboards.per-session') }}</span>
          </div>
        </div>
          <b-dropdown id="ddown5" :text="$t('dashboards.this-week')" size="xs" variant="outline-secondary" class="float-right float-none-xs mt-2">
              <b-dropdown-item>{{ $t('dashboards.last-week') }}</b-dropdown-item>
              <b-dropdown-item>{{ $t('dashboards.this-month') }}</b-dropdown-item>
          </b-dropdown>
        </b-card-body>
        <div class="chart card-body pt-0">
          <area-shadow-chart :data="conversionChartData" :height="195"/>
        </div>
      </b-card>
    </b-colxx>
  </b-row>
  <b-row>
    <b-colxx lg="4" class="mb-4">
      <gradient-with-radial-progress-card
        icon="iconsmind-Alarm"
        :title="`5 ${$t('dashboards.posts')}`"
        :detail="$t('dashboards.pending-for-publish')"
        :percent="5*100/12"
        progressText="5/12"
        />
      </b-colxx>
      <b-colxx lg="4" class="mb-4">
      <gradient-with-radial-progress-card
        icon="iconsmind-Male"
        :title="`4 ${$t('dashboards.users')}`"
        :detail="$t('dashboards.on-approval-process')"
        :percent="4*100/6"
        progressText="4/6"
        />
      </b-colxx>
      <b-colxx lg="4" class="mb-4">
      <gradient-with-radial-progress-card
        icon="iconsmind-Bell-2"
        :title="`8 ${$t('dashboards.alerts')}`"
        :detail="$t('dashboards.waiting-for-notice')"
        :percent="8*100/10"
        progressText="8/10"
        />
      </b-colxx>
  </b-row>
   

  </div>
</template>
<script>
import { ResizeObserver } from 'vue-resize'

import { swiper, swiperSlide } from 'vue-awesome-swiper'
import 'swiper/dist/css/swiper.css'

import vSelect from "vue-select";

import Vuetable from 'vuetable-2/src/components/Vuetable'
import VuetablePaginationBootstrap from 'components/Common/VuetablePaginationBootstrap'

import IconCard from 'components/Cards/IconCard'
import GradientWithRadialProgressCard from 'components/Cards/GradientWithRadialProgressCard'
import AreaShadowChart from "components/Charts/AreaShadow";
import TwoColumnList from 'components/Listing/TwoColumnList';
import ListWithUserItem from 'components/Listing/ListWithUserItem';

import {areaChartData,conversionChartData} from "data/charts"
import cakes from "data/cakes"
import comments from "data/comments"

import { apiUrl } from "constants/config";

export default  {
  components: {
    IconCard,
    swiper,
    swiperSlide,
    ResizeObserver,
    vSelect,
    Vuetable,
    VuetablePaginationBootstrap,
    TwoColumnList,
    ListWithUserItem,
    AreaShadowChart,
    GradientWithRadialProgressCard
  },
  data() {
      return {
        swiperIconsOption: {
          slidesPerView: 4,
          breakpoints: {
             0: {slidesPerView:1},
            320: {slidesPerView:1},
            576: {slidesPerView:2},
            1800: {slidesPerView:3},
          }
        },
        selectData: [
          { label: "Chocolate", value: "chocolate" },
          { label: "Vanilla", value: "vanilla" },
          { label: "Strawberry", value: "strawberry" },
          { label: "Caramel", value: "caramel" },
          { label: "Cookies and Cream", value: "cookiescream" },
          { label: "Peppermint", value: "peppermint" }
        ],
        quickPost:{
          title:'',
          content:'',
          category:''
        },
        areaChartData,
        conversionChartData,
        cakes,
        comments,
        bestsellers:{
          apiUrl:apiUrl+"/cakes/fordatatable",
          fields:[{
              name:'title',
              sortField: 'title',
              title:'Name',
              titleClass:'',
              dataClass:'list-item-heading'
            },{
              name:'sales',
              sortField: 'sales',
              title:'Sales',
              titleClass:'',
              dataClass:'text-muted'
            },{
              name:'stock',
              sortField: 'stock',
              title:'Stock',
              titleClass:'',
              dataClass:'text-muted'
            },{
              name:'category',
              sortField: 'category',
              title:'Category',
              titleClass:'',
              dataClass:'text-muted'
            }]
        },
      }
    },
    mounted(){
      setTimeout(()=>{
        this.handleSwipersResize()
      },50)
    },
    methods: {
      refreshButtonClick(){
        console.log("refreshButtonClick")
      },
      handleSwipersResize () {
        this.$refs.swiperIcons.update()
      },
      onPaginationData (paginationData) {
        this.$refs.pagination.setPaginationData(paginationData)
      },
      onChangePage (page) {
        this.$refs.vuetable.changePage(page)
      },
      quickPostSubmit(){
        console.log(this.quickPost)
      }
    },
};
</script>
