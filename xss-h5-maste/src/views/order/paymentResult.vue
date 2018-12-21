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
import { Msg, Divider, XButton } from "vux";

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
      "已支付成功，点击“订单详情”可查看订单详细信息；点击“兑换商品”可选择要兑换的商品。";
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
      this.$router.push("/member/order/orderDetail?id=" + this.id);
    },
    toProductList() {
      this.$router.push("/exchange/list");
    }
  },
  data() {
    return {
      id: undefined,
      sn: undefined,
      title: "支付成功",
      description: "",
      icon: require("../../assets/icon-success.png"),
      buttons: [
        {
          type: "primary",
          text: "订单详情",
          onClick: this.toOrderDetail.bind(this)
        },
        {
          type: "default",
          plain: true,
          text: "兑换商品",
          link: "/product/list"
        }
      ]
    };
  }
};
</script>
