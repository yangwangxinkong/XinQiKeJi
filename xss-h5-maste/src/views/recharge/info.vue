<template>
    <!-- 支付订单 -->

    <div class="page">
        <div class="container bg_f">
            <p class="f20 tc amount_color pt50">￥{{amount | money}}</p>
            <p class="tc">服务项目：会员充值</p>
            <group class="mt50 no-border">
                <cell title="支付方式"></cell>
                <!-- 支付方式 list -->
                <div class="list pay-list f14 bg_f">
                  <ul class="payment-fold">
                    <li v-for="(item,index) in payMethodList" :key="index" @click="selectPay(item,index)" v-if="item.isShow ">
                        <div class="item ui-flex justify-space-between align-center" :class="{on:defaultPayCode===item.code?item.checked:false}">
                            <a href="javascript:;" :style="'background-image: url('+item.icon+');'" style="background-size: 30px 30px;background-repeat:no-repeat;">
                                {{item.name}}
                                <p>{{item.description}}</p>
                            </a>
                        </div>
                    </li>
                  </ul>
                </div>
            </group>
            <section class="footer-actionBar" >
                <section class="footer-actionBar-container flex-ui ">
                    <div class="flex1 tc  white footer-btn" @click="toPay"><span class="f16 pl0">确认支付</span></div>
                </section>
            </section>
        </div>
    </div>
</template>
<script>
import { Group,Cell,Radio,CheckIcon} from 'vux'
import { get, post, execute, isWeiXin } from '@/api/server'
import url from '@/api/apiUrl'
import money from "@/utils/utils"

