<template>
  <!-- 参保信息 -->
  <div class="page">
    <div class="container bg_f">
      <flow class="flow-style">
        <flow-state state="1" title="个人信息" is-done></flow-state>
        <flow-line is-done></flow-line>
        <flow-state state="2" title="参保信息" is-done></flow-state>
        <flow-line></flow-line>
        <flow-state state="3" title="确认支付"></flow-state>
      </flow>
      <group title="服务信息" title-color="#333" style="font-size: 16px;" :gutter="0">
        <popup-radio
          title="参保类型"
          class="f14"
          :options="insuredTypes"
          v-model="insuredType"
          @on-hide="hideFun()"
        ></popup-radio>
        <popup-radio
          title="代缴月数"
          class="f14"
          :options="monthTypes"
          v-model="postForm.monthCount"
          @on-hide="monthHideFun(postForm.monthCount)"
        ></popup-radio>
        <datetime
          title="开始时间"
          class="f14"
          format="YYYY-MM"
          v-model="postForm.startDate"
          @on-change="change"
          @on-cancel="log('cancel')"
          @on-hide="log('hide', $event)"
        ></datetime>
        <x-input
          title="结束时间"
          class="f14"
          text-align="right"
          v-model="postForm.endDate"
          v-bind:readonly="true"
        ></x-input>
        <!-- <popup-radio title="参保基数(3390-25401)" :options="insuredNums" v-model="insuredNum"></popup-radio> -->
        <x-input
          :title="titleSocialBase"
          class="f14"
          text-align="right"
          v-model="postForm.socialBase"
          @on-blur="baseChanged"
          v-if="feeCategoryId === 0 || feeCategoryId === 2"
        ></x-input>

        <!-- <x-input title="公积金比例" class="f14" text-align="right" v-model="postForm.socialBase"></x-input> -->
        <x-input
          :title="titleProvidentBase"
          class="f14"
          text-align="right"
          v-model="postForm.providentBase"
          @on-blur="baseChanged"
          v-if="feeCategoryId === 1 || feeCategoryId === 2"
        ></x-input>
      </group>

      <cell class="hint-info" title value inline-desc="*此基数为所选地区最低基数，如有调整，请自行修改"></cell>
      <group title="费用信息" title-color="#333" class="cost-info" style="font-size: 16px;font-weight: bold;margin-top:0;" :gutter="0">
        <cell class="cell-text f14" title="缴纳金额" :value="payAmount+'元'"></cell>
        <cell
          class="cell-text f14"
          title="服务费用"
          :value="serviceMonthFee+'元*'+postForm.monthCount+'月'"
        ></cell>
        <cell class="cell-text f14" title="会员套餐优惠" :value="vipDiscountFee+'元'"></cell>
        <cell class="cell-text f14" title="新手套餐优惠" :value="firstOrderDiscountFee+'元'"></cell>
        <!-- <cell class="cell-text f14" title="服务费用" :value="serviceFee+'元'"></cell>                 -->
      </group>
      <div class="div-tip mt10 padding-15">
        <check-icon :value.sync="isAgree" class="f14">同意人事代理服务条款</check-icon>
        <router-link
          :to="{path:'/register/useragreement/index'}"
          class="agreement color_base"
          style="vertical-align: -2px;"
        >《小豆社保协议》</router-link>
      </div>

      <!-- 底部按钮 -->
      <section class="footer-bar">
        <flexbox>
          <flexbox-item>
            <p class="amount-text">
              合计：
              <span style="font-weight:bold;">{{amount | money}}</span>
              元
            </p>
          </flexbox-item>
          <flexbox-item :span="5">
            <div
              class="flex1 tc white footer-btn"
              :disabled="btnNext"
              @click="!btnNext && nextPage()"
            >
              <span class="f16 pl0">下一步</span>
            </div>
          </flexbox-item>
        </flexbox>
      </section>
    </div>
  </div>
</template>
<script>
import {
  PopupRadio,
  Datetime,
  Cell,
  Group,
  CheckIcon,
  Flexbox,
  FlexboxItem,
  Flow,
  FlowState,
  FlowLine,
  XInput
} from "vux";
import { get, execute } from "@/api/server";
import url from "@/api/apiUrl";
import storage from "@/utils/common";
import money from "@/utils/utils";
import { validIsBlank } from "@/utils/validate";

