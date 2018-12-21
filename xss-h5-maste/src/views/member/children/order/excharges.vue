<!--我的订单  -->
<template>
  <div class='page'>
      <div class="container">
        <!-- tab -->
        <div class="tab-box bg_f">
          <tab custom-bar-width="30px">
              <tab-item active-class="active-tab" v-for="item in list" @on-item-click="onItemClick" :key="item.value" :selected="item.selected">{{item.value}}</tab-item>
          </tab>
        </div>
        <!-- <div v-if="orderList && orderList.length"> -->
          <!-- list -->
          <div class="order-info js-tabber-detail"
             v-infinite-scroll="loadMore" infinite-scroll-disabled="onFetching" infinite-scroll-distance="10" infinite-scroll-throttle-delay="300">
              <group v-for="(item,index) in orderList" :key="item.id" v-if="orderList && orderList.length" :gutter="10" >
                <cell class="value_color" :title="item.typeName" :value="item.status"></cell>
                <cell-box :link="'/member/order/exchargeDetail?id='+ item.id">
                  <flexbox class="f14">
                    <flexbox-item :span="3" class="tr">
                      <p class="mb5" style="height:35px;" v-if="item.serviceName.length > 26">服务项目：</p>
                      <p class="mb5" v-else>服务项目：</p>
                      <p class="mb5">姓名：</p>
                      <p>订单金额：</p>
                    </flexbox-item>
                    <flexbox-item>
                      <p class="mb5" style="height:35px;" v-if="item.serviceName.length > 26">{{item.serviceName}}</p>
                      <p class="mb5" v-else>{{item.serviceName}}</p>
                      <p class="mb5">{{item.userName}}</p>
                      <p>{{item.amount | money}}元</p>
                    </flexbox-item>
                  </flexbox>
                </cell-box>
                <cell-box>
                  <div style="width:100%">
                    <span class="f14 color_content1" v-if="item.expiredTime">还有{{item.expiredTime}}失效</span>
                    <div class="fr" v-if="!item.isExpired">
                      <template v-if="item.orderStatus === 'unconfirmed'">
                        <x-button class="mt0 mr0 ml10 btn_round" mini plain @click.native="cancelOrder(item.id)">取消订单</x-button>
                        <x-button class="mt0 mr0 ml10 btn_round base_btn_border" mini plain type="primary" @click.native="toPay(item.id)">去支付</x-button>
                      </template>
                      <template v-if="(item.orderStatus === 'confirmed' || item.orderStatus === 'completed') && item.paymentStatus === 'paid' && item.cancelPaid">
                        <x-button class="mt0 mr0 ml10 btn_round" mini plain @click.native="cancelPaid(item.sn)">申请停缴</x-button>
                      </template>
                      <template v-if="item.orderStatus === 'cancelled'">
                        <x-button class="mt0 mr0 ml10 btn_round base_btn_border" mini plain type="primary" @click.native="payAgain(item,index)">重新购买</x-button>
                      </template>
                    </div>
                  </div>
                </cell-box>
              </group>

          </div>
          <!-- 没有数据的默认展示 -->
          <load-more :tip="tipText" v-if="!onFetching"></load-more>
          <load-more :show-loading="false" tip="没有更多数据" background-color="#fbf9fe" v-if="onFetching"></load-more>
          <!-- <divider :show-loading="false" tip="没有更多数据" background-color="#fbf9fe" v-if="onFetching"></divider> -->
        <!-- </div> -->
        <div v-if="dataNull">
          <img class="no_data" src="~@/assets/no_data.png" alt="">
          <p class="f18 tc">您还没有相关订单</p>
          <p class="f12 tc">去下一单试试吧~</p>
          <div class="mt30 tc">
            <x-button class="fp_btn f16 mb10 base_btn_border" plain mini type="primary" @click.native="toExcharge">我要兑换</x-button>
          </div>
        </div>
      </div>
  </div>
</template>

