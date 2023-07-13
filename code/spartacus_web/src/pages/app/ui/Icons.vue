<template>
<div>
  <b-row>
    <b-colxx xxs="12">
      <piaf-breadcrumb :heading="$t('menu.icons')"/>
      <div class="separator mb-5"></div>
    </b-colxx>
  </b-row>
  <b-row>
    <b-colxx xxs="12">
        <b-card class="mb-4" :title="$t('icons.simplelineicons') +' ('+simplelineicons.length+ ' icons)'">
            <div class="simple-line-icons">
                <div class="glyph" v-for="(icon, index) in simplelineicons" :key="index">
                <div :class="'glyph-icon ' + icon"/>
                <div class="class-name">{{icon}}</div>
                </div>
            </div>
        </b-card>
    </b-colxx>

    <b-colxx xxs="12">
        <b-card class="mb-4" :title="$t('icons.iconsmind') +' ('+iconsmind.length+ ' icons)'">
            <div class="simple-line-icons">
                <div class="glyph" v-for="(icon, index) in iconsmind" :key="index">
                <div :class="'glyph-icon ' + icon"/>
                <div class="class-name">{{icon}}</div>
                </div>
            </div>
        </b-card>
    </b-colxx>

  </b-row>
  </div>
</template>
<script>
import { iconsmind, simplelineicons } from "data/icons";
export default {
    data(){
        return{
            simplelineicons,
            iconsmind
        }
    }
};
</script>
