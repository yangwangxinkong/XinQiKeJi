<template>
  <div class="page">
    <div class="container bg_f" v-if="order">
      <div class="order-status bg_f pt10 mt10 padding-15">
        <!-- <cell class="f14" title :value="order.status"></cell>
        <cell class="f14" title="订单编号" style="font-size: 14px;" :value="order.sn"></cell>
        <cell
          class="f14"
          title
          :value="'还有'+order.expiredTime+'失效'"
          v-if="order.status === '等待付款' && order.expireDate"
        ></cell> -->
        <h3 class="fw f18 mb10">{{order.status}}</h3>
        <p  class="mt5 f14">订单编号：{{order.sn}}</p>
        <p class="mt5 f14" v-if="order.status === '等待付款' && order.expireDate">还有{{order.expiredTime}}失效</p>
         <p class="mt5 f14" v-if="order.status === '已取消'|| order.status === '已支付'">创建时间：{{order.createDate}}</p>
      </div>
      <group title-color="#333" style="font-size: 16px;" :gutter="10">
        <cell title="订单信息" :link="'/order/orderItemInfo?id='+ order.quotation.id" value="查看明细"></cell>

        <cell-form-preview :list="list"></cell-form-preview>

        <!-- <cell class="balance" title="余额">
                    <div class="right">
                        <span>共{{lumpSum | money}}元</span>
                        <span v-if="isSwitch">已使用{{usedAmount | money}}元</span>
                    </div>
                    <inline-x-switch v-model="isSwitch" @on-click="onClick"></inline-x-switch>
                </cell>

                <cell title="使用余额">
                    <span v-if="isSwitch">{{usedAmount | money}}元</span>
                    <span v-else>0元</span>
        </cell>-->
      </group>
      <group :gutter="10">
        <cell-form-preview :list="amountList"></cell-form-preview>
      </group>
      <!-- 底部按钮 -->
      <section class="footer-bar" v-if="order.status === '等待付款'">
        <flexbox>
          <flexbox-item>
            <p class="amount-text">
              还需付款：
              <span>{{order.amountPayable | money}}</span>
              元
            </p>
          </flexbox-item>
          <flexbox-item :span="4">
            <div class="flex1 tc white footer-btn" @click="toPay(order.id)">
              <span class="f16 pl0">去支付</span>
            </div>
          </flexbox-item>
        </flexbox>
      </section>
    </div>
  </div>
</template>
<script>
import {
  Cell,
  CellFormPreview,
  InlineXSwitch,
  Group,
  Flexbox,
  FlexboxItem,
  Flow,
  FlowState,
  FlowLine
} from "vux";
import { get } from "@/api/server";
import url from "@/api/apiUrl";
import storage from "@/utils/common";
import money from "@/utils/utils";

export default {
  components: {
    Cell,
    CellFormPreview,
    InlineXSwitch,
    Group,
    Flexbox,
    FlexboxItem,
    Flow,
    FlowState,
    FlowLine
  },
  data() {
    return {
      orderId: this.$route.query && this.$route.query.id,
      order: [],
      list: [],
      isSwitch: true,
      amountList: []
    };
  },
  methods: {
    initData() {
      // 选择信息获取
      if (this.orderId != null) {
        get(url.orderDetail, { id: this.orderId })
          .then(response => {
            if (response.data.result == "00000000") {
              this.order = response.data.data;
              this.list = response.data.data.quotation.infoList;
              this.amountList = response.data.data.quotation.payList;
            }
          })
          .catch(e => {
            console.log(e);
          });
      }
    },
    toPay(id) {
      //console.log(item, index)
      this.$router.push({ path: "/order/orderPay", query: { id: id } });
    },
    onClick() {}
  },
  created() {
    this.initData();
  }
};
</script>
<style lang="less">
@color_text: #333;
.balance .right {
  display: inline-block;
  margin-right: 40px;
  font-size: 14px;
  color: @color_text;
  span:nth-of-type(2) {
    color: @amount_color;
  }
}

label.weui-form-preview__label {
  float: left;
  margin-right: 1em;
  min-width: 4em;
  font-size: 14px !important;
  color: #333;
  text-align: justify;
  text-align-last: justify;
}

span.weui-form-preview__value {
  display: block;
  overflow: hidden;
  word-break: normal;
  word-wrap: break-word;
  font-size: 14px !important;
  //color: #333;
}

.order-status {
  background: url(../../../../assets/order-detail-bg.png) no-repeat;
  width: 100%;
  background-size: 100% 100%;
  background-position: center;
  color: #fff;
  position: relative;
  height: 3.067rem;
}
</style>