<script type="text/ecmascript-6">
import { Tab,TabItem,XButton,Group,Cell,CellBox,Flexbox, FlexboxItem, LoadMore, Divider } from 'vux'
import { get, post } from '@/api/server'
import url from '@/api/apiUrl'
import money from "@/utils/utils"

export default {
  components:{ Tab,TabItem,XButton,Group,Cell,CellBox,Flexbox, FlexboxItem, LoadMore, Divider },
  data () {
    return {
      onFetching: false,//为true的时候禁止无线滚动
      tipText: "加载中",
      pageNumber: 1,
      pageSize: 10,
      currentType: 'all',
      list:[{
        value:'全部',
        selected:true
      }, {
        value: '未支付',
        selected: false
      },{
        value: '已支付',
        selected: false
      },{
        value: '已取消',
        selected: false
      }],
      orderList:[],
      dataNull: false,
    };
  },

  watch: {},
  computed: {},

  mounted(){},

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
        type: this.currentType,
        orderType:'service'
      };
      get(url.orderList, params).then(res => {
        //if(res.data.result==='00000000'){
          let curLists = res.data.list;
          let totalPages = Math.ceil(res.data.total / this.pageSize);
          if (totalPages <= this.pageNumber) {
            this.onFetching = true;
          }
          if(curLists && curLists.length > 0) {
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

    onItemClick(val){
      //console.log(val);
      if(val === 0) {
        this.currentType = "all";
      } else if(val === 1) {
        this.currentType = "topay";
      } else if(val === 2) {
        this.currentType = "paid";
      } else if(val === 3) {
        this.currentType = "cancelled";
      }
      this.orderList = [];
      this.pageNumber = 1;
      this.loadMore();
    },
    cancelOrder(orderid){
      //console.log(item, index)

      let _this = this;

      this.$vux.confirm.show({
        title: "提示",
        content: "你确定要取消订单",
        // onShow() {
        //   console.log("plugin show");
        // },
        // onHide() {
        //   console.log("plugin hide");
        // },
        onCancel() {
          //console.log("plugin cancel");
        },
        onConfirm() {
          //console.log("plugin confirm");
          post(url.orderCancel, {id: orderid}).then(res => {
            if (res.data.result==='00000000') {
              _this.pageNumber = 1
              _this.orderList = []
              _this.loadMore()
            }else if(res.data.result == '10000007'){
              _this.$router.push({path:"/login", query:{redirect: _this.$route.fullPath}})
            }else{
              _this.$vux.toast.show({
                type:'warn',
                text: res.data.msg,
                width:'120px',
                time:1000
              })
            }
          })
        }
      });
    },
    toPay(id) {
      //console.log(item, index)
      this.$router.push({path:"/order/orderPay", query:{id: id}});
    },
    cancelPaid(sn) {
      this.$router.push({path:"/member/order/cancelOrder", query:{sn: sn}});
    },
    payAgain(item, index) {
      //console.log(item, index)
      if(item.feeCategory === 'fc2') {
        this.nextPageSocialProvident();
      } else if(item.feeCategory === 'fc1') {
        this.nextPageProvident();
      } else {
        this.nextPageSocial();
      }
    },
    toExcharge() {
      this.$router.push({path:"/product/list"});
    },
    nextPageProvident() {
      this.$router.push({path:"/order/orderMember?type=GJ"});
    },
    nextPageSocialProvident() {
      this.$router.push({path:"/order/orderMember?type=SG"});
    }
  }
}

</script>
<style lang='less' scoped>
.no_data{
  margin: 60px auto 30px;
}
.fp_btn{
  display: block;
  padding: 2px 0;
  width: 180px;
}
.active-tab{
  color: @color_base !important;
}
.base_btn_border{
  border-color: @color_base;
  color: @color_base;
  border-radius: 90px;
}
.btn_round{
  border-radius: 20px;
}
</style>
<style lang='less'>
.value_color{
  .weui-cell__ft{
    font-size:12px;
    color: @color_title
  }
}
</style>
