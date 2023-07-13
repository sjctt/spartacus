<template>
<div>
<b-card v-if='data.id'  @click.prevent="toggleItem($event,data.id)" :class="{'active' : selectedItems.includes(data.id)}" no-body>
        <div class="position-relative">
            <a href="#" class="w-40 w-sm-100">
                <img src="/assets/img/card-thumb-1.jpg"  class="card-img-top" :alt="data.account"/>
            </a>
             <b-badge pill v-show="data.accountStatus==1" :variant="'success'" @click.stop="changeStatus(data.id,0)" >正常</b-badge>
            <b-badge pill v-show="data.accountStatus==0" :variant="'warning'" @click.stop="changeStatus(data.id,1)">锁定</b-badge>
        </div>
        <b-card-body>
            <b-row>
                <b-colxx xxs="2">
                    <b-form-checkbox :checked="selectedItems.includes(data.id)" class="itemCheck mb-0"/>
                </b-colxx>
                <b-colxx xxs="10" class="mb-3">
                    <h6 class="mb-4 card-subtitle">{{data.account}}</h6>
                    <p class="text-muted text-small mb-0 font-weight-light card-text">{{data.createTime}}</p>
                </b-colxx>
            </b-row>
        </b-card-body>
    </b-card>
    <b-card v-else-if='data.idspartacus_assets' @click.prevent="toggleItem($event,data.idspartacus_assets)" :class="{'active' : selectedItems.includes(data.idspartacus_assets)}" no-body>
        <div class="position-relative">
            <a href="#" class="w-40 w-sm-100">
                <img src="/assets/img/card-thumb.jpg"  class="card-img-top" :alt="data.assetsname"/>
            </a>
        </div>
        <b-card-body>
            <b-row>
                <b-colxx xxs="2">
                    <b-form-checkbox :checked="selectedItems.includes(data.idspartacus_assets)" class="itemCheck mb-0"/>
                </b-colxx>
                <b-colxx xxs="10" class="mb-3">
                    <h6 class="mb-4 card-subtitle">{{data.assetsname}}</h6>
                    <p class="text-muted text-small mb-0 font-weight-light card-text">{{data.createTime}}</p>
                </b-colxx>
            </b-row>
        </b-card-body>
    </b-card>
</div>   
</template>
<script>
export default {
    props:['data','selectedItems'],
    methods:{
        toggleItem(event,itemId){
            this.$emit('toggle-item',event,itemId)
        },
        changeStatus(itemId,satusId){
            this.$emit('change-status',itemId,satusId)
        }
    }
}
</script>