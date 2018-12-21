<template>
  <div class="page">
    <!-- 明细 -->
    <div class="container calculator-container bg_f" v-if="quotation">
      <group :gutter="10">
        <!--title="计算结果" :gutter="1" -->
        <cell-form-preview :list="infoList"></cell-form-preview>
      </group>
      <group :gutter="10">
        <p class="f14 tc mt10">总金额(元)</p>
        <p class="f20 tc amount_color">{{quotation.amount | money}}</p>
        <flexbox class="flexbox-list pb10">
          <flexbox-item class="tc" v-for="item in amountList" :key="item.label">
            <p style="color: #2D2E46;">{{item.label}}</p>
            <p class="amount_color mt10">{{item.value | money}}元</p>
          </flexbox-item>
        </flexbox>
      </group>

      <x-table :cell-bordered="false" :content-bordered="false" class="bg_f mt10">
        <thead>
          <tr>
            <th style="color:#333333;font-weight: bold; width:25%;">项目</th>
            <th style="color:#333333;font-weight: bold; width:25%;">基数</th>
            <th style="color:#333333;font-weight: bold; width:25%;">个人</th>
            <th style="color:#333333;font-weight: bold; width:25%;">公司</th>
          </tr>
        </thead>
        <tbody>
          <tr
            :class="item.project === '总计' ? 'amount_color':''"
            v-for="item in itemlists"
            :key="item.id"
          >
            <td>{{item.project}}</td>
            <td>{{item.cardinalNum | money}}</td>
            <td>{{item.personalNum | money}}</td>
            <td>{{item.companyNum | money}}</td>
          </tr>
        </tbody>
      </x-table>

      <!-- footer -->
      <section class="footer-actionBar">
        <section class="footer-actionBar-container flex-ui">
          <div class="flex1 tc white" @click="nextPageProvident" v-if="feeCategoryId == 1">
            <span class="f16 pl0">去缴公积金</span>
          </div>
          <div class="flex1 tc white" @click="nextPageSocial" v-else>
            <span class="f16 pl0">去缴社保</span>
          </div>
        </section>
      </section>
    </div>
  </div>
</template>
<script>
import {
  CellFormPreview,
  Flexbox,
  FlexboxItem,
  XTable,
  Group,
  Datetime
} from "vux";
import { get } from "@/api/server";
import url from "@/api/apiUrl";
import storage from "@/utils/common";
import money from "@/utils/utils";

export default {
  components: {
    CellFormPreview,
    Flexbox,
    FlexboxItem,
    XTable,
    Group,
    Datetime
  },
  data() {
    return {
      id: this.$route.query && this.$route.query.id,
      amountList: [],
      itemlists: [],
      infoList: [],
      quotation: undefined,
      feeCategoryId: undefined
    };
  },
  methods: {
    initData() {
      // 获取信息
      if (!this.$route.query) {
        return;
      }

      let params = {
        cityId: this.$route.query.cityId,
        feeCategoryId: this.$route.query.feeCategoryId,
        residenceTypeId: this.$route.query.residenceTypeId,
        socialBase: this.$route.query.socialBase,
        providentBase: this.$route.query.providentBase
      };

      // 计算结果显示
      get(url.calculateQuotation, params)
        .then(response => {
          if (response.data.result == "00000000") {
            this.quotation = response.data.data;
            this.infoList = response.data.data.infoList;
            this.itemlists = response.data.data.quotationItem;
            this.amountList = response.data.data.amountList;
          }
        })
        .catch(e => {
          console.log(e);
        });
    },
    hideFun() {},
    change() {},
    log(str1, str2) {
      console.log(str1, str2);
    },
    title() {},
    nextPageSocial() {
      this.$router.push({ path: "/order/orderMember?type=SB" });
    },
    nextPageProvident() {
      this.$router.push({ path: "/order/orderMember?type=GJ" });
    }
  },
  created() {
    this.feeCategoryId = this.$route.query && this.$route.query.feeCategoryId;
    this.initData();
  }
};
</script>
<style lang="less">
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
.calculator-container {
  .vux-table:after {
    height: 0;
    border-top: 0;
  }
}
</style>