export default {
    components:{ Group,Cell,Radio,CheckIcon},
    data(){
      return{
          id: this.$route.query && this.$route.query.id,
          paymentMethodName: '支付', //当前选择支付方式name
          paymentMethodId: undefined, //当前选择支付方式id
          currentIndex: undefined,
          payMethodList:["zhifubao","weixin"],
          defaultPayCode:'weixin',
          code:'weixin',
          order:[],
          amount:undefined,
          isBankPay:false,     //是否对公转账
          bankName:'招商银行股份有限公司北京东方广场支行',            //开户行
          bankAccountName:'北京新琪科技有限公司',//开户名
          bankAccount:'110935437210902'  //开户账号
      }
  },
  created() {
    this.initData();
  },
  methods:{
      //获取订单信息
    initData() {
      get(url.recharge, {}).then(res => {

        if(res.data.result==='00000000') {
            this.amount = res.data.data.amount;
            this.payMethodList = res.data.data.paymentMethods;
            //动态添加属性
            this.payMethodList.forEach(item => {
              if(item.code == "alipay" && isWeiXin()){
                this.$set( item, 'isShow',false)
              }else{
                this.$set( item, 'isShow',true)
              }
              this.$set( item, 'checked',true)
            });
        }else if(res.data.result==='10000007'){
         this.$router.push({path:"/login/index", query:{redirect: this.$route.fullPath}})
        }

      });
    },
    change(val,label){

    },
    selectPay(item, index) {
      this.paymentMethodName = item.name;
      this.currentIndex = index;
      this.paymentMethodId = item.id;
      this.defaultPayCode = item.code;
      this.code = item.code;
      this.isBankPay = this.code == "bank" ? true : false;
    },

    // 支付
    toPay(){
      if(this.code===undefined){
        return
      }
      //if(this.code == "weixin" || this.code == "alipay") {
      let code = "bank"
      if(this.code == "alipay"){
        code = "alipay_h5";
      }else{
        if(isWeiXin()){
          code = "weixin_mp";
        }else if(this.code == "weixin"){
          code = "weixin_h5";
        }
      }
      let params = {
        type: 'recharge',
        code: code,
        amount: this.amount
      }
      get(url.payOnline, params).then(response => {
        //console.log("response:" + JSON.stringify(response));

        if(response.data.result == "00000000"){
          if(code == "weixin_h5"){
            //this.$vux.toast.text(response.data.mwebUrl,'middle');
            location.href = response.data.data.mwebUrl;
            //this.weixinPayRequest(response.data.data);
          }else if(code == "weixin_mp"){
            this.weixinPayRequest(response.data.data);
          }else if(code == "alipay_h5"){
            document.getElementById("payForm").innerHTML = response.data.data.form;
            this.$nextTick(() => {
              document.forms[0].submit();
          });
          }else{
            //this.$router.push('/member/order/orderDetail?id='+this.order.id);
          }
        }else{
          //this.$router.push('/member/order/orderDetail?id='+this.order.id);
        }
      })
    },
    weixinPayRequest(data) {

      var vm= this;
      if (typeof WeixinJSBridge == "undefined"){//微信浏览器内置对象。参考微信官方文档
        if( document.addEventListener ){
          document.addEventListener('WeixinJSBridgeReady', vm.onBridgeReady(data), false);
        }else if (document.attachEvent){
          document.attachEvent('WeixinJSBridgeReady', vm.onBridgeReady(data));
          document.attachEvent('onWeixinJSBridgeReady',vm.onBridgeReady(data));
        }
      }else{
        vm.onBridgeReady(data);
      }
    },
    /**
     * @method 支付费用方法
     * @param data:后台返回的支付对象,(详情微信公众号支付API中H5提交支付);
     */
    onBridgeReady:function(data){
      //this.$vux.toast.text(data.appId,'middle');
      let  vm = this;
      WeixinJSBridge.invoke(
        'getBrandWCPayRequest',{
          "appId":data.appId,     //公众号名称，由商户传入
          "timeStamp":data.timeStamp, //时间戳，自1970年以来的秒数
          "nonceStr":data.nonceStr, //随机串
          "package":data.package,
          "signType":data.signType, //微信签名方式：
          "paySign":data.paySign //微信签名
        },
        function(res){
          //vm.wxmsg = JSON.stringify(res);
          //Toast(JSON.stringify(res),10000)
          // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
          if (res.err_msg === 'get_brand_wcpay_request:ok') {
            vm.$vux.toast.show({
              type:'success',
              text: "支付成功",
              width:'120px',
              time:1000
            })
            vm.$router.push('/recharge/result');
            //vm.$router.push('/member/order/orderDetail?id='+vm.order.id);
            //window.location.href = '/member/order/'+[vm.order.orderType==='activity'?'activityDetail':'detail']+'/'+vm.order.id
            //this.$router.push('/MineOrder')
          } else if (res.err_msg === 'get_brand_wcpay_request:cancel') {
            //Toast('用户取消支付')
            vm.$vux.toast.show({
              type:'warn',
              text: "用户取消支付",
              width:'120px',
              time:1000
            })
            //window.location.href = '/member/order/'+[vm.order.orderType==='activity'?'activityDetail':'detail']+'/'+vm.order.id
            vm.$router.push('/');
          } else if (res.err_msg === 'get_brand_wcpay_request:fail') {
            //Toast('支付失败，请重试')
            vm.$vux.toast.show({
              type:'warn',
              text: "支付失败，请重试",
              width:'120px',
              time:1000
            })
          }
        }
      );
    },
  }
}
</script>
<style scoped lang="less">
@import "../../assets/styles/less/public.less";
.color_base{
    color: @color_base
}
.no-border{
    >.weui-cells:before{
        border-top: none;
    }
    .weui-cells{
        margin-top: 0;
    }
    // .weui-cell:before{
    //     left: 0;
    // }
}

.payment-fold {
  background: #ffffff;
  li {
    padding: 0 15px;
    position: relative;
    &:not(:last-child):after {
      .border_half;
      left: 15px;
    }
    .item {
      padding-right: 30px;
      background-image: url(../../assets/check_normal.png);
      background-position: 100% 50%;
      background-repeat: no-repeat;
      background-size: 20px 20px;
      &.on {
        background-image: url(../../assets/check_press.png);
      }
      a {
        display: block;
        line-height: 24px;
        padding-left: 40px;
        padding-top: 10px;
        padding-bottom: 10px;
        color: #333;
        text-decoration: none;
        p {
          .f12;
          .grey_6;
        }
      }
    }
  }
}
</style>

