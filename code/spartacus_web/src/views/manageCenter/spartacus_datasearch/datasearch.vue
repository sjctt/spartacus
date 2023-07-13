<template>
    <b-row @mousemove="body_mousemove" @click="body_click">
        <b-colxx class="disable-text-selection">
            <!--面包屑-->
            <b-row>
                <b-colxx xxs="12">
                <piaf-breadcrumb :heading="$t('datasearch.title')"/>
                <div class="separator mb-5"></div>
                </b-colxx>
            </b-row>
            <!--面包屑 end-->
            <!--查询控制器和柱状图板块-->
            <b-card class="mb-4">
                <!--查询控制器-->
                    <b-row>
                        <b-colxx xxs="2">
                            <v-select v-model="dateselect_param.selectd_text" :options="dateselect_param.datasource" @change="time_select_change" />
                        </b-colxx>
                        <b-colxx xxs="3"  v-if="picker_param.picker_isshow">
                            <VueCtkDateTimePicker v-model="picker_param.formtime" :format="picker_param.format" :no-label="true" :formatted="picker_param.formtime"/>
                        </b-colxx>
                        <b-colxx xxs="3" v-if="picker_param.picker_isshow">
                            <VueCtkDateTimePicker v-model="picker_param.totime" :format="picker_param.format" :no-label="true" :formatted="picker_param.totime"/>
                        </b-colxx>
                        <b-colxx>
                            <b-input-group class="mb-3">
                                <b-form-input v-model="searchscript"/>
                                <b-input-group-append>
                                    <b-button id="search_button"  variant="outline-primary" @click="search()"><i class="iconsmind-Folder-Search"></i>&nbsp;{{ $t('datasearch.button-search') }}</b-button>
                                </b-input-group-append>
                            </b-input-group>
                        </b-colxx>
                    </b-row>
                <b-row>
                    <b-colxx xxs="12">
                        已匹配到数据总量：{{page_param.data_count}}
                    </b-colxx>
                </b-row>
                <!--查询控制器 end-->
                <!--柱状图-->
                <b-row>
                    <b-colxx xxs="11">
                        <div id="Log_histogram" style="height:300px;"></div>
                    </b-colxx>
                </b-row>
                <!--柱状图 end-->
            </b-card>
            <!--查询控制器和柱状图板块 end-->

            <!--数据列表和字段统计板块-->
            <b-card class="mb-4">
                <!--字段统计板-->
                <b-colxx xxs="0">
                    <!--这里以后要放入字段统计-->
                </b-colxx>
                <!--字段统计板 end-->

                <!--数据列表板-->
                <b-colxx xxs=12>
                    <!--分页工具-->
                    <b-row>
                        <b-colxx xxs="3">
                            <b-dropdown right :text="`${page_param.perPage}`" variant="outline-dark" class="d-inline-block" size="sm">                
                                <b-dropdown-item name="pagecount" v-for="(count,index) in page_param.datasource" :key="index" @click="changePagecount(count)" >{{ count }}</b-dropdown-item>
                            </b-dropdown>
                            <span class="text-muted text-small mr-1 mb-2">&nbsp;&nbsp;每页显示条目</span>
                        </b-colxx>
                        <b-colxx xxs="9">
                            <b-pagination-nav size="sm" align="right" 
                                :number-of-pages="page_param.pagesum"
                                :link-gen="fun" 
                                v-model="page_param.currentPage" 
                                :per-page="5"
                                @change="changepage"
                            >
                                <template v-slot:next-text>
                                    <i class="simple-icon-arrow-right"/>
                                </template>
                                <template v-slot:prev-text>
                                    <i class="simple-icon-arrow-left"/>
                                </template>
                                <template v-slot:first-text>
                                    <i class="simple-icon-control-start"/>
                                </template>
                                <template v-slot:last-text>
                                    <i class="simple-icon-control-end"/>
                                </template>
                            </b-pagination-nav>
                        </b-colxx>
                    </b-row>
                    <!--分页工具 end-->
                    <b-row>
                        <!--数据列表  testdetailrow-->
                        <b-colxx xxs="12">
                            <vuetable
                                class="vuetable"
                                ref="vuetable"
                                :fields="datalist_param.title"
                                :per-page="page_param.perPage"
                                :api-model="false"
                                :data="datalist_param.datasource"
                                :detail-row-component="datalist_param.data_detairow"
                                @vuetable:cell-clicked="onCellClicked"
                            >
                            </vuetable>
                        </b-colxx>
                        <!--数据列表 end-->
                    </b-row>
                </b-colxx>
                <!--数据列表板 end-->
            </b-card>
            <div id='word_menu' style="position: absolute;border:0px solid #A2A2A2;left:0;top:0" hidden>
                    <b-row>
                        <b-colxx>
                        <b-button id="addforscript" @click="word_click('',$event.target.name,'')" class="mb-1" size="xs" variant="primary">
                            <i style="font-size:12px" class="simple-icon-magnifier-add"></i>
                            &nbsp;添加到搜索条件
                        </b-button>
                        </b-colxx>
                    </b-row>
                    <b-row>
                        <b-colxx>
                        <b-button id="removeforscript" @click="word_click('',$event.target.name,'NOT')" class="mb-1" size="xs" variant="primary">
                            <i style="font-size:12px" class="simple-icon-magnifier-remove"></i>
                            &nbsp;从搜索条件删除
                        </b-button>
                        </b-colxx>
                    </b-row>
            </div>
        </b-colxx>
    </b-row>
