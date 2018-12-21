<template>
  <div class="page">
    <div class="container">
      <flow class="flow-style">
        <flow-state state="1" title="个人信息" is-done></flow-state>
        <flow-line is-done></flow-line>
        <flow-state state="2" title="参保信息" is-done></flow-state>
        <flow-line is-done></flow-line>
        <flow-state state="3" title="确认支付" is-done></flow-state>
      </flow>
      <group title-color="#333" class="bg_f" style="font-size: 16px;" :gutter="10">
        <cell title="订单信息" :link="'/order/orderItemInfo?id='+ quotationId" value="查看明细"></cell>
        <cell-form-preview style="color:#333;" :list="list"></cell-form-preview>
        <!-- <cell class="balance" :title="balanceTile"> -->
      </group>
      <group :gutter="10">
        <cell class="balance" title="余额">
          <div class="right">
            <span>共{{lumpSum | money}}元</span>
            <span v-if="isSwitch">已使用{{usedAmount | money}}元</span>
          </div>
          <inline-x-switch
            style="border-color:#E60014; background-color:#E60014;"
            v-model="isSwitch"
            @on-click="onClick"
          ></inline-x-switch>
        </cell>
       
      </group>
      <group :gutter="10">
         <cell title="优惠券" @click.native="useCoupon" :value="couponCode.name" isLink></cell>
        <cell-form-preview :list="amountList"></cell-form-preview>
        <cell title="使用余额" class="f14">
          <span v-if="isSwitch">{{usedAmount | money}}元</span>
          <span v-else>0.00元</span>
        </cell>
        <cell title="使用优惠券"  class="f14">
          <span>{{couponDiscountFee | money}}元</span>
        </cell>
      </group> 
      <!-- 底部按钮 -->
      <section class="footer-bar">
        <flexbox>
          <flexbox-item>
            <p class="amount-text">
              合计：
              <span v-if="isSwitch">{{usedBalanceAmount | money}}元</span>
              <span v-else>{{amount | money}}元</span>
            </p>
          </flexbox-item>
          <flexbox-item :span="4">
            <div
              class="flex1 tc white footer-btn"
              :disabled="btnNext"
              @click="!btnNext && createOrder()"
            >
              <span class="f16 pl0">提交订单</span>
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
import { get, post } from "@/api/server";
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
      btnNext: false,
      quotationId: undefined,
      list: [],
      lumpSum: "0.00",
      usedAmount: "0.00",
      usedBalanceAmount: "0.00",
      couponDiscount: "0.00",
      couponDiscountFee: "0.00",
      isSwitch: true,
      amountList: [
        //     {
        //       label:'缴纳费用',
        //       value:'41147.88元'
        //   }, {
        //       label: '服务费',
        //       value: '41147.88元'
        //   },
        //   {
        //       label: '滞纳金',
        //       value: '0.00元'
        //   }
      ],
      balanceTile: "余额",
      amount: "0.00",
      fee: 0,
      couponCode: {
        name: "使用优惠券"
      }
    };
  },
  methods: {
    initData() {
      // 选择信息获取
      if (this.quotationId != null) {
        get(url.quotationConfirmInfo, { id: this.quotationId })
          .then(response => {
            if (response.data.result == "00000000") {
              this.list = response.data.data.infoList;
              this.amountList = response.data.data.payList;
              this.amount = response.data.data.amount;
              this.fee = response.data.data.fee;

              this.lumpSum = response.data.data.balance;
              this.balanceTile =
                this.balanceTile + "   " + "   共" + this.lumpSum + "元";
              this.usedAmount = response.data.data.useBalance;
              this.usedBalanceAmount = response.data.data.usedBalanceAmount;
              this.resetCoupon();
            }
          })
          .catch(e => {
            console.log(e);
          });
      }
    },
    useCoupon() {
      this.$router.push({ path: "/member/coupon/select" });
    },
    resetCoupon() {
      let couponCode = localStorage.getItem("couponCode");
      couponCode = JSON.parse(couponCode);
      if (couponCode != null) {
        //this.isSwitch = false;
        this.couponCode = couponCode;
        this.couponDiscount = couponCode.discount / 10;
        this.couponDiscountFee = this.fee * (1 - this.couponDiscount);
        this.usedBalanceAmount =
          this.usedBalanceAmount - this.couponDiscountFee;
        this.amount = this.amount - this.couponDiscountFee;
        localStorage.setItem("couponCode", null);
      }
    },
    onClick() {},
    createOrder() {
      this.btnNext = true;
      post(url.createOrder, {
        id: this.quotationId,
        useBalance: this.isSwitch,
        couponDiscount: this.couponDiscount,
        couponCodeId: this.couponCode.id
      })
        .then(response => {
          if (response.data.result == "00000000") {
            localStorage.clear("quotationId");
            this.quotationId = undefined;
            this.$router.push({
              path: "/order/orderPay",
              query: { id: response.data.data }
            });
          } else {
            this.$vux.toast.text(response.data.msg, "middle");
            this.btnNext = false;
            return false;
            //            this.$notify({
            //              title: "失败",
            //              message: response.data.message,
            //              type: "error",
            //              duration: 2000
            //            });
          }
        })
        .catch(e => {
          console.log(e);
        });
    }
  },
  created() {
    let id = localStorage.getItem("quotationId");
    if (id != null && id != undefined && id != "") {
      this.quotationId = id;
    }

    // 是否登录
    let member = storage.get("member");
    if (member == null && member == undefined) {
      this.$router.push({
        path: "/login/index",
        query: { redirect: this.$route.fullPath }
      });
    }

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
  min-width: 120px;
  text-align: left;
  span:nth-of-type(2) {
    color: @amount_color;
  }
}

label.weui-form-preview__label {
  float: left;
  margin-right: 1em;
  min-width: 4em;
  font-size: 14px !important;
  color: #2D2E46;
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
</style>


