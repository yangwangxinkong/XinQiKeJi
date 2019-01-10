<template>
  <div class="page bg-f5">
    <!-- 引用header -->
    <Header></Header>
    <!-- banner -->
    <div class="index-banner ">
      <div class="w1200 pr">
        <div class="banner-left pa">
          <div class="banner-left-t" v-for="(item,index) in activitylist " :key="index" v-if="activitylist">
            <h4 class="banner-left-title" v-if="context" v-html="context"></h4>
            <img :src="item.img" class="image" alt="">
            <!--<p class="f14 mt5">
              <span>关注</span>
              <span class="color-base">“小豆社保”</span>
              <span>微信公众号</span>
            </p>-->
          </div>

        </div>
      </div>
      <div class="swiper-container banner-sliders">
        <div class="swiper-wrapper">
          <div class="swiper-slide" v-for="(item,index) in swiperlist " :key="index" v-if="swiperlist">
            <a :href="item.url">
              <img :src="item.img" alt="">
            </a>
          </div>
        </div>
        <!-- Add Pagination -->
        <div class="swiper-pagination"></div>
      </div>
    </div>
    <div class="page-content  bg-white  mb20">
      <!-- <div class="w1200 jisuanqi clearfix ">
        <div class="pull-left fl bg-white border">
          <div class="channel-title border_b">
            <h4 class="f18">社保计算器</h4>
          </div>
          <div class="div-info">
            <ul class="clearfix">
              <li class="fl">
                <label class="label-name f14">选择城市</label>
                <select @change="onChange" v-model="postForm.city.id">
                  <option value="0">请选择</option>
                  <option :value="item.value" v-for="(item,index) in cityList " :key="index">{{item.name}}</option>
                </select>
              </li>
              <li class="fl">
                <label class="label-name f14">个人缴纳</label>
                <span class="f14 grey-6">￥{{shbPersonAmount}}</span>
              </li>
              <li class="fl">
                <label class="label-name f14">选择类型</label>
                <select v-model="postForm.residenceType.id">
                  <option value="0">请选择</option>
                  <option :value="item.key" v-for="(item,index) in personalType " :key="index">{{item.value}}</option>
                </select>
              </li>
              <li class="fl">
                <label class="label-name f14">企业缴纳</label>
                <span class="f14 grey-6">￥{{shbCompanyAmount}}</span>
              </li>
              <li class="fl">
                <label class="label-name f14">社保基数</label>
                <span class="f14 grey-6">{{postForm.socialBase}}</span>
              </li>
              <li class="fl">
                <label class="label-name f14">总缴纳</label>
                <span class="f14 grey-6">￥{{shbAmount}}</span>
              </li>
            </ul>
          </div>
          <div class="div-action">
            <a href="javascript:void(0)" class="btn btn-orange btn-radius first" @click="caculateShb">开始计算</a>
            <a href="javascript:void(0)" class="btn btn-orange btn-line btn-radius last" @click="btnClick">我要缴社保</a>
          </div>
        </div>
        <div class="pull-right fr  bg-white border">
          <div class="channel-title border_b">
            <h4 class="f18">公积金计算器</h4>
          </div>
          <div class="div-info">
            <ul class="clearfix">
              <li class="fl">
                <label class="label-name f14">选择城市</label>
                <select @change="onChange" v-model="postForm.city.id">
                  <option value="0">请选择</option>
                  <option :value="item.value" v-for="(item,index) in cityList " :key="index">{{item.name}}</option>
                </select>
              </li>
              <li class="fl">
                <label class="label-name f14">个人缴纳</label>
                <span class="f14 grey-6">￥{{gjPersonAmount}}</span>
              </li>
              <li class="fl">
                <label class="label-name f14">选择类型</label>
                <select v-model="postForm.residenceType.id">
                  <option value="0">请选择</option>
                  <option :value="item.key" v-for="(item,index) in personalType " :key="index">{{item.value}}</option>
                </select>
              </li>
              <li class="fl">
                <label class="label-name f14">企业缴纳</label>
                <span class="f14 grey-6">￥{{gjCompanyAmount}}</span>
              </li>
              <li class="fl">
                <label class="label-name f14">社保基数</label>
                <span class="f14 grey-6">{{postForm.providentBase}}</span>
              </li>
              <li class="fl">
                <label class="label-name f14">总缴纳</label>
                <span class="f14 grey-6">￥{{gjAmount}}</span>
              </li>
            </ul>
          </div>
          <div class="div-action">
            <a href="javascript:void(0)" class="btn btn-orange btn-radius first" @click="caculateGj">开始计算</a>
            <a href="javascript:void(0)" class="btn btn-orange btn-line btn-radius last" @click="btnClick">我要缴社保</a>
          </div>
        </div>
      </div> -->
      <!-- <div class=" w1200  module-cont bg-white mt20">
        <div class="div-about clearfix">
          <div class="div-about-l fl"> -->
      <!-- 这里放介绍小豆社保的文字 -->
      <!-- <p>
              实现网络社保代理共赢，小豆社保期待您携手同行！<br/>
              新琪科技（北京）有限公司成立于2001年，并于2013年在上海股权交易中心挂牌上市。公司现拥有小豆社保、网才网等多个在线项目，业务覆盖北京、上海、天津、武汉等多个一线城市。现由于公司发展需求，小豆社保项目需要更多有资质、有经验、以及有实力的合作企业。 目前，小豆社保的客户量较大，且分布在全国各城市以及北京各区县。所以，小豆社保希望能与各城市或各区县人力资源行业的社保代理公司合作，通过小豆社保线上及线下推广，将客户引流到各贵公司，实现双赢。
              <br/>
              我们的优势：<br/>
              1、网站24小时在线服务<br/>
              2、全程短信监控系统，更安全<br/>
              3、15年从业经验，更专业<br/>
              4、线上线下推广，客流量大<br/>
              5、曼帕尔及小豆社保的品牌效应<br/>

              如果您感受到了我们的诚意，并且信任我们，无论你身在哪一个城市，只要贵公司有意向与我们合作，就请联系我们的商务部门，或再客服人员处留下贵公司的联系方式，我们会联系您，具体合作细节可以详谈，我们在此敬候您的佳音，同时，我们更希望贵公司能与小豆社保共同成长。
              <br/>
              联系方式：<br/>
              合作热线：010-53515305<br/>
              电子邮箱：manager@xincci.com
            </p>
          </div>
          <div class="div-about-r fr">
            <div class="qrcode-box">
              <img src="https://www.rrb365.com/images/www/ziliao/ios.jpg" width="120" height="120" alt="">
            </div>
            <p>
              <span>关注</span>
              <span class="color-orange">“小豆社保”</span>
              <span>微信公众号</span>
            </p>
          </div>
        </div>
      </div> -->

      <!-- 公告 -->
      <div class="notice wrapper f14 border_b">
        <div class="w1200 clearfix">
          <div :class="'left-icon fl'">
            <span>公告</span>
            <img :src="leftIcon" width="20" height="20">
          </div>
          <!--倒计时 -->
          <div class="countdown-box f14">
            <count-down v-if="noticesShow" v-on:start_callback="countDownS_cb(1)" v-on:end_callback="countDownE_cb(1)" :currentTime="currentTime" :startTime="startDate" :endTime="endDate" :tipTextStart="cityName+'城市代缴开始倒计时'" :tipTextEnd="cityName+'城市代缴倒计时'" :endText="cityName+'城市本月代缴已结束，服务电话：'+serviceCall" :dayTxt="'天'" :hourTxt="'小时'" :minutesTxt="'分钟'" :secondsTxt="'秒'"></count-down>
          </div>
          <!-- <marquee :interval="4000">
            <marquee-item @click.native="onClick()" class="align-middle" v-for="(item, index) in notices" :key="index" v-if="notices">
              {{item.title}}
            </marquee-item>
          </marquee> -->
        </div>
      </div>
      <!--  -->
      <div class="info-disClosure-cont border_b">
        <div class="info_disClosure w1200  wrapper">
          <ul class="clearfix border_b">
            <li class="fl">
              <div class="img fl"><img src="https://www.51jiecai.com/s/images/new_index/IPO.png" alt="" ondragstart="return false;"></div>
              <div class="fl">
                <p>{{slogans[0]}}</p>
                <p>{{slogans[1]}}</p>
              </div>
            </li>
            <li class="fl">
              <div class="img fl"><img src="https://www.51jiecai.com/s/images/new_index/jxBank.png" alt="" ondragstart="return false;"></div>
              <div class="fl">
                <p>{{slogans[2]}}</p>
                <p>{{slogans[3]}}</p>

              </div>
            </li>
            <li class="fl">
              <div class="img fl"><img src="https://www.51jiecai.com/s/images/new_index/yzzc.png" alt="" ondragstart="return false;"></div>
              <div class="fl">
                <p>{{slogans[4]}}</p>
                <p>{{slogans[5]}}</p>
              </div>
            </li>
          </ul>
        </div>
        <div class="realData wrapper w1200">
          <ul class=" clearfix">
            <li class="fl">
              <p class="data">10,000+
                <span>人</span>
              </p>
              <p class="tit">累计参保</p>
            </li>
            <li class="fl">
              <p class="data">799,127
                <span>元</span>
              </p>
              <p class="tit">社保缴费</p>
            </li>
            <li class="fl">
              <p class="data">128,699.48
                <span>元</span>
              </p>
              <p class="tit">公积金缴费</p>
            </li>
            <li class="fl">
              <p class="data">0
                <span>%</span>
              </p>
              <p class="tit">社保失败率</p>
            </li>
            <li class="fl">
              <p class="data">0
                <span>%</span>
              </p>
              <p class="tit">公积金失败率</p>
            </li>
          </ul>
        </div>
      </div>
      <!-- 新手福利 -->
      <div class="index-fuli">
        <div class="w1200">
          <div class="index-title">
            <h2 class="plate-tit">
              <span class="span1">推荐活动</span>
              <span class="span2">新手专享，每人仅有一次机会</span>
            </h2>
          </div>
          <!-- <div class="noob clearfix">
                <div class="redPack fl" >
                  <img src="https://www.51jiecai.com/s/images/new_index/redPack.png" width="330" height="190" alt="">
                </div>
                <div class="noob-fl fl">
                    <div class="title pr">
                        <div class="pro-num clearfix">
                            <span class="fl"> 分享即免服务费</span>
                        </div>
                        <div class="pro-desc">分享10次即可免收一个月手续费</div>
                        <div class="status saleOut pa">已满额</div>
                    </div>
                    <div class="about clearfix">
                        <div class="yield fl">
                            <p class="item-detail"><span class="color-base">10 </span><span>次</span></p>
                            <p class="item-name">分享数量</p>
                        </div>
                        <div class="period fl">
                            <p class="item-detail"><span class="color-base">200 </span><span>元以上</span></p>
                            <p class="item-name">免服务费</p>
                        </div>
                        <div class="way fl">
                            <p class="item-detail">分享即送，累计有用
                            </p>
                            <p class="item-name">免服务费</p>
                        </div>
                    </div>
                </div>
                <div class="redPack fr" >
                  <img src="https://www.51jiecai.com/s/images/new_index/redPack.png"  width="330" height="190" alt="">
                </div>
            </div> -->
          <div class="sectionbox area1" v-if="activityLeft">
            <!--text-->
            <div class="text-column per-width">
              <div class="ser-title">
                <h2>{{activityLeft.mainTitle}}</h2>
                <p>{{activityLeft.subTitle}}</p>
              </div>
              <div class="text">
                <p class="ellipsis2">{{activityLeft.memo}}</p>
              </div>
              <a class="sec-btn" target="_blank" :href="'activity.html?id='+activityLeft.id">查看详情</a>
            </div>
            <!--img-->
            <div class="img-column per-width padd-l">
              <div class="imagbox">
                <a href="#">
                  <img :src="activityLeft.bannerPc">
                </a>
              </div>
            </div>
          </div>
          <div class="sectionbox area2" v-if="activityRight">
            <!--img-->
            <div class="img-column per-width padd-r2">
              <div class="imagbox">
                <a href="#">
                  <img :src="activityRight.bannerPc">
                </a>
              </div>
            </div>
            <!--text-->
            <div class="text-column per-width padd-r">
              <div class="ser-title">
                <h2>{{activityRight.mainTitle}}</h2>
                <p>{{activityRight.subTitle}}</p>
              </div>
              <div class="text">
                <p class="ellipsis2">{{activityRight.memo}}</p>
              </div>
              <a class="sec-btn" target="_blank" :href="'activity.html?id='+activityRight.id">查看详情</a>
            </div>
          </div>
        </div>
      </div>

      <!-- 缴社保 -->
      <div class="index-shebao">
        <div class="w1200">
          <div class="index-title">
            <h2 class="plate-tit">
              <span class="span1">缴社保</span>
              <span class="span2">个人缴社保套餐</span>
            </h2>
          </div>
          <div class="table-content">
            <table class="tb-void">
              <thead>
                <tr>
                  <th>套餐名称</th>
                  <th>缴纳期限</th>
                  <th>缴纳总金额</th>
                  <th>公司缴纳</th>
                  <th>个人缴纳</th>
                  <th>服务费</th>
                  <th>月服务费</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in sssets" :key="index" v-if="sssets">
                  <td>{{item.name}}</td>
                  <td>
                    <span>{{item.monthCount}}</span>个月</td>
                  <td>
                    <span>{{item.orderAmount}}</span>元</td>
                  <td>
                    <span>{{item.companyAmount}}</span>元</td>
                  <td>
                    <span>{{item.personalAmount}}</span>元</td>
                  <td>
                    <span>{{item.fee}}</span>元</td>
                  <td>
                    <span>{{item.monthFee}}</span>元</td>
                  <td>
                    <a href="javascript:void(0)" class="btn btn-red" @click="btnClick">我要缴社保</a>
                  </td>
                </tr>

              </tbody>
            </table>
          </div>
        </div>
      </div>
      <!-- 缴公积金 -->
      <div class="index-shebao">
        <div class="w1200">
          <div class="index-title">
            <h2 class="plate-tit">
              <span class="span1">缴公积金</span>
              <span class="span2">个人缴公积金套餐</span>
            </h2>
          </div>
          <div class="table-content">
            <table class="tb-void">
              <thead>
                <tr>
                  <th>套餐名称</th>
                  <th>缴纳期限</th>
                  <th>缴纳总金额</th>
                  <th>公司缴纳</th>
                  <th>个人缴纳</th>
                  <th>服务费</th>
                  <th>月服务费</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in gjsets" :key="index" v-if="gjsets">
                  <td>{{item.name}}</td>
                  <td>
                    <span>{{item.monthCount}}</span>个月</td>
                  <td>
                    <span>{{item.orderAmount}}</span>元</td>
                  <td>
                    <span>{{item.companyAmount}}</span>元</td>
                  <td>
                    <span>{{item.personalAmount}}</span>元</td>
                  <td>
                    <span>{{item.fee}}</span>元</td>
                  <td>
                    <span>{{item.monthFee}}</span>元</td>
                  <td>
                    <a href="javascript:void(0)" class="btn btn-red" @click="btnClick">我要缴公积金</a>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div class="index-news">
        <div class="w1200 clearfix">
          <div class="news_left fl">
            <div class="news_l_title">小豆社保</div>
            <div class="news_video">
              <video class="video-box" controls="controls" v-if="webVideoUrl">
                <source :src="webVideoUrl">
                <embed :src="webVideoUrl">
              </video>
            </div>
          </div>
          <div class="news_right fl">
            <div class="new_title">
              <span class="news_r_title">新闻资讯</span>
              <a href="news.html?index=3" class="news_more">更多&gt;&gt;</a>
            </div>
            <div class="news_cont fl">
              <div class="news_cont_l ">
                <div class="lists con1">
                  <ul>
                    <li v-for="(item, index) in xwzxArticles" :key="index" v-if="xwzxArticles && (index<10)">
                      <b>[{{item.articleCategory.name}}]&nbsp;&nbsp;</b>
                      <a :href="'/news-detail.html?index=3&id='+item.id+'&code=XWZX'" target="_blank" :title="item.title">{{item.title}}</a>
                      <span>{{item.createDate|parseTime('{m}-{d}')}}</span>
                    </li>
                  </ul>
                </div>
              </div>
              <div class="news_cont_r ">
                <div class="lists con2">
                  <ul>
                    <li v-for="(item, index) in xwzxArticles" :key="index" v-if="xwzxArticles && (index>=10)">
                      <b>[{{item.articleCategory.name}}]&nbsp;&nbsp;</b>
                      <a :href="'/news-detail.html?index=3&id='+item.id+'&code=XWZX'" target="_blank" :title="item.title">{{item.title}}</a>
                      <span>{{item.createDate|parseTime('{m}-{d}')}}</span>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
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
import weixinPopup from "@/components/weixinPopup/index.vue";
import Footer from "@/components/footer/index.vue";
import marquee from "@/components/marquee/marquee.vue";
import MarqueeItem from "@/components/marquee/marquee-item.vue";
import CountDown from "@/components/countdown/index";
import { get } from "@/modules/js/api/server";
import url from "@/modules/js/api/apiUrl";
import $ from "jquery";
import eventVue from "../../modules/js/eventBus.js";
const defaultForm = {
  feeCategoryId: 0, //缴费方式
  city: { id: 1 }, //城市
  residenceType: { id: 1 }, //户口性质
  socialBase: undefined,
  providentBase: undefined
};
export default {
  components: { Header, Footer, marquee, MarqueeItem, weixinPopup, CountDown },
  props: {},
  data() {
    return {
      noticesShow: false,
      cityName: "北京",
      webVideoUrl: undefined,
      swiperlist: [],
      activitylist:[],//首页活动广告位
      context:"",//首页活动广告位标题
      notices: {},
      articles: [],
      sssets: [],
      gjsets: [],
      currentTime: undefined,
      startDate: undefined,
      endDate: undefined,
      xwzxArticles: [],
      serviceCall: undefined,
      raterValue: 3.5,
      type: undefined,
      currentValue: 1,
      currentValueTemp: undefined,
      personalType: [],
      cityId: undefined,
      city: [],
      cityList: [],
      socialBaseMin: "0",
      socialBaseMax: "0",
      providentBaseMin: "0",
      providentBaseMax: "0",
      titleSocialBase: "参保基数",
      titleProvidentBase: "公积金基数",
      postForm: Object.assign({}, defaultForm),
      shbAmount: 0,
      shbCompanyAmount: 0,
      shbPersonAmount: 0,
      gjAmount: 0,
      gjCompanyAmount: 0,
      gjPersonAmount: 0,

      leftIcon: require("../../assets/images/notice-left.png"),
      tabs: ["社保讲堂", "社保问答", "社保资讯"],
      currentIndex: 0,
      slogans: [],
      activityLeft:undefined,
      activityRight: undefined
    };
  },
  watch: {},
  computed: {},
  methods: {
    getIndex() {
      // 编码
      //   articleCategoryCode1: "XWZX", -> TOP页面三列左边
      //   articleCategoryCode2: "XDJT", -> TOP页面三列中
      //   articleCategoryCode3: "XDJT", -> TOP页面三列右边
      //   articleCategoryCode4: "XWZX", -> 下部新闻资讯
      let params = {
        articleCategoryCode1: "SHBJT",
        articleCategoryCode2: "SHBWD",
        articleCategoryCode3: "ZCZX",
        articleCategoryCode4: "XWZX",
        cityCode: "chaoyang"
      };
      get(url.indexData, params).then(response => {
        //console.log("Index response:" + JSON.stringify(response));
        if (response.data.result == "00000000") {
          // banner图片
          const ads = response.data.data.ads;
          if (ads) {
            this.swiperlist = ads;
          }

          const text = response.data.data.adContext;
          if (text){
            this.context = text;
          }

          //web活动广告位图片
          const activity = response.data.data.adActivity;
          if (activity){
            this.activitylist = activity;
          }

          this.startDate = response.data.data.beginDate;
          this.currentTime = this.startDate;
          this.endDate = response.data.data.endDate;
          this.noticesShow = true;

          //口号
          const slogans = response.data.data.slogans;
          if (slogans) {
            this.slogans = slogans;
          }

          //视频
          const webVideoUrl = response.data.data.webVideoUrl;
          if (webVideoUrl) {
            this.webVideoUrl = webVideoUrl;
          }          

          // top文章
          const articles = response.data.data.articles;
          if (articles) {
            this.tabs = articles;
            if (this.tabs[0].articles) {
              this.articles = this.tabs[0].articles;
              //console.log(this.articles)
            }
          }

          // 公告
          const notices = response.data.data.notice;
          if (notices) {
            this.notices = notices;
          }

          // 推荐活动
          const activities = response.data.data.activities;
          if (activities) {
            if(activities[0] != undefined && activities[0] != null) {
              this.activityLeft = activities[0];
            }
            if(activities[1] != undefined && activities[1] != null) {
              this.activityRight = activities[1];
            }
          }

          // 社保套餐
          const sssets = response.data.data.sssets;
          if (sssets) {
            this.sssets = sssets;
          }

          // 公积金套餐
          const gjsets = response.data.data.gjsets;
          if (gjsets) {
            this.gjsets = gjsets;
          }

          // 新闻资讯
          const xwzxArticles = response.data.data.xwzxArticles;
          if (xwzxArticles && xwzxArticles.articles) {
            // if(xwzxArticles.articles.length <= 10) {
            //   for (var i in xwzxArticles.articles) {
            //     var temp = {};
            //     temp.id = list[i].id;
            //     temp.createDate = list[i].createDate;
            //     temp.title = list[i].title;
            //     temp.articleCategoryName = list[i].articleCategory.name;
            //     this.xwzxArticlesL.push(temp);
            //   }
            // }
            this.xwzxArticles = xwzxArticles.articles;
          }

          //this.serviceCall = response.data.data.serviceCall;
        }
        //console.log("swiperlist:" + JSON.stringify(this.swiperlist));
      });
    },

    btnClick() {
      //console.log("1111");
      eventVue.$emit("PopShowTap", true);
    },
    initData() {
      // 获取城市列表
      get(url.cityList, {})
        .then(response => {
          if (response.data.result == "00000000") {
            this.cityList = response.data.data;
            console.log(JSON.stringify(this.cityList));
            if (this.cityList != null && this.cityList.length > 0) {
              this.cityId = this.cityList[0].value;
              this.postForm.city.id = this.cityId;
              this.onChange();

              get(url.calculateQuotationBaseInfo, {
                cityId: this.cityList[0].value
              })
                .then(response => {
                  if (response.data.result == "00000000") {
                    // 参保基数
                    this.payBase = response.data.data.payBase;
                    this.postForm.socialBase = this.payBase.socialBaseMin;

                    this.socialBaseMin = this.payBase.socialBaseMin;
                    this.socialBaseMax = this.payBase.socialBaseMax;

                    this.titleSocialBase =
                      "参保基数(" +
                      this.payBase.socialBaseMin +
                      "-" +
                      this.payBase.socialBaseMax +
                      ")";

                    // 公积金参保基数
                    this.postForm.providentBase = this.payBase.providentBaseMin;

                    this.providentBaseMin = this.payBase.providentBaseMin;
                    this.providentBaseMax = this.payBase.providentBaseMax;

                    this.titleProvidentBase =
                      "公积金基数(" +
                      this.payBase.providentBaseMin +
                      "-" +
                      this.payBase.providentBaseMax +
                      ")";
                    this.caculateShb();
                    this.caculateGj();
                  }
                })
                .catch(e => {
                  console.log(e);
                });
            }
          }
        })
        .catch(e => {
          console.log(e);
        });
    },
    onChange() {
      if (this.cityId == null || this.cityId == undefined) {
        return;
      }
      if (this.type != null && this.type != undefined && this.type === "GJ") {
        return;
      }
      get(url.residenceTypeListByCityId, { id: this.cityId })
        .then(response => {
          let list = response.data.list;
          this.personalType = [];
          this.currentValue = undefined;
          for (var i in list) {
            var temp = {};
            temp.key = list[i].id;
            temp.value = list[i].name;
            this.personalType.push(temp);
          }
          if (list != null && list.length > 0) {
            this.currentValue = list[0].id;
            this.postForm.residenceType.id = list[0].id;
          }
        })
        .catch(e => {
          console.log(e);
        });
    },
    caculateShb() {
      console.log(JSON.stringify(this.postForm));
      let params = {
        feeCategoryId: this.postForm.feeCategoryId,
        cityId: this.postForm.city.id,
        residenceTypeId: this.postForm.residenceType.id,
        socialBase: this.postForm.socialBase
      };
      get(url.calculateQuotation, params).then(response => {
        console.log("calculateQuotation:" + JSON.stringify(response));
        if (response.data.result == "00000000") {
          this.shbAmount = response.data.data.amount;
          this.shbCompanyAmount = response.data.data.companyAmount;
          this.shbPersonAmount = response.data.data.personSocialAmount;
        }
      });
    },
    caculateGj() {
      console.log(JSON.stringify(this.postForm));
      let params = {
        feeCategoryId: 1,
        cityId: this.postForm.city.id,
        residenceTypeId: this.postForm.residenceType.id,
        providentBase: this.postForm.providentBase
      };
      get(url.calculateQuotation, params).then(response => {
        console.log("calculateQuotation:" + JSON.stringify(response));
        if (response.data.result == "00000000") {
          this.gjAmount = response.data.data.amount;
          this.gjCompanyAmount = response.data.data.companyAmount;
          this.gjPersonAmount = response.data.data.personalAmount;
        }
      });
    },

    onClick() {
      console.log("----");
    },
    tabChange(item, index) {
      this.articles = [];
      if (item.articles) {
        this.articles = item.articles;
      }
      this.currentIndex = index;
      //console.log(this.articles)
    },
    padLeftZero(str) {
      return ("00" + str).substr(str.length);
    },
    
    //时间开始的回调
    countDownS_cb: function(a) {
      //console.log('callBack--'+a+'--开始倒计时结束回调');
    },
    ///时间结束的回调
    countDownE_cb: function(a) {
      //console.log('callBack--'+a+'--活动剩余倒计时结束回调');
    },
  },
  created() {
    //this.initData();
    this.getIndex();
  },
  mounted() {
    //banner-sliders
    var bannerSliders = new Swiper(".swiper-container.banner-sliders", {
      slidesPerView: 1,
      loop: true,
      pagination: ".swiper-pagination",
      paginationClickable: true,
      autoplay: 4000,
      autoplayDisableOnInteraction: false
    });
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

<style   lang="less">
@import "~@/common/less/common.less";
.index-title {
  .plate-tit {
    margin: 0;
    width: 100%;
    margin-top: 51px;
    height: 27px;
    font-weight: normal;
    margin-bottom: 20px;
    .span1 {
      font-size: 20px;
      line-height: 28px;
      color: #333;
      margin-right: 18px;
    }
    .span2 {
      font-size: 14px;
      color: #666;
    }
  }
}
//banner
.index-banner {
  height: 500px;
  width: 100%;
  .banner-left {
    height: 500px;
    right: 0;
    z-index: 99;
    text-align: left;
    top: 20px;
    .banner-left-title {
      height: 35px;
      line-height: 35px;
      font-size: 18px;
      text-align: left;
      margin-bottom: 5px;
    }
    .banner-left-t {
      padding: 0;//20px;
      box-sizing: border-box;
      background: #ffffff;
      /*height: 385px;*/
      overflow: hidden;
      img {
        width: 350px;
        height: 250px;
      }
    }

  }
  .swiper-slide {
    a {
      display: block;
      width: 100%;
      position: relative;
      height: 500px;
      overflow: hidden;
    }
    img {
      position: absolute;
      width: 1920px;
      height: 500px;
      left: 50%;
      margin-left: -960px;
    }
  }
  .swiper-pagination-bullet {
    width: 10px;
    height: 10px;
    display: inline-block;
    border-radius: 100%;
    background: transparent;
    border: 2px solid #ffffff;
    opacity: 1;
  }
  .swiper-pagination-bullet-active {
    background-color: transparent;
    width: 10px;
    border-radius: 50%;
    height: 10px;
    border: 2px solid @color-base;
  }
}
.page-content {
  .jisuanqi {
    .pull-left,
    .pull-right {
      padding: 20px;
      width: 590px;
      box-sizing: border-box;
      .div-info {
        ul {
          li {
            height: 35px;
            line-height: 35px;
            padding: 10px 0;
            width: 50%;
            .label-name {
              min-width: 70px;
              display: inline-block;
              vertical-align: 1px;
            }
          }
        }
      }
      .div-action {
        margin-top: 10px;
        a.btn {
          width: 200px;
          text-align: center;
          font-size: 16px;
          margin-right: 25px;
        }
      }
    }
  }

  .channel-title {
    h4 {
      line-height: 50px;
    }
  }
  // 关于小豆社保
  .div-about {
    padding: 20px;
    .div-about-l {
      width: 70%;
      .border_r;
      min-height: 200px;
      padding-right: 20px;
      box-sizing: border-box;
      p {
        font-size: 14px;
      }
    }
    .div-about-r {
      width: 30%;
      text-align: center;
      .qrcode-box {
        margin-top: 25px;
      }
    }
  }
  .notice {
    padding: 10px 0px;
    height: 25px;
    line-height: 25px;
    .left-icon {
      padding-right: 10px;
      height: 25px;
      overflow: hidden;
      display: flex;
      align-items: center;
      span {
        display: inline-block;
        line-height: 25px;
        padding-right: 10px;
      }
    }
  }
  //
  .info_disClosure {
    ul {
      li {
        width: 33.333%;
        padding: 25px 0;
        padding-left: 100px;
        box-sizing: border-box;
        p {
          height: 20px;
          line-height: 20px;
          font-size: 14px;
          color: #333;
          user-select: none;
        }
        div.img {
          margin-right: 15px;
          img {
            line-height: 40px;
            width: 35px;
            height: 35px;
            margin-top: 3px;
            overflow: hidden;
          }
        }
      }
    }
  }
  .realData {
    ul {
      padding: 30px 20px;
      li {
        float: left;
        width: 20%;
        text-align: center;
        .data {
          font-size: 20px;
          line-height: 18px;
          font-weight: bold;
          color: #333;
        }
        .tit {
          font-size: 12px;
          line-height: 12px;
          color: #979797;
          margin-top: 12px;
        }
      }
    }
  }
  //新手福利
  .noob-fl {
    position: relative;
    width: 510px;
    margin: 0 15px;
    height: 190px;
    padding: 15px;
    box-sizing: border-box;
    background: #fff;
    border: 1px solid #e3e3e3;
    border-radius: 5px;
    .title {
      color: #3c3c3c;
      padding-bottom: 15px;
      border-bottom: 1px dashed #dfdfdf;
      height: auto;
      .pro-num {
        font-size: 16px;
        line-height: 30px;
        height: 30px;
        a {
          color: #3c3c3c;
        }
      }
      .pro-desc {
        width: 240px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        font-size: 12px;
        line-height: 12px;
      }
      .status {
        position: absolute;
        right: 15px;
        top: 0;
        width: 122px;
        height: 34px;
        background: #f6f6f6;
        color: #666;
        border: 1px solid transparent;
        border-radius: 5px;
        text-align: center;
        line-height: 34px;
        cursor: pointer;
        margin-top: 3px;
        font-size: 14px;
      }
    }
    .about {
      padding: 30px 0;
      text-align: center;
      display: flex;
      align-items: center;
      .item-detail {
        height: 30px;
        line-height: 30px;
        font-size: 24px;
        font-weight: bold;
        span:last-child {
          font-size: 16px;
          font-weight: normal;
        }
      }
      .item-name {
        height: 16px;
        font-size: 12px;
        color: #999;
        margin-top: 12px;
      }
      .yield {
        flex: 1;
        border-right: 1px dashed #e3e3e3;
      }
      .period {
        flex: 1;
        border-right: 1px dashed #e3e3e3;
        color: #333;
      }
      .way {
        flex: 1;
        .item-detail {
          line-height: 40px;
          font-size: 14px;
          color: #333;
        }
      }
    }
  }
  .sectionbox {
    padding: 50px 0px 20px;
    &:after {
      visibility: hidden;
      display: block;
      font-size: 0;
      content: " ";
      clear: both;
      height: 0;
    }
    .text-column {
      padding-right: 5px;
      box-sizing: border-box;
      .text {
        min-height: 110px;
        margin-bottom: 20px;
        p {
          font-size: 16px;
          color: #747474;
          line-height: 40px;
        }
      }
    }
    .per-width {
      width: 50%;
      float: left;
    }
    .ser-title {
      margin-bottom: 50px;
      h2 {
        font-size: 30px;
        color: #373737;
        font-weight: 600;
      }
      p {
        margin-top:5px;
        font-size: 20px;
        color: #373737;
        font-weight: 600;
      }
    }
    .sec-btn {
      width: 148px;
      height: 48px;
      font-size: 20px;
      color: @color-base;
      display: inline-block;
      text-align: center;
      line-height: 48px;
      border: 1px solid @color-base;
      background-color: #fff;
      position: relative;
      cursor: pointer;
      transition: color 0.3s ease-in-out, background 0.3s ease-in-out;
      &:after {
        content: "";
        width: 70px;
        height: 1px;
        border-bottom: 1px solid @color-base;
        position: absolute;
        top: 50%;
        right: -55px;
        margin-top: -1px;
      }
      &:hover {
        background-color: @color-base;
        color: #fff;
      }
    }
    &.area2{
       .sec-btn {
         float: right;
         &:after{
           left: -55px;
         }
       }
    }
    .img-column {
      position: relative;
      box-sizing: border-box;
      .imagbox {
        position: relative;
        border: 6px solid #48dfff;
        max-height: 300px;
        img {
          position: relative;
          top: 25px;
          left: 25px;
          display: block;
          width: 100%;
          height: 300px;
          transition: all 500ms ease;
        }
        &:hover img {
          top: 0;
          left: 0;
        }
      }
    }
  }
  .padd-l {
    padding-right: 20px;
    padding-left: 45px;
  }
  .padd-r {
    padding-right: 5px;
    padding-left: 20px;
  }
  .padd-r2 {
    padding-right: 45px;
    padding-left: 20px;
  }
  .img-column.padd-r2 .imagbox img {
    top: -25px;
    left: -25px;
  }
  .img-column img:hover {
    opacity: 1;
  }

  //新闻资讯
  .index-news {
    padding-top: 50px;
    padding-bottom: 45px;
    overflow: hidden;
    .news_l_title {
      font-size: 20px;
      line-height: 28px;
      color: #333;
      margin-right: 18px;
    }
    .news_r_title {
      display: inline-block;
      border-bottom: 2px solid @color-base;
      margin-bottom: -2px;
    }
    .news_more {
      font-size: 14px;
      color: #dadada;
      float: right;
    }
    .new_title {
      font-size: 20px;
      border-bottom: 2px solid #dadada;
    }
    .news_video {
      width: 436px;
      height: 303px;
      margin: 25px 20px 0 0;
      .video-box {
        width: 100%;
        height: 100%;
      }
    }
    .news_cont_l,
    .news_cont_r {
      width: 350px;
      padding: 10px;
      float: left;
    }
    .con1,
    .con2 {
      height: 330px;
      overflow: hidden;
      float: left;
      width: 100%;
      ul {
        overflow: hidden;
        height: 348px;
        color: @color-base;
        li {
          padding: 0 0 0 10px;
          font-size: 14px;
          line-height: 45px;
          height: 45px;
          color: #252525;
          text-overflow: ellipsis;
          a {
            .tran;
            text-overflow: ellipsis;
            float: left;
            overflow: hidden;
            width: 180px;
            white-space: nowrap;
            color: #333;
          }
          b {
            color: #252525;
            .tran;
            float: left;
          }
          &:hover {
            a {
              color: @color-base;
            }
          }
        }
      }
    }
  }
}

select {
  min-width: 100px;
  padding-left: 10px;
  padding: 6px 10px;
  border: 1px solid @color-base;
  padding-right: 14px;
}
table.tb-void {
  width: 100%;
  th {
    background: #f5f5f5;
    height: 50px;
    line-height: 50px;
    padding: 0 5px;
    text-align: center;
    font-weight: normal;
    font-size: 16px;
  }
  td {
    border-bottom: 1px solid #e4e4e4;
    padding: 15px 5px;
    text-align: center;
    font-size: 14px;
    span {
      color: @red-1;
    }
  }
  tbody {
    tr {
      .tran;
      &:hover {
        box-shadow: 0px 5px 10px #dbdbdb;
        z-index: 5;
      }
    }
  }
}
</style>