const defaultForm = {
  id: undefined,
  payCategory: "", //参保类型
  monthCount: undefined, //缴费月数
  startDate: undefined, //开始日期
  endDate: undefined, //结束日期
  socialBase: undefined, //社保缴费基数
  providentBase: undefined //公积金缴费基数
};

export default {
  data() {
    return {
      btnNext: false,
      insuredType: 0,
      insuredTypes: [
        // {
        //     value:'新参',
        //     key:0,
        // },
        // {
        //     value:'续缴',
        //     key:1
        // },
        // {
        //     value:'补缴',
        //     key:2
        // }
      ],
      monthTypes: [
        {
          value: "1个月",
          key: 1
        },
        {
          value: "2个月",
          key: 2
        },
        {
          value: "3个月",
          key: 3
        },
        {
          value: "4个月",
          key: 4
        },
        {
          value: "5个月",
          key: 5
        },
        {
          value: "6个月",
          key: 6
        },
        {
          value: "7个月",
          key: 7
        },
        {
          value: "8个月",
          key: 8
        },
        {
          value: "9个月",
          key: 9
        },
        {
          value: "10个月",
          key: 10
        },
        {
          value: "11个月",
          key: 11
        },
        {
          value: "12个月",
          key: 12
        }
      ],
      monthType: 1,
      insuredNum: "0",
      isAgree: true,
      payAmount: "0",
      serviceMonthFee: "0",
      serviceFee: "0",
      amount: "0",
      quotationId: undefined,
      payBase: {},
      socialBaseMin: "0",
      socialBaseMax: "0",
      providentBaseMin: "0",
      providentBaseMax: "0",
      titleSocialBase: "参保基数",
      titleProvidentBase: "公积金基数",
      feeCategoryId: undefined,
      vipDiscountFee: undefined,
      firstOrderDiscountFee: undefined,
      postForm: Object.assign({}, defaultForm)
    };
  },
  components: {
    PopupRadio,
    Datetime,
    Cell,
    Group,
    CheckIcon,
    Flexbox,
    FlexboxItem,
    Flow,
    FlowState,
    FlowLine,
    XInput
  },
  methods: {
    initData() {
      // 选择信息获取
      if (this.quotationId != null) {
        get(url.quotationPayInfo, { id: this.quotationId })
          .then(response => {
            if (response.data.result == "00000000") {
              // 缴费类型 fc0(0, "社保"),fc1(1, "公积金"),fc2(2, "社保+公积金");
              this.feeCategoryId = response.data.data.feeCategoryId;

              // 开始时间
              if (
                response.data.data.startDate == null ||
                response.data.data.startDate == undefined
              ) {
                this.postForm.startDate = this.getCurrentYM();
                this.postForm.endDate = this.postForm.startDate;
                this.postForm.monthCount = 1;
              } else {
                this.postForm.startDate = response.data.data.startDate;
                // 结束时间
                this.postForm.endDate = response.data.data.endDate;
                this.postForm.monthCount = response.data.data.monthCount;
              }

              // 缴费月数
              // if(response.data.data.monthCount == null || response.data.data.monthCount == undefined) {
              //     this.postForm.monthCount = 1;
              // } else {
              //     this.postForm.monthCount = response.data.data.monthCount;
              // }
              // 参保类型
              this.insuredTypes = response.data.data.payCategorys;
              if (
                response.data.data.payCategoryId == null ||
                response.data.data.payCategoryId == undefined
              ) {
                this.insuredType = response.data.data.payCategorys[0].key;
              } else {
                this.insuredType = response.data.data.payCategoryId;
              }

              // 参保基数
              this.payBase = response.data.data.payBase;
              if (
                response.data.data.socialBase == null ||
                response.data.data.socialBase == undefined
              ) {
                this.postForm.socialBase = this.payBase.socialBaseMin;
              } else {
                this.postForm.socialBase = response.data.data.socialBase;
              }

              this.socialBaseMin = this.payBase.socialBaseMin;
              this.socialBaseMax = this.payBase.socialBaseMax;

              this.titleSocialBase =
                "参保基数(" +
                this.payBase.socialBaseMin +
                "-" +
                this.payBase.socialBaseMax +
                ")";

              // 公积金参保基数
              if (
                response.data.data.providentBase == null ||
                response.data.data.providentBase == undefined
              ) {
                this.postForm.providentBase = this.payBase.providentBaseMin;
              } else {
                this.postForm.providentBase = response.data.data.providentBase;
              }

              this.providentBaseMin = this.payBase.providentBaseMin;
              this.providentBaseMax = this.payBase.providentBaseMax;

              this.titleProvidentBase =
                "公积金基数(" +
                this.payBase.providentBaseMin +
                "-" +
                this.payBase.providentBaseMax +
                ")";

              this.payAmount = response.data.data.allAmount;
              this.serviceMonthFee = response.data.data.monthFee;
              this.serviceFee = response.data.data.fee;
              this.amount = response.data.data.orderAmount;
              this.vipDiscountFee = response.data.data.vipDiscountFee;
              this.firstOrderDiscountFee =
                response.data.data.firstOrderDiscountFee;
            }
          })
          .catch(e => {
            console.log(e);
          });
      }
    },
    hideFun() {
      // 重新计算报价金额
      this.doCalculator();
    },
    monthHideFun(intMonth) {
      if (
        this.postForm.startDate != null &&
        this.postForm.startDate != undefined &&
        this.postForm.startDate != "" &&
        intMonth != null &&
        intMonth != undefined &&
        intMonth != ""
      ) {
        this.postForm.endDate = this.getEndYM(
          this.postForm.startDate,
          intMonth
        );
      }

      // 重新计算报价金额
      this.doCalculator();
    },
    change(value) {
      if (
        value != null &&
        value != undefined &&
        value != "" &&
        this.postForm.monthCount != null &&
        this.postForm.monthCount != undefined &&
        this.postForm.monthCount != ""
      ) {
        this.postForm.endDate = this.getEndYM(value, this.postForm.monthCount);
      }

      // 重新计算报价金额
      //this.doCalculator();
    },
    baseChanged() {
      // 重新计算报价金额
      this.doCalculator();
    },
    log(str1, str2) {
      console.log(str1, str2);
    },
    title() {},
    getCurrentYM() {
      var date = new Date();
      var seperator1 = "-";
      var year = date.getFullYear();
      var month = date.getMonth() + 1; //var strDate = date.getDate();
      if (month >= 1 && month <= 9) {
        month = "0" + month;
      }
      //   if (strDate >= 0 && strDate <= 9) {
      //     strDate = "0" + strDate;
      //   }
      var currentdate = year + seperator1 + month;
      return currentdate;
    },

    getEndYM(startYM, intMonth) {
      var date = new Date(startYM + "-01");
      date.setMonth(date.getMonth() + (intMonth - 1));

      var seperator1 = "-";
      var year = date.getFullYear();
      var month = date.getMonth() + 1;

      if (month >= 1 && month <= 9) {
        month = "0" + month;
      }
      //   if (strDate >= 0 && strDate <= 9) {
      //     strDate = "0" + strDate;
      //   }
      var currentdate = year + seperator1 + month;
      return currentdate;
    },

    nextPage() {
      this.postForm.payCategory = "pc" + this.insuredType;
      this.postForm.id = this.quotationId;

      // 参保类型输入项校验
      if (validIsBlank(this.postForm.payCategory)) {
        this.$vux.toast.text("请选择参保类型", "middle");
        return false;
      }

      // 开始日期输入项校验
      if (
        this.postForm.startDate === null ||
        this.postForm.startDate === undefined ||
        validIsBlank(this.postForm.startDate)
      ) {
        this.$vux.toast.text("请选择开始日期", "middle");
        return false;
      }

      // 缴费月数输入项校验
      if (validIsBlank(this.postForm.monthCount)) {
        this.$vux.toast.text("请输入代缴月数", "middle");
        return false;
      }

      if (this.feeCategoryId === 0 || this.feeCategoryId === 2) {
        // 社保缴费基数
        if (
          this.postForm.socialBase === null ||
          this.postForm.socialBase === undefined ||
          validIsBlank(this.postForm.socialBase)
        ) {
          this.$vux.toast.text("请输入正确的社保缴费基数", "middle");
          return false;
        }
        if (
          parseInt(this.postForm.socialBase) < parseInt(this.socialBaseMin) ||
          parseInt(this.postForm.socialBase) > parseInt(this.socialBaseMax)
        ) {
          this.$vux.toast.text("请输入正确的社保缴费基数", "middle");
          return false;
        }
      }

      if (this.feeCategoryId === 1 || this.feeCategoryId === 2) {
        // 公积金缴费基数
        if (
          this.postForm.providentBase === null ||
          this.postForm.providentBase === undefined ||
          validIsBlank(this.postForm.providentBase)
        ) {
          this.$vux.toast.text("请输入正确的公积金缴费基数", "middle");
          return false;
        }
        if (
          parseInt(this.postForm.providentBase) <
            parseInt(this.providentBaseMin) ||
          parseInt(this.postForm.providentBase) >
            parseInt(this.providentBaseMax)
        ) {
          this.$vux.toast.text("请输入正确的公积金缴费基数", "middle");
          return false;
        }
      }
      if (!this.isAgree) {
        this.$vux.toast.text("请先同意小豆社保协议", "middle");
        return false;
      }

      this.btnNext = true;

      execute(url.saveQuotationPayInfo, this.postForm)
        .then(response => {
          if (response.data.result == "00000000") {
            //localStorage.setItem("quotationId", response.data.data);
            this.$router.push({ path: "/order/orderConfirm" });
          } else if (response.data.result == "10000045") {
            var that = this;
            this.$vux.confirm.show({
              type: "alert",
              title: "温馨提示",
              content: response.data.msg,
              confirmText: "我知道了",
              //cancelText: "继续支付",
              //onCancel() {},
              onConfirm() {
                //that.preventGoBack = false;
                that.$router.push({ path: "/member/order/index" });
              }
            });

            //this.$vux.toast.text(response.data.msg, 'middle')
            this.btnNext = false;
            return false;
          } else {
            this.$vux.toast.text(response.data.msg, "middle");
            this.btnNext = false;
            return false;
          }
        })
        .catch(e => {
          console.log(e);
        });
    },

    doCalculator() {
      this.postForm.payCategory = "pc" + this.insuredType;
      this.postForm.id = this.quotationId;

      // 参保类型输入项校验
      if (validIsBlank(this.postForm.payCategory)) {
        return false;
      }

      // 开始日期输入项校验
      if (
        this.postForm.startDate === null ||
        this.postForm.startDate === undefined ||
        validIsBlank(this.postForm.startDate)
      ) {
        return false;
      }

      // 缴费月数输入项校验
      if (validIsBlank(this.postForm.monthCount)) {
        return false;
      }

      if (this.feeCategoryId === 0 || this.feeCategoryId === 2) {
        // 社保缴费基数
        if (
          this.postForm.socialBase === null ||
          this.postForm.socialBase === undefined ||
          validIsBlank(this.postForm.socialBase)
        ) {
          return false;
        }
        if (
          parseInt(this.postForm.socialBase) < parseInt(this.socialBaseMin) ||
          parseInt(this.postForm.socialBase) > parseInt(this.socialBaseMax)
        ) {
          return false;
        }
      }

      if (this.feeCategoryId === 1 || this.feeCategoryId === 2) {
        // 公积金缴费基数
        if (
          this.postForm.providentBase === null ||
          this.postForm.providentBase === undefined ||
          validIsBlank(this.postForm.providentBase)
        ) {
          return false;
        }
        if (
          parseInt(this.postForm.providentBase) <
            parseInt(this.providentBaseMin) ||
          parseInt(this.postForm.providentBase) >
            parseInt(this.providentBaseMax)
        ) {
          return false;
        }
      }

      let params = {
        id: this.postForm.id,
        payCategoryId: this.insuredType,
        monthCount: this.postForm.monthCount,
        socialBase: this.postForm.socialBase,
        providentBase: this.postForm.providentBase
      };

      get(url.calculatePayInfo, params)
        .then(response => {
          if (response.data.result == "00000000") {
            this.payAmount = response.data.data.allAmount;
            this.serviceMonthFee = response.data.data.monthFee;
            this.serviceFee = response.data.data.fee;
            this.amount = response.data.data.orderAmount;
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

    this.initData();
  }
};
</script>
<style lang="less">
.hint-info {
    &:before{
        border-top:0 !important;
    }
}
.hint-info .vux-label-desc {
  //color:#CC3300;
  color: #e60014;
}
.agree-info > span {
  color: #a9a9a9 !important;
}
.cell-text .weui-cell__ft {
  color: @amount_color;
}
.weui-icon-circle,
.weui-icon-success {
  font-size: 18px !important;
}
label.weui-label {
  display: block;
  width: 180px;
  word-wrap: break-word;
  word-break: break-all;
}
input.weui-input {
  width: 100%;
  border: 0;
  outline: 0;
  -webkit-appearance: none;
  background-color: transparent;
  font-size: inherit;
  color: inherit;
  height: 1.41176471em;
  line-height: 1.41176471;
  padding-right: 12px;
}
.cost-info{
    .weui-cells__title{
        margin-top: 0 !important;
    }
}
</style>


