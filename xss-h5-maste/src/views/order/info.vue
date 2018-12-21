<template>
    <div class="page">
        <div class="container order-container" v-if="order">
            <!--收货地址  -->
            <div class='address_defalut address_border bg_f'>
                <!--存在收货地址  -->
                <div class="show-address flex-ui space-between align-items-center f14 " @click="selectAddress" v-if="receiver">
                    <div class="show-address-info flex-ui">
                        <div class="fl">
                            <p class="addr-text ">{{receiver.consignee}}</p>
                            <p class="default-tag">
                                <span>默认</span>
                            </p>
                        </div>
                        <div class=" addr-base-info flex1">
                            <div class="addr-text">{{receiver.phone.substring(0,3)}}****{{receiver.phone.substring(receiver.phone.length-4)}}</div>
                            <div class="name-address">{{receiver.areaName}}{{receiver.address}}</div>
                        </div>
                    </div>
                    <i class="iconfont icon-arrow-right grey_be"></i>
                </div>
                <!--不存在收货地址  -->
                <div class="add-address tc" v-else>
                    <div class="title f14" @click="addAddress">
                        <span class="gray_base">新增收货地址</span>
                    </div>
                </div>
            </div>
            <!-- 商品信息 -->
            <div class="order-info bg_f mt10">
                <!-- 店铺信息 -->
                <div class="block block-order block-cart f14 padding-15">
                    <div class="order_good_bd pt10 pb10">
                        <div class="block-item block-item-cart order_good_item flex-ui" v-for="(orderItem, itemIndex) in orderConfirmLists" :key="itemIndex">
                            <div class="good_cover mr10">
                                <img class="image" :src="orderItem.product.image" alt="">
                            </div>
                            <div class="good-info flex1">
                                <div class=" flex-ui space-between">
                                    <div class="good_title ellipsis2 pr10">
                                        {{orderItem.name}}
                                    </div>
                                    <div class="good_num">
                                        <span class="grey_be">x{{orderItem.quantity}}</span>
                                    </div>
                                </div>
                                <!-- 商品的颜色 -->
                                <div class="good_color grey_be mt5">{{orderItem.product.specification}}</div>
                                <!-- <div class="good_sku flex-ui space-between align-items-center mt10">
                                    <div class="good_price color_red">金豆:{{orderItem.product.exchangePoint}}个</div>
                                    <div class="good_num">
                                        <span class="grey_be">x{{orderItem.quantity}}</span>
                                    </div>
                                </div> -->
                                <div class="good_price color_base mt10">{{orderItem.product.exchangePoint}}金豆</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 留言-->
            <div class="f14  bg_f mt10">
                <x-input title="留言" name="message" v-model="memo" placeholder="填写留言 "></x-input>
            </div>
            <!-- 额外信息 -->
            <!-- <div class="order_total f14 bg_f mt10 padding-15" v-if="order">
                <ul class="buy_chart clearfix">
                    <li class="buy_chart_item">
                        <p class="buy_chart_item_text">商品金豆</p>
                        <span class="buy_chart_item_price">{{order.point}}个</span>
                    </li>
                    <li class="buy_chart_item">
                        <p class="buy_chart_item_text">账户金豆</p>
                        <span class="buy_chart_item_price">{{memberPoint}}个</span>
                    </li>
                </ul>
                <p class="total tr">
                    <span class="f14"> 应付金豆：
                        <strong class="color_red">{{order.point}}个</strong>
                    </span>
                </p>
            </div> -->
            <!-- 提交订单 -->
            <!-- <div class="order-submit-bar">
            <div class="order-submit-bar__bar">
              <x-button @click.native="orderTap" type="primary" v-if="isValid" >提交订单</x-button>
              <x-button type="primary" style="background-color:#666;" v-else>{{greyBtnMsg}}</x-button> -->
            <!--<div class="order-submit-bar__text">
                    <span>合计：</span>
                    <span class="order-submit-bar__price">{{order.point}}金豆</span>
                </div>
                <button class="order-button btn btn-green btn-bg btn-radiu mr15" @click="orderTap">
                    <span class="order-button__text">提交订单</span>
                </button>-->
            <!-- </div>
        </div> -->

            <!-- 底部按钮 -->
            <section class="footer-bar">
                <flexbox>
                    <flexbox-item>
                        <div style="padding:6px 0 5px 15px">
                            <p style="line-height:.533rem;font-size:.373rem">应付金豆：
                                <span class="color_base">{{order.point}}</span>个</p>
                            <p style="line-height:.533rem;font-size:.32rem;color: rgba(45,46,70,0.50);">您目前可用金豆：{{memberPoint}}</p>
                        </div>

                    </flexbox-item>
                    <flexbox-item :span="5">
                        <div class="flex1 tc white footer-btn" @click="orderTap()" v-if="isValid">
                            <span class="f16 pl0">提交订单</span>
                        </div>
                        <div class="flex1 tc white footer-btn" style="background: #C5C5CC;" v-else>
                            <span class="f16 pl0">提交订单</span>
                        </div>
                    </flexbox-item>
                </flexbox>
            </section>
        </div>
    </div>
</template>

<script type="text/ecmascript-6">
import { XButton, XInput, Flexbox, FlexboxItem } from "vux";
import { get, post } from "@/api/server";
import url from "@/api/apiUrl";
import { numFilter } from "@/utils/utils";

