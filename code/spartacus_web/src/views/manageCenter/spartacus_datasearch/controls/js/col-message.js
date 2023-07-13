import Vue from 'vue';
import searchJS from 'views/manageCenter/spartacus_datasearch/js/datasearch.js';

export default {
    
    newTag (obj) 
    {
        // 创建组件逻辑
        let TagClass = Vue.extend(
        {
            // 向界面渲染的dom方法。
            render (createElement) 
            {
                var html_list=[];
                var es_light = obj.es_light;//高亮数据
                //#region 原始消息处理
                //#region 分词处理
                var message = obj.datacontent;
                var lower_message = message.toLowerCase();
                this.tags = obj.es_word;//分词
                for(var i=0;i<this.tags.length;i++)
                {
                    var startindex = lower_message.indexOf(this.tags[i]);
                    var endindex = startindex+this.tags[i].length;
                    var split_content = message.substring(0,startindex);

                    var html_span = createElement('span',{attrs:{style:"font-weight:bold "}},[split_content]);
                    html_list.push(html_span);


                    split_content = message.substring(startindex,endindex);
                    var html_content;
                    if(es_light.length>0)
                    {
                        for(var j=0;j<es_light.length;j++)
                        {
                            if(es_light[j].fieldname=="datacontent")
                            {
                                if(split_content == es_light[j].value)
                                {
                                    html_content = createElement('mark',{},[split_content]);
                                    break;
                                }
                                
                                if(j==es_light.length-1)
                                {
                                    html_content = createElement('a',{attrs:{class:"message",href:"javascript:void(0);",name:split_content,id:"es_word",style:"font-weight:bold "},on:{'mousedown':this.show_word_menu}},[split_content]);
                                    break;
                                }
                            }
                            else
                            {
                                html_content = createElement('a',{attrs:{class:"message",href:"javascript:void(0);",name:split_content,id:"es_word",style:"font-weight:bold "},on:{'mousedown':this.show_word_menu}},[split_content]);
                            }
                        }
                    }
                    else
                    {
                        html_content = createElement('a',{attrs:{class:"message",href:"javascript:void(0);",name:split_content,id:"es_word",style:"font-weight:bold "},on:{'mousedown':this.show_word_menu}},[split_content]);
                    }
                    html_list.push(html_content);
                    
                    message = message.substring(endindex);
                    lower_message = message.toLowerCase();
 
                    if(i==(this.tags.length-1) && endindex<message.length)
                    {
                       var html_span = createElement('span',{},[message]);
                       html_list.push(html_span);
                    }
                }
                //#endregion
                //#endregion
               
                //#region 优选字段处理
                var html_br = createElement('br');
                html_list.push(html_br);//先换行

                var selected_word = obj.selected_word;
                var selected_word_list=[];
                selected_word.forEach(item => {
                    var value = obj[item];
                    if(typeof(value) != 'undefined')
                    {
                        //如果不是undefined表示已获取到这个字段
                        var html_span = createElement('span',{attrs:{style:"min-height:30px;line-height:30px;font-weight:bold "}},[item+":"]);
                        selected_word_list.push(html_span);
                        
                        if(value=="")
                        {
                            var html_span_empty = createElement('b-nav-item-dropdown',{props:{text:"--"},attrs:{disabled:true}},[]);
                            selected_word_list.push(html_span_empty);
                        }
                        else
                        {
                            var html_content;
                            if(es_light.length>0)
                            {
                                for(var j=0;j<es_light.length;j++)
                                {
                                    if(es_light[j].fieldname==item)
                                    {
                                        var es_light_mark = createElement('mark',{attrs:{style:" min-height:15px;line-height:15px;"}},["[ "+value+" ]"]);
                                        html_content = createElement('span',{attrs:{style:"min-height:30px;line-height:30px;"}},[es_light_mark]);
                                        break;
                                    }
                                    if(j==es_light.length-1)
                                    {
                                        var html_add_i = createElement('i',{attrs:{class:"simple-icon-magnifier-add",id:item+"|"+value}},[" 添加到搜索条件"]);
                                        var html_add_item = createElement('b-dropdown-item',{attrs:{id:item+"|"+value},nativeOn:{click:this.word_click_add}},[html_add_i]);
            
                                        var html_remove_i = createElement('i',{attrs:{class:"simple-icon-magnifier-remove",id:item+"|"+value}},[" 从搜索结果排除"]);
                                        var html_remove_item = createElement('b-dropdown-item',{attrs:{id:item+"|"+value},on:{"click":this.word_click_remove}},[html_remove_i]);

                                        html_content = createElement('b-nav-item-dropdown',{props:{text:value}},[html_add_item,html_remove_item]);
                                        break;
                                    }
                                }
                            }
                            else
                            {
                                //这里会影响数据列表加载效率，列表单页条目数越多 加载会越慢 暂时没有想到好的方法解决
                                var html_add_i = createElement('i',{attrs:{class:"simple-icon-magnifier-add",id:item+"|"+value}},[" 添加到搜索条件"]);
                                var html_add_item = createElement('b-dropdown-item',{attrs:{id:item+"|"+value},nativeOn:{click:this.word_click_add}},[html_add_i]);
            
                                var html_remove_i = createElement('i',{attrs:{class:"simple-icon-magnifier-remove",id:item+"|"+value}},[" 从搜索结果排除"]);
                                var html_remove_item = createElement('b-dropdown-item',{attrs:{id:item+"|"+value},on:{"click":this.word_click_remove}},[html_remove_i]);

                                html_content = createElement('b-nav-item-dropdown',{props:{text:value}},[html_add_item,html_remove_item]);
                            }
                            selected_word_list.push(html_content);
                        }
                    }
                });
                var html_nav = createElement('b-nav',{attrs:{style:" min-height:15px;line-height:15px;"}},[selected_word_list]);
                html_list.push(html_nav);
                //#endregion
                var html_div = createElement('div',{},[html_list]);
                return html_div;
            },
            methods: 
            {
                show_word_menu(e)
                {
                    var control = document.getElementById(e.target.id);
                    searchJS.show_word_menu(control,e.target.name);
                },
                word_click_add(e)
                {
                    var obj = e.target.id.split("|")
                    searchJS.word_click(obj[0],obj[1],":");
                },
                word_click_remove(e)
                {
                    var obj = e.target.id.split("|")
                    searchJS.word_click(obj[0],obj[1],"NOT");
                }
            },
            data () {
                return {
                    
                }
            }
        });
        return new TagClass();
    }
}