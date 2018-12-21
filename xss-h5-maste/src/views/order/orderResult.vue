<template>
  <div class="page">
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
import { Divider, XButton } from "vux";
import Msg from "@/components/msg";
export default {
  components: {
    Msg,
    Divider,
    XButton
  },
  created() {
    this.id = this.$route.query && this.$route.query.id;
    this.sn = this.$route.query && this.$route.query.sn;
    this.description =
      "订单编号" +
      this.sn +
      "已下单成功，系统会及时处理，24小时热线电话400-9999-075。";
  },
  methods: {
    //      changeIcon () {
    //        if (!this.icon || this.icon === 'success') {
    //          this.icon = 'warn'
    //          return
    //        }
    //        if (this.icon === 'warn') {
    //          this.icon = 'info'
    //          return
    //        }
    //        if (this.icon === 'info') {
    //          this.icon = 'waiting'
    //          return
    //        }
    //        if (this.icon === 'waiting') {
    //          this.icon = 'success'
    //        }
    //      }
    toOrderDetail() {
      this.$router.push("/member/order/exchargeDetail?id=" + this.id);
    },
    toProductList() {
      this.$router.push("/");
    }
  },
  data() {
    return {
      id: undefined,
      sn: undefined,
      title: "下单成功",
      description: "",
      icon: require("../../assets/icon-success.png"),
      buttons: [
        {
          type: "warn",
          text: "订单详情",
          onClick: this.toOrderDetail.bind(this),
        },
        {
          type: "warn",
          text: "回到首页",
          plain: true,
          link: "/"
        }
      ]
    };
  }
};
</script>
<style scoped>
.page {
  width: 100%;
  height: 100%;
  position: absolute;
  background: #ffffff !important;
}
</style>