export default {
  components: {
    XButton,
    XInput,
    Flexbox,
    FlexboxItem
  },
  props: {},
  data() {
    return {
      itemId: this.$route.query && this.$route.query.itemId,
      cartItemIds: this.$route.query && this.$route.query.cartItemIds,
      total: 0,
      cashback: 0,
      freight: 0,
      paytotal: 0,
      promotionDiscount: 0,
      receiver: null,
      orderConfirmLists: null,
      lists: null,
      buylists: null,
      buyMethod: null,
      payMethod: null,
      cartToken: null,
      receiverId: null,
      paymentMethodId: 1,
      shippingMethodId: 7,
      order: null,
      memo: undefined,
      orderTypeId: undefined,
      order_type: undefined,
      memberPoint: 0,
      isValid: false,
      greyBtnMsg: "余额不足"
    };
  },
  watch: {},
  computed: {},
  methods: {
    //调整地址新增页面
    addAddress() {
      this.$router.push({
        path: "/address/add",
        query: { cartItemIds: this.cartItemIds }
      });
    },
    //选择地址
    selectAddress() {
      this.$router.push({
        path: "/address/list",
        query: { cartItemIds: this.cartItemIds }
      });
    },
    //支付
    orderTap() {
      if (
        this.receiverId == undefined ||
        this.receiverId == null ||
        this.receiverId == ""
      ) {
        this.$vux.toast.show({
          type: "warn",
          text: "请填写收货地址!",
          width: "120px",
          time: 1000
        });

        return;
      }

      //传递参数
      let params = {
        cartItemIds: this.cartItemIds,
        paymentMethodId: this.paymentMethodId,
        shippingMethodId: this.shippingMethodId,
        cartToken: this.cartToken,
        receiverId: this.receiverId,
        memo: this.memo
      };
      // 提交信息
      post(url.orderCreate, params)
        .then(res => {
          if (res.data.result === "00000000") {
            this.$router.push({
              path: "/order/result",
              query: { id: res.data.data.orderId, sn: res.data.data.orderSn }
            });
          } else if (res.data.result == "10000007") {
            this.$router.push({
              path: "/login",
              query: { redirect: this.$route.fullPath }
            });
          } else if (response.data.result === "10000035") {
            this.$vux.toast.show({
              type: "alert",
              text: response.data.msg,
              width: "120px",
              time: 1000
            });
            this.$router.push({ path: "/" });
          } else {
            this.$vux.toast.show({
              type: "warn",
              text: res.data.msg,
              width: "120px",
              time: 1000
            });
          }
        })
        .catch(e => {
          console.log(e);
        });
    },
    //地址
    resetAddress() {
      let address = localStorage.getItem("address");
      address = JSON.parse(address);
      if (address != null) {
        this.receiver.id = address.id;
        this.receiver.consignee = address.consignee;
        this.receiver.phone = address.phone;
        this.receiver.address = address.address;
        this.receiver.areaName = address.areaName;
        localStorage.setItem("address", null);
      }
    },
    //获取订单信息
    getCartItemsConfirmLists() {
      //传递分页的参数
      let params = {
        cartItemIds: this.cartItemIds,
        paymentMethodId: this.paymentMethodId,
        shippingMethodId: this.shippingMethodId
      };

      get(url.orderInfo, params).then(response => {
        if (response.data.result === "10000007") {
          this.$router.push({
            path: "/login",
            query: { redirect: this.$route.fullPath }
          });
        } else if (response.data.result === "10000035") {
          this.$router.back(-2);
        } else if (response.data.result != "00000000") {
          this.$vux.toast.show({
            type: "alert",
            text: response.data.msg,
            width: "120px",
            time: 1000
          });
        } else {
          this.order = response.data.data;
          this.memberPoint = this.order.memberPoint;
          this.orderConfirmLists = response.data.data.orderItems;
          this.orderTypeId = this.order.orderTypeId;
          let receiver = response.data.data.receiver;
          this.receiver = receiver;
          this.resetAddress();
          if (receiver == null || receiver.id == null || receiver.id == "") {
            this.receiver = null;
            this.receiverId = null;
          } else {
            this.receiverId = response.data.data.receiver.id;
          }
          this.cartToken = response.data.data.cartToken;
          this.isValid = true;
          if (this.order.point > this.memberPoint) {
            this.isValid = false;
            this.greyBtnMsg = "余额不足";
          } else if (!this.receiver) {
            this.isValid = false;
            this.greyBtnMsg = "请填写收货地址";
          }
        }
      });
    }
  },
  created() {
    this.getCartItemsConfirmLists();
  },
  mounted() {}
};
</script>

<style scoped lang="less">
@import "../../assets/styles/less/public.less";
.order-submit-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: #fff;
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
  .order-submit-bar__bar {
    height: 50px;
    .f14;
    .flex-ui;
    .align-items-center;
    .order-submit-bar__text {
      .flex1;
      padding-right: 12px;
      font-weight: 500;
      .tr;
      .grey_3;
      .order-submit-bar__price {
        .color_red;
      }
    }
    .order-button {
      border-radius: 20px;
      padding: 0 15px;
    }
  }
}

.weui-btn_primary {
  background-color: @color_base;
}
</style>
