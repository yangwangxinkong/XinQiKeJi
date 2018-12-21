<template>
  <div class="page">
    <div class="container" v-if="order">
      <!-- 订单进程 -->
      <!-- <div class="order-step bg_f padding-15 pt10 pb10">
        <v-step :step-list="stepList"></v-step>
      </div>-->

      <!-- 配送信息 -->
      <!--<div class="detailcard-delivery card bg_f padding-15 mt10" v-if="order.shippingList">
        <div class="wuliu-box f14">
          <div class="icon-box">
            <img class="icon" src="../../../assets/icon-wuliu.png"/>
          </div>
          <div class="right-text" v-for="(shipping, shippingIndex) in order.shippingList" :key="shippingIndex" v-if="order && order.shippingList">
            <router-link :to="{path:'/member/order/delivery/' + order.id}">
              <div class="order-number grey_3">快递单号：{{shipping.shippingTrackingNo}}</div>
              <div class="grey_be f12">
                <div class='wuliu-statu'>{{shipping.shippingStatu}}</div>
                <div class="wuliu-text">{{shipping.shippingLastInfo}}</div>
                <div class="wuliu-date">{{shipping.shippingLastTime}}</div>
              </div>
            </router-link>
          </div>
          <i class="iconfont icon-arrow-right grey_be"></i>
        </div>
      </div>-->
      <!-- 收货信息 -->
      <div class='address_defalut address_border bg_f mt10'>
        <!--存在收货地址  -->
        <div class="show-address flex-ui space-between align-items-center f14 ">
          <div class="show-address-info flex-ui">
            <div class="fl">
              <p class="addr-text ">{{order.consignee}}</p>
              <p class="default-tag">
                <span>默认</span>
              </p>
            </div>
            <div class=" addr-base-info flex1">
              <div class="addr-text">{{order.phone}}</div>
              <div class="name-address">{{order.areaName}}{{order.address}}</div>
            </div>
          </div>
        </div>
      </div>
      <!-- 订单信息 -->
      <div class="order-info bg_f mt10">
        <!-- 店铺信息 -->
        <div class="block block-order block-cart f14 padding-15">
          <div class="order_good_bd pt10 pb10">
            <div class="block-item block-item-cart order_good_item flex-ui" v-for="(orderItem, shopIndex) in order.orderItems" :key="shopIndex">
              <div class="good_cover mr10">
                <img class="image" :src="orderItem.product.image" alt="">
              </div>
              <div class="good-info flex1">
                <div class="good_title ellipsis2">
                  {{orderItem.fullName}}
                </div>
                <!-- 商品的颜色 -->
                <div class="good_color grey_be mt5">{{orderItem.product.specification}}</div>
                <div class="good_sku flex-ui space-between align-items-center mt10">
                  <div class="good_price color_red">金豆：{{orderItem.product.exchangePoint}}个</div>
                  <div class="good_num">
                    <span class="grey_be">x{{orderItem.quantity}}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 订单信息inner -->
      <div class="inner bg_f padding-15 mt10 f14">
        <div class="inner_line">
          <span class="title">订单状态：</span>
          <div class="content">
            <em class="color_red">{{order.allStatus}}</em>
          </div>
        </div>
        <div class="inner_line">
          <span class="title">订单编号：</span>
          <div class="content">{{order.sn}}</div>
        </div>
        <div class="inner_line">
          <span class="title">下单时间：</span>
          <div class="content">{{order.createDate}}</div>
        </div>
        <div class="inner_line" v-if="order.shippingStatus==='shipped' || order.shippingStatus==='received'">
          <span class="title">物流公司：</span>
          <div class="content" v-if="order.deliveryCorp">{{order.deliveryCorp}}</div>
        </div>
        <div class="inner_line" v-if="order.shippingStatus==='shipped' || order.shippingStatus==='received'">
          <span class="title">物流单号：</span>
          <div class="content" v-if="order.trackingNo">{{order.trackingNo}}</div>
        </div>
      </div>
      <!-- 统计信息 -->
      <div class="order_total f14 bg_f mt10 padding-15">
        <ul class="buy_chart clearfix">
          <li class="buy_chart_item">
            <p class="buy_chart_item_text">订单金豆</p>
            <span class="buy_chart_item_price">{{order.point}}个</span>
          </li>
        </ul>
      </div>
      <!-- footer -->

    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import { XButton } from "vux";
import url from "@/api/apiUrl";
import { get, post } from "@/api/server";
export default {
  components: { XButton },
  props: {},
  data() {
    return {
      order: {},
      stepList: [],
      id: this.$route.query && this.$route.query.id
    };
  },
  watch: {},
  computed: {},
  methods: {
    getOrderDetailInfo() {
      get(url.exchargeInfo, { id: this.id }).then(response => {
        //console.log(response.data);
        if (response.data.result === "00000000") {
          //this.standard = response.data.data;
          this.order = response.data.data;
          console.log("order:" + JSON.stringify(this.order));
          this.stepList = response.data.data.stepList;
        } else if (response.data.result === "10000007") {
          this.$router.push({
            path: "/login",
            query: { redirect: this.$route.fullPath }
          });
        } else {
          this.$vux.toast.show({
            type: "warn",
            text: response.data.msg,
            width: "120px",
            time: 1000
          });
        }
      });
    }
  },
  created() {
    this.getOrderDetailInfo();
  },
  mounted() {}
};
</script>
<style lang="less">
@import "../../../../assets/styles/less/public.less";
.vux-step-item-title {
  .f12;
}
</style>

<style scoped lang="less">
@import "../../../../assets/styles/less/public.less";

.bottom-bar {
  position: fixed;
  bottom: 0;
  width: 100%;
  margin: 0 auto;
  z-index: 110;
  background-color: #fff;
  height: 50px;
  line-height: 50px;
  &:before {
    content: " ";
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    height: 1px;
    border-top: 1px solid #c0bfc4;
    color: #c0bfc4;
    -webkit-transform-origin: 0 0;
    transform-origin: 0 0;
    -webkit-transform: scaleY(0.5);
    transform: scaleY(0.5);
  }
  > div {
    line-height: 50px;
    text-align: right;
    font-size: 14px;
    .weui-btn {
      margin: 0;
      padding: 0 10px;
      border-radius: 20px;
      line-height: 25px;
      font-size: 12px;
      margin-left: 5px;
    }
  }
}
</style>
