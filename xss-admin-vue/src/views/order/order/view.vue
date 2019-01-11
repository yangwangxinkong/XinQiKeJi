<template>
  <div class="createPost-container tab-container">

    <sticky className="sub-navbar" v-if="order">
      <el-button @click="confirm();" v-bind:disabled="order.isExpired || order.orderStatusId != 0">{{$t('order.confirm')}}</el-button>
      <el-button @click="pay();" v-bind:disabled="order.isExpired || order.orderStatusId != 1 || (order.paymentStatusId != 0 && order.paymentStatusId != 1)">{{$t('order.pay')}}</el-button>
      <el-button @click="completed();" style="margin-right: 60px;" v-bind:disabled="order.isExpired || (order.orderStatusId != 1 && order.orderStatusId != 3) || (order.paymentStatusId != 2 && order.paymentStatusId != 5)">{{$t('order.fishish')}}</el-button>
      <el-button @click="cancelApply(order.sn);" v-if="order.orderStatusId == 2">{{$t('order.cancelling')}}</el-button>
      <el-button @click="cancel();" v-bind:disabled="order.isExpired || order.orderStatusId != 0">{{$t('order.cancel')}}</el-button>
      <el-button @click="closePage();" style="margin-left: 60px;">返回</el-button>
    </sticky>
    <el-tabs style='margin-top:15px;' v-model="activeName" type="border-card">
      <!--//订单基本信息-->
      <el-tab-pane :label="tabMapOptions[0].label" :key='tabMapOptions[0].key' :name="tabMapOptions[0].key" v-if="order">
        <keep-alive>
            <el-form label-position="right" inline class="order-table-expand" label-width="130px"  v-if='activeName===tabMapOptions[0].key'>
              <el-form-item :label="$t('order.sn')+'：'">
                <span>{{order.sn}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.createDate')+'：'">
                <span>{{order.createDate}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.orderStatus')+'：'">
                <span>{{order.orderStatusDesc}}</span><!-- <span v-if="order.isExpired && order.orderStatus === 'unconfirmed'">(已过期)</span><span v-if="!order.isExpired">(到期时间:{{order.expire}})</span> -->
              </el-form-item>
              <el-form-item :label="$t('order.paymentStatus')+'：'">
                <span>{{order.paymentStatusDesc}}</span>
              </el-form-item>
              <el-form-item label="参保城市：">
                <span>{{order.city.fullName}}</span>
              </el-form-item>
              <el-form-item label="参保人姓名：">
                <span>{{order.userName}}</span>
              </el-form-item>
              <el-form-item label="参保人身份证号：">
                <span>{{order.identification}}</span>
              </el-form-item>
              <el-form-item label="参保人电话号码：">
                <span>{{order.mobile}}</span>
              </el-form-item>
              <el-form-item label="缴费类别">
                <span>{{order.typeName}}</span>
              </el-form-item>
              <el-form-item label="缴费方式">
                <span>{{order.payCategoryName}}</span>
              </el-form-item>
              <el-form-item label="缴费期间">
                <span>{{order.startEndDate}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.amount')+'：'">
                <span>{{order.amount}}元</span>
              </el-form-item>
              <el-form-item :label="$t('order.amountPaid')+'：'">
                <span>{{order.amountPaid}}元</span>
              </el-form-item>
              <el-form-item label="服务费：">
                <span>{{order.fee}}元</span>
              </el-form-item>
              <el-form-item :label="$t('order.paymentMethodName')+'：'">
                <span>{{order.paymentMethodName}}</span>
              </el-form-item>
              <el-form-item label="开票状态：">
                <span>{{order.invoiceDesc}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.invoiceAmount')+'：'">
                <span>{{order.tax}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.memo')+'：'">
                <span v-if="order.memo">{{order.memo}}</span>
                <span v-else>无</span>
              </el-form-item>
          </el-form>
        </keep-alive>
      </el-tab-pane>

    </el-tabs>

    <!--支付 dialog-->
    <el-dialog title="支付" :visible.sync="payDialogFormVisible" v-if="order">
      <el-form :rules="payRules" ref="payDataForm" :model="payDataTemp" inline class="order-table-expand" label-position="right" label-width="100px" style="line-height: 40px;">
        <el-form-item :label="$t('order.sn')+'：'" prop="">
          <span>{{order.sn}}</span>
        </el-form-item>
        <el-form-item :label="$t('order.createDate')+'：'" prop="">
          <span>{{order.createDate}}</span>
        </el-form-item>
        <el-form-item :label="$t('order.amount')+'：'" prop="">
          <span>{{order.amount}}</span>
        </el-form-item>
        <el-form-item :label="$t('order.amountPaid')+'：'" prop="">
          <span>{{order.amountPaid}}</span>
        </el-form-item>
        <el-form-item :label="$t('order.invoiceTitle')+'：'" prop="" v-if="order.isInvoice">
          <span>{{order.invoiceTitle}}</span>
        </el-form-item>
        <el-form-item :label="$t('order.invoiceNo')+'：'" prop="" v-if="order.isInvoice">
          <span>{{order.invoiceNo}}</span>
        </el-form-item>
        <el-form-item :label="$t('payment.bank')+'：'" prop="bank">
          <el-input v-model="payDataTemp.bank"></el-input>
        </el-form-item>
        <el-form-item :label="$t('payment.account')+'：'" prop="account">
          <el-input v-model="payDataTemp.account"></el-input>
        </el-form-item>
        <el-form-item :label="$t('payment.amount')+'：'" prop="amount">
          <el-input v-model="payDataTemp.amount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('payment.payer')+'：'" prop="payer">
          <el-input v-model="payDataTemp.payer"></el-input>
        </el-form-item>
        <el-form-item :label="$t('payment.method')+'：'" prop="methodId" >
          <el-select class="filter-item" v-model="payDataTemp.methodId" name="methodId" @change="selectMethodChange">
            <el-option
              v-for="(item,index) in methods" :style="'padding-left:' + ((item.grade)*20+20) + 'px'"  :key="item.id" :label="item.name"  :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('payment.paymentMethod')+'：'" prop="paymentMethodId" >
          <el-select class="filter-item" v-model="payDataTemp.paymentMethodId" name="paymentMethodId" @change="selectPaymentMethodChange">
            <el-option
              v-for="(item,index) in paymentMethods" :style="'padding-left:' + ((item.grade)*20+20) + 'px'"  :key="item.id" :label="item.name"  :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('payment.memo')+'：'" prop="memo">
          <el-input v-model="payDataTemp.memo"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="payDialogFormVisible = false">{{$t('table.cancel')}}</el-button>
        <el-button type="primary" @click="createPayData">{{$t('table.confirm')}}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
//import orderInfo from './components/orderInfo'
import { fetchView, fetchConfirm, fetchCancel, fetchMethods, fetchPay, fetchCompleted } from "@/api/order";
import Sticky from '@/components/Sticky' // 粘性header组件
import { goback } from '@/utils/common'
import { getAreaOptions } from '@/api/sys'
import $ from 'jquery';
import { getToken } from '@/utils/auth'

export default {
  name: "orderTab",
  //components: { orderInfo },
  components: { Sticky},
  data() {
    return {
      tabMapOptions: [
        { label: "订单信息", key: "Order" }
      ],
      activeName: "Order",
      createdTimes: 0,
      order: null,
      orderLogList: null,
      methods: [],
      paymentMethods: [],
      listQuery: {
        type: this.type
      },
      payDialogFormVisible: false,
      payDataTemp: {
        orderId:'',
        //sn:'',
        //createDate:'',
        //amount:'',
        //amountPaid:'',
        bank:'',
        account:'',
        amount:'',
        payer:'',
        method:'',
        paymentMethodId:'',
        memo:''
      },
      payRules: {
        amount: [{ required: true, message: '付款金额必填', trigger: "blur" }]
        //,  { max: this.order.amountPayable, message: '付款金额必填', trigger: 'change' },
        //{ decimal: true, message: '付款金额必须为数字', trigger: 'change' }
      },

      loading: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
    resetTemp() {
      this.payDataTemp = {
        orderId:'',
        paymentMethodId:'',
        bank:'',
        account:'',
        amount:'',
        payer:'',
        methodId:'',
        memo:''
      }
    },
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
        this.order = response.data.data;

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

  handleChange(val) {
    console.log(val);
    //this.postForm.returnQuantity = val;
  },
     //确认
    confirm() {

      this.$confirm('订单确认后将无法编辑，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        var id = this.$route.params && this.$route.params.id;
      fetchConfirm({id:id}).then(response => {
        if(response.data.result==='00000000'){
        this.$notify({
          title: '成功',
          message: '订单确认完成',
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

    //支付
    pay(){
      this.resetTemp();
      this.getMethods();
      this.payDialogFormVisible = true;
    },
    createPayData(){
      this.payDataTemp.orderId = this.$route.params && this.$route.params.id;
      this.$refs['payDataForm'].validate(valid => {
        if(valid){
            if(!this.payDataTemp.methodId){
              this.$message({
                message: '请选择支付方式',
                type: 'error'
              })
              return false;
            }
            fetchPay(this.payDataTemp).then(response => {
              if (response.data.result==='00000000'){
              this.$message({
                message: '支付成功',
                type: 'success'
              })
            }else {
              this.$message({
                message: '支付失败',
                type: 'error'
              })
            }

            this.payDialogFormVisible = false;
            this.getList();
          })
        }
      })
    },

    // 订单完成
    completed() {
      this.$confirm('订单完成后将无法操作，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        var id = this.$route.params && this.$route.params.id;
      fetchCompleted({id:id}).then(response => {
        if(response.data.result==='00000000'){
        this.$notify({
          title: '成功',
          message: '订单完成',
          type: 'success',
          duration: 2000
        })
      } else {
        this.$notify({
          title: '订单完成',
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
