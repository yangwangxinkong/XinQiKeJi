<!--  首页-->
<template>
  <div class="page">
      <div class="container">
        <div class="banner pr">
            <swiper :aspect-ratio="360/750" dots-position="center" auto>
                <swiper-item class="swiper-index-img" v-for="(item, index) in swiperlist" :key="index"><img class="image" :src="item.img"></swiper-item>
            </swiper>
            <!--<div class="banner-info pa">
                <p>北京正常代缴倒计时</p>
                <p>9月份剩余</p>
                <p>20天16时55分55秒</p>
                <p>服务热线：400-000-0000</p>
            </div>-->
        </div>
        <!-- 公告 -->
        <!-- <div class="notice">
            <NoticeBar
             text="北京  刘** 成功缴纳社保 2018-09-22 17::40:00"
             :left-icon="leftIcon"></NoticeBar>
        </div> -->
       <div class="notice">
            <div :class="'left-icon'">
                <img :src="leftIcon" >
            </div>
            <marquee :interval="4000">
                <marquee-item  @click.native="onClick()" class="align-middle">
                    北京  刘** 成功缴纳社保 2018-09-22 17::40:00
                </marquee-item>
                <marquee-item  @click.native="onClick()" class="align-middle">
                    北京  刘** 成功缴纳社保 2018-09-22 17::40:00
                </marquee-item>
            </marquee>
        </div>
        <!-- 主体 -->
        <div class="model-user-wrap bg_f">
            <grid :cols="3" :show-lr-borders="false">
                <grid-item :link="grid.link" :label="grid.label" v-for="(grid,index) in GridList" :key="index" v-if="grid.label != '免费咨询'">
                    <img  slot="icon" :src="grid.icon">
                </grid-item>
              <grid-item @click.native="call" :label="grid.label" v-for="(grid,index) in GridList" :key="index" v-if="grid.label == '免费咨询'">
                <img  slot="icon" :src="grid.icon">
              </grid-item>
            </grid>
        </div>


      </div>
      <!--引入底部组件  -->
        <v-footer></v-footer>
  </div>
</template>

<script>
import footer from "@/components/footer/index";
import NoticeBar from "@/components/notice-bar/index";
import {
  Grid,
  GridItem,
  Swiper,
  SwiperItem,
  Marquee,
  MarqueeItem
} from "vux";
import url from "@/api/apiUrl";
import { get, post, execute } from "@/api/server";
export default {
  data() {
    return {
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
        {
          id: 3,
          label: "社保+公积金",
          icon: require("../../assets/homepage_icon_service@2x.png"),
          link: "/order/orderMember?type=SG"
        },
        {
          id: 4,
          label: "自助工具",
          icon: require("../../assets/homepage_icon_tool@2x.png"),
          link: "/instrument/index"
        },
        {
          id: 5,
          label: "参保资料",
          icon: require("../../assets/homepage_icon_help@2x.png"),
          link: "/pre_insurance/index"
        },
        {
          id: 6,
          label: "政策解读",
          icon: require("../../assets/homepage_icon_contruling_study@2x.png"),
          link: "/policy/index"
        },
        {
          id: 7,
          label: "新手帮助",
          icon: require("../../assets/homepage_icon_new_help@2x.png"),
          link: "/help/index"
        },
        {
          id: 8,
          label: "新闻资讯",
          icon: require("../../assets/homepage_icon_xwzx@2x.png"),
          link: "/consulting/list?code=XWZX"
        },
        {
          id: 9,
          label: "免费咨询",
          icon: require("../../assets/homepage_icon_help@2x.png"),
          link: "tel:010-9999-075"
        }
      ],
      swiperlist: [
        //             {
        //                id:1,
        //                img:'https://man.rrb365.com/Uploads/imageOne/2018-08-23/5b7e212f1a04f.png'
        //              },
        //              {
        //                id:2,
        //                img:'https://man.rrb365.com/Uploads/imageOne/2018-08-23/5b7e2197377c5.png'
        //              }
      ]
    };
  },

  components: {
    "v-footer": footer,
    Grid,
    GridItem,
    NoticeBar,
    Swiper,
    SwiperItem,
    Marquee,
    MarqueeItem
  },

  computed: {},

  mounted() {},
  created() {
    this.initData();
  },
  methods: {
    initData() {
      get(url.indexData, {}).then(response => {
        //console.log("Index response:" + JSON.stringify(response));
        if (response.data.result == "00000000") {
          const ads = response.data.data.ads;
          if (ads) {
            this.swiperlist = ads;
          }
          const notice = response.data.data.notice;
          if (notice) {
            this.notice = notice;
          }
          this.serviceCall = response.data.data.serviceCall;
        }
        console.log("swiperlist:" + JSON.stringify(this.swiperlist));
      });
    },
    call() {
      window.location.href = `tel:${this.serviceCall}`
    }
  }
};
</script>
<style lang='less'>
.model-user-wrap {
  .weui-grid__icon + .weui-grid__label {
    margin-top: 10px;
  }
}
</style>
<style  lang='less' scoped>
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
.notice {
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
  position: relative;
  background-color: #fff7cc;
  border-top: 1px solid #fff7cc;
  align-items: center;
  padding:0 12px;
  .left-icon {
    img {
      width: 16px;
      height: 16px;
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
</style>
