<template>
  <div>
    <msg :title="title" :description="description" :buttons="buttons" :icon="icon"></msg>
  </div>
</template>

<!--<i18n>
  Yeah! You make it:
  zh-CN: 支付成功
  Msg description:
  zh-CN: 订单编号{{sn}}已支付成功，点击“订单详情”可查看订单详细信息；点击“兑换商品”可选择要兑换的商品。
  Primary button:
  zh-CN: 订单详情
  Secondary button:
  zh-CN: 兑换商品
</i18n>-->

<script>
  import { Msg, Divider, XButton } from 'vux'
  import { get, post, execute, isWeiXin } from '@/api/server'
  import url from '@/api/apiUrl'
  export default {
    components: {
      Msg,
      Divider,
      XButton
    },
    created(){
      this.getRechargeInfo();
    },
    methods: {
      getRechargeInfo(){
        get(url.current, {}).then(response => {
          if(response.data.result == "00000000"){
            const expireDate = response.data.data.vipExpireDate;
            const vipDiscount = response.data.data.vipDiscount;
            this.description = "会员套餐充值成功，有效期至" + expireDate + ",期间享受" + vipDiscount*10 + "折优惠。"
          }
        })
      },
        toOrderDetail(){
          this.$router.push('/member/order/orderDetail?id='+this.id);
        },
        toProductList(){
          this.$router.push('/product/list');
        }
    },
    data () {
      return {
        id:undefined,
        sn:undefined,
        title: '支付成功',
        description: '会员套餐充值成功',
        icon: '',
        buttons: [{
          type: 'primary',
          text: '缴纳社保',
          link: '/order/orderMember?type=SB'
        }, {
          type: 'primary',
          text: '缴公积金',
          link: '/order/orderMember?type=GJ'
        }, {
          type: 'default',
          text: '回到首页',
          link: '/'
        }]
      }
    }
  }
</script>
