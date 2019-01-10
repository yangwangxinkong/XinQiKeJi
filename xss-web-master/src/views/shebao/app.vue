<template>
 <div  class="bg-f">
    <!-- 引用header -->
    <Header></Header>
    <!-- bannner -->
    <div class="index-banner ">
      <div class="swiper-container banner-sliders">
        <div class="swiper-wrapper">
          <div class="swiper-slide" v-for="(item,index) in swiperlist " :key="index" v-if="swiperlist">
            <a :href="item.url">
              <img :src="item.img" :alt="item.name">
            </a>
          </div>
        </div>
      </div>
    </div>
     <Breadcrumb :routerList="routerList" :slotRight="true" title="我要交社保"></Breadcrumb>
   
     <div class="page-content mb20">
       <div class="w1200 bg-white">
           <div class="social-title">
            <h2>我们一起，让社保管理更高效</h2>
            </div>
          <div class="social-service">
              <dl>
                  <dt>省钱</dt>
                  <dd>自营模式</dd>
                  <dd>价格透明，全国统一</dd>
                  <dd>在线管理，降低成本</dd>
                  <dd>升级套餐，享受增值服务</dd>
              </dl>
              <dl>
                  <dt>省心</dt>
                  <dd>服务团队80%以上人资行业从业经验</dd>
                  <dd>4800+社保政策包实时更新</dd>
                  <dd>系统自动提醒，规避断缴风险</dd>
                  <dd>同时提供简版详版账单</dd>
                  <dd>在线归档，随时查阅与工资结算</dd>
              </dl>
              <dl class="small-width">
                  <dt>省力</dt>
                  <dd>全国400+城市网点</dd>
                  <dd>服务标准化，一地签约全国服务</dd>
                  <dd>签约、派单、进度、发票全程在线</dd>
                  <dd>所需工作系统智能化管理</dd>
              </dl>
              <dl>
                  <dt>安全</dt>
                  <dd>业内领先及独创的云+SaaS系统</dd>
                  <dd>采用阿里云，数据加密，权限分级</dd>
                  <dd>工商银行&amp;浙商银行等资金监管</dd>
              </dl>
              <dl>
                  <dt>承诺</dt>
                  <dd>首问责任制，服务出现问题负责到底</dd>
                  <dd>专属顾问，高效沟通</dd>
                  <dd>常规事件，24小时内反馈</dd>
                  <dd>突发事件，2小时内快速响应</dd>
              </dl>
              <dl class="small-width">
                  <dt>工具包</dt>
                  <dd>实时社保计算器</dd>
                  <dd>实时个税计算器</dd>
                  <dd>实时社保材料查询器</dd>
                  <dd>实时社保百科（重磅）</dd>
              </dl>
          </div>
          <div class="social-prodcut">
              <div class="social-title">
                  <h2>社保产品套餐服务详情</h2>
                  <p class="service-tip">服务详情：因各城市政策差异，为了更好地保障您可享受的服务，请联系客服或者销售具体咨询</p>
              </div>
              <div class="product-item-list product-two-item-list product-shebao">
                  <ul class="list-unstyled">
                      <!-- <li class="product-item">
                          <i class="combo-zuan"></i>
                          <h3 class="combo-title">金柚宝</h3>
                          <p class="combo-subtitle">社保专职顾问管理</p>
                          <span class="combo-line"></span>
                          <p class="combo-vip-title">会员费</p>
                          <p class="combo-vip-price">¥ 5880/年</p>
                          <p class="combo-service-title">+</p>
                          <p class="combo-service-price">订制服务内容</p>
                          <a class="btn buy-btn  btn-red" href="javascript:" rel="nofollow">立即咨询</a>
                      </li>
                      <li class="product-item ">
                          <h3 class="combo-title">企业社保通</h3>
                          <p class="combo-subtitle">升级套餐专职顾问</p>
                          <span class="combo-line"></span>
                          <p class="combo-service-info">订制服务内容</p>
                          <a class="btn buy-btn  btn-red" href="javascript:" rel="nofollow">立即咨询</a>
                      </li>
                      <li class="product-item ">
                          <h3 class="combo-title">企业社保通（小微版）</h3>
                          <p class="combo-subtitle">在线自主签约</p>
                          <span class="combo-line"></span>
                          <p class="combo-service-title">服务费</p>
                          <p class="combo-service-price">¥ 29.9/人·月</p>
                          <a class="btn buy-btn  btn-red" href="javascript:" rel="nofollow">立即咨询</a>
                      </li>
                      <li class="product-item ">
                          <h3 class="combo-title">企业社保通（小微版）</h3>
                          <p class="combo-subtitle">在线自主签约</p>
                          <span class="combo-line"></span>
                          <p class="combo-service-title">服务费</p>
                          <p class="combo-service-price">¥ 29.9/人·月</p>
                            <a class="btn buy-btn  btn-red" href="javascript:" rel="nofollow">立即咨询</a>
                      </li> -->
                      <li class="product-item " v-for="(item, index) in sssets" :key="index" v-if="sssets">
                          <h3 class="combo-title">{{item.name}}</h3>
                          <p class="combo-subtitle" v-if="item.subTitle">{{item.subTitle}}</p>
                          <span class="combo-line"></span>
                          <p class="combo-service-title">服务费</p>
                          <p class="combo-service-price">¥ {{item.monthFee}}/人·月</p>
                            <a class="btn buy-btn  btn-red" href="javascript:void(0)" rel="nofollow" @click="btnClick">立即咨询</a>
                      </li>
                  </ul>
              </div>
          </div>
        </div>
      </div>
       <div class="container-fluid cnt-news">
              <div class="newsWrap w1200">
                  <div class="news news-01" v-if="articleLeft">
                      <p>
                          <a class="strong" :href="'news.html?index=3&code='+articleLeft.code">{{articleLeft.name}}</a>
                          <a class="news-more" :href="'news.html?index=3&code='+articleLeft.code">更多</a>
                          <span class="news-line"></span>
                      </p>
                      <hr class="news-hr">
                      <ul>
                        <li v-for="(item, index) in articleLeft.articles" :key="index" v-if="articleLeft.articles">
                          <a :href="'/news-detail.html?index=3&id='+item.id+'&code='+articleLeft.code" target="_blank" :title="item.title">{{item.title}}</a>
                          <span>{{item.createDate|parseTime('{m}-{d}')}}</span>
                        </li>                         
                      </ul>
                  </div>
                   <div class="news news-02" v-if="articleCenter">
                      <p>
                          <a class="strong" :href="'news.html?index=3&code='+articleCenter.code">{{articleCenter.name}}</a>
                          <a class="news-more" :href="'news.html?index=3&code='+articleCenter.code">更多</a>
                          <span class="news-line"></span>
                      </p>
                      <hr class="news-hr">
                      <ul>
                        <li v-for="(item, index) in articleCenter.articles" :key="index" v-if="articleCenter.articles">
                          <a :href="'/news-detail.html?index=3&id='+item.id+'&code='+articleCenter.code" target="_blank" :title="item.title">{{item.title}}</a>
                          <span>{{item.createDate|parseTime('{m}-{d}')}}</span>
                        </li> 
                      </ul>
                  </div>
                   <div class="news news-03" v-if="articleRight">
                      <p>
                          <a class="strong" :href="'news.html?index=3&code='+articleRight.code">{{articleRight.name}}</a>
                          <a class="news-more" :href="'news.html?index=3&code='+articleRight.code">更多</a>
                          <span class="news-line"></span>
                      </p>
                      <hr class="news-hr">
                      <ul>
                        <li v-for="(item, index) in articleRight.articles" :key="index" v-if="articleRight.articles">
                          <a :href="'/news-detail.html?index=3&id='+item.id+'&code='+articleRight.code" target="_blank" :title="item.title">{{item.title}}</a>
                          <span>{{item.createDate|parseTime('{m}-{d}')}}</span>
                        </li>
                      </ul>
                  </div>
              </div> 
          </div>
     <!-- 引入 微信弹窗 -->
     <weixinPopup></weixinPopup>
    <!-- 引入footer -->
    <Footer></Footer>
  </div>
