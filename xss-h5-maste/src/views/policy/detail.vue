<!-- 政策解读 -->
<template>
  <div class='page'>
      <div class="container-wrap">
       <tab active-color='#E60014' :line-width=2  v-model="currentValue" custom-bar-width="30px" :scroll-threshold="5">
        <tab-item  :selected="currentValue === index" v-for="(item, index) in tabList" @click="changeTab(item,index)" :key="index">{{item.name}}</tab-item>
      </tab>
      <swiper v-model="currentValue" height="100vh" :show-dots="false">
        <swiper-item v-for="(item, index) in swiperList" :key="index">
          <div class="tab-swiper vux-center">
              <!--<div class="pic-box">-->
                  <!--<img class="image" :src="item.imgsrc" alt="">-->
              <!--</div>-->
              <div class="info-box tc padding-15" v-html="item.content"></div>
          </div>
        </swiper-item>
      </swiper>
    </div>
  </div>
</template>

<script>
import { Tab, TabItem, Swiper, SwiperItem } from "vux";
import url from '@/api/apiUrl'
import {get, post, execute} from '@/api/server'
export default {

  data() {
    return {
        code: undefined,
        tabList: [

        ],
      currentValue: 0,
      swiperList: [
//        {
//          id: 1,
//          imgsrc:
//            "https://m.renrenbao.com/phone/images/homepage_room_city@2x.png",
//          content: ` <h3>北京买房——社保连续缴纳满五年</h3>
//                            <span>
//                         连续3年缴纳社保的认证标准为：<br>自提出申请5年内在北京连续缴纳3年以上的社会保险，补缴无效。
//                            </span> `
//        },
//        {
//          id: 2,
//          imgsrc:
//            "https://m.renrenbao.com/phone/images/homepage_room_city@2x.png",
//          content: ` <h3>上海买房——社保连续缴纳满五年</h3>
//                            <span>
//                         连续3年缴纳社保的认证标准为：<br>自提出申请5年内在上海连续缴纳3年以上的社会保险，补缴无效。
//                            </span> `
//        },
//        {
//          id: 2,
//          imgsrc:
//            "https://m.renrenbao.com/phone/images/homepage_room_city@2x.png",
//          content: ` <h3>广州买房——社保连续缴纳满五年</h3>
//                            <span>
//                         连续3年缴纳社保的认证标准为：<br>自提出申请5年内在广州连续缴纳3年以上的社会保险，补缴无效。
//                            </span> `
//        }
      ]
    };
  },

  components: {
    Tab,
    TabItem,
    Swiper,
    SwiperItem
  },

  computed: {},

  mounted() {},
  created() {
      this.code = this.$route.query && this.$route.query.code;
      console.log("code:" + this.code);
      this.initData();
  },

  methods: {
      initData(){
          get(url.articleCategoryChildrenArticles, {code:this.code}).then(response => {
              //console.log("init data: " + JSON.stringify(response))
              if(response.data.result == "00000000"){
                  const data = response.data.data;
                  if(data){
                      for(let i in data){
                          let tabInfo = {id:data[i].id,name:data[i].name};
                          this.tabList.push(tabInfo);
                          if(data[i].articles && data[i].articles.length >= 1){
                            let article = data[i].articles[0];
                            this.swiperList.push(article);
                          }
                      }
                  }
              }
          });
      },
      changeTab(item, index) {
        this.currentValue = index;
      }
  }
};
</script>
<style lang='less' scoped>
.tab-swiper {
  .pic-box {
    width: 150px;
    height: 150px;
    margin: 50px auto;
  }
}
</style>
