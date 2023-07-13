<template>
    <th v-if="isHeader" class="vuetable-th-component-switch" v-html="title"></th>
    <td v-else class="vuetable-td-component-switch">
        <b-nav  pills ref="tagsContext"> 
        </b-nav>
    </td>
</template>
<script>
import VuetableFieldMixin from "vuetable-2/src/components/VuetableFieldMixin.vue";
import Util from 'views/manageCenter/spartacus_datasearch/controls/js/col-message.js';

export default {
    name: "vuetable-field-switch",
    mixins: [VuetableFieldMixin],
    methods: 
    {
        load_data()
        {
            if(this.rowData!=null)
            {
                var tag = Util.newTag(this.rowData);
                tag.$mount();
                this.$refs.tagsContext.appendChild(tag.$el);
            }
        }
    },
    mounted()
    {
        this.load_data();
    }
}
</script>