</template>

<script type="text/ecmascript-6">
import Header from "@/components/header/index.vue";
import Breadcrumb from "@/components/breadcrumb/index.vue";
import weixinPopup from "@/components/weixinPopup/index.vue";
import Footer from "@/components/footer/index.vue";
import { get } from "@/modules/js/api/server";
import url from "@/modules/js/api/apiUrl";
import $ from "jquery";
import eventVue from "../../modules/js/eventBus.js";
export default {
  components: { Header, Footer, Breadcrumb, weixinPopup },
  props: {},
  data() {
    return {
      sssets: [],
      articleLeft: undefined,
      articleCenter: undefined,
      articleRight: undefined,
      routerList: [
        {
          value: "缴社保",
          linkUrl: ""
        }
      ],
      swiperlist: [
        {
          id: 1,
          url: "",
          img:
            "",
          name: ""
        }
      ]
    };
  },
  watch: {},
  computed: {},
  methods: {
    getSheBaoIndex() {
      // 编码
      let params = {
        articleCategoryCode1: "SHBJT",
        articleCategoryCode2: "SHBWD",
        articleCategoryCode3: "SHBZX",
        count: 5,
        cityCode: "bjcy",
        feeCategory: "fc0"
      };
      get(url.getShbInfo, params).then(response => {
        if (response.data.result == "00000000") {
          // banner图片
          const ads = response.data.data.ads;
          if (ads) {
            this.swiperlist = ads;
          }

          // 社保套餐
          const sssets = response.data.data.sssets;
          if (sssets) {
            this.sssets = sssets;
          }

          // 文章列表
          const articles = response.data.data.articles;
          if (articles) {
            if(articles[0] != undefined && articles[0] != null) {
              this.articleLeft = articles[0];
            }
            if(articles[1] != undefined && articles[1] != null) {
              this.articleCenter = articles[1];
            }
            if(articles[2] != undefined && articles[2] != null) {
              this.articleRight = articles[2];
            }
          }
        }
      });
    },
    btnClick() {
      //console.log("1111");
      eventVue.$emit("PopShowTap", true);
    },
  },
  created() {
    this.getSheBaoIndex();
  },
  mounted() {

  },
  filters: {
    parseTime(time) {
      var date = new Date(time);
      let fmt = "MM-dd";

      let o = {
        "M+": date.getMonth() + 1,
        "d+": date.getDate()
      };
      for (let k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
          let str = o[k] + "";
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length === 1 ? str : ("00" + str).substr(str.length)
          );
        }
      }
      return fmt;
    }
  }
};
</script>

