<template>
     <tabbar @on-index-change="onIndexChange" slot="bottom" style="background-color: #ffffff;">
        <tabbar-item   :selected="$route.path == tab.href" :link="tab.href" v-for="(tab,index) in navConfig" :key="index">
            <img slot="icon" :src="tab.icon">
            <img slot="icon-active" :src="tab.iconActive">
            <span slot="label">{{tab.name}}</span>
        </tabbar-item>
    </tabbar>
</template>

<script>
import { Tabbar, TabbarItem } from "vux";
 let navConfig = [{
      name: '首页',
      icon: require('../../assets/tabbar/home.png'),
      iconActive:require('../../assets/tabbar/home-active.png'),
      href: '/'
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
      name: '缴费',
      icon: require('../../assets/tabbar/tool.png'),
      iconActive:require('../../assets/tabbar/tool-active.png'),
      href: '/order/orderMember'
    },

    {
      name: '福利',
      icon: require('../../assets/tabbar/consulting.png'),
      iconActive:require('../../assets/tabbar/consulting-active.png'),
      href: '/product/list'
    },

    {
      name: '我的',
      icon: require('../../assets/tabbar/my.png'),
      iconActive:require('../../assets/tabbar/my-active.png'),
      href: '/member/index'
    }]
export default {
  data() {
    return {
      navConfig
    };
  },
  components: {
    Tabbar,
    TabbarItem
  },
  methods: {
    onIndexChange(newIndex, oldIndex) {
      // console.log(newIndex, oldIndex);
    }
  }
};
</script>


