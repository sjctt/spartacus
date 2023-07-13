<template>
    <b-colxx class="disable-text-selection">
        <b-colxx xxs="12" class="mb-3" v-for="(key,index) in keys"   :key="index"  :id="key.id">
            <div style="height:25px">
                <key_value
                    :_key="key"
                    :_value="rowData[key]"
                    :_lightlist="rowData.es_light"
                    @edit_script="searchscript">
                </key_value>
            </div>
        </b-colxx>
    </b-colxx>
</template>
<script>
import key_value from "views/manageCenter/spartacus_datasearch/controls/key-value"
import searchJS from 'views/manageCenter/spartacus_datasearch/js/datasearch.js';
export default {
    props: {
        rowData: {
            type: Object,
            required: true
        }
    },
    components:{
        key_value
    },
    data(){
        return{
            keys:[],
            es_light:[]
        }
    },
    methods:{
        initialize()
        {
            this.keys = this.rowData.keys.split('|');
            this.es_light = this.rowData.es_light;
        },
        searchscript(event,_key,_value,type)
        {
            searchJS.detail_edit_script(_key,_value,type);
        }
    },
    mounted()//页面加载执行
    {
        this.initialize();
    }
}
</script>