
<template>
     <div class="sidebar" @mouseenter="isMenuOver=true" @mouseleave="isMenuOver=false"  @touchstart="isMenuOver=true" >
        <div class="main-menu">
            <vue-perfect-scrollbar class="scroll" :settings="{ suppressScrollX: true, wheelPropagation: false }" >
                <ul class="list-unstyled">
                    <router-link :class="{ active : selectedParentMenu==='search' }" @click.native="changeSelectedParentHasNoSubmenu('search')" to="search" tag="li">
                      <a><i class="iconsmind-File-Search"></i> 数据检索</a>
                    </router-link>
                </ul>
            </vue-perfect-scrollbar>
        </div>

        <div class="sub-menu">
            <vue-perfect-scrollbar class="scroll" :settings="{ suppressScrollX: true, wheelPropagation: false }" >


            </vue-perfect-scrollbar>
          
        </div>
    </div>
</template>
<script>
import { mapGetters, mapMutations } from "vuex";
import { menuHiddenBreakpoint, subHiddenBreakpoint } from "constants/config";

export default {
  data() {
    return {
      selectedParentMenu: "",
      isMenuOver: false
    };
  },
  mounted() {
    this.selectMenu()

    window.addEventListener("resize", this.handleWindowResize);
    document.addEventListener("click", this.returnSelectedMenu);
    this.handleWindowResize();
    this.changeDefaultMenuType('menu-sub-hidden');
  },
  beforeDestroy(){
    document.removeEventListener("click", this.returnSelectedMenu);
    window.removeEventListener("resize", this.handleWindowResize);

  },
  
  methods: {
    ...mapMutations(["changeSideMenuStatus", "addMenuClassname","changeSelectedMenuHasSubItems"]),
    selectMenu(){
      const currentParentUrl = this.$route.path.split("/").filter(x => x != "")[1];
      if (currentParentUrl != undefined || currentParentUrl != null) {
        this.selectedParentMenu = currentParentUrl.toLowerCase();
      } else {
        this.selectedParentMenu = "dashboards";
      }
    },
    changeSelectedParentHasNoSubmenu(parentMenu){
      this.selectedParentMenu=parentMenu;
      this.changeSelectedMenuHasSubItems(false)
    },
    openSubMenu(e, selectedParent) {
      this.changeSelectedMenuHasSubItems(true)

      const currentClasses = this.menuType ? this.menuType.split(" ").filter(x => x != ""): "";

      if (!currentClasses.includes("menu-mobile")) {
        if (
          currentClasses.includes("menu-sub-hidden") &&
          (this.menuClickCount == 2 || this.menuClickCount == 0)
        ) {
          this.changeSideMenuStatus({ step: 3, classNames: this.menuType });
        } else if (
          currentClasses.includes("menu-hidden") &&
          (this.menuClickCount == 1 || this.menuClickCount == 3)
        ) {
          this.changeSideMenuStatus({ step: 2, classNames: this.menuType });
        } else if (
          currentClasses.includes("menu-default") &&
          !currentClasses.includes("menu-sub-hidden") &&
          (this.menuClickCount == 1 || this.menuClickCount == 3)
        ) {
          this.changeSideMenuStatus({ step: 0, classNames: this.menuType });
        }
      } else {
        this.addMenuClassname({classname: "sub-show-temporary",currentClasses: this.menuType});
      }
      this.selectedParentMenu = selectedParent;
    },
    addEvents() {
      document.addEventListener("click", this.handleDocumentClick);
    },
    removeEvents() {
      document.removeEventListener("click", this.handleDocumentClick);
    },
    returnSelectedMenu(){
      if (!this.isMenuOver) {
        this.selectMenu();
      }
    },
    handleDocumentClick(e) {
      if(!this.isMenuOver){
        let cont=true;
        e.path.map(p=>{
          if(p.nodeName!="svg" && p.className != undefined && p.className.indexOf('menu-button')>-1){
            cont=false
          }
        })
        if (cont) {
          this.toggle();
        }
      }
    },
    toggle() {
      const currentClasses = this.menuType.split(" ").filter(x => x != "");
      if (
        currentClasses.includes("menu-sub-hidden") &&
        this.menuClickCount == 3
      ) {
        this.changeSideMenuStatus({ step: 2, classNames: this.menuType });
      } else if (
        currentClasses.includes("menu-hidden") ||
        currentClasses.includes("menu-mobile")
      ) {
        this.changeSideMenuStatus({ step: 0, classNames: this.menuType });
      }
    },
    //Resize 
    handleWindowResize(event) {
      if (event && !event.isTrusted) {
        return;
      }
      let nextClasses = this.getMenuClassesForResize(this.menuType);
      this.changeSideMenuStatus({ step: 0, classNames: nextClasses.join(" ") });
    },
    getMenuClassesForResize(classes) {
      let nextClasses = classes.split(" ").filter(x => x != "");
      const windowWidth = window.innerWidth;

      if (windowWidth < menuHiddenBreakpoint) {
        nextClasses.push("menu-mobile");
      } else if (windowWidth < subHiddenBreakpoint) {
        nextClasses = nextClasses.filter(x => x != "menu-mobile");
        if (
          nextClasses.includes("menu-default") &&
          !nextClasses.includes("menu-sub-hidden")
        ) {
          nextClasses.push("menu-sub-hidden");
        }
      } else {
        nextClasses = nextClasses.filter(x => x != "menu-mobile");
        if (
          nextClasses.includes("menu-default") &&
          nextClasses.includes("menu-sub-hidden")
        ) { 
          nextClasses = nextClasses.filter(x => x != "menu-sub-hidden");
        }
      }
      return nextClasses;
    },
    //Change Default Menu Type
    changeDefaultMenuType(containerClassnames) {
    let nextClasses = this.getMenuClassesForResize(containerClassnames);
     this.changeSideMenuStatus({ step: 0, classNames: nextClasses.join(" ") });
  }
  },
  computed: {
    ...mapGetters({
      menuType: "getMenuType",
      menuClickCount: "getMenuClickCount",
      selectedMenuHasSubItems: "getSelectedMenuHasSubItems",
    })
  },
  watch: {
    menuType: function(val) {
      if (val.indexOf("show-temporary") > -1) {
        this.addEvents();
      } else {
        this.removeEvents();
      }
    },
    '$route' (to, from) {
        if(to.path!==from.path)
        {
          this.changeSideMenuStatus({ step: 0, classNames: this.menuType });
          this.selectMenu()
          window.scrollTo(0, top);
        }
    }
  }
};
</script>
