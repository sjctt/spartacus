<template>
    <b-colxx class="disable-text-selection">
        <b-row>
            <b-colxx>
                <div style="float:left;line-height:25px; width:400px;">
                    <div style="float:left;width:100%; height:2px">
                        <hr/>
                    </div>
                    <b-badge class="mb-1" variant="primary">
                        <span style="font-size:14px">
                            [ {{ _key }} ]
                        </span>
                    </b-badge>
                </div>
                <div style="float:left;">
                    <b-nav v-if="islight()&&(_value!=''&&typeof(_value)!='undefined')" class="nav-pills">
                        <mark style="font-size:13px;font-weight:bold">[{{" "+_value+" "}}]</mark>
                    </b-nav>
                    <b-nav v-else-if="_value!=''&&typeof(_value)!='undefined'" class="nav-pills">
                        <b-nav-item-dropdown :text="_value">
                            <b-dropdown-item @click.prevent="searchscript($event,_key,_value,':')">
                                <i style="font-size:12px" class="simple-icon-magnifier-add"></i>
                                &nbsp;添加到搜索条件
                            </b-dropdown-item>
                            <b-dropdown-item @click.prevent="searchscript($event,_key,_value,'NOT')">
                                <i style="font-size:12px" class="simple-icon-magnifier-remove"></i>
                                &nbsp;从搜索结果排除
                            </b-dropdown-item>
                        </b-nav-item-dropdown>
                    </b-nav>
                    <b-nav v-else class="nav-pills">
                        <b-nav-item-dropdown disabled>
                        </b-nav-item-dropdown>
                    </b-nav>
                </div>
            </b-colxx>
        </b-row>
    </b-colxx>
</template>
<script>
import { timeout } from 'q';
export default 
{
    props:['_key','_value','_lightlist'],
    methods:{
        searchscript(event,_key,_value,type){
            this.$emit('edit_script',event,_key,_value,type)
        },
        islight(){
            var result=false;
            this._lightlist.forEach(item => {
                if(item.fieldname == this._key)
                {
                    result = true;
                }
            });
            return result;
        }
    },
    mounted()//页面加载执行
    {
        
    }
}
</script>