<style  lang="less">
@import "~@/common/less/common.less";
.page-content {
  .mouldle-detail {
    padding: 20px;
    min-height: 200px;
    box-sizing: border-box;
    p {
      line-height: 20px;
      font-size: 14px;
    }
  }
}
.social-title {
  margin: 50px auto;
  text-align: center;
  h2 {
    margin: 0;
    font-size: 30px;
    color: #666666;
    text-align: center;
  }
  .service-tip {
    font-size: 16px;
    color: #666;
    text-align: center;
    line-height: 2;
  }
}

.social-service {
  width: 1190px;
  height: 450px;
  margin: 0 auto;
  position: relative;
  background: url(https://staticweb.joyowo.com/modules/website/images/ssmsIndex/3.0.0/shebaobg.png)
    top center no-repeat;
  dl {
    float: left;
    width: 320px;
    height: 220px;
    margin-left: 100px;
    dt {
      font-size: 20px;
      font-weight: bold;
      color: #E60014;
      margin-bottom: 20px;
    }
    dd {
      font-size: 16px;
      color: #666;
      line-height: 1.6;
    }
    &.small-width {
      width: 240px;
    }
  }
}

.cnt-news {
  background-color: #fbfbfb;
}

.product-service {
  width: 1190px;
  margin: 0 auto;
}
.service-info {
  width: 269px;
  float: left;
  margin-top: 270px;
  border: 1px solid #c2c2c2;
  border-top-left-radius: 10px;
  border-bottom-left-radius: 10px;
  strong {
    display: block;
    font-size: 16px;
    color: #E60014;
    font-weight: bold;
  }
  span {
    display: block;
    font-size: 14px;
    color: #666666;
  }
  .info-item {
    border-right: 0;
    padding: 12px 8px;
    height: 40px;
    box-sizing: border-box;
    white-space: nowrap;
    &:nth-of-type(even) {
      background-color: #f6f6f6;
    }
    &:last-child {
      margin-bottom: 0;
    }
  }
}
.social-prodcut {
  .product-item-list {
    width: 1200px;
    height: 375px;
    margin: 0 auto;
    overflow: hidden;
    ul {
      overflow: hidden;
      width: 1078px;
      margin: 0 auto;
    }
    p {
      font-size: 16px;
      color: #666;
      line-height: 30px;
    }
    .product-item {
      width: 260px;
      height: 340px;
      float: left;
      background-color: #fff;
      border: 1px solid #d1efcd;
      border-radius: 10px;
      position: relative;
      list-style: none;
      margin-left: 0;
      margin-right: 10px;
      &:last-child {
        margin-right: 0;
      }
      p {
        margin-bottom: 0;
        text-align: center;
        line-height: 24px;
      }
      .combo-service-price {
        margin-bottom: 20px;
      }
      .combo-title {
        font-size: 20px;
        margin: 0;
        margin-top: 50px;
        color: #666;
        text-align: center;
      }
      .combo-subtitle {
        text-align: center;
        color: @color-base;
        font-size: 16px;
      }
      .combo-line {
        display: block;
        width: 46px;
        height: 2px;
        background-color: @color-base;
        margin: 0 auto 15px;
      }
      .combo-service-info {
        margin: 62px 0;
      }
      .buy-btn {
        display: block;
        width: 170px;
        height: 40px;
        padding: 0;
        background-color: #dd3334;
        text-align: center;
        line-height: 40px;
        margin: 0 auto;
        color: #fff;
        font-size: 16px;
        position: absolute;
        bottom: 30px;
        left: 50%;
        margin-left: -85px;
        border: 1px solid transparent;
        border-radius: 4px;
        &:hover {
          background: rgba(221, 51, 51, 0.8);
        }
        .combo-zuan {
          display: block;
          width: 25px;
          height: 23px;
          background: url(https://staticweb.joyowo.com/modules/website/images/index/4.0.0/u332.png)
            no-repeat;
          background-size: 100% auto;
          position: absolute;
          top: 20px;
          left: 50%;
          margin-left: -12px;
        }
        .combo-new {
          position: absolute;
          top: 20px;
          left: 150px;
          width: 44px;
          height: 20px;
          background: url(https://staticweb.joyowo.com/modules/website/images/index/4.0.0/u332.png)
            no-repeat;
        }
      }
    }
  }
}

.newsWrap {
  margin-top: 10px;
  overflow: hidden;
  padding-bottom: 70px;
  width: 1200px;
  margin-bottom: 30px;
  .news {
    float: left;
    height: 270px;
    margin: 0 10px;
    overflow: hidden;
    width: 374px;
    p {
      height: 38px;
      margin: 38px 15px 0;
      position: relative;
      .news-line {
        display: block;
        width: 130px;
        height: 2px;
        background-color: @color-base;
        position: absolute;
        bottom: -1px;
        left: 0;
      }
      .news-more {
        color: #666;
        float: right;
        margin-right: 10px;
        margin-top: 12px;
        text-align: right;
      }
      .strong {
        color: @color-base;
        font-size: 21px;
      }
    }
    hr {
      margin: 0 20px;
    }
    ul {
      padding: 0 20px;
      li {
        line-height: 26px;
        list-style-type: none;
        margin: 8px 0;
        overflow: hidden;
        white-space: nowrap;
        border-bottom: 1px solid #eeeeee;
        &:last-child {
          border-bottom: 0;
        }
        a {
          color: #666;
          float: left;
          overflow: hidden;
          text-overflow: ellipsis;
          width: 270px;
          font-size: 16px;
          .tran;
          &:hover {
            color: @color-base;
          }
        }
        span {
          float: right;
        }
      }
    }
  }
}
</style>
<style >
.jsb_why_box_l {
}
.jsb_why_box_l ul {
  overflow: hidden;
}
.jsb_why_box_l ul li {
  float: left;
  padding-left: 40px;
  margin-bottom: 30px;
}
.jsb_content {
  visibility: visible;
  text-align: center;
}
.jsb_content .jsb_title {
  display: inline-block;
  font-size: 48px;
  color: #02214c;
  margin-top: 110px;
  font-weight: 100;
  margin-bottom: 100px;
}
.jsb_why_box {
  overflow: hidden;
}
.jsb_why_box_l {
  width: 710px;
  float: left;
  overflow: hidden;
  padding-bottom: 100px;
}
.jsb_icon {
  width: 76px;
  height: 76px;
  border-radius: 50%;
  margin-right: 18px;
  position: relative;
  box-sizing: border-box;
  float: left;
  border: 1px solid #e1e1e1;
}
.jsb_icon_r {
  text-align: left;
  color: #969595;
  font-size: 15px;
  overflow: hidden;
}
</style>
