<!--  首页-->
<template>
  <div class="page">
    <div class="container pb0">
      <div class="head-nav-fix" v-if="!isWX">
        <div class="head-nav flex-ui align-items-center space-between">
          <img src="../../assets/index_logo.png" style="width:70px;height:35px " alt>
          <div class="admin_icon" v-if="isLogin">
            <a href="#/member/index" style="display: inherit;">
              <img src="../../assets/touxiang.png">
            </a>
          </div>
          <div class="btn-login-box" v-else>
            <router-link :to="{ path: '/login/index' }" class="btn btn-login">登录</router-link>
          </div>
        </div>
      </div>
      <div class="banner pr" :style="{'padding-top': isWX?'0px':'45px'}">
        <swiper :aspect-ratio="360/750" dots-position="center" auto>
          <swiper-item class="swiper-index-img" v-for="(item, index) in swiperlist" :key="index">
            <img class="image" :src="item.img">
          </swiper-item>
        </swiper>
      </div>
      <div class="model-user-wrap bg_f notice">
        <!-- <div :class="'left-icon'">
          <img :src="leftIcon">
        </div>-->
        <div class="notice-left f12 tc">
          <span>通知</span>
        </div>
        <!--倒计时 -->
        <div class="countdown-box flex1 f12" style="padding: 10px 5px 10px 10px">
          <count-down v-if="noticesShow" v-on:start_callback="countDownS_cb(1)" v-on:end_callback="countDownE_cb(1)" :currentTime="currentTime" :startTime="startDate" :endTime="endDate" :tipTextStart="cityName+'城市代缴开始倒计时'" :tipTextEnd="cityName+'城市代缴倒计时'" :endText="cityName+'城市本月代缴已结束，服务电话：'+serviceCall" :dayTxt="'天'" :hourTxt="'小时'" :minutesTxt="'分钟'" :secondsTxt="'秒'"></count-down>
          <!-- <count-down  :currentTime="currentTime" :startTime="startDate" :endTime="endDate" :tip-text-start="'距离活动结束还有'" :tip-text-end="'截止活动时间'" :end-text="'已结束'" :dayTxt="'天'" :hourTxt="'时'" :minutesTxt="'分'" :secondsTxt="'秒'"  v-on:start_callback="countDownS_cb(3)" v-on:end_callback="countDownE_cb(3)"></count-down> -->
        </div>
      </div>
      <!--行业优势-->
      <div class="model-user-wrap summary bg_f weui-grids mt10">
        <!-- <flexbox>
          <flexbox-item>
            <div class="flex-item tc" style="text-align:left;">
              {{timespan}}</div>




          </flexbox-item>
        </flexbox>-->
        <flexbox>
          <flexbox-item>
            <div class="flex-item tc">
              <span class="tag"></span>
              {{slogans[0]}}
            </div>
          </flexbox-item>
          <flexbox-item>
            <div class="flex-item tc">
              <span class="tag"></span>
              {{slogans[1]}}
            </div>
          </flexbox-item>
          <flexbox-item>
            <div class="flex-item tc">
              <span class="tag"></span>
              {{slogans[2]}}
            </div>
          </flexbox-item>
        </flexbox>
      </div>
      <!-- 主体 -->
      <div class="model-user-wrap nav-entry bg_f">
        <grid :show-vertical-dividers="false">
          <grid-item class="f14" :link="grid.link" :label="grid.label" v-for="(grid,index) in GridList" :key="index" v-if="grid.label != '免费咨询'">
            <img slot="icon" :src="grid.icon">
          </grid-item>
          <grid-item class="f14" @click.native="call" :label="grid.label" v-for="(grid,index) in GridList" :key="index" v-if="grid.label == '免费咨询'">
            <img slot="icon" :src="grid.icon">
          </grid-item>
        </grid>
      </div>
      <!-- 公告 -->
      <!-- <div class="notice">
        <div :class="'left-icon'">
          <img :src="leftIcon">
        </div>
        <marquee :interval="4000">
          <marquee-item @click.native="onClick()" class="align-middle">
            北京 刘** 成功缴纳社保 2018-09-22 17::40:00
          </marquee-item>
          <marquee-item @click.native="onClick()" class="align-middle">
            北京 何** 成功缴纳社保 2018-09-22 17::40:00
          </marquee-item>
        </marquee>
      </div>-->
      <!-- 套餐区域 -->
      <div class="package bg_f mt10">
        <flexbox :gutter="0" wrap="wrap">
          <flexbox-item :span="1/2" v-for="(activity,index) in activities.slice(0,2)" :key="index" v-if="activities">
            <div class="flex-item">
              <router-link :to="{path:activity.url + '&id='+activity.id}">
                <dl>
                  <dt>
                    <img :src="activity.bannerWx" class="image">
                  </dt>
                  <dd class="nav_dd1 ellipsis">{{activity.mainTitle}}</dd>
                  <dd class="nav_dd2 ellipsis2">{{activity.subTitle}}</dd>
                </dl>
              </router-link>
            </div>
          </flexbox-item>
        </flexbox>
        <flexbox :gutter="0" wrap="wrap">
          <flexbox-item :span="1/2" v-for="(activity,index) in activities.slice(2,4)" :key="index" v-if="activities">
            <div class="flex-item">
              <router-link :to="{path:activity.url + '&id='+activity.id}">
                <dl>
                  <dt>
                    <img :src="activity.bannerWx" class="image">
                  </dt>
                  <dd class="nav_dd1 ellipsis">{{activity.mainTitle}}</dd>
                  <dd class="nav_dd2 ellipsis2">{{activity.subTitle}}</dd>
                </dl>
              </router-link>
            </div>
          </flexbox-item>
        </flexbox>
      </div>

      <!-- 政策解读 -->
      <div class="model-zhengce-wrap mt10">
        <!-- <group-title style="margin-top:0;padding-left:0px;">促销活动</group-title> -->
        <!-- <flexbox>
          <flexbox-item>
            <div class="flex-item tc">
              <router-link :to="{ path: '/consulting/list', query: {code: 'XWZX'} }">
                <img src="../../assets/homepage_calculator_house@2x.png" alt="">
                <p class="text">买房政策</p>
              </router-link>
            </div>
          </flexbox-item>
          <flexbox-item>
            <div class="flex-item tc">
              <router-link :to="{ path: '/consulting/list', query: {code: 'XWZX'} }">
                <img src="../../assets/homepage_calculator_car@2x.png" alt="">
                <p class="text">买房政策</p>
              </router-link>
            </div>
          </flexbox-item>
          <flexbox-item>
            <div class="flex-item tc">
              <router-link :to="{ path: '/consulting/list', query: {code: 'XWZX'} }">
                <img src="../../assets/homepage_calculator_school@2x.png" alt="">
                <p class="text">上学政策</p>
              </router-link>
            </div>
          </flexbox-item>
          <flexbox-item>
            <div class="flex-item tc">
              <router-link :to="{ path: '/consulting/list', query: {code: 'XWZX'} }">
                <img src="../../assets/homepage_calculator_lh@2x.png" alt="">
                <p class="text">落户政策</p>
              </router-link>
            </div>
          </flexbox-item>
        </flexbox>-->
        <swiper :aspect-ratio="160/690" dots-position="center" auto>
          <swiper-item class="swiper-index-img" v-for="(item, index) in swiperZclist" :key="index">
            <!-- <a :href="item.url"> -->
            <router-link :to="{path:item.url}">
              <img class="image" :src="item.img">
              <!-- </a> -->
            </router-link>
          </swiper-item>
        </swiper>
      </div>
      <!-- 新闻资讯-->
      <div class="weui-panel weui-panel_access">
        <group-title class="flex-ui align-items-center bg_f" style="border-bottom: 1px solid #f5f5f5;padding-bottom: 10px; margin-bottom:0">新闻资讯
          <div class="fr f12 flex1 tr" style="font-weight:normal">
            <router-link :to="{path:'/consulting/list?code=XWZX'}">更多></router-link>
          </div>
        </group-title>
        <!-- <div class="weui-panel__bd">
          <router-link :to="{path:'/article/detail?id='+article.id}" class="weui-media-box weui-media-box_appmsg" v-for="(article,index) in articles" :key="index">
            <div class="weui-media-box__bd">
              <p class="weui-media-box__desc grey_3">{{article.title}}</p>
              <h4 class="weui-media-box__title  flex-ui align-items-center space-between f12 mt10 grey_9 clearfix">
                <span class="news_tag">{{article.articleCategory.name}}</span>
                <span class="flex1 time tr">{{article.createDate}}</span>
              </h4>
            </div>
          </router-link>
        </div>-->
        <!--新闻推送-->
        <!-- <div class="weui_panel_access news-list">
          <div class="weui_panel" v-for="(article,index) in articles" :key="index">
            <router-link :to="{path:'/article/detail?id='+article.id}">
              <div class="weui_panel_bd">
                <div class="weui_media_box weui_media_text">
                  <div class="card-content">
                    <img class="card-img image" :src="article.cover" alt>
                    <p
                      class="weui_media_desc ellipsis2 mt5"
                    >【{{article.articleCategory.name}}】{{article.title}}</p>
                  </div>
                </div>
              </div>
              <div class="weui_panel_ft">查看全文</div>
            </router-link>
          </div>
        </div>-->
        <ul class="news-list">
          <li v-for="(article,index) in articles" :key="index">
            <router-link :to="{path:'/article/detail?id='+article.id}" class="flex-ui align-items-center">
              <div class="news_title flex1">
                <span class="ellipsis">{{article.title}}</span>
              </div>
              <div class="news_time">
                <span>2018-12-07</span>
              </div>
            </router-link>
          </li>
        </ul>
      </div>
      <div class="weui-footer weui-footer_fixed-bottom tc">
        <p class="weui-footer__links">
          <a href="javascript:home();" class="weui-footer__link">小豆社保</a>
        </p>
        <p class="weui-footer__text">专业社保公积金代理服务
          <br>400-9999-075
        </p>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import {
  Grid,
  GridItem,
  Swiper,
  SwiperItem,
  Marquee,
  MarqueeItem,
  Flexbox,
  FlexboxItem,
  GroupTitle
} from "vux";
import NoticeBar from "@/components/notice-bar/index";
import CountDown from "@/components/countdown/index";
import url from "@/api/apiUrl";
import { get, post, execute, isWeiXin } from "@/api/server";
import storage from "@/utils/common";
export default {
  data() {
    return {
      noticesShow: false,
      cityName: "北京",
      currentTime: undefined,
      startDate: undefined,
      endDate: undefined,
      isLogin: false,
      isWX: false,
      ads: [],
      notice: {},
      serviceCall: undefined,
      leftIcon: require("../../assets/notice-left.png"),
      GridList: [
        {
          id: 1,
          label: "缴社保",
          icon: require("../../assets/homepage_icon_sb@2x.png"),
          link: "/order/orderMember?type=SB"
        },
        {
          id: 2,
          label: "缴公积金",
          icon: require("../../assets/homepage_icon_gjj@2x.png"),
          link: "/order/orderMember?type=GJ"
        },
        // {
        //   id: 3,
        //   label: "社保+公积金",
        //   icon: require("../../assets/homepage_icon_service@2x.png"),
        //   link: "/order/orderMember?type=SG"
        // },
        {
          id: 3,
          label: "自助工具",
          icon: require("../../assets/homepage_icon_tool@2x.png"),
          link: "/instrument/index"
        },
        {
          id: 4,
          label: "参保资料",
          icon: require("../../assets/homepage_icon_document@2x.png"),
          link: "/pre_insurance/index"
        },
        // {
        //   id: 6,
        //   label: "政策解读",
        //   icon: require("../../assets/homepage_icon_sb@2x.png"),
        //   link: "/policy/index"
        // },
        // {
        //   id: 7,
        //   label: "新手帮助",
        //   icon: require("../../assets/homepage_icon_service@2x.png"),
        //   link: "/help/index"
        // },
        // {
        //   id: 8,
        //   label: "新闻资讯",
        //   icon: require("../../assets/homepage_icon_tool@2x.png"),
        //   link: "/consulting/list?code=XWZX"
        // },
        {
          id: 5,
          label: "免费咨询",
          icon: require("../../assets/homepage_icon_help@2x.png"),
          link: "tel:010-9999-075"
        }
      ],
      swiperlist: [],
      swiperZclist: [
        // {
        //   id: 1,
        //   img:
        //     "https://man.rrb365.com/Uploads/imageOne/2018-08-22/5b7d0e9c484ca.png"
        // },
        // {
        //   id: 2,
        //   img:
        //     "https://man.rrb365.com/Uploads/imageOne/2018-08-23/5b7e21f289f2d.png"
        // }
      ],
      articles: [],
      activities: [],
      slogans: ["17年专业服务", "全天在线缴纳", "保障隐私安全"],
      timespan: undefined,
      appId: "wxe91fe766376f1f83"
    };
  },

  components: {
    Grid,
    GridItem,
    NoticeBar,
    Swiper,
    SwiperItem,
    Marquee,
    MarqueeItem,
    Flexbox,
    FlexboxItem,
    GroupTitle,
    CountDown
  },

  computed: {},

  mounted() {
    //this.getJsapiSign();
  },
  created() {
    let member = storage.get("member");
    if (member != null && member != undefined) {
      this.isLogin = true;
    }
    if (isWeiXin()) {
      this.isWX = true;
    }
    this.initData();
  },
  methods: {
    initUrl() {
      this.$reqSignByUrl(location.href.split("#")[0], this.sayHello);
    },
    sayHello(url) {
      console.log("Hello:", url);
      this.url = url;
    },
    getJsapiSign() {
      this.initUrl();
      get(url.jsapiSign, { url: this.url }).then(response => {
        if (response.data.result == "00000000") {
          console.log(JSON.stringify(response.data.data));
          let signInfo = response.data.data;
          signInfo.appId = this.appId;
          this.$initWXConfig(signInfo, true);
          let shareParams = {
            title: "我的分享",
            link: this.url,
            image:
              "http://m.xiaodoushebao.com/static/img/index_logo.2b34754.png",
            abstract: "我的分享描述"
          };
          this.$shareWeChat(shareParams);
          //alert(this.ticket);
        }
      });
    },
    initData() {
      this.getAds();
      this.getArticles();
    },
    call() {
      window.location.href = `tel:${this.serviceCall}`;
    },
    getAds() {
      get(url.indexData, {}).then(response => {
        //console.log("Index response:" + JSON.stringify(response));
        if (response.data.result == "00000000") {
          const ads = response.data.data.ads;
          if (ads) {
            this.swiperlist = ads;
          }
          const consultingAds = response.data.data.consultingAds;
          if (consultingAds) {
            this.swiperZclist = consultingAds;
          }
          // const notice = response.data.data.notice;
          // if (notice) {
          //   this.articles = notice;
          // }
          this.serviceCall = response.data.data.serviceCall;
          if (response.data.data.slogans) {
            this.slogans = response.data.data.slogans;
          }

          //this.currentTime = new Date().getTime();
          this.startDate = response.data.data.beginDate;
          this.currentTime = this.startDate;
          this.endDate = response.data.data.endDate;
          this.noticesShow = true;

          // let surrenderCountDown = response.data.data.surrenderCountDown;
          // if (surrenderCountDown) {
          //   this.setTimeSpan(surrenderCountDown);
          // }

          if (response.data.data.activities) {
            this.activities = response.data.data.activities;
          }
        }
        //console.log("swiperlist:" + JSON.stringify(this.swiperlist));
      });
    },
    getArticles() {
      get(url.indexArticlesData, { code: "SHBJT", count: 5 }).then(response => {
        //console.log("init data: " + JSON.stringify(response))
        if (response.data.result == "00000000") {
          this.articles = response.data.data;
        }
      });
    },

    //时间开始的回调
    countDownS_cb: function(a) {
      //console.log('callBack--'+a+'--开始倒计时结束回调');
    },
    ///时间结束的回调
    countDownE_cb: function(a) {
      //console.log('callBack--'+a+'--活动剩余倒计时结束回调');
    },

    //XX城市代缴倒计时：XX天XX时XX分XX秒
    setTimeSpan(endtime) {
      if (endtime == undefined) {
        return;
      }
      let self = this;
      setInterval(function() {
        //self.timespan = getTimeSpan(endtime);
        let nowtime = new Date();
        endtime = endtime.replace(/\-/g, "/");
        let time = new Date(endtime) - nowtime;

        if (time <= 0) {
          self.timespan = self.cityName + "城市代缴时间已过。";
        } else {
          let day = parseInt(time / 1000 / 60 / 60 / 24);
          let hour = parseInt((time / 1000 / 60 / 60) % 24);
          let minute = parseInt((time / 1000 / 60) % 60);
          let seconds = parseInt((time / 1000) % 60);
          self.timespan =
            self.cityName +
            "城市代缴倒计时：" +
            day +
            "天" +
            hour +
            "小时" +
            minute +
            "分" +
            seconds +
            "秒";
        }
      }, 1000);
    }
  }
};
</script>
<style lang='less'>
.nav-entry {
  .weui-grids {
    &:before {
      left: 15px;
      right: 15px;
    }
  }
}
.model-user-wrap {
  .weui-grid {
    padding: 15px 10px;
  }
  .weui-grid__icon {
    width: 40px;
    height: 40px;
  }
  .weui-grid__icon + .weui-grid__label {
    margin-top: 10px;
  }
  .weui-grid__label {
    font-size: 13px;
  }
  .weui-grid:before {
    border-right: none;
  }
  .weui-grids:after {
    border-left: none;
  }
}
.model-zhengce-wrap {
  .vux-slider {
    overflow: inherit;
    .vux-indicator {
      bottom: 0px;
    }
  }
}
.summary {
  &.weui-grids:after {
    border-left: 0;
  }
  .vux-flexbox-item {
    .flex-item {
      position: relative;
      &:after {
        content: " ";
        position: absolute;
        top: 50%;
        margin-top: -0.16rem;
        right: 0;
        width: 1px;
        height: 0.32rem;
        border-right: 1px solid #d9d9d9;
        color: #d9d9d9;
        -webkit-transform-origin: 0 0;
        transform-origin: 0 0;
        -webkit-transform: scaleX(0.5);
        transform: scaleX(0.5);
      }
    }
    &:nth-child(1) {
      .flex-item {
        span {
          width: 0.267rem;
          height: 0.56rem;
          background: url(../../assets/summary01.png) left center no-repeat;
          background-size: 100% 100%;
        }
      }
    }
    &:nth-child(2) {
      .flex-item {
        span {
          width: 0.48rem;
          height: 0.453rem;
          background: url(../../assets/summary02.png) left center no-repeat;
          background-size: 100% 100%;
        }
      }
    }
    &:nth-child(3) {
      .flex-item {
        &::after {
          width: 0;
          border-right: 0;
        }
        span {
          width: 0.4rem;
          height: 0.507rem;
          background: url(../../assets/summary03.png) left center no-repeat;
          background-size: 100% 100%;
        }
      }
    }
  }
}
</style>
<style  lang='less' scoped>
@import "~vux/src/styles/weui/widget/weui_panel/weui_panel";
@import "~vux/src/styles/weui/widget/weui_media_box/weui_media_box";
.head-nav-fix {
  height: 45px;
  width: 100%;
  max-width: 750px;
  background: white;
  position: fixed;
  display: block;
  overflow: hidden;
  top: 0;
  z-index: 100;
  border-bottom: 1px solid #cccccc;
  .head-nav {
    width: 100%;
    padding: 5px 15px;
  }

  .btn-login-box {
    .btn-login {
      color: white;
      background: @color_base;
      z-index: 10;
      text-align: center;
      border-radius: 5px;
      cursor: pointer;
      padding: 3px 10px;
    }
  }
}

