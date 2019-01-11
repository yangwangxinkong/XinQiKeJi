<template>
  <div class="createPost-container tab-container">

    <sticky className="sub-navbar" v-if="order">
      <!-- <el-button @click="confirm();" v-bind:disabled="order.expired || order.orderStatusId != 0">{{$t('order.confirm')}}</el-button>
      <el-button @click="pay();" v-bind:disabled="order.expired || order.orderStatusId != 1 || (order.paymentStatusId != 0 && order.paymentStatusId != 1)">{{$t('order.pay')}}</el-button>
      <el-button @click="shipping();" v-bind:disabled="order.expired || (order.orderStatusId != 1 && order.orderStatusId != 5) || (order.shippingStatusId != 0 && order.shippingStatusId != 1) || order.paymentStatusId != 2">{{$t('order.shippings')}}</el-button> -->
      <!-- <el-button @click="completed();" style="margin-right: 60px;" v-bind:disabled="order.expired || (order.orderStatusId != 1 && order.orderStatusId != 5) || (order.paymentStatusId != 2 && order.paymentStatusId != 6) || (order.shippingStatusId != 3 && order.shippingStatusId != 8 && order.shippingStatusId != 11)">{{$t('order.fishish')}}</el-button>
      <el-button @click="cancelApply(order.sn);" v-if="order.orderStatusId == 2">{{$t('order.cancelling')}}</el-button>
      <el-button @click="refundsApply(order.sn);" v-bind:disabled="order.expired || order.paymentStatusId != 5">{{$t('order.refunds')}}</el-button>
      <el-button @click="returnsApply(order.sn);" v-bind:disabled="order.expired || order.shippingStatusId != 4">{{$t('order.returns')}}</el-button>
      <el-button @click="changesApply(order.sn);" v-bind:disabled="order.expired || order.shippingStatusId != 5">{{$t('order.changes')}}</el-button>
      <el-button @click="cancel();" v-bind:disabled="order.expired || order.orderStatusId != 0">{{$t('order.cancel')}}</el-button>
      <el-button @click="shippingTimeOut();" v-bind:disabled="!order.shippingTimeout">{{$t('order.shippingTimeOutRefunds')}}</el-button> -->
      
      <el-button @click="confirm();" v-bind:disabled="order.expired || order.orderStatusId != 0">{{$t('order.confirm')}}</el-button>
      <el-button @click="completed();" style="margin-right: 60px;" v-bind:disabled="order.expired || (order.orderStatusId != 1 && order.orderStatusId != 5) || (order.paymentStatusId != 2 && order.paymentStatusId != 6)">{{$t('order.fishish')}}</el-button>
      <el-button @click="refundsApprove(order.sn);" v-bind:disabled="order.expired || order.paymentStatusId != 5">{{$t('order.refundsApprove')}}</el-button>
      <el-button @click="refundsReject(order.sn);" v-bind:disabled="order.expired || order.paymentStatusId != 5">{{$t('order.refundsReject')}}</el-button>
      <el-button @click="cancel();" v-bind:disabled="order.expired || order.orderStatusId != 0">{{$t('order.cancel')}}</el-button>
      
      <el-button @click="closePage();" style="margin-left: 60px;">返回</el-button>
    </sticky>
    <el-tabs style='margin-top:15px;' v-model="activeName" type="border-card">
      <!--//订单基本信息-->
      <el-tab-pane :label="tabMapOptions[0].label" :key='tabMapOptions[0].key' :name="tabMapOptions[0].key" v-if="order">
        <keep-alive>
            <el-form label-position="right" inline class="order-table-expand" label-width="120px"  v-if='activeName===tabMapOptions[0].key'>
              <el-form-item :label="$t('order.sn')+'：'">
                <span>{{order.sn}}</span>
              </el-form-item>              
              <el-form-item :label="$t('order.createDate')+'：'">
                <span>{{order.createDate}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.mainTitle')+'：'">
                <span>{{order.main_title}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.subTitle')+'：'">
                <span>{{order.sub_title}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.startDate')+'：'">
                <span>{{order.startDate}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.endDate')+'：'">
                <span>{{order.endDate}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.oldPrice')+'：'">
                <span>￥{{order.old_price}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.proPrice')+'：'">
                <span>￥{{order.pro_price}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.orderStatus')+'：'">
                <span>{{order.orderStatus}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.paymentStatus')+'：'">
                <span>{{order.paymentStatus}}</span>
              </el-form-item>
              <!-- <el-form-item :label="$t('order.shippingStatus')+'：'">
                <span>{{order.shippingStatus}}</span>
              </el-form-item> -->
              
              <!-- <el-form-item :label="$t('order.amount')+'：'">
                <span>￥{{order.amount}}</span>
              </el-form-item> -->
              
              <el-form-item :label="$t('order.downPayment')+'：'">
                <span>￥{{order.down_payment}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.amountPaid')+'：'">
                <span>￥{{order.amountPaid}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.resource')+'：'">
                <span>{{order.resource}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.userName')+'：'">
                <span>{{order.userName}}</span>
              </el-form-item>
              <!-- <el-form-item :label="$t('order.weight')+'：'">
                <span>{{order.weight}}</span>
              </el-form-item>
               <el-form-item :label="$t('order.quantity')+'：'">
                <span>{{order.quantity}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.promotion')+'：'">
                 <span v-if="order.promotion">{{order.promotion}}</span>
                <span v-else>无</span>
              </el-form-item>
              <el-form-item :label="$t('order.coupon')+'：'">
                <span v-if="order.coupon">{{order.coupon}}</span>
                <span v-else >无</span>
              </el-form-item>
               <el-form-item :label="$t('order.promotionDiscount')+'：'">
                <span>{{order.promotionDiscount}}</span>
              </el-form-item>
               <el-form-item :label="$t('order.couponDiscount')+'：'">
                <span>{{order.couponDiscount}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.offsetAmount')+'：'">
                <span>{{order.offsetAmount}}</span>
              </el-form-item>
               <el-form-item :label="$t('order.point')+'：'">
                <span>{{order.point}}</span>
              </el-form-item>
               <el-form-item :label="$t('order.freight')+'：'">
                <span>{{order.freight}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.fee')+'：'">
                <span>{{order.fee}}</span>
              </el-form-item> -->
              <!-- <el-form-item :label="$t('order.paymentMethodName')+'：'">
                <span>{{order.paymentMethodName}}</span>
              </el-form-item>
               <el-form-item :label="$t('order.shippingMethodName')+'：'">
                <span>{{order.shippingMethodName}}</span>
              </el-form-item> -->
              <!-- <el-row  v-if="order.isInvoice">
                <el-col>
                    <el-form-item :label="$t('order.invoiceTitle')+'：'">
                      <span>{{order.invoiceTitle}}</span>
                    </el-form-item>
                    <el-form-item :label="$t('order.invoiceNo')+'：'">
                      <span>{{order.invoiceNo}}</span>
                    </el-form-item>
                </el-col>
              </el-row>
              <el-form-item :label="$t('order.invoiceAmount')+'：'">
                <span>{{order.tax}}</span>
              </el-form-item> -->
              <!-- <el-form-item :label="$t('order.consignee')+'：'">
                <span>{{order.consignee}}</span>
              </el-form-item> -->
              <!-- <el-form-item :label="$t('order.area')+'：'">
                <span>{{order.areaName}}</span>
              </el-form-item> -->
                <el-form-item :label="$t('order.address')+'：'">
                <span>{{order.address}}</span>
              </el-form-item>
               <el-form-item :label="$t('order.zipCode')+'：'">
                <span>{{order.zipCode}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.phone')+'：'">
                <span>{{order.phone}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.memo')+'：'">
                <span v-if="order.memo">{{order.memo}}</span>
                <span v-else>无</span>
              </el-form-item>
          </el-form>
        </keep-alive>
      </el-tab-pane>

     <!-- //订单商品信息-->
      <el-tab-pane :label="tabMapOptions[1].label" :key='tabMapOptions[1].key' :name="tabMapOptions[1].key">
        <keep-alive>

          <el-table :data="orderItemList" v-if='activeName==tabMapOptions[1].key' :type='tabMapOptions[1].key'  border fit highlight-current-row style="width: 100%">

            <el-table-column min-width="100px" align="center" :label="$t('order.orderitem.sn')">
              <template slot-scope="scope">
                <span>{{scope.row.productSn}}</span>
              </template>
            </el-table-column>

            <el-table-column min-width="180px" align="center" :label="$t('order.orderitem.fullName')">
              <template slot-scope="scope">
                <span>{{scope.row.fullName}}</span>
              </template>
            </el-table-column>

            <!-- <el-table-column min-width="180px" align="center" :label="$t('order.orderitem.store')">
              <template slot-scope="scope">
                <span>{{scope.row.storeName}}</span>
              </template>
            </el-table-column> -->

            <el-table-column min-width="80px" align="center" :label="$t('order.orderitem.price')">
              <template slot-scope="scope">
                <span>￥{{scope.row.price}}</span>
              </template>
            </el-table-column>
          </el-table>
        </keep-alive>
      </el-tab-pane>

      <!--//订单日志信息-->
      <el-tab-pane :label="tabMapOptions[2].label" :key='tabMapOptions[2].key' :name="tabMapOptions[2].key">
        <keep-alive>
          <el-table :data="orderLogList" v-if='activeName==tabMapOptions[2].key' :type='tabMapOptions[2].key'  border fit highlight-current-row style="width: 100%">

            <el-table-column min-width="100px" align="center" :label="$t('order.log.type')">
              <template slot-scope="scope">
                <span>{{$t('order.logtype.'+scope.row.type)}}</span>
              </template>
            </el-table-column>

            <el-table-column min-width="180px" align="center" :label="$t('order.log.operator')">
              <template slot-scope="scope">
                <span>{{scope.row.operator}}</span>
              </template>
            </el-table-column>

            <el-table-column min-width="180px" align="center" :label="$t('order.log.content')">
              <template slot-scope="scope">
                <span>{{scope.row.content}}</span>
              </template>
            </el-table-column>

            <el-table-column min-width="120px" align="center" :label="$t('order.log.createDate')">
              <template slot-scope="scope">
                <span>{{scope.row.createDate}}</span>
              </template>
            </el-table-column>
          </el-table>
        </keep-alive>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>
//import orderInfo from './components/orderInfo'
import { fetchView, fetchConfirm, fetchCancel, fetchCompleted, fetchApproval, fetchReject } from "@/api/activityOrder";
import Sticky from '@/components/Sticky' // 粘性header组件
import { goback } from '@/utils/common'
import $ from 'jquery';
import { getToken } from '@/utils/auth'

export default {
  name: "orderTab",
  //components: { orderInfo },
  components: { Sticky},
  data() {
    return {
      tabMapOptions: [
        { label: "活动订单信息", key: "Order" },
        { label: "活动商品信息", key: "Product" },
        { label: "订单日志", key: "Log" }
      ],
      activeName: "Order",
      createdTimes: 0,
      order: null,
      orderItemList: [],
      orderLogList: null,
      methods: [],
      paymentMethods: [],
      deliveryCorps: [],
      listQuery: {
        type: this.type
      },     
      selectedAreaOptions: [],
      loading: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getMethods(){
      fetchMethods().then(response => {
          this.methods = response.data.data.methods;
          this.paymentMethods = response.data.data.paymentMethods;
      })
    },
    getList() {
     // this.$emit("create"); // for test
      const id = this.$route.params && this.$route.params.id;
  //console.log("id=" + id);
      fetchView(id).then(response => {
        this.order = response.data.data.orderInfo;
        this.orderItemList = response.data.data.orderItemInfo.data.activity.promotionProducts;
        this.orderLogList = response.data.data.orderLogInfo;
        this.loading = false;

      });
    },
  closePage() {//返回商品列表
    goback(this.$route.path);
    // this.$router.push({
    //   path: "/member/memberRank_list"
    // });
  },
  selectMethodChange(val){
    this.payDataTemp.methodId = val
  },
  selectPaymentMethodChange(val){
    this.payDataTemp.paymentMethodId = val
  },
  selectShippingMethodChange(val){
    this.shippingDataTemp.shippingMethodId = val
  },
  selectDeliveryCorpChange(val){
    this.shippingDataTemp.deliveryCorpId = val
  },
  areaSuccessCBK(selectAreas) {
    this.selectedAreaOptions = selectAreas;
  },
  handleChange(val) {
    console.log(val);
    //this.postForm.returnQuantity = val;
  },
     //确认
    confirm() {

      this.$confirm('活动订单确认后将无法编辑，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        var id = this.$route.params && this.$route.params.id;
      fetchConfirm({id:id}).then(response => {
        if(response.data.result==='00000000'){
        this.$notify({
          title: '成功',
          message: '活动订单确认完成',
          type: 'success',
          duration: 2000
        })
      } else {
        this.$notify({
          title: '订单确认',
          message: response.data.msg,
          type: 'error',
          duration: 2000
        })
      }
      this.getList();
    })
    }).catch(err => {
      console.log(err)
    })
    },

    //取消
    cancel() {

      this.$confirm('订单取消后将无法操作，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
          var id = this.$route.params && this.$route.params.id;
          fetchCancel({id:id}).then(response => {
            if(response.data.result==='00000000'){
            this.$notify({
              title: '成功',
              message: '订单取消完成',
              type: 'success',
              duration: 2000
            })
          } else {
            this.$notify({
              title: '订单取消',
              message: response.data.msg,
              type: 'error',
              duration: 2000
            })
          }
          this.getList();
        })
      }).catch(err => {
        console.log(err)
      })
    },

    // 订单完成
    completed() {
      this.$confirm('活动订单完成后将无法操作，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        var id = this.$route.params && this.$route.params.id;
      fetchCompleted({id:id}).then(response => {
        if(response.data.result==='00000000'){
        this.$notify({
          title: '成功',
          message: '活动订单完成',
          type: 'success',
          duration: 2000
        })
      } else {
        this.$notify({
          title: '活动订单完成',
          message: response.data.msg,
          type: 'error',
          duration: 2000
        })
      }
      this.getList();
    })
    }).catch(err => {
      console.log(err)
    })
    },

    // 退款通过
    refundsApprove(orderSn) {
      //this.$router.push({path: '/order/refunds_apply/view', query:{sn:orderSn, isEdit:true}});
      this.$confirm('活动订单退款通过后将无法恢复，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //var id = this.$route.params && this.$route.params.id;
      fetchApproval({sn:orderSn}).then(response => {
        if(response.data.result==='00000000'){
        this.$notify({
          title: '成功',
          message: '活动订单退款通过',
          type: 'success',
          duration: 2000
        })
      } else {
        this.$notify({
          title: '活动订单退款通过',
          message: response.data.msg,
          type: 'error',
          duration: 2000
        })
      }
      this.getList();
    })
    }).catch(err => {
      console.log(err)
    })
    },

    // 退款拒绝
    refundsReject(orderSn) {
      //this.$router.push({path: '/order/refunds_apply/view', query:{sn:orderSn, isEdit:true}});
      this.$confirm('活动订单退款拒绝后将无法恢复，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //var id = this.$route.params && this.$route.params.id;
      fetchReject({sn:orderSn}).then(response => {
        if(response.data.result==='00000000'){
        this.$notify({
          title: '成功',
          message: '活动订单退款拒绝',
          type: 'success',
          duration: 2000
        })
      } else {
        this.$notify({
          title: '活动订单退款拒绝',
          message: response.data.msg,
          type: 'error',
          duration: 2000
        })
      }
      this.getList();
    })
    }).catch(err => {
      console.log(err)
    })
    },

    // 订单取消
    cancelApply(orderSn) {
      this.$router.push({path: '/order/order_cancel_apply/view', query:{sn:orderSn, isEdit:true}});
    }

  }
};
</script>


<style rel="stylesheet/scss" lang="scss" >
.tab-container {
  margin: 30px;
}
.order-table-expand {
  font-size: 0;
  .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
     color:#393939;
    label{
      width: 90px;
      color:#393939;
      font-weight: normal
    }
  }
}
</style>
