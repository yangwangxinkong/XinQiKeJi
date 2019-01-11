<template>
  <div class="createPost-container tab-container">

    <sticky className="sub-navbar" v-if="order">
      <el-button @click="confirm();" v-bind:disabled="order.expired || order.orderStatusId != 0">{{$t('order.confirm')}}</el-button>
      <el-button @click="shipping();" v-bind:disabled="order.expired || (order.orderStatusId != 1 && order.orderStatusId != 3) || (order.shippingStatusId != 0 && order.shippingStatusId != 1) || order.paymentStatusId != 2">{{$t('order.shippings')}}</el-button>
      <el-button @click="completed();" style="margin-right: 60px;" v-bind:disabled="order.expired || (order.orderStatusId != 1 && order.orderStatusId != 3) || (order.paymentStatusId != 2 && order.paymentStatusId != 6) || (order.shippingStatusId != 3 && order.shippingStatusId != 8 && order.shippingStatusId != 11)">{{$t('order.fishish')}}</el-button>
      <el-button @click="closePage();" style="margin-left: 60px;">返回</el-button>
    </sticky>
    <el-tabs style='margin-top:15px;' v-model="activeName" type="border-card">
      <!--//订单基本信息-->
      <el-tab-pane :label="tabMapOptions[0].label" :key='tabMapOptions[0].key' :name="tabMapOptions[0].key" v-if="order">
        <keep-alive>
            <el-form label-position="right" inline class="order-table-expand" label-width="100px"  v-if='activeName===tabMapOptions[0].key'>
              <el-form-item :label="$t('order.sn')+'：'">
                <span>{{order.sn}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.createDate')+'：'">
                <span>{{order.createDate}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.orderStatus')+'：'">
                <span>{{order.orderStatus}}<span v-if="!order.expired && order.orderStatusId === 0">(到期时间:{{order.expiredDate}})</span></span>
              </el-form-item>
              <el-form-item :label="$t('order.paymentStatus')+'：'">
                <span>{{order.paymentStatus}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.shippingStatus')+'：'">
                <span>{{order.shippingStatus}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.userName')+'：'">
                <span>{{order.userName}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.amount')+'：'">
                <span>{{order.amount}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.amountPaid')+'：'">
                <span>{{order.amountPaid}}</span>
              </el-form-item>
              <el-form-item label="订单金豆：">
                <span>{{order.point}}</span>
              </el-form-item>
               <el-form-item :label="$t('order.quantity')+'：'">
                <span>{{order.quantity}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.consignee')+'：'">
                <span>{{order.consignee}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.area')+'：'">
                <span>{{order.areaName}}</span>
              </el-form-item>
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

          <el-table :data="orderItemList" v-if='activeName==tabMapOptions[1].key' :type='tabMapOptions[1].key'  border fit highlight-current-row style="width: 80%">

            <el-table-column min-width="80px" align="center" :label="$t('order.orderitem.sn')">
              <template slot-scope="scope">
                <span>{{scope.row.sn}}</span>
              </template>
            </el-table-column>

            <el-table-column width="230px" align="center" :label="$t('order.orderitem.fullName')">
              <template slot-scope="scope">
                <span>{{scope.row.fullName}}</span>
              </template>
            </el-table-column>

            <el-table-column width="150px" align="center" :label="$t('order.orderitem.tel')">
              <template slot-scope="scope">
                <span>{{scope.row.storeTel}}</span>
              </template>
            </el-table-column>

            <el-table-column width="150px" align="center" :label="$t('order.orderitem.price')">
              <template slot-scope="scope">
                <span>{{scope.row.price}}</span>
              </template>
            </el-table-column>

            <el-table-column width="100px" align="center" :label="$t('order.orderitem.quantity')">
              <template slot-scope="scope">
                <span>{{scope.row.quantity}}</span>
              </template>
            </el-table-column>

            <el-table-column width="100px" align="center" :label="$t('order.orderitem.shippedQuantity')">
              <template slot-scope="scope">
                <span>{{scope.row.shippedQuantity}}</span>
              </template>
            </el-table-column>

            <el-table-column width="100px" align="center" :label="$t('order.orderitem.returnQuantity')">
              <template slot-scope="scope">
                <span>{{scope.row.returnQuantity}}</span>
              </template>
            </el-table-column>

            <el-table-column width="100px" align="center" :label="$t('order.orderitem.subtotal')">
              <template slot-scope="scope">
                <span>{{scope.row.subtotal}}</span>
              </template>
            </el-table-column>
          </el-table>
        </keep-alive>
      </el-tab-pane>

      <!--//发货信息-->
      <el-tab-pane :label="tabMapOptions[3].label" :key='tabMapOptions[3].key' :name="tabMapOptions[3].key">
        <keep-alive>

          <el-table :data="shippingList" v-if='activeName==tabMapOptions[3].key' :type='tabMapOptions[3].key'  border fit highlight-current-row style="width: 100%">

            <el-table-column min-width="100px" align="center" :label="$t('order.shipping.sn')">
              <template slot-scope="scope">
                <span>{{scope.row.sn}}</span>
              </template>
            </el-table-column>

            <el-table-column min-width="180px" align="center" :label="$t('order.shipping.shippingMethod')">
              <template slot-scope="scope">
                <span>{{scope.row.shippingMethod}}</span>
              </template>
            </el-table-column>

            <el-table-column min-width="180px" align="center" :label="$t('order.shipping.deliveryCorp')">
              <template slot-scope="scope">
                <span>{{scope.row.deliveryCorp}}</span>
              </template>
            </el-table-column>

            <el-table-column width="120px" align="center" :label="$t('order.shipping.trackingNo')">
              <template slot-scope="scope">
                <span>{{scope.row.trackingNo}}</span>
              </template>
            </el-table-column>

            <el-table-column min-width="80px" align="center" :label="$t('order.shipping.consignee')">
              <template slot-scope="scope">
                <span>{{scope.row.consignee}}</span>
              </template>
            </el-table-column>

            <el-table-column min-width="120px" align="center" :label="$t('order.shipping.createDate')">
              <template slot-scope="scope">
                <span>{{scope.row.createDate}}</span>
              </template>
            </el-table-column>
          </el-table>
        </keep-alive>
      </el-tab-pane>



      <!--//日志信息-->
      <el-tab-pane :label="tabMapOptions[7].label" :key='tabMapOptions[7].key' :name="tabMapOptions[7].key">
        <keep-alive>
          <el-table :data="orderLogList" v-if='activeName==tabMapOptions[7].key' :type='tabMapOptions[7].key'  border fit highlight-current-row style="width: 100%">

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
    <!--发货 dialog-->
    <el-dialog title="发货" :visible.sync="shippingDialogFormVisible" v-if="order">
      <el-form :rules="shippingRules" ref="shippingDataForm" :model="shippingDataTemp" inline class="order-table-expand" label-position="right" label-width="100px" style="line-height: 40px;">
        <el-form-item :label="$t('order.sn')+'：'" prop="">
          <span>{{order.sn}}</span>
        </el-form-item>
        <el-form-item :label="$t('order.createDate')+'：'" prop="">
          <span>{{order.createDate}}</span>
        </el-form-item>
        <el-form-item :label="$t('shipping.trackingNo')+'：'" class="postInfo-container-item" prop="trackingNo">
          <el-input :placeholder="'输入运单号'" v-model="shippingDataTemp.trackingNo"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shipping.freight')+'：'" class="postInfo-container-item" prop="freight">
          <el-input :placeholder="'物流费用，必须为数字'" v-model="shippingDataTemp.freight"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shipping.shippingMethod')+'：'" class="postInfo-container-item" prop="shippingMethodName">
          <el-input :placeholder="'输入配送方式'" v-model="shippingDataTemp.shippingMethodName" ></el-input>
        </el-form-item>
        <el-form-item :label="$t('shipping.deliveryCorp')+'：'" class="postInfo-container-item" prop="deliveryCorp">
          <el-input :placeholder="'输入物流公司'" v-model="shippingDataTemp.deliveryCorp" ></el-input>
        </el-form-item>

        <el-form-item :label="$t('returns.consignee')+'：'" class="postInfo-container-item" prop="consignee">
          <el-input :placeholder="'输入发货人'" v-model="shippingDataTemp.consignee" ></el-input>
        </el-form-item>
        <el-form-item :label="$t('returns.zipCode')+'：'" class="postInfo-container-item" prop="zipCode">
          <el-input :placeholder="'输入邮编'" v-model="shippingDataTemp.zipCode" ></el-input>
        </el-form-item>
        <el-form-item :label="$t('returns.area')+'：'" class="postInfo-container-item" prop="areaId">
          <!--<el-cascader
              :options="options"
              v-model="selectedOptions"
              @change="handleChangeAddress"
              style="width:240px">
            </el-cascader>-->
          <areaCascader @successCBK="areaSuccessCBK" ref="areaCascaderRef" style="width:185px"></areaCascader>
        </el-form-item>
        <el-form-item  :label="$t('returns.address')+'：'" class="postInfo-container-item" prop="address">
          <el-input :placeholder="'输入地址'" v-model="shippingDataTemp.address"></el-input>
        </el-form-item>
        <el-form-item  :label="$t('returns.phone')+'：'" class="postInfo-container-item" prop="phone">
          <el-input :placeholder="'输入电话'" v-model="shippingDataTemp.phone"></el-input>
        </el-form-item>
        <el-form-item  :label="$t('returns.memo')+'：'" class="postInfo-container-item" prop="memo">
          <el-input :placeholder="'输入备注'" v-model="shippingDataTemp.memo" ></el-input>
        </el-form-item>


      <el-table :data="orderItemList"  border fit highlight-current-row  :header-cell-style="{background:'#ededed',color:'#666'}" style=" margin-bottom: 20px;">
        <el-table-column min-width="110px" align="center" :label="$t('returnApply.orderItemInfo.sn')">
          <template slot-scope="scope">
            <span>{{scope.row.sn}}</span>
          </template>
        </el-table-column>

        <el-table-column min-width="120px" align="center" :label="$t('returnApply.orderItemInfo.fullName')">
          <template slot-scope="scope">
            <span>{{scope.row.fullName}}</span>
          </template>
        </el-table-column>

        <el-table-column min-width="90px" align="center" :label="$t('returnApply.orderItemInfo.stock')">
          <template slot-scope="scope">
            <span>{{scope.row.stock}}</span>
          </template>
        </el-table-column>

        <el-table-column min-width="100px" align="center" :label="$t('order.orderitem.orderQuantity')">
          <template slot-scope="scope">
            <span>{{scope.row.quantity}}</span>
          </template>
        </el-table-column>

        <el-table-column min-width="90px" align="center" :label="$t('order.orderitem.shippedQuantity')">
          <template slot-scope="scope">
            <span>{{scope.row.shippedQuantity}}</span>
          </template>
        </el-table-column>

        <el-table-column min-width="120px" align="center" :label="$t('order.orderitem.shippingQuantity')">
          <template slot-scope="scope">
            <el-form-item label="" class="postInfo-container-item" prop="shippingQuantity">
              <el-input-number v-model="scope.row.shippingQuantity" controls-position="right"  :min="0" @change="handleChange" style="width:80px" v-if="scope.row.stock <= 0 || scope.row.quantity - scope.row.shippedQuantity  <= 0" disabled></el-input-number>
              <el-input-number v-model="scope.row.shippingQuantity" controls-position="right"  :min="0" @change="handleChange" style="width:80px" v-else></el-input-number>
            </el-form-item>
          </template>
        </el-table-column>
      </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="shippingDialogFormVisible = false">{{$t('table.cancel')}}</el-button>
        <el-button type="primary" @click="createShippingData">{{$t('table.confirm')}}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
//import orderInfo from './components/orderInfo'
import { fetchExchargeView, fetchConfirm, fetchCancel, fetchMethods, fetchPay, fetchShippings, fetchShipping, fetchCompleted, fetchShippingTimeOut } from "@/api/order";
import areaCascader from '@/components/cascader/areaCascader'
import Sticky from '@/components/Sticky' // 粘性header组件
import { goback } from '@/utils/common'
import { getAreaOptions } from '@/api/sys'
import $ from 'jquery';
import { getToken } from '@/utils/auth'
import { validatorIsPhone, validNumber } from '@/utils/validate'

export default {
  name: "orderTab",
  //components: { orderInfo },
  components: { Sticky, areaCascader},
  data() {
    return {
      tabMapOptions: [
        { label: "订单信息", key: "Order" },
        { label: "商品信息", key: "Product" },
        { label: "收款信息", key: "Receivable" },
        { label: "发货信息", key: "Shipping" },
        { label: "退款信息", key: "Refund" },
        { label: "退货信息", key: "Return" },
        { label: "换货信息", key: "Exchange" },
        { label: "订单日志", key: "Log" }
      ],
      activeName: "Order",
      createdTimes: 0,
      order: null,
      orderItemList: [],
      paymentList: null,
      shippingList: null,
      refundsList: null,
      returnsList: null,
      changeList: null,
      orderLogList: null,
      methods: [],
      paymentMethods: [],
      shippingMethods: [],
      deliveryCorps: [],
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
      shippingDialogFormVisible: false,
      shippingDataTemp: {
        orderId:undefined,
        shippingMethodName: undefined,
        deliveryCorpId: undefined,
        orderItems: '',
        areaId: undefined,
        trackingNo:'',
        freight:'',
        consignee:'',
        zipCode:'',
        address:'',
        phone:'',
        memo:''
      },
      shippingRules: {
        trackingNo: [{ required: true, message: '运单号必填', trigger: "blur" }],
        freight: [
          { required: true, message: "	物流费用必填", trigger: "blur" },
          { validator: validNumber, message: "	必须为数字", trigger: "blur" }
        ],
        areaId: [
          { required: true, message: "	地区必填", trigger: "blur" }
        ],
        shippingMethodName: [
          { required: true, message: "	配送方式必填", trigger: "blur" }
        ],
        deliveryCorp: [
          { required: true, message: "	物流公司必填", trigger: "blur" }
        ],
        address: [
          { required: true, message: "	地址必填", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "	电话必填", trigger: "blur" },
          { validator: validatorIsPhone, message:'电话格式不对',trigger:'blur'}
        ],
        zipCode: [
          { required: true, message: "	邮编必填", trigger: "blur" }
        ],
        consignee: [
          { required: true, message: "	发货人必填", trigger: "blur" }
        ]
      },
      selectedAreaOptions: [],
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
      fetchExchargeView(id).then(response => {
        this.order = response.data.data.orderInfo;
        this.orderItemList = response.data.data.orderItemInfo;
        this.paymentList = response.data.data.paymentInfo;
        this.shippingList = response.data.data.shippingInfo;
        this.refundsList = response.data.data.refundsInfo;
        this.returnsList = response.data.data.returnsInfo;
        this.changeList = response.data.data.changeInfo;
        this.orderLogList = response.data.data.orderLogInfo;

        this.loading = false;

        const area = this.order.area;
        if(area){
          let treePath = area.treePath;
          treePath += area.id;
          treePath = treePath.substring(1);
          let areaIds = treePath.split(",");
          this.selectedAreaOptions = [];
          for(var i in areaIds){
            this.selectedAreaOptions.push(parseInt(areaIds[i]));
          }
        }

        if(this.$refs.areaCascaderRef != null
        && this.$refs.areaCascaderRef != undefined
        && this.$refs.areaCascaderRef.selectOptions != null
        && this.$refs.areaCascaderRef.selectOptions != undefined) {
          this.$refs.areaCascaderRef.selectOptions=this.selectedAreaOptions;
        }
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
            fetchPay(this.payDataTemp).then(response => {
              if (response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '订单支付成功',
                type: 'success',
                duration: 2000
              })

            }else {
              this.$notify({
                title: '失败',
                message: '订单支付失败',
                type: 'error',
                duration: 2000
              })
            }
            this.getList();
          })

          this.payDialogFormVisible = false;
        }
      })
    },
      resetShippingTemp() {
        this.shippingDataTemp = {
          orderId:'',
          shippingMethodId: '',
          deliveryCorpId: '',
          areaId: '',
          trackingNo:'',
          freight:'',
          consignee:'',
          zipCode:'',
          address:'',
          phone:'',
          memo:''
        }
      },
      getShippingMethods(){
//        fetchShippings().then(response => {
//            this.deliveryCorps = response.data.data.deliveryCorps;
//            this.shippingMethods = response.data.data.shippingMethods;
//        })
      },
    //发货
    shipping(){
      this.resetShippingTemp();
      this.getShippingMethods();
      this.shippingDialogFormVisible = true;
    },
    createShippingData(){

      var validFlag = 0;
      for(var p in this.orderItemList){
        if(this.orderItemList[p].shippingQuantity == "" || this.orderItemList[p].shippingQuantity == undefined){
          validFlag = 1;
        } else if(this.orderItemList[p].quantity + this.orderItemList[p].shippedQuantity < this.orderItemList[p].shippingQuantity) {
          validFlag = 2;
        }
      }

      if (validFlag === 1){
        this.$notify({
          title: '失败',
          message: '发货数量必填',
          type: 'error',
          duration: 2000
        })
        return false;
      }

      if (validFlag === 2){
        this.$notify({
          title: '失败',
          message: '发货数量超过了应发货数量',
          type: 'error',
          duration: 2000
        })
        return false;
      }

      this.shippingDataTemp.orderId = this.$route.params && this.$route.params.id;
      this.shippingDataTemp.areaId = this.selectedAreaOptions[this.selectedAreaOptions.length-1]
      this.shippingDataTemp.orderItems = JSON.stringify(this.orderItemList);
      this.$refs['shippingDataForm'].validate(valid => {
        if(valid){
          fetchShipping(this.shippingDataTemp).then(response => {
            if (response.data.result==='00000000'){
            this.$notify({
                title: '成功',
                message: '订单发货成功',
                type: 'success',
                duration: 2000
              })


          }else {
            this.$notify({
                  title: '失败',
                  message: '订单发货失败',
                  type: 'error',
                  duration: 2000
                })
          }

          this.getList();
        })

        this.shippingDialogFormVisible = false;

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

        this.getList();
      } else {
        this.$notify({
          title: '订单完成',
          message: response.data.msg,
          type: 'error',
          duration: 2000
        })
      }

    })
    }).catch(err => {
      console.log(err)
    })
    },

    // 发货超时退款
    shippingTimeOut() {
      this.$confirm('平台主动退款，不可回退，请谨慎。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        var id = this.$route.params && this.$route.params.id;
      fetchShippingTimeOut({id:id}).then(response => {
        if(response.data.result==='00000000'){
        this.$notify({
          title: '成功',
          message: '退款完成',
          type: 'success',
          duration: 2000
        })
      } else {
        this.$notify({
          title: '退款完成',
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

    // 退款
    refundsApply(orderSn) {
      this.$router.push({path: '/order/refunds_apply/view', query:{sn:orderSn, isEdit:true}});
    },

    // 退货
    returnsApply(orderSn) {
      this.$router.push({path: '/order/returns_apply/view', query:{sn:orderSn, isEdit:true}});
    },

    // 换货
    changesApply(orderSn) {
      this.$router.push({path: '/order/change_apply/view', query:{sn:orderSn, isEdit:true}});
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
