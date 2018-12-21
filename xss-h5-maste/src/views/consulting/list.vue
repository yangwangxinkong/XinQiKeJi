<!--  资讯-->
<template>
  <div class="page">
    <div class="container">
      <!-- tab切换 -->
      <div class="model-tab-wrap bg_f">
        <tab active-color='#E60014' :line-width=2 v-model="currentValue" custom-bar-width="60px" :scroll-threshold="5">
          <tab-item :selected="currentValue === index" v-for="(item, index) in tabList" @click.native="changeTab(item,index)" :key="index">{{item.name}}</tab-item>
        </tab>
      </div>
      <!-- list -->
      <div class="weui-panel weui-panel_access">

        <div class="weui-panel__bd">
          <router-link :to="{path:'/article/detail?id='+article.id}" class="weui-media-box weui-media-box_appmsg" v-for="(article,index) in articles" :key="index">
            <div class="weui-media-box__hd"><img :src="article.cover" alt="" class="weui-media-box__thumb image"></div>
            <div class="weui-media-box__bd">
              <p class="weui-media-box__desc grey_3">{{article.title}}</p>
              <h4 class="weui-media-box__title f12 mt5 grey_9">{{article.createDate}}</h4>
            </div>
          </router-link>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { Tab, TabItem } from "vux";
import { Panel, Group } from "vux";
import url from "@/api/apiUrl";
import { get, post, execute } from "@/api/server";
export default {
  data() {
    return {
      currentValue: 0,
      tabList: [],
      articles: []
    };
  },

  components: {
    Panel,
    Group,
    Tab,
    TabItem
  },

  computed: {},

  mounted() {},
  created() {
    this.code = this.$route.query && this.$route.query.code;
    //console.log("code:" + this.code);
    if (this.code === undefined || this.code === null) {
      this.code = "XWZX";
    }
    this.initData();
  },
  methods: {
    initData() {
      get(url.articleCategoryChildrenArticles, { code: this.code }).then(
        response => {
          //console.log("init data: " + JSON.stringify(response))
          if (response.data.result == "00000000") {
            const data = response.data.data;
            this.tabList = data;
            if (this.tabList[0].articles) {
              this.articles = this.tabList[0].articles;
            }
            //          if(data){
            //            for(let i in data){
            //              let tabInfo = {id:data[i].id,name:data[i].name};
            //              this.tabList.push(tabInfo);
            ////              if(data[i].articles && data[i].articles.length >= 1){
            ////                let article = data[i].articles[0];
            ////                this.swiperList.push(article);
            ////              }
            //            }
            //          }
          }
        }
      );
    },
    changeTab(item, index) {
      console.log("item: " + JSON.stringify(item));
      this.articles = [];
      if (item.articles) {
        this.articles = item.articles;
      }
      this.currentValue = index;
    }
  }
};
</script>
<style lang='less' scoped>
@import "../../assets/styles/less/public.less";
@import "~vux/src/styles/weui/widget/weui_panel/weui_panel";
@import "~vux/src/styles/weui/widget/weui_media_box/weui_media_box";
.model-tab-wrap {
  .filter-tab {
    .tab-item {
      .f14;
      height: 45px;
      line-height: 45px;
      position: relative;
      &:not(:last-child):after {
        content: " ";
        position: absolute;
        top: 0;
        width: 1px;
        bottom: 0;
        color: #c7c7c7;
        right: 0;
        border-right: 1px solid #c7c7c7;
        -webkit-transform-origin: 100% 0;
        transform-origin: 100% 0;
        -webkit-transform: scaleX(0.5);
        transform: scaleX(0.5);
      }
    }
  }
}
</style>