</template>
<script src="assets/js/laydate/laydate.js"></script>

<script>
import searchJS from 'views/manageCenter/spartacus_datasearch/js/datasearch.js';

/*数据表的相关引用 */
import Vuetable from 'vuetable-2/src/components/Vuetable'
import detairow from "views/manageCenter/spartacus_datasearch/controls/detailrow"
/*数据表的相关引用 end*/

import vSelect from "vue-select";//v-select 相关引用

import Vue from "vue";


export default {
  components: {
    vSelect,
    Vuetable
  },
  data() {
    return {
        //#region 日期选择器的相关参数
        dateselect_param: 
        {
            datasource:[{}],//日期选择器的数据源
            selectd_text: '近15分钟',//单选时当前选项
            selected_value:0
        },
        //#endregion

        //#region 时间选择器的相关参数
        picker_param:
        {
            picker_isshow:false,//控制时间选择器显示与隐藏
            formtime : '',//时间选择器起始时间
            totime : '',//时间选择器结束时间
            format:"YYYY-MM-DD HH:mm",
        },
        //#endregion
        
        
        //#region数据分页的相关参数
        page_param:
        {
            data_count:0,//匹配到的数据总量
            pagesum:1,//总分页数
            currentPage:1,//获取或设置分页控件的当前页
            perPage:10,//获取或设置每页显示数量
            datasource:[10,20,50],//每页显示数量的数据源
            selectedpage:1,//在页面跳转时，获取分页控件的当前选择页
        },
        //#endregion

        //#region  数据列表相关参数
        datalist_param:
        {
            datasource:[],//数据列表的数据对象
            data_detairow:detairow,//数据下拉板
            title:[]
        },
        //#endregion

        searchscript:'*',//查询脚本
    }
  },
    methods://函数声明
    {
        //#region body事件
        body_click(e)//body的点击事件
        {
            searchJS.body_click(e)
        },
        body_mousemove(e)//获取鼠标位置，用于点击弹出窗口的事件
        {
            searchJS.body_mousemove(e);
        },
        //#endregion

        //#region 查询控制器的相关函数 
        time_select_change(obj)//日期选择器更改事件
        {
            searchJS.time_select_change(obj);
        },
        //#endregion



        //#region 数据分页的相关函数
        fun(pageNum) //占位分页更改事件
        {
            /*这里不会执行任何方法,控件问题，
            /当切换页面这个方法会被重复调用,不设置此属性又会造成页面跳转*/
        },
        changepage(pageNum) //分页更改事件
        {
            this.page_param.selectedpage = pageNum;
            searchJS.get_page_data();
        },
        changePagecount(count)//页数据数量更改方法
        {
            searchJS.changePagecount(count);
        },
        //#endregion



        //#region 数据列表的相关函数
        search()//查询按钮表单提交方法
        {
            searchJS.search();
        },
        onCellClicked(data)//打开列表详细行
        {
            searchJS.onCellClicked(data);
        },
        word_click(key,value,type)//将分词添加到条件
        {
            searchJS.word_click(key,value,type);
        }
        //#endregion
    },
    mounted()//页面加载执行
    {
        searchJS.initialize(this);
    }
}
</script>
<style scpoed>
    /* color: #145388; */
    a:link {  text-decoration: none; }
    .message:hover {text-decoration:underline;}
    mark{
            background:#FBB450;
            border-radius: 5px;
        }
</style>
