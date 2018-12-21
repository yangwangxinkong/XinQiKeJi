<template>
  <div class="page">
    <!-- 社保 -->
    <div class="container bg_f">
      <group title="计算信息" :gutter="0">
        <!-- 弹出层 -->
        <popup-picker
          title="参保城市"
          class="f14"
          :data="cityList"
          :columns="2"
          v-model="city"
          show-name
          @on-hide="onHide"
          @on-change="onChange"
          placeholder="请选择参保城市"
        >
          <template slot="title" slot-scope="props">
            <span class="f14">参保城市
              <!-- <icon type="info" class="f14"></icon> -->
            </span>
          </template>
        </popup-picker>
        <!-- 弹出层 -->
        <popup-radio
          title="户口性质"
          class="f14"
          :options="personalType"
          v-model="currentValue"
          @on-hide="hideFun(currentValue)"
          v-if="type === 'SB' || type === 'SG'"
        ></popup-radio>

        <!-- <popup-radio title="参保基数(3390-25401)" :options="insuredNums" v-model="insuredNum"></popup-radio> -->
        <x-input
          :title="titleSocialBase"
          class="f14"
          text-align="right"
          v-model="postForm.socialBase"
          v-if="type === 'SB' || type === 'SG'"
        ></x-input>

        <!-- <x-input title="公积金比例" class="f14" text-align="right" v-model="postForm.socialBase"></x-input> -->
        <x-input
          :title="titleProvidentBase"
          class="f14"
          text-align="right"
          v-model="postForm.providentBase"
          v-if="type === 'GJ' || type === 'SG'"
        ></x-input>
      </group>
      <box gap="30px 10px">
        <x-button :gradients="['#C5282B', '#C5282B']" @click.native="nextPage"  style="border-radius:99px;">计算</x-button>
      </box>
      <!-- footer -->
      <!-- <section class="footer-actionBar" >
          <section class="footer-actionBar-container flex-ui ">
              <div class="flex1 tc  white" @click="nextPage"><span class="f16 pl0">计算</span></div>
          </section>
      </section>-->
    </div>
  </div>
</template>

<script>
import {
  Cell,
  CellBox,
  Group,
  Badge,
  Icon,
  PopupRadio,
  PopupPicker,
  XInput,
  XButton,
  Box
} from "vux";
import { get, execute } from "@/api/server";
import url from "@/api/apiUrl";
import storage from "@/utils/common";
import { validIsBlank } from "@/utils/validate";

const defaultForm = {
  feeCategoryId: 0, //缴费方式
  city: { id: undefined }, //城市
  residenceType: { id: undefined }, //户口性质
  socialBase: undefined,
  providentBase: undefined
};
export default {
  components: {
    Cell,
    CellBox,
    Group,
    Badge,
    Icon,
    PopupRadio,
    PopupPicker,
    XInput,
    XButton,
    Box
  },
  props: {},
  data() {
    return {
      type: this.$route.query && this.$route.query.type,
      currentValue: 1,
      currentValueTemp: undefined,
      personalType: [],
      city: [],
      cityList: [],
      socialBaseMin: "0",
      socialBaseMax: "0",
      providentBaseMin: "0",
      providentBaseMax: "0",
      titleSocialBase: "参保基数",
      titleProvidentBase: "公积金基数",
      postForm: Object.assign({}, defaultForm)
    };
  },
  watch: {},
  computed: {},
  methods: {
    initData() {
      // 获取城市列表
      get(url.cityList, {})
        .then(response => {
          if (response.data.result == "00000000") {
            this.cityList = response.data.data;
            if (this.cityList != null && this.cityList.length > 0) {
              //this.city.push(this.cityList[0].value);
              //this.city.push(this.cityList[1].parent+'');
              //this.city.push(this.cityList[1].value+'');
              this.city.push("1");
              this.city.push("2");
              // if(this.type != null && this.type != undefined && this.type != "GJ") {
              //     this.onChange(this.cityList[0].value);
              // }

              this.getBasePayInfo("2");
            }
          }
        })
        .catch(e => {
          console.log(e);
        });
    },

    hideFun(val) {
      //console.log(val)
    },
    onHide() {},
    onChange(val) {
      //console.log(val);
      if (val == null || val == undefined) {
        return;
      }
      if (this.type != null && this.type != undefined && this.type === "GJ") {
        return;
      }
      this.getResidenceType(val[1]);
      this.getBasePayInfo(val[1]);
    },

    getBasePayInfo(cityIdVal) {
      get(url.calculateQuotationBaseInfo, { cityId: cityIdVal })
        .then(response => {
          if (response.data.result == "00000000") {
            // 参保基数
            this.payBase = response.data.data.payBase;
            this.postForm.socialBase = this.payBase.socialBaseMin;

            this.socialBaseMin = this.payBase.socialBaseMin;
            this.socialBaseMax = this.payBase.socialBaseMax;

            this.titleSocialBase =
              "参保基数(" +
              this.payBase.socialBaseMin +
              "-" +
              this.payBase.socialBaseMax +
              ")";

            // 公积金参保基数
            this.postForm.providentBase = this.payBase.providentBaseMin;

            this.providentBaseMin = this.payBase.providentBaseMin;
            this.providentBaseMax = this.payBase.providentBaseMax;

            this.titleProvidentBase =
              "公积金基数(" +
              this.payBase.providentBaseMin +
              "-" +
              this.payBase.providentBaseMax +
              ")";
          }
        })
        .catch(e => {
          console.log(e);
        });
    },

    getResidenceType(cityIdVal) {
      get(url.residenceTypeListByCityId, { id: cityIdVal })
        .then(response => {
          let list = response.data.list;
          this.personalType = [];
          this.currentValue = undefined;
          for (var i in list) {
            var temp = {};
            temp.key = list[i].id;
            temp.value = list[i].name;
            this.personalType.push(temp);
          }

          if (list != null && list.length > 0) {
            this.currentValue = list[0].id;
          }
        })
        .catch(e => {
          console.log(e);
        });
    },
    nextPage() {
      this.postForm.city.id = this.city[this.city.length - 1];

      if (this.type === "SB") {
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

      if (this.type === "GJ") {
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

      if (this.type === "GJ") {
        this.postForm.feeCategoryId = 1;

        this.$router.push({
          path: "/calculator/calculatorDetail",
          query: {
            cityId: this.postForm.city.id,
            feeCategoryId: this.postForm.feeCategoryId,
            providentBase: this.postForm.providentBase
          }
        });
      } else {
        this.postForm.feeCategoryId = 0;
        this.postForm.residenceType.id = this.currentValue;

        this.$router.push({
          path: "/calculator/calculatorDetail",
          query: {
            cityId: this.postForm.city.id,
            feeCategoryId: this.postForm.feeCategoryId,
            residenceTypeId: this.postForm.residenceType.id,
            socialBase: this.postForm.socialBase
          }
        });
      }
      //console.log(JSON.stringify(this.postForm));
    }
  },
  created() {
    this.initData();
  },
  mounted() {}
};
</script>

<style lang="less">
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
</style>
