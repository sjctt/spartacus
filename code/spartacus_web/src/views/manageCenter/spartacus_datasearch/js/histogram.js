import echarts from "echarts";
import datasearch from "views/manageCenter/spartacus_datasearch/js/datasearch.js";


var data=[0],dataAxis=[''],formatterData=[''],starttimelist=[''],endtimelist=[''];
function initialize()
{
    let myChart = echarts.init(document.getElementById("Log_histogram"));
    myChart.clear();
    data=[];
    dataAxis=[];
    formatterData=[];
    starttimelist=[];
    endtimelist=[];
}

function show_histogram(histogramdata)
{
    // 基于准备好的dom，初始化echarts实例
    let myChart = echarts.init(document.getElementById("Log_histogram"));
    if(histogramdata!=null)
    {
        dataAxis.push(histogramdata.key);
		data.push(histogramdata.value);
        formatterData.push(histogramdata.content);
        starttimelist.push(histogramdata.histogram_startTime);
        endtimelist.push(histogramdata.histogram_endTime);
    }
    
    var dataMax=Math.max.apply(null,data);//获取最大值的位数
    var a=dataMax.toString().length;
    var b=1;//除数
    for(var i=1;i<a;i++)
    {
    	b=b*10;
    }
    var yMax = Math.ceil(dataMax/b)*b;
    var dataShadow = [];

    for (var i = 0; i < data.length; i++) 
    {
        dataShadow.push(yMax);
    }
    var option = {
        title: {
            text: '',
            subtext: ''
        },
        tooltip : {
            trigger: 'axis',
            hideDelay:100,
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            formatter: function(params) 
            {
                var index=params[0].dataIndex;
                if(histogramdata!=null)
                {
                    return dataAxis[index]+"<br/>"+
                    formatterData[index]+'<br/>数据总量：'+
                    data[index]+'<br/>'+
                    starttimelist[index]+" - "+endtimelist[index];
                }
                else
                {
                    var str=params[0].dataIndex;
                    return formatterData[str]+'<br/>数据总量：'+data[str];
                }
            }
        },
        xAxis: {
            data: dataAxis,
            axisLabel: {
                inside: false,
                textStyle: {
                    color: 'black'
                }
            },
            axisTick: {
                show: false
            },
            axisLine: {
                show: false
            },
            z: 10
        },
        yAxis: {
            axisLine: {
                show: false
            },
            axisTick: {
                show: false
            },
            axisLabel: {
                textStyle: {
                    color: '#999'
                }
            }
        },
        dataZoom: [
            {
                type: 'inside'
            }
        ],
        series: [
            { // For shadow
                type: 'bar',
                itemStyle: {
                    normal: {color: 'rgba(0,0,0,0.05)'}
                },
                barGap:'-100%',
                barCategoryGap:'40%',
                data: dataShadow,
                animation: false
            },
            {
                type: 'bar',
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                {offset: 0, color: '#83bff6'},
                                {offset: 0.5, color: '#188df0'},
                                {offset: 1, color: '#188df0'}
                            ]
                        ),  
                        label: {
                            show: true,
                            position: 'top',
                            textStyle: {
                                color: '#188df0'
                            },
                            formatter:function(params)
                            {
                                 if(params.value===0)
                                 {
                                     return '';
                                 }
                                 else
                                 {
                                     return params.value;
                                 }
                            }
                        }
                    },
                    emphasis: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                {offset: 0, color: '#2378f7'},
                                {offset: 0.7, color: '#2378f7'},
                                {offset: 1, color: '#83bff6'}
                            ]
                        )
                    }
                },
                data: data
            }
        ]
    };
    myChart.setOption(option,true);
    window.onresize = function()
    {
        myChart.resize();
    }
    var zoomSize = 6;
    myChart.on('click', function(params){
        var click_index = params.dataIndex;
        var start_time = starttimelist[click_index];
        var end_time= endtimelist[click_index];
        datasearch.histogram_click(start_time,end_time);
    });
    
    // function (params) {
    //     console.log(dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)]);
    //     myChart.dispatchAction({
    //         type: 'dataZoom',
    //         startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
    //         endValue: dataAxis[Math.min(params.dataIndex + zoomSize / 2, data.length - 1)]
    //     });
    // }
}


export default
{
    show_histogram,
    initialize
}