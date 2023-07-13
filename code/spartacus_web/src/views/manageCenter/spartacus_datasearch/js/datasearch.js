import axios from 'axios'
import histogram from 'views/manageCenter/spartacus_datasearch/js/histogram.js'
import colmessage from "views/manageCenter/spartacus_datasearch/controls/col-message.vue"

var _this;
var histogram_search_end=false;
var pagedata_search_end=false;
var evt;
var rightEdge,bottomEdge;
//#region 初始化_this
function initialize(obj)
{
    _this = obj;//初始化this
    get_dateselect_data();//加载日期选择器数据
    histogram.show_histogram(null);//展示直方图
}
//#endregion

//#region 实时获取鼠标位置
function body_mousemove(e)
{  
    evt = e || window.event;
    rightEdge = e.pageX || e.clientX + document.documentElement.scroolLeft;
    bottomEdge = e.pageY || e.clientY + document.documentElement.scrollTop;
}
//#endregion

//#region 查询初始化
function search_initialize()
{
    histogram_search_end = false;
    pagedata_search_end = false;
    histogram.initialize();//初始化直方图
    histogram_lasttime=0;//将查询记录清零
    if(_this.searchscript=="")
    {
        _this.searchscript = "*";
    }
}
//#endregion

//#region 数据表相关函数
    //#region 获取标准title
    function get_table_title_standard()
    {
        var title = [
            {
                name: 'id',
                title: '',
                titleClass: "center aligned",
                dataClass: "list-item-heading",
                width:'40px',
                formatter: (value, vuetable) => {
                    let icon = vuetable.isVisibleDetailRow(value) ? 'simple-icon-arrow-up-circle':'simple-icon-arrow-down-circle'
                    var html='<a class="show-detail-row">'
                            +'<B><i class="'+icon+'"></i></B>'
                            +'</a>'
                    return html;
                }
            },
            {
                name:'receivetime',
                title:'<i class="simple-icon-clock"></i>&nbsp;接收时间',
                titleClass:'',
                dataClass:'text-muted',
                width:'120px'
            },
            {
                name:colmessage,
                title:'<i class="simple-icon-list"></i>&nbsp;原始信息',
                titleClass:'',
                dataClass:'',
                switch: 
                {
                    label: (data) => data.name,
                    field: (data) => data.gender === 'M',
                }
            }
        ];
        return title;
    }
    //#endregion
    //#region 打开数据表详细行
    function onCellClicked(data)
    {
        var cell_index=data.field.$_index;
        if(cell_index==0)//只有点第一列时执行
        {
            _this.$refs.vuetable.toggleDetailRow(data.data.id)
        }
    }
    //#endregion
    //#region 页数据量更改方法
    function changePagecount(count)
    {
        _this.page_param.perPage = count;
        var data_count = _this.page_param.data_count;
        if(data_count>0)
        {
            //重新计算分页数并取当页数据
            var pagecount = (data_count/count);
            _this.page_param.pagesum=pagecount;
            _this.page_param.currentPage=1;
            _this.page_param.selectedpage=1;
            get_page_data();
        }
    }
    //#endregion
    //#region 执行总页数查询
    function get_list_pageinfo()
    {
        var apiUrl="http://127.0.0.1:8001/get_datacount";

        const postobj=new URLSearchParams();
        postobj.append("timerange",_this.dateselect_param.selected_value);
        postobj.append("pagecount",_this.page_param.perPage);
        postobj.append("fromtime",_this.picker_param.formtime);
        postobj.append("totime",_this.picker_param.totime);
        postobj.append("searchscript",_this.searchscript);
        
        axios.post(apiUrl,postobj).then((response) => 
        {
            var pagecount=0;//计算分页数
            var data_count = parseInt(response.data.data_message);//匹配到的数据总量
            var page_count =parseInt(_this.page_param.perPage);//每页显示数据条目数
            if(data_count>0)
            {
                pagecount = (data_count/page_count);
            }
            _this.page_param.data_count=data_count;
            _this.page_param.pagesum=pagecount;
            get_page_data();
        })
    }
    //#endregion
    //#region 查询当页数据
    function get_page_data()
    {
        _this.datalist_param.datasource=[];//清空数据列表
        if(_this.page_param.data_count>0)
        {
            var apiUrl="http://127.0.0.1:8001/get_pagedata";

            const postobj=new URLSearchParams();
            postobj.append("timerange",_this.dateselect_param.selected_value);
            postobj.append("pagecount",_this.page_param.perPage);
            postobj.append("fromtime",_this.picker_param.formtime);
            postobj.append("totime",_this.picker_param.totime);
            postobj.append("searchscript",_this.searchscript);
            postobj.append("selectedpage",_this.page_param.selectedpage);

            axios.post(apiUrl,postobj).then((response) => 
            {
                
                _this.datalist_param.title=get_table_title_standard();//获表头
                _this.datalist_param.datasource = response.data.data_message//放数据

                pagedata_search_end = true;
                if(pagedata_search_end&&histogram_search_end)
                {
                    document.getElementById("search_button").disabled=false;
                }
            })
        }
        else
        {
            pagedata_search_end = true;
            if(pagedata_search_end&&histogram_search_end)
            {
                document.getElementById("search_button").disabled=false;
            }
        }
    }
    //#endregion
    //#region 将用户选择添加到检索条件
    function detail_edit_script(_key,_value,type)
    {
        var searchscript = _this.searchscript=="*"?"":_this.searchscript;
        var new_searchscript="";
        if(type=="NOT")
        {
            if(_key=="")
            {
                new_searchscript = type+" "+_value
            }
            else
            {
                new_searchscript = type+" "+_key+":"+_value
            }
        }
        else
        {
            if(_key=="")
            {
                new_searchscript = type+_value;
            }
            else
            {
                new_searchscript = _key+type+_value;
            }
        }
        
        new_searchscript = (searchscript!=""?(searchscript+" AND "):searchscript)+new_searchscript;
        _this.searchscript = new_searchscript;
        search();
    }
    //#endregion
    //#region 日志体分词点击事件
    function show_word_menu(control,word)
    {
        var word_menu = document.getElementById("word_menu");
        word_menu.hidden=false;
        document.getElementById("addforscript").name = word;
        document.getElementById("removeforscript").name = word;
        if (rightEdge < word_menu.offsetWidth)
        {
            word_menu.style.left = control.scrollLeft + (evt.clientX-40)  + "px";
        }
        else
        {
            word_menu.style.left = control.scrollLeft + (evt.clientX-40) - word_menu.offsetWidth+ "px";
        }
        word_menu.style.top = control.scrollTop + (evt.clientY-90) - word_menu.offsetHeight + document.documentElement.scrollTop+"px";
    }
    //#endregion
    //#region 将分词添加到查询脚本
    function word_click(key,value,type)
    {
        detail_edit_script(key,value,type);
    }
    //#endregion
