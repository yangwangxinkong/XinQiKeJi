<template>
  <div class="createPost-container tab-container">

    <sticky className="sub-navbar" v-if="order">
      <el-button type="success" @click="updateOrderData();">{{$t('table.save')}}</el-button>
      <el-button @click="closePage();" style="margin-left: 10px;">返回</el-button>
    </sticky>
    <el-tabs style='margin-top:15px;' v-model="activeName" type="border-card">
      <!--//订单基本信息-->
      <el-tab-pane :label="tabMapOptions[0].label" :key='tabMapOptions[0].key' :name="tabMapOptions[0].key" v-if="order">
        <keep-alive>
            <el-form :rules="orderRules" ref="orderDataFormTable" :model="orderDataForm" inline class="order-table-expand" label-position="right" label-width="100px" style="line-height: 40px;">
              <el-form-item :label="$t('order.sn')+'：'">
                <span>{{order.sn}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.createDate')+'：'">
                <span>{{order.createDate}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.orderStatus')+'：'">
                <span>{{order.orderStatus}}<span v-if="order.expired">(已过期)</span><span v-else>(到期时间:{{order.expiredDate}})</span></span>
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
                <span>￥{{order.amount}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.amountPaid')+'：'">
                <span>￥{{order.amountPaid}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.weight')+'：'">
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
              <el-form-item :label="$t('order.offsetAmount')+'：'" prop="offsetAmount">
                <el-input v-model="orderDataForm.offsetAmount"></el-input>
              </el-form-item>
               <el-form-item :label="$t('order.point')+'：'" prop="point">
                 <el-input v-model="orderDataForm.point"></el-input>
              </el-form-item>
               <el-form-item :label="$t('order.freight')+'：'" prop="freight">
                 <el-input v-model="orderDataForm.freight"></el-input>
              </el-form-item>
              <el-form-item :label="$t('order.fee')+'：'">
                <span>￥{{order.fee}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.paymentMethodName')+'：'" prop="paymentMethodId">
                <el-select class="filter-item" v-model="orderDataForm.paymentMethodId" name="paymentMethodId" placeholder="选择支付方式" @change="selectPaymentMethodChange">
                  <el-option
                    v-for="(item,index) in paymentMethods"
                    :style="'padding-left:' + ((item.grade)*20+20) + 'px'"  :key="item.id" :label="item.name"  :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('order.shippingMethodName')+'：'" class="postInfo-container-item" prop="shippingMethodId">
                <el-select v-model="orderDataForm.shippingMethodId" name="shippingMethodId" placeholder="选择配送方式" @change="selectShippingMethodChange">
                  <el-option
                    v-for="item in shippingMethods"
                    :style="'padding-left:' + ((item.grade)*20+20) + 'px'"  :key="item.id" :label="item.name"  :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <!--<el-row>
                <el-col>-->
                    <el-form-item :label="$t('order.invoiceTitle')+'：'" prop="invoiceTitle">
                      <el-input v-model="orderDataForm.invoiceTitle" v-if="isInvoice"></el-input>
                      <el-input v-model="orderDataForm.invoiceTitle" disabled v-else></el-input>
                    </el-form-item>
                    <el-form-item>
                       <el-checkbox label="是否开据发票" v-model="isInvoice" @click='isInvoice = !isInvoice' :checked="isInvoice"></el-checkbox>
                    </el-form-item>
                    <el-form-item :label="$t('order.invoiceNo')+'：'" prop="invoiceNo">
                      <el-input v-model="orderDataForm.invoiceNo" v-if="isInvoice"></el-input>
                      <el-input v-model="orderDataForm.invoiceNo" disabled v-else></el-input>
                    </el-form-item>
                <!--</el-col>
              </el-row>-->
              <el-form-item :label="$t('order.invoiceAmount')+'：'" prop="tax">
                <el-input v-model="orderDataForm.tax" v-if="isInvoice"></el-input>
                <el-input v-model="orderDataForm.tax" disabled v-else></el-input>
              </el-form-item>
              <el-form-item :label="$t('order.consignee')+'：'">
                <el-input v-model="orderDataForm.consignee"></el-input>
              </el-form-item>
              <el-form-item :label="$t('order.area')+'：'" class="postInfo-container-item" prop="areaId">
                <areaCascader @successCBK="areaSuccessCBK" ref="areaCascaderRef"></areaCascader>
              </el-form-item>
                <el-form-item :label="$t('order.address')+'：'" prop="address">
                  <el-input v-model="orderDataForm.address"></el-input>
              </el-form-item>
               <el-form-item :label="$t('order.zipCode')+'：'" prop="zipCode">
                 <el-input v-model="orderDataForm.zipCode"></el-input>
              </el-form-item>
              <el-form-item :label="$t('order.phone')+'：'" prop="phone">
                <el-input v-model="orderDataForm.phone"></el-input>
              </el-form-item>
              <el-form-item :label="$t('order.memo')+'：'">
                <el-input v-model="orderDataForm.memo"></el-input>
              </el-form-item>
          </el-form>
        </keep-alive>
      </el-tab-pane>

     <!-- //订单商品信息-->
      <el-tab-pane :label="tabMapOptions[1].label" :key='tabMapOptions[1].key' :name="tabMapOptions[1].key">
        <keep-alive>
            <!-- <el-form-item label="商品图片" :key='tabMapOptions[1].key' :name="tabMapOptions[0].key">
              <el-col :span="3" class="orange">
                <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
              </el-col>
            </el-form-item> -->
            <el-form  inline class="order-table-expand" label-position="right" label-width="100px" style="line-height: 40px;">
              <!-- <el-form-item :label="$t('order.sn')+'：'">
                <span>{{order.sn}}</span>
              </el-form-item> -->
              <el-input style="width: 200px;" class="filter-item" :placeholder="$t('order.orderitem.sn')" v-model="orderItemSn"></el-input>
              <el-button class="filter-item" type="primary" style="margin-left:10px;" @click="addOrderItem(orderItemSn)">{{$t('order.addOrderItem')}}</el-button>
            
              <el-table :data="orderItemList" ref="orderItemTable" :label="tabMapOptions[1].label" :key='tabMapOptions[1].key' :name="tabMapOptions[0].key" border fit highlight-current-row style="width: 100%">
              <el-table-column min-width="120px" align="center" :label="$t('order.orderitem.sn')">
                <template slot-scope="scope">
                  <span>{{scope.row.sn}}</span>
                </template>
              </el-table-column>

              <el-table-column width="160px" align="center" :label="$t('order.orderitem.fullName')">
                <template slot-scope="scope">
                  <span>{{scope.row.fullName}}</span>
                </template>
              </el-table-column>

              <el-table-column min-width="160px" align="center" :label="$t('order.orderitem.store')">
                <template slot-scope="scope">
                  <span>{{scope.row.storeName}}</span>
                </template>
              </el-table-column>

              <el-table-column width="160px" align="center" :label="$t('order.orderitem.price')">
                <template slot-scope="scope">
                  <span v-if="scope.row.isGift">-</span>
                  <el-input-number v-model="scope.row.price" controls-position="right"  :min="0"  style="width:120px" v-else></el-input-number>
                </template>
              </el-table-column>

              <el-table-column width="140px" align="center" :label="$t('order.orderitem.quantity')">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.quantity" controls-position="right" :min="0"  style="width:120px"></el-input-number>
                </template>
              </el-table-column>

              <el-table-column align="center" :min="0" :label="$t('order.orderitem.subtotal')">
                <template slot-scope="scope">
                  <span v-if="scope.row.isGift">-</span>
                  <span v-else>￥{{scope.row.price * scope.row.quantity | numFilter}}</span>
                </template>
              </el-table-column>
              <el-table-column width="100px" align="center" :label="$t('order.actions')">
                <template slot-scope="scope">
                  <el-button size="mini" type="danger" @click="delOrderItemDataChild(scope.$index, scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form>
        </keep-alive>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>
//import orderInfo from './components/orderInfo'
import { fetchEdit, fetchUpdate, fetchGetProduct } from "@/api/order";
import areaCascader from '@/components/cascader/areaCascader'
import Sticky from '@/components/Sticky' // 粘性header组件
import { goback } from '@/utils/common'
import { getAreaOptions } from '@/api/sys'
import $ from 'jquery';
import { getToken } from '@/utils/auth'
import { validatorIsPhone } from '@/utils/validate'

const defaultForm = {
  orderId:undefined,
  shippingMethodId: undefined,
  paymentMethodId: undefined,
  strOrderItems: '',
  areaId: undefined,
  offsetAmount:'',
  point:'',
  freight:'',
  isInvoice:false,
  invoiceTitle:'',
  invoiceNo:'',
  invoiceAmount:'',
  consignee:'',
  address:'',
  zipCode:'',
  phone:'',
  memo:''
};

export default {
  name: "orderTab",
  //components: { orderInfo },
  components: { Sticky, areaCascader},
  data() {
    return {
      tabMapOptions: [
        { label: "订单信息", key: "Order" },
        { label: "商品信息", key: "Product" }
      ],
      orderDataForm: Object.assign({}, defaultForm),
      activeName: "Order",
      isInvoice: false,
      order: null,
      orderItemList: [],
      paymentMethods: [],
      shippingMethods: [],
      orderItemSn: null,
      /*orderDataTemp: {
        orderId:undefined,
        shippingMethodId: undefined,
        paymentMethodId: undefined,
        orderItems: '',
        areaId: undefined,
        offsetAmount:'',
        point:'',
        freight:'',
        isInvoice:false,
        invoiceTitle:'',
        invoiceNo:'',
        invoiceAmount:'',
        consignee:'',
        address:'',
        zipCode:'',
        phone:'',
        memo:''
      },*/
      orderRules: {
        offsetAmount: [{ required: true, message: '调整金额必填', trigger: "blur" }],
          point: [{ required: true, message: '赠送积分必填', trigger: "blur" }],
          freight: [
            { required: true, message: '运费必填', trigger: "blur" }/*,
            { min: 0, message: "	不能小于零", trigger: "blur" },
            { decimal: true, message: "	必须为数字", trigger: "blur" }*/
          ],
          consignee: [{ required: true, message: '收货人必填', trigger: "blur" }],
          address: [{ required: true, message: '地址必填', trigger: "blur" }],
          zipCode: [{ required: true, message: '邮编必填', trigger: "blur" }],
          phone: [{ required: true, message: "	电话必填", trigger: "blur" },
          { validator: validatorIsPhone, message:'电话格式不对',trigger:'blur'}],
          areaId: [
            { required: true, message: "	地区必填", trigger: "blur" }
          ],
            paymentMethodId: [
            { required: true, message: "	支付方式必填", trigger: "blur" }
          ],
          shippingMethodId: [
            { required: true, message: "	配送方式必填", trigger: "blur" }
          ]
      },
      selectedAreaOptions: [],
      loading: false
    };
  },
  created() {
    this.getList();
  },
  // 保留小数点后两位的过滤器，尾数四舍五入
  filters: {
    numFilter(value) {
    // 截取当前数据到小数点后两位
      let realVal = Number(value).toFixed(2)
      // num.toFixed(2)获取的是字符串
      return Number(realVal)
    }
  },
  methods: {
    getList() {
     // this.$emit("create"); // for test
      const id = this.$route.params && this.$route.params.id;
  //console.log("id=" + id);
      fetchEdit({id:id}).then(response => {
        this.order = response.data.data.orderInfo;
        this.orderItemList = response.data.data.orderItemInfo;
        this.paymentMethods = response.data.data.paymentMethods;
        this.shippingMethods = response.data.data.shippingMethods;
        this.isInvoice = response.data.data.orderInfo.isInvoice;
        this.loading = false;

        //初始值设置
        this.orderDataForm.shippingMethodId = this.order.shippingMethodId;
        this.orderDataForm.paymentMethodId = this.order.paymentMethodId;
        this.orderDataForm.offsetAmount = this.order.offsetAmount;
        this.orderDataForm.point = this.order.point;
        this.orderDataForm.freight = this.order.freight;
        this.orderDataForm.isInvoice = this.order.isInvoice;
        this.orderDataForm.invoiceTitle = this.order.invoiceTitle;
        this.orderDataForm.invoiceNo = this.order.invoiceNo;
        this.orderDataForm.tax = this.order.tax;
        this.orderDataForm.consignee = this.order.consignee;
        this.orderDataForm.address = this.order.address;
        this.orderDataForm.zipCode = this.order.zipCode;
        this.orderDataForm.memo = this.order.memo;
        this.orderDataForm.phone = this.order.phone;
        this.orderDataForm.isInvoice = this.order.isInvoice;

        this.selectedAreaOptions=[]
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
        //Dom更新完才执行
        this.$nextTick(() => {
          this.$refs.areaCascaderRef.selectOptions=this.selectedAreaOptions;
          this.$refs.orderDataFormTable.clearValidate()
        })
      });
    },
  closePage() {//返回商品列表
    goback(this.$route.path);
    // this.$router.push({
    //   path: "/member/memberRank_list"
    // });
  },
  selectPaymentMethodChange(val){
    this.orderDataForm.paymentMethodId = val
  },
  selectShippingMethodChange(val){
    this.orderDataForm.shippingMethodId = val
  },
  areaSuccessCBK(selectAreas) {
    this.selectedAreaOptions = selectAreas;
  },

  delOrderItemDataChild(index, r) {
    this.orderItemList.splice(index, 1);
  },

  handleChange(val) {
    console.log(val);
    //this.postForm.returnQuantity = val;
  },
   addOrderItem(inputSn) {

    if(inputSn == null || inputSn == "") {
      this.$message({
        message: "请输入商品编号",
        type: 'error',
        duration: 2 * 1000
      })
      return;
    }

    var repeat = false;
		for(var p in this.orderItemList){
      if(this.orderItemList[p].sn == inputSn){
        repeat = true;
        break;
      }
    }

		if (repeat) {
			this.$message({
        message: "改商品编号已存在",
        type: 'error',
        duration: 2 * 1000
      })
      return;
		}

    //商品取得
    fetchGetProduct({productSn:inputSn}).then(response => {

      if(response.data.result == '00000000') {
        this.orderItemList.push(response.data.data);
        setTimeout(() => {
          this.$refs.orderItemTable.setCurrentRow(
            response.data.data
          );
        }, 10); //<==用于延时渲染后选中这行
      } else {
        this.$message({
          message: response.data.msg,
          type: 'error',
          duration: 2 * 1000
        })
      }
      
    }).catch((e)=>{
      console.log(e)
      this.$message({
        message: '添加商品失败',
        type: 'error',
        duration: 2 * 1000
      })
  });
    //添加到列表
  },

    updateOrderData(){

      var priceValidFlag = true;
      var quantityValidFlag = true;
      for(var p in this.orderItemList){
        if(this.orderItemList[p].price == undefined || this.orderItemList[p].price == ""){
          priceValidFlag = false;
          break;
        }

        if(this.orderItemList[p].quantity == undefined || this.orderItemList[p].quantity == ""){
          quantityValidFlag = false;
          break;
        } else if(!/^\+?[1-9][0-9]*$/.test(this.orderItemList[p].quantity)){
          quantityValidFlag = false;
          break;
        }
      }

      if (!priceValidFlag){
        this.$notify({
          title: '失败',
          message: '单价必填',
          type: 'error',
          duration: 2000
        })
        return false;
      }

      if (!quantityValidFlag){
        this.$notify({
          title: '失败',
          message: '数量必须为正整数',
          type: 'error',
          duration: 2000
        })
        return false;
      }

      this.orderDataForm.isInvoice = this.isInvoice;
      this.orderDataForm.orderId = this.$route.params && this.$route.params.id;
      this.orderDataForm.areaId = this.selectedAreaOptions[this.selectedAreaOptions.length-1];
      this.orderDataForm.strOrderItems = JSON.stringify(this.orderItemList);
      //console.log(JSON.stringify(this.orderDataForm));
      //this.$refs.orderDataForm.validate(valid => {

        this.$refs.orderDataFormTable.validate(valid => {
          if(valid){
              fetchUpdate(this.orderDataForm).then(response => {
                  if (response.data.result==='00000000'){
                  this.$message({
                    message: '保存成功',
                    type: 'success'
                  })
                }else {
                  this.$message({
                    message: '保存失败',
                    type: 'error'
                  })
                }
              })

              //返回列表
              goback(this.$route.path);
          }
      });
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
