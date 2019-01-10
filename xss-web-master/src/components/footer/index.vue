<template>
  <div class="footer">
    <div class="footer-content w1200 clearfix">
      <div class="footer-left fl">
        <div class="left-logo fl clearfix">
          <img src="../../assets/images/logo2.png" alt="" width="200" height="200">
        </div>
        <div class="left-tel fl">
          <h4 class="f20">全国客户热线：</h4>
          <p class="f20">{{serviceCall}}</p>
        </div>
      </div>
      <div class="footer-right fl clearfix ">
        <div class="mod_help_list fl clearfix">
          <div class="mod_help_nav fl">
            <h5 class="mod_help_nav_tit">产品服务</h5>
            <ul class="mod_help_nav_con">
              <li>
                <a href="shebao.html?index=1" title="缴社保" target="_blank">缴社保 </a>
              </li>
              <li>
                <a href="gongjijin.html?index=2" title="缴公积金" target="_blank">缴公积金 </a>
              </li>
              <li>
                <a href="news.html?index=3&code=ZCJD" title="政策资讯" target="_blank">政策资讯 </a>
              </li>
            </ul>
          </div>
          <div class="mod_help_nav fl">
            <h5 class="mod_help_nav_tit">帮助中心</h5>
            <ul class="mod_help_nav_con">
              <li>
                <a href="help.html?index=4&code=XDLC" title="下单流程" target="_blank">下单流程 </a>
              </li>
              <li>
                <a href="help.html?index=4&code=CJWT" title="常见问题" target="_blank">常见问题 </a>
              </li>
            </ul>
          </div>
          <div class="mod_help_nav fl">
            <h5 class="mod_help_nav_tit">新闻资讯</h5>
            <ul class="mod_help_nav_con">
              <li>
                <a href="news.html?index=3&code=SHBJT" title="社保讲堂" target="_blank">社保讲堂 </a>
              </li>
              <li>
                <a href="news.html?index=3&code=SHBWD" title="社保问答" target="_blank">社保问答 </a>
              </li>
               <li>
                <a href="news.html?index=3&code=SHBZX" title="社保资讯" target="_blank">社保资讯 </a>
              </li>
              <li>
                <a href="news.html?index=3&code=GJZX" title="公积金资讯" target="_blank">公积金资讯 </a>
              </li>
            </ul>
          </div>
           <div class="mod_help_nav fl">
            <h5 class="mod_help_nav_tit">关于小豆社保</h5>
            <ul class="mod_help_nav_con">
              <li>
                <a href="news.html?index=3&code=GYWM" title="关于我们" target="_blank">关于我们 </a>
              </li>
              <li>
                <a href="news.html?index=3&code=DQHZ" title="地区合作" target="_blank">地区合作 </a>
              </li>
            </ul>
          </div>
        </div>
          <div class="left-qrcode fr tc">
          <div class="qrcode-box">
            <img src="../../assets/images/login-qrcode.png" width="120" height="120" alt="">
          </div>
          <p>
            <span>关注</span>
            <span class="color-base">“小豆社保”</span>
            <span>微信公众号</span>
          </p>
        </div>
      </div>
    </div>
    <div class="w1200 clearfix  footer-bootom">
      <p class="tc f12">Copyright © 2001-2018 北京新琪科技有限公司 版权归北京新琪科技有限公司所有&nbsp;&nbsp; &nbsp; &nbsp;京ICP备18027766号-1 人力资源服务证号110105010069号 </p>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import { get } from "@/modules/js/api/server";
import url from "@/modules/js/api/apiUrl";
export default {
  components: {},
  props: {},
  data() {
    return {
      serviceCall: ""
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
