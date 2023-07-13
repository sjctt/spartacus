<template>
<div>
    <!--数据检索模块表格-->
    <!--标准格式 数据列表-->
    <div v-if='data.receivetime_view'>
    <b-card  @click.prevent="toggleItem($event,data.receivetime)" :class="{'d-flex flex-row':true,'active' : selectedItems.includes(data.receivetime)}" no-body>
        <div class="pl-2 d-flex flex-grow-1 min-width-zero">
            <div  class="card-body align-self-center d-flex flex-column flex-lg-row justify-content-between min-width-zero align-items-lg-center">
                <p class="mb-1 text-muted text-small">{{data.receivetime_view}}</p>
                <p class="mb-1 text-muted text-small w-85">
                    {{data.message}}
                    <br/><br/>
                    <span>
                        assetsname：{{data.assetsname}}
                    </span>
                    |
                    <span>
                        assetsip：{{data.hostip}}
                    </span>
                    |
                    <span>
                        assetsip：{{data.hostip}}
                    </span>
                </p>
            </div>
        </div>
    </b-card>
    </div>
    <!--标准格式 数据列表 end-->
    <!--数据检索模块表格 end-->
    <b-card v-else-if='data.id' @click.prevent="toggleItem($event,data.id)" :class="{'d-flex flex-row':true,'active' : selectedItems.includes(data.id)}" no-body>
        <div class="pl-2 d-flex flex-grow-1 min-width-zero">
            <div  class="card-body align-self-center d-flex flex-column flex-lg-row justify-content-between min-width-zero align-items-lg-center">
                <router-link :to="`?p=${data.id}`" class="w-40 w-sm-100">
                    <p class="list-item-heading mb-1 truncate">{{data.account}}</p>
                </router-link>
                <p class="mb-1 text-muted text-small w-15 w-sm-100">{{data.createTime}}</p>
                <p class="mb-1 text-muted text-small w-15 w-sm-100">{{data.email}}</p>
                <div class="w-15 w-sm-100">
                    <b-badge pill v-show="data.accountStatus==1" :variant="'success'" @click.stop="changeStatus(data.id,0)" >正常</b-badge>
                     <b-badge pill v-show="data.accountStatus==0" :variant="'warning'" @click.stop="changeStatus(data.id,1)">锁定</b-badge>
                </div>
            </div>
            <div class="custom-control custom-checkbox pl-1 align-self-center pr-4">
                <b-form-checkbox :checked="selectedItems.includes(data.id)" class="itemCheck mb-0"/>
            </div>
        </div>
    </b-card>
     <b-card v-else-if='data.idspartacus_assets' @click.prevent="toggleItem($event,data.idspartacus_assets)" :class="{'d-flex flex-row':true,'active' : selectedItems.includes(data.idspartacus_assets)}" no-body>
        <div class="pl-2 d-flex flex-grow-1 min-width-zero">
            <div  class="card-body align-self-center d-flex flex-column flex-lg-row justify-content-between min-width-zero align-items-lg-center">
                <router-link :to="`?p=${data.idspartacus_assets}`" class="w-40 w-sm-100">
                    <p class="list-item-heading mb-1 truncate">{{data.assetsname}}</p>
                </router-link>
                <p class="mb-1 text-muted text-small w-15 w-sm-100">{{data.assetsip}}</p>
                <p class="mb-1 text-muted text-small w-15 w-sm-100">{{data.createtime}}</p>
                <p class="mb-1 text-muted text-small w-15 w-sm-100">{{data.securitydomain}}</p>
                <p class="mb-1 text-muted text-small w-15 w-sm-100">{{data.businessdomain}}</p>
                <p class="mb-1 text-muted text-small w-15 w-sm-100">{{data.physicalposition}}</p>
                <p class="mb-1 text-muted text-small w-15 w-sm-100">{{data.ref_node}}</p>
                <p class="mb-1 text-muted text-small w-15 w-sm-100">{{data.remark}}</p>
            </div>
            <div class="custom-control custom-checkbox pl-1 align-self-center pr-4">
                <b-form-checkbox :checked="selectedItems.includes(data.idspartacus_assets)" class="itemCheck mb-0"/>
            </div>
        </div>
    </b-card>
</div>
</template>
<script>
export default {
    props:['data','selectedItems'],
    methods:{
        searchscript(event,itemId){
            this.$emit('edit_script',event,itemId)
        },
        changeStatus(itemId,satusId){
            this.$emit('change-status',itemId,satusId)
        }
    }
}
</script>