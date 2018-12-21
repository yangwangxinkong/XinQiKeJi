<template>
    <div class="page">
        <div class="container bg_f  padding-15 pt15" style="background-color:#fff !important;min-height:500px;">
            <div class="weui-article bg_f">
                <h2 class="weui-weixin-title">{{article.title}}</h2>
                <!-- <div class="weui-weixin-info">
                    <a class="weui-weixin-a weui-weixin-nickname" href="javascript:void(0);" v-if="article.author">发布者：{{article.author}}</a>
                    <em class="weui-weixin-em">
                        <span class="">{{article.createDate}}</span>
                    </em>
                </div> -->
                <!--meta结束-->
                <div class="module-content bg_f" data-mod-name="mui/mdv/zebra" mdv-cls="mui/desc-mods/custommodule/index" v-html="article.text">
                    <!-- <p style="text-indent: 0em; text-align: left;" v-html="article.memo"> -->
                        <!-- <span style="font-family: 微软雅黑, Microsoft\ YaHei; color: rgb(89, 89, 89); font-size: 16px;">
                            是是是是是是是
                        </span> -->
                    <!-- </p> -->
                    <!-- <p style="font-size: 18px;padding: 10px 0" class="135brush" v-html="article.text"></p> -->

                </div>
                <!--内容结束-->
            </div>
        </div>
        <!-- footer -->
        <section class="footer-actionBar">
            <section class="footer-actionBar-container flex-ui ">
              <div class="flex1 tc  white" @click="recharge" v-if="type=='vip'">
                <span class="f16 pl0">我要充值</span>
              </div>
                <div class="flex1 tc  white" @click="nextPageSocial" v-else>
                    <span class="f16 pl0">我要下单</span>
                </div>
            </section>
        </section>
    </div>
</template>

<!--<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script language="javascript" src='http://mp.weixin.qq.com/debug/zh_CN/htmledition/js/jsapisign/sha121d415.js'></script>-->
<script type="text/ecmascript-6">
  import { Alert } from 'vux'
import url from "@/api/apiUrl";
import { get } from "@/api/server";
export default {
  components: {Alert},
  props: {},
  data() {
    return {
      id: undefined,
      article: {},
      appId: 'wxe91fe766376f1f83',
      ticket:undefined,
      url:undefined,
      type: ''
    };
  },
  watch: {},
  computed: {

  },
  created() {
    this.id = this.$route.query && this.$route.query.id;
    this.type = this.$route.query && this.$route.query.type;
    this.getJsapiSign();
    this.getInfo();
  },
  mounted() {

  },
  methods: {
    initUrl(){
      this.$reqSignByUrl(location.href.split('#')[0],this.sayHello);
    },
    sayHello(url){
      console.log("Hello:",url);
      this.url = "http://m.xiaodoushebao.com/index.html?redirectType=wx_snsapi_base";
    },
    getJsapiSign(){
        this.url = "http://m.xiaodoushebao.com/index.html";
      //this.initUrl();
      get(url.jsapiSign, {url:this.url}).then(response => {
        if(response.data.result == "00000000"){
          console.log(JSON.stringify(response.data.data));
          let signInfo = response.data.data
          signInfo.appId=this.appId;
          this.$initWXConfig(signInfo, false)
          let shareParams = {
            title:'我的分享',
            link:"http://m.xiaodoushebao.com/index.html?targetId=3",
            image:'http://m.xiaodoushebao.com/static/img/index_logo.2b34754.png',
            abstract:'我的分享描述'
          }
          this.$shareWeChat(shareParams)
          //alert(this.ticket);
        }
      })
    },
    getJsapiTicket(){
      get(url.jsapiTicket, {}).then(response => {
        if(response.data.result == "00000000"){
            this.ticket = response.data.data
            alert(this.ticket);
        }
      })
    },
    getInfo() {
      get(url.activityDetail, {activityId:this.id}).then(response => {
          //console.log("article info:" + JSON.stringify(response))
          if(response.data.result == "00000000"){
              this.article = response.data.data.activity;
              //document.title = this.article.title;
          }
      })
    },
    nextPageSocial() {
      this.$router.push({path:"/order/orderMember?type=SB"});
    },
    recharge() {
      get(url.current, {}).then(response => {
        if(response.data.result == "00000000"){
          const isVip = response.data.data.isVip;
          console.log("isVip:" + isVip)
          if(isVip){
            this.$vux.alert.show({
              title: '提醒',
              content: '您已经是VIP会员啦！',
              onShow () {
                console.log('Plugin: I\'m showing')
              },
              onHide () {
                console.log('Plugin: I\'m hiding now')
              }
            })
          }else{
            this.$router.push({path:"/recharge/info"});
          }
        }
      });

    },
  }
};
</script>

<style lang="less">
  .module-content {
    img {
      width: 100%;
      display: block;
    }
  }
</style>

<style scoped lang="less">

.weui-weixin-title {
  font-size: 20px;
  color: #000000;
  font-weight: 400;
  margin-bottom: 5px;
  line-height: 1.4;
}
.weui-weixin-info {
  margin-bottom: 5px;
  line-height: 20px;
  font-size: 0;
  .weui-weixin-a {
    display: inline-block;
    vertical-align: middle;
    margin-right: 12px;
    margin-bottom: 10px;
    font-size: 16px;
    color: @grey_be;
  }
  .weui-weixin-em {
    color: #8c8c8c;
    font-style: normal;
    display: inline-block;
    font-size: 16px;
    margin-bottom: 10px;
    margin-right: 8px;
    vertical-align: middle;
  }
}

.weui-weixin-content {
  img {
    height: auto !important;
    max-width: 100%;
    display: block;
  }
}

</style>