//#endregion

//#region 直方图相关函数
    //#region 直方图数据查询
    var histogram_lasttime=0;
    function get_histogramdata()
    {
        var apiUrl="http://127.0.0.1:8001/get_histogramdata";

        const postobj=new URLSearchParams();
        postobj.append("timerange",_this.dateselect_param.selected_value);
        postobj.append("pagecount",_this.page_param.perPage);
        postobj.append("fromtime",_this.picker_param.formtime);
        postobj.append("totime",_this.picker_param.totime);
        postobj.append("searchscript",_this.searchscript);
        postobj.append("selectedpage",_this.page_param.selectedpage);
        postobj.append("histogram_lasttime",histogram_lasttime);

        axios.post(apiUrl,postobj).then((response) => 
        {
            histogram_lasttime = response.data.data_message.histogram_lasttime;
            histogram.show_histogram(response.data.data_message);//展现柱状图
            if(response.data.data_message.search_isend==false)
            {
                get_histogramdata();
            }
            else
            {
                histogram_search_end = true;
                if(pagedata_search_end&&histogram_search_end)
                {
                    document.getElementById("search_button").disabled=false;
                }
            }
        })
    }
    //#endregion
    //#region 直方图点击事件
    function histogram_click(start_time,end_time)
    {
        if(start_time != end_time)
        {
            var selected=JSON.parse('{"label":"自定义","value":11}')
            time_select_change(selected);
            _this.picker_param.formtime = start_time.replace(/\//g, '-');
            _this.picker_param.totime = end_time.replace(/\//g, '-');
            search();
        }
    }
    //#endregion
//#endregion

//#region 查询控制器相关函数
    //#region 查询按钮表单提交
    function search()
    {
        document.getElementById("search_button").disabled=true;//禁用查询按钮
        search_initialize();
        get_histogramdata();//加载柱状图数据
        if(true)//标准数据格式
        {
            get_list_pageinfo();//取分页信息
        }
    }
    //#endregion
    //#region 日期选择器数据
    /*
    * 时间段选择器数据内容
    */
    function get_dateselect_data()
    {
        var selectData =[
                        { label: "近15分钟", value: 0},
                        { label: "近30分钟", value: 1},
                        { label: "近1小时", value: 2 },
                        { label: "近3小时", value: 3 },
                        { label: "近6小时", value: 4 },
                        { label: "近1天", value: 5 },
                        { label: "近2天", value: 6 },
                        { label: "近7天", value: 7 },
                        { label: "近15天", value: 8 },
                        { label: "近1月", value: 9 },
                        { label: "近3月", value: 10 },
                        { label: "自定义", value: 11 }
                    ];
        _this.dateselect_param.datasource = selectData;
    }
    //#endregion
    //#region 时间段选择器更改事件
    /*
    * 时间段选择器更改事件
    * @param {*} obj 控制器对象，通过这个对象可以获取选择属性
    */
    function time_select_change(obj)
    {
        // var a = JSON.stringify(obj)
        // alert(a);
        //除非选择自定义，否则起止时间选择器不会出现
        if(obj.value>=0)
        {
            _this.dateselect_param.selected_value = obj.value;
            _this.dateselect_param.selectd_text=obj.label;
            if(obj.value=="11")
            {
                _this.picker_param.picker_isshow = true;
            }
            else
            {
                _this.picker_param.picker_isshow = false;
            }
        }
    }
    //#endregion
//#endregion

//#region body点击事件
function body_click(e)
{
    if(e.target.id != "es_word")
    {
        var word_menu = document.getElementById("word_menu");
        if(word_menu.hidden==false)
        {
            word_menu.hidden=true;
        }
    }
}
//#endregion
export default
{
        time_select_change,
        get_dateselect_data,
        get_page_data,
        get_list_pageinfo,
        search,
        initialize,
        changePagecount,
        onCellClicked,
        histogram_click,
        detail_edit_script,
        body_mousemove,
        show_word_menu,
        body_click,
        word_click
}