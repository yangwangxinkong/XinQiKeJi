<!--我的优惠券  -->
<template>
  <div class="page">
    <div class="container">
      <!-- <div v-if="couponList && couponList.length"> -->
      <!-- list -->
      <tab custom-bar-width="30px">
        <tab-item selected @on-item-click="onItemClick">已使用</tab-item>
        <tab-item @on-item-click="onItemClick">未使用</tab-item>
      </tab>
      <div
        class="order-info js-tabber-detail mt10"
        v-infinite-scroll="loadMore"
        infinite-scroll-disabled="onFetching"
        infinite-scroll-distance="10"
        infinite-scroll-throttle-delay="300"
      >
        <!-- 已使用 -->
        <div class="coupons-0" v-if="currentIndex===0">
          <group
            :gutter="0"
            v-for="(item,index) in couponList"
            :key="index"
            v-if="couponList && couponList.length"
          >
            <cell class="value_color" :title="item.name" :value="item.isUsed"></cell>
            <cell-box>
              <flexbox class="f14">
                <flexbox-item :span="3" class="tr">
                  <p class="mb5">有效期：</p>
                  <p class="mb5">使用日期：</p>
                  <p>优惠折扣：</p>
                </flexbox-item>
                <!-- <flexbox-item>
                  <p class="mb5">{{item.beginDate}}至{{item.endDate}}</p>
                  <p class="mb5">{{item.usedDate}}</p>
                  <p>{{item.discount}}折</p>
                </flexbox-item>-->
              </flexbox>
            </cell-box>
          </group>
        </div>
        <!-- 未使用 -->
        <div class="coupons-1" v-if="currentIndex===1">
          <group
            :gutter="0"
            v-for="(item,index) in couponList"
            :key="index"
            v-if="couponList && couponList.length"
          >
            <cell class="value_color" :title="item.name" :value="item.isUsed"></cell>
            <cell-box>
              <flexbox class="f14">
                <flexbox-item :span="3" class="tr">
                  <p class="mb5">有效期：</p>
                  <p class="mb5">使用日期：</p>
                  <p>优惠折扣：</p>
                </flexbox-item>
                <flexbox-item>
                  <p class="mb5">{{item.beginDate}}至{{item.endDate}}</p>
                  <p class="mb5">{{item.usedDate}}</p>
                  <p>{{item.discount}}折</p>
                </flexbox-item>
              </flexbox>
            </cell-box>
          </group>
        </div>
      </div>
      <!-- 没有数据的默认展示 -->
      <load-more :tip="tipText" v-if="!onFetching"></load-more>
      <load-more :show-loading="false" tip="没有更多数据" background-color="#fbf9fe" v-if="onFetching"></load-more>
      <!-- <divider :show-loading="false" tip="没有更多数据" background-color="#fbf9fe" v-if="onFetching"></divider> -->
      <!-- </div> -->
      <div v-if="dataNull">
        <img class="no_data" src="~@/assets/no_data.png" alt>
        <p class="f18 tc">您还没有优惠券</p>
        <p class="f12 tc">去兑换试试吧~</p>
        <div class="mt30 tc">
          <x-button
            class="fp_btn f16 mb10 base_btn_border"
            plain
            mini
            type="primary"
            @click.native="toExchange"
          >我要兑换</x-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import {
  Tab,
  TabItem,
  XButton,
  Group,
  Cell,
  CellBox,
  Flexbox,
  FlexboxItem,
  LoadMore,
  Divider
} from "vux";
import { get, post } from "@/api/server";
import url from "@/api/apiUrl";
import money from "@/utils/utils";

export default {
  components: {
    Tab,
    TabItem,
    XButton,
    Group,
    Cell,
    CellBox,
    Flexbox,
    FlexboxItem,
    LoadMore,
    Divider
  },
  data() {
    return {
      onFetching: false, //为true的时候禁止无线滚动
      tipText: "加载中",
      pageNumber: 1,
      pageSize: 10,
      couponList: [],
      dataNull: false,
      currentIndex: 0
    };
  },

  watch: {},
  computed: {},

  mounted() {},

  methods: {
    //滚动加载数据
    loadMore() {
      this.getCouponList();
    },
    //获取订单列表
    getCouponList() {
      this.dataNull = false;
      let params = {
        pageNumber: this.pageNumber,
        pageSize: this.pageSize
      };
      get(url.memberCoupons, params).then(res => {
        //if(res.data.result==='00000000'){
        let curLists = res.data.list;
        let totalPages = res.data.total;
        if (totalPages <= this.pageNumber) {
          this.onFetching = true;
        }
        if (curLists && curLists.length > 0) {
          if (this.couponList) {
            this.couponList = this.couponList.concat(curLists);
          } else {
            this.couponList = curLists;
          }
        } else {
          this.dataNull = true;
        }
        this.pageNumber++;
      });
    },
    toExchange() {
      this.$router.push({ path: "/exchange/list" });
    },
    onItemClick(index) {
      this.currentIndex = index;
    }
  }
};
</script>
<style lang='less' scoped>
.no_data {
  margin: 60px auto 30px;
  width: 3.28rem;
  height: 3.013rem;
}
.fp_btn {
  display: block;
  padding: 2px 0;
  width: 180px;
}
.active-tab {
  color: @color_base !important;
}
.base_btn_border {
  border-color: @color_base;
  color: @color_base;
  border-radius: 90px;
}
.btn_round {
  border-radius: 20px;
}
.coupons-0 {
  .weui-cell__ft {
    color: #ff860d;
  }
  .vux-cell-box {
    background: url(../../../../assets/coupons-seal.png) no-repeat right;
    background-size: 2.32rem 2.32rem;
    padding: 25px 15px;
  }
}
</style>
<style lang='less'>
.value_color {
  .weui-cell__ft {
    color: @color_title;
  }
}
</style>