.banner {
  .banner-info {
    top: 0;
    right: 0;
    bottom: 0;
    text-align: right;
    z-index: 9;
    color: #fff;
    padding-right: 10px;
    p:first-child {
      padding-top: 45px;
    }
  }
}

//通知
.notice {
  .notice-left {
    width: 0.933rem;
    float: left;
    line-height: 0;
    margin-top: -4px;
    span {
      background: url(../../assets/icon-notice.png) no-repeat;
      background-size: 100% 100%;
      font-size: 0;
      width: 0.933rem;
      height: 0.48rem;
      display: inline-block;
    }
  }
}

//行业趋势
.summary {
  .flex-item {
    padding: 10px 5px;
    font-size: 12px;
    display: flex;
    align-items: center;
    justify-content: center;

    span.tag {
      // width: 6px;
      // // height: 6px;
      // border-radius: 50%;
      display: inline-block;
      // background: @color_base;
      margin-right: 8px;
    }
  }
}
.notice {
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
  position: relative;
  background-color: #fff;
  border-bottom: 1px solid #f5f5f5;
  align-items: center;
  padding: 0 12px;
  .left-icon {
    img {
      width: 16px;
      height: 16px;
    }
  }
}
//套餐区域
.package {
  .vux-flexbox {
    position: relative;
    &:last-child:after {
      content: " ";
      position: absolute;
      top: 0;
      right: 15px;
      height: 1px;
      border-top: 1px solid #d9d9d9;
      color: #d9d9d9;
      -webkit-transform-origin: 0 0;
      transform-origin: 0 0;
      -webkit-transform: scaleY(0.5);
      transform: scaleY(0.5);
      left: 15px;
    }
  }
  .vux-flexbox-item {
    padding: 15px;
    position: relative;
    &:nth-child(odd) {
      &:after {
        content: " ";
        position: absolute;
        top: 50%;
        right: 0;
        width: 1px;
        color: #d9d9d9;
        border-right: 1px solid #d9d9d9;
        transform-origin: 0 100%;
        transform: scaleX(0.5);
        height: 40px;
        margin-top: -20px;
      }
    }
    &:nth-child(2n) {
    }
    .flex-item {
      background-color: #fff;
      max-height: 95px;
      width: 100%;
      position: relative;
      dl {
        width: 100%;
        dt {
          position: absolute;
          right: 0px;
          width: 1.333rem;
          height: 1.333rem;
          top: 50%;
          margin-top: -0.667rem;
        }
        dd {
          margin-right: 45px;
          &.nav_dd1 {
            color: #2d2e46;
            font-size: 16px;
          }
          &.nav_dd2 {
            margin-top: 2px;
            font-size: 12px;
            color: #81839f;
          }
        }
      }
    }
  }
}
//政策解读
.model-zhengce-wrap {
  padding: 00px 12px 0px;
  .flex-item {
    padding: 10px;
    box-shadow: 1px 4px 5px #f5f5f5;
    img {
      width: 42px;
      height: 42px;
      margin: 0 auto;
    }
    p.text {
      font-size: 12px;
      margin-top: 10px;
    }
  }
}
.vux-marquee {
  flex: 1;
  color: #f85;
  font-size: 12px;
  line-height: 1.5;

  padding: 0 12px;

  li {
    height: 35px;
    line-height: 35px;
  }
}
.news_tag {
  border: 1px solid @color_base;
  color: @color_base;
  padding: 0px 4px;
  font-size: 12px;
}
//新闻推送
.news-list {
  position: relative;
  overflow: hidden;
  font-size: 14px;
  position: relative;
  overflow: hidden;
  margin: 0px 15px 0;
  li {
    padding: 10px 0;
    position: relative;
    &::after {
      content: " ";
      position: absolute;
      right: 0;
      height: 1px;
      bottom: 0;
      border-bottom: 1px solid #d9d9d9;
      color: #d9d9d9;
      -webkit-transform-origin: 0 0;
      transform-origin: 0 0;
      -webkit-transform: scaleY(0.5);
      transform: scaleY(0.5);
      left: 0px;
    }
    &:last-child::after {
      height: 0;
      border-bottom: 0;
    }
    .news_title {
      color: rgba(45, 46, 70, 0.8);
      padding-right: 5px;
    }
    .news_time {
      color: #b2b4c6;
    }
  }
  .weui_panel {
    margin-top: 0;
    margin-bottom: 10px;
    background-color: #fff;
    border-radius: 2px;
    border: 1px solid #e5e5e5;
  }
}
.admin_icon {
  position: absolute;
  top: 0.133rem;
  right: 0.2rem;
  width: 0.933rem;
  height: 0.933rem;
  line-height: 0.933rem;
  border-radius: 50%;
  z-index: 10;
  border: 2px solid #eee;
  text-align: center;
  vertical-align: middle;
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
  -webkit-box-pack: center;
  -webkit-justify-content: center;
  justify-content: center;
  -webkit-box-align: center;
  -webkit-align-items: center;
  align-items: center;
}
.admin_icon img {
  margin: 0 auto;
  border-radius: 50%;
  width: 0.933rem;
  height: 0.933rem;
}

div.weui-grid__icon {
  width: 38px;
  height: 38px;
  margin: 0 auto;
}

label.model-user-wrap.weui-grid__label {
  font-size: 12px;
}

.module_x {
  padding: 0.53rem 0.4rem 0.27rem;
  background: white;
  margin-top: 0.27rem;
}

.weui-grids {
  position: relative;
  overflow: hidden;
}

//页脚
.weui-footer {
  color: #808080;
  font-size: 14px;
  text-align: center;
  padding: 10px 15px;
  .weui-footer__link {
    font-family: HYk1gj;
    font-size: 20px;
    color: rgba(178, 180, 198, 0.5);
    letter-spacing: 0.24px;
  }
  .weui-footer__text {
    font-family: PingFangSC-Light;
    font-size: 13px;
    color: rgba(178, 180, 198, 0.5);
    letter-spacing: 0.16px;
  }
}
</style>
