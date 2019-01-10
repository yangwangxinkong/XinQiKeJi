<template>
  <div class="header-conent clearfix">
    <!-- 导航栏 -->
    <div class="header-nav-content  bg-white">
      <div class="w1200 pr clearfix">
        <div class="nav-content-left fl">
          <div class="logo-box">
            <a href="index.html">
              <img class="header-logo-png image" src="../../assets/images/logo_trans.png">
            </a>
          </div>
        </div>
        <ul class="nav-content-center clearfix">
          <li :class="{'active':curIndex==index}" v-for="(nav,index) in navConfig" :key="index" @click="changeNav(nav,index)">
            <a href="javascript:void(0);" class="navMenu city-site" :data-url="nav.url">{{nav.name}}</a>
          </li>
        </ul>
        <div class="nav-content-right">
          <!-- <div class="qrcode-box">
            <img src="https://www.rrb365.com/images/www/ziliao/ios.jpg" class="image" alt="">
          </div> -->
          <div  class="header_content_phone">
            <div class="phone_img">
             <span>咨询热线：{{serviceCall}}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import { get } from "@/modules/js/api/server";
import url from "@/modules/js/api/apiUrl";
import qs from "qs";
let { index } = qs.parse(location.search.substr(1));
let navConfig = [
  { id: 1, name: "首页", url: "index.html" },
  { id: 1, name: "缴社保", url: "shebao.html" },
  { id: 1, name: "缴公积金", url: "gongjijin.html" },
  { id: 1, name: "新闻资讯", url: "news.html" },
  { id: 1, name: "帮助中心", url: "help.html" }
];
export default {
  components: {},
  props: {},
  data() {
    return {
      serviceCall: "",
      navConfig,
      curIndex: parseInt(index) || 0
    };
  },
  watch: {},
  computed: {},
  methods: {
    getServiceCall(){
      get(url.serviceCall, {}).then(response => {
        //console.log("Index response:" + JSON.stringify(response));
        if (response.data.result == "00000000") {
          this.serviceCall = response.data.data.serviceCall;
        }
        //console.log("swiperlist:" + JSON.stringify(this.swiperlist));
      });
    },

    changeNav(nav, index) {
      window.location.href = `${nav.url}?index=${index}`;
      console.log(`${nav.url}?index=${index}`);
    }
  },
  created() {
    this.getServiceCall();
  },
  mounted() {}
};
</script>

<style scoped lang="less">
</style>
