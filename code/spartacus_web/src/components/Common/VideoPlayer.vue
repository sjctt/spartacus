<template>
    <div data-vjs-player>
        <video ref="myPlayer" :poster="options.poster" :class="`video-js vjs-default-skin ${options.class}`"></video>
    </div>
</template>

<script>
import videojs from 'video.js'
import "video.js/dist/video-js.css";

export default {
    props:['options'],
    data(){
        return{
            player: ''
        };
    },
    mounted(){
        this.player = videojs(this.$refs.myPlayer,this.options,()=>{
            //console.log("player ok")
        });
    },
    beforeDestroy() { 
        if (this.player) {
            this.player.dispose()
        }
    }
}
</script>