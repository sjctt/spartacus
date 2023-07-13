<template>
    <div :class="{'theme-colors':true, 'shown' : isOpen}"  @mouseenter="isMenuOver=true" @mouseleave="isMenuOver=false">
        <div class="p-4">
            <p class="text-muted mb-2">Light Theme</p>
            <div class="d-flex flex-row justify-content-between mb-4">
                <a href="#" :class="{'theme-color theme-color-purple':true, 'active' :selectedColor==='light.purple'}" @click.prevent="changeThemeColor('light.purple')"></a>
                <a href="#" :class="{'theme-color theme-color-blue':true, 'active' :selectedColor==='light.blue'}" @click.prevent="changeThemeColor('light.blue')"></a>
                <a href="#" :class="{'theme-color theme-color-green':true, 'active' :selectedColor==='light.green'}" @click.prevent="changeThemeColor('light.green')"></a>
                <a href="#" :class="{'theme-color theme-color-orange':true, 'active' :selectedColor==='light.orange'}" @click.prevent="changeThemeColor('light.orange')"></a>
                <a href="#" :class="{'theme-color theme-color-red':true, 'active' :selectedColor==='light.red'}" @click.prevent="changeThemeColor('light.red')"></a>
            </div>
            <p class="text-muted mb-2">Dark Theme</p>
            <div class="d-flex flex-row justify-content-between">
                <a href="#" :class="{'theme-color theme-color-purple':true, 'active' :selectedColor==='dark.purple'}" @click.prevent="changeThemeColor('dark.purple')"></a>
                <a href="#" :class="{'theme-color theme-color-blue':true, 'active' :selectedColor==='dark.blue'}" @click.prevent="changeThemeColor('dark.blue')"></a>
                <a href="#" :class="{'theme-color theme-color-green':true, 'active' :selectedColor==='dark.green'}" @click.prevent="changeThemeColor('dark.green')"></a>
                <a href="#" :class="{'theme-color theme-color-orange':true, 'active' :selectedColor==='dark.orange'}" @click.prevent="changeThemeColor('dark.orange')"></a>
                <a href="#" :class="{'theme-color theme-color-red':true, 'active' :selectedColor==='dark.red'}" @click.prevent="changeThemeColor('dark.red')"></a>
            </div>
        </div>
        <a href="#" class="theme-button" @click.prevent="toggle"> <i class="simple-icon-magic-wand"></i> </a>
    </div>
</template>

<script>
import {colors} from 'constants/config'
export default {
  data() {
    return {
        isOpen:false,
        isMenuOver:false,
        
        selectedColor:"",
    };
  },
  methods: {
    addEvents() {
      document.addEventListener("click", this.handleDocumentClick);
      document.addEventListener("touchstart", this.handleDocumentClick);
    },
    removeEvents() {
      document.removeEventListener("click", this.handleDocumentClick);
      document.removeEventListener("touchstart", this.handleDocumentClick);
    },
    handleDocumentClick(e) {
        if(!this.isMenuOver){
            this.toggle()
        }
    },
    toggle(){
        this.isOpen=!this.isOpen
    },
    changeThemeColor(color){
        localStorage.setItem('themeColor', color)
        this.toggle();
        setTimeout(()=>{
            window.location.reload();
        },500)
    }
  },
  watch: {
    isOpen(val) {
      if (val) {
        this.addEvents();
      } else {
        this.removeEvents();
      }
    }
  },
  beforeDestroy(){
    this.removeEvents();
  },
  mounted(){
      const color = localStorage.getItem('themeColor');
      if(color!=null && colors.includes(color))
       { 
           this.selectedColor=color;
        }else{
            this.selectedColor='light.orange'
        }
  }
};

</script>