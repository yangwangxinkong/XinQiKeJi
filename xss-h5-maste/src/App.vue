<template>
  <div id="app">
    <x-header v-if="isShowNav" slot="header" :left-options="{backText: '',preventGoBack:preventGoBack}" style="width:100%;position:fixed;left:0;top:0;z-index:100;background-color:#fff;border-bottom: 1px solid #f5f5f5;" :title="title" @on-click-back="goBack">
    </x-header>
    <div class="viewBox" ref="viewBox" :style="{'padding-top':isShowNav ? '46px' : '0','padding-bottom':isShowBar?'55px':'0'}">
      <router-view></router-view>
    </div>
    <tabbar @on-index-change="onIndexChange" slot="bottom" style="position:fixed;background-color: #ffffff;" v-if="isShowBar">
      <tabbar-item :selected="$route.path == tab.href" :link="tab.href" v-for="(tab,index) in navConfig" :key="index">
        <img slot="icon" :src="tab.icon">
        <img slot="icon-active" :src="tab.iconActive">
        <span slot="label">{{tab.name}}</span>
      </tabbar-item>
    </tabbar>
  </div>
</template>

<script>
import { XHeader, ViewBox, Tabbar, TabbarItem } from "vux";
import { mapState, mapActions } from "vuex";
import flexible from "../static/js/flexible";
import footer from "@/components/footer/index";
import {getUrlStr} from '@/api/server'
let navConfig = [
  {
    name: "首页",
    icon: require("./assets/tabbar/home.png"),
    iconActive: require("./assets/tabbar/home-active.png"),
    href: "/"
  },
  // {
  //   name: '资讯',
  //   icon: require('../../assets/tabbar/consulting.png'),
  //   iconActive:require('../../assets/tabbar/consulting-active.png'),
  //   href: '/consulting/list'
  // },

  // {
  //   name: '工具',
  //   icon: require('../../assets/tabbar/tool.png'),
  //   iconActive:require('../../assets/tabbar/tool-active.png'),
  //   href: '/instrument/index'
  // },

  {
    name: "缴费",
    icon: require("./assets/tabbar/tool.png"),
    iconActive: require("./assets/tabbar/tool-active.png"),
    href: "/order/orderMember"
  },

  {
    name: "福利",
    icon: require("./assets/tabbar/consulting.png"),
    iconActive: require("./assets/tabbar/consulting-active.png"),
    href: "/product/list"
  },

  {
    name: "我的",
    icon: require("./assets/tabbar/my.png"),
    iconActive: require("./assets/tabbar/my-active.png"),
    href: "/member/index"
  }
];

export default {
  name: "app",
  data() {
    return {
      preventGoBack: false,
      navConfig
    };
  },
  components: {
    XHeader,
    ViewBox,
    Tabbar,
    TabbarItem
    // "v-footer": footer
  },
  created() {
    if(document.getElementById("Loading")){
      document.body.removeChild(document.getElementById("Loading"));
    }
    let targetId = getUrlStr("targetId");
      //console.log("targetId:" + targetId)
    if (targetId){
      if(targetId == 3){
        this.$router.push({path:"/package/index?type=vip"});
      }

    }
  },
  computed: {
    ...mapState({
      route: state => state.route
    }),
    title() {
      if (this.route.meta.title) {
        if (this.route.meta.isShowNav) {
          const parts = this.route.meta.title;
          //console.log(parts)
          if (
            parts == "支付订单" ||
            parts == "我的订单" ||
            parts == "用户中心"
          ) {
            this.preventGoBack = true;
          } else {
            this.preventGoBack = false;
          }
          return parts;
        }
      }
    },
    isShowNav() {
      return this.route.meta.isShowNav;
    },
    isShowBar() {
      return this.route.meta.isShowBar;
    }
  },
  methods: {
    goBack() {
      var that = this;

      // 订单支付 返回到我的订单
      if (this.route.meta.title === "支付订单") {
        this.$vux.confirm.show({
          title: "温馨提示",
          content:
            "是否取消支付？您的订单在24小时内未完成支付将被取消，请尽快支付",
          confirmText: "确认离开",
          cancelText: "继续支付",
          onCancel() {},
          onConfirm() {
            that.preventGoBack = false;
            that.$router.push({ path: "/member/order/index" });
          }
        });
        // 订单列表 返回到 个人中心。
      } else if (this.route.meta.title === "我的订单") {
        this.preventGoBack = false;
        this.$router.push({ path: "/member/index" });

        // 个人中心 返回到 首页。
      } else if (this.route.meta.title === "用户中心") {
        this.preventGoBack = false;
        this.$router.push({ path: "/" });
      } else {
        this.preventGoBack = false;
      }
    },
    onIndexChange(newIndex, oldIndex) {
      // console.log(newIndex, oldIndex);
    }
  }
};
</script>

<style lang="less">
@import "~vux/src/styles/reset.less";
@import "../src/assets/styles/less/public.less";
html,
body {
  height: 100%;
  width: 100%;
  overflow-x: hidden;
}
.page {
  .container {
    background-color: transparent !important;
  }
}
</style>
