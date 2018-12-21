<!--我的订单  -->
<template>
  <div class='page'>
    <div class="container">
      <!-- tab -->
      <!--<div class="tab-box bg_f">
        <tab bar-active-color="#E60014">
          <tab-item active-class="active-tab" v-for="item in list" @on-item-click="onItemClick" :key="item.value" :selected="item.selected">{{item.value}}</tab-item>
        </tab>
      </div>-->
      <div v-if="orderList && orderList.length">
        <!-- list -->
        <div class="order-info js-tabber-detail mt10" v-infinite-scroll="loadMore" infinite-scroll-disabled="onFetching" infinite-scroll-distance="10" infinite-scroll-throttle-delay="300">
          <group v-for="(item,index) in orderList" :key="item.id">
            <!-- <cell class="value_color" :title="item.typeName" :value="item.status"></cell> -->
            <cell-box>
              <flexbox class="f14">
                <flexbox-item :span="3" class="tr">
                  <p class="mb5">发票抬头：</p>
                  <p class="mb5">发票税号：</p>
                  <p>发票金额：</p>
                  <p>发票类型：</p>
                  <p>发票状态：</p>
                </flexbox-item>
                <flexbox-item>
                  <p class="mb5">{{item.invoiceTitle}}</p>
                  <p class="mb5" v-if="item.invoiceNo">{{item.invoiceNo}}</p>
                  <p class="mb5" v-else>-</p>
                  <p>{{item.amount | money}}元</p>
                  <p>{{item.invoiceTypeDesc}}</p>
                  <p>{{item.invoiceStatusDesc}}</p>
                </flexbox-item>
              </flexbox>
            </cell-box>
          </group>

        </div>
        <!-- 没有数据的默认展示 -->
        <load-more :tip="tipText" v-if="!onFetching"></load-more>
        <divider :show-loading="false" tip="没有更多数据" background-color="#fbf9fe" v-if="onFetching"></divider>
      </div>
      <div v-if="dataNull">
        <img class="no_data" src="~@/assets/no_data.png" alt="">
        <p class="f18 tc">{{messageTip}}</p>
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
      currentType: "all",
      messageTip: "您还没有可开票的订单",
      list: [
        {
          value: "全部",
          selected: true
        },
        {
          value: "未开票",
          selected: false
        },
        {
          value: "已开票",
          selected: false
        },
        {
          value: "已邮寄",
          selected: false
        }
      ],
      orderList: [],
      dataNull: false
    };
  },

  computed: {},

  mounted() {},
  created() {
    this.loadMore();
  },
  methods: {
    //滚动加载数据
    loadMore() {
      this.getOrderList();
    },
    //获取订单列表
    getOrderList() {
      this.dataNull = false;
      let params = {
        pageNumber: this.pageNumber,
        pageSize: this.pageSize,
        type: this.currentType
      };
      get(url.invoiceList, params).then(res => {
        //if(res.data.result==='00000000'){
        let curLists = res.data.list;
        let totalPages = Math.ceil(res.data.total / this.pageSize);
        if (totalPages <= this.pageNumber) {
          this.onFetching = true;
        }
        if (curLists && curLists.length > 0) {
          if (this.orderList) {
            this.orderList = this.orderList.concat(curLists);
          } else {
            this.orderList = curLists;
          }
        } else {
          this.dataNull = true;
        }

        this.pageNumber++;
        //}else if(res.data.result==='10000007'){
        //  this.$router.push({path:"/login/index", query:{redirect: this.$route.fullPath}})
        //}
      });
    },

    onItemClick(val) {
      //console.log(val);
      if (val === 0) {
        this.currentType = "all";
        this.messageTip = "您还没有订单";
      } else if (val === 1) {
        this.currentType = "toticket";
        this.messageTip = "您还没有可开票的订单";
      } else if (val === 2) {
        this.currentType = "invoiced";
        this.messageTip = "您还没有已开票的订单";
      } else if (val === 3) {
        this.currentType = "mailed";
        this.messageTip = "您还没有已邮寄的订单";
      }
      this.orderList = [];
      this.pageNumber = 1;
      this.loadMore();
    }
  }
};
</script>
<style lang='less' scoped>
.no_data {
  margin: 60px auto 30px;
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
}
.btn_round {
  border-radius: 20px;
}
.value_color {
  .weui-cell__ft {
    color: @color_title;
    font-size:12px;
  }
}
</style>

