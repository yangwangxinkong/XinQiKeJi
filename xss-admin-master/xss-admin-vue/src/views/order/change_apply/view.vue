<template>
  <div class="order-container">
    <sticky className="sub-navbar"  :zIndex="9">
      <el-button type="success" @click="submitForm();" v-if="isEdit">{{$t('table.approval')}}</el-button>
      <el-button type="warning" @click="rejectForm();" v-if="isEdit">{{$t('table.reject')}}</el-button>
      <el-button @click="closePage();">返回</el-button>
    </sticky>
    <div class="tab-container">
      <el-tabs style='margin-top:15px;' v-model="activeName" type="border-card">
        <!--//订单基本信息-->
        <el-tab-pane :label="tabMapOptions[0].label" :key='tabMapOptions[0].key' :name="tabMapOptions[0].key">
          <keep-alive>
            <div class="" v-if='activeName==tabMapOptions[0].key && changeApply'>
              <div class="order-base-title"><h3>基本信息</h3></div>
              <el-form label-position="left" inline class="order-table-expand">
                <el-form-item :label="$t('returnApply.sn')+'：'">
                  <span>{{changeApply.sn}}</span>
                </el-form-item>
                <el-form-item :label="$t('returnApply.createDate2')+'：'">
                  <span>{{changeApply.createDate}}</span>
                </el-form-item>
                <el-form-item :label="$t('returnApply.consignee')+'：'">
                  <span>{{changeApply.consignee}}</span>
                </el-form-item>
                <el-form-item :label="$t('returnApply.phone')+'：'">
                  <span>{{changeApply.phone}}</span>
                </el-form-item>
                <el-form-item :label="$t('returnApply.userName')+'：'">
                  <span>{{changeApply.userName}}</span><span style="color: red; margin-left: 10px;">{{changeApply.rankName}}</span>
                </el-form-item>
                <el-form-item :label="$t('returnApply.freight')+'：'">
                  <span  class="color_mu">￥{{changeApply.freight}}</span>
                </el-form-item>
                <el-form-item :label="$t('returnApply.amount')+'：'">
                  <span  class="color_mu">￥{{changeApply.amount}}</span>
                </el-form-item>

                <el-form-item :label="$t('returnApply.amountPaid')+'：'">
                  <span class="color_mu">￥{{changeApply.amountPaid}}</span>
                </el-form-item>
                <!-- <el-row>
                 <el-col>
                   <el-form-item :label="$t('returnApply.rejectContent')+'：'" style="width:auto">
                     <span>{{returnApply.rejectContent}}</span>
                   </el-form-item>
                 </el-col>
               </el-row> -->
                <el-row>
                  <el-col>
                    <el-form-item :label="$t('returnApply.content')+'：'">
                      <span>{{changeApply.content}}</span>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-table :data="changeApply.changeItemInfo"  border fit
                          :header-cell-style="{background:'#ededed',color:'#666'}">
                  <!--<el-table-column prop="id" width="150px" label="商品编号"></el-table-column>-->
                  <el-table-column prop="productFullName" label="产品名称">
                    <template slot-scope="scope">
                      <div class="shop-pic" style="float:left">
                        <!--<a :href="scope.row.path}">-->
                        <img :src="scope.row.image" width="100" height="100" >
                        <!--</a>-->
                      </div>
                      <div class="shop-name" style="width：300px;float:left;margin-left:12px">{{scope.row.productFullName}}</div>
                    </template>
                  </el-table-column>
                  <el-table-column  width="100px"  prop="quantity" :label="$t('returnApply.orderItemInfo.quantity')"></el-table-column>
                  <!--<el-table-column  width="100px"  prop="minPrice" label="现价"></el-table-column>-->
                </el-table>

              </el-form>
              <div class="order-base-title"><h3 v-if="isEdit">填写换货单</h3><h3 v-else>换货单信息</h3></div>
              <div class="createPost-container">

                <el-form class="form-container" :inline="true" :model="postForm" :rules="rules" ref="postForm" label-width="100px">

                  <div class="createPost-main-container">

                    <el-table :data="changeApply.buyerInfo" border fit highlight-current-row  :header-cell-style="{background:'#ededed',color:'#666'}"  style="width: 100%;margin-bottom:20px">

                      <el-table-column min-width="100px" align="center" :label="$t('changeApply.buyerShipper')">
                        <template slot-scope="scope">
                          <span>{{scope.row.buyerShipper}}</span>
                        </template>
                      </el-table-column>

                      <el-table-column min-width="180px" align="center" :label="$t('changeApply.ZipCode')">
                        <template slot-scope="scope">
                          <span>{{scope.row.buyerZipCode}}</span>
                        </template>
                      </el-table-column>

                      <el-table-column min-width="100px" align="center" :label="$t('changeApply.Area')">
                        <template slot-scope="scope">
                          <span>{{scope.row.buyerArea}}</span>
                        </template>
                      </el-table-column>

                      <el-table-column min-width="100px" align="center" :label="$t('changeApply.Address')">
                        <template slot-scope="scope">
                          <span>{{scope.row.buyerAddress}}</span>
                        </template>
                      </el-table-column>

                      <el-table-column min-width="100px" align="center" :label="$t('changeApply.Phone')">
                        <template slot-scope="scope">
                          <span>{{scope.row.buyerPhone}}</span>
                        </template>
                      </el-table-column>
                    </el-table>

                    <el-table :data="changeApply.sellerInfo" border fit highlight-current-row  :header-cell-style="{background:'#ededed',color:'#666'}"  style="width: 100%;margin-bottom:20px" v-if="!isEdit">

                      <el-table-column min-width="100px" align="center" :label="$t('changeApply.sellerShipper')">
                        <template slot-scope="scope">
                          <span>{{scope.row.sellerShipper}}</span>
                        </template>
                      </el-table-column>

                      <el-table-column min-width="180px" align="center" :label="$t('changeApply.ZipCode')">
                        <template slot-scope="scope">
                          <span>{{scope.row.sellerZipCode}}</span>
                        </template>
                      </el-table-column>

                      <el-table-column min-width="100px" align="center" :label="$t('changeApply.Area')">
                        <template slot-scope="scope">
                          <span>{{scope.row.sellerArea}}</span>
                        </template>
                      </el-table-column>

                      <el-table-column min-width="100px" align="center" :label="$t('changeApply.Address')">
                        <template slot-scope="scope">
                          <span>{{scope.row.sellerAddress}}</span>
                        </template>
                      </el-table-column>

                      <el-table-column min-width="100px" align="center" :label="$t('changeApply.Phone')">
                        <template slot-scope="scope">
                          <span>{{scope.row.sellerPhone}}</span>
                        </template>
                      </el-table-column>
                    </el-table>

                    <div class="write-form" v-else>
                      <el-form-item :label="$t('changeApply.sellerShipper')" class="postInfo-container-item" prop="sellerShipper">
                        <el-input :placeholder="'输入卖家收货人'" v-model="postForm.sellerShipper"  style="width:240px"></el-input>
                      </el-form-item>
                      <el-form-item :label="$t('changeApply.ZipCode')" class="postInfo-container-item" prop="sellerZipCode">
                        <el-input :placeholder="'输入邮编'" v-model="postForm.sellerZipCode"  style="width:240px"></el-input>
                      </el-form-item>
                      <el-form-item :label="$t('changeApply.Area')" class="postInfo-container-item" prop="areaId">
                        <areaCascader @successCBK="areaSuccessCBK" :selectOptions="selectedAreaOptions" style="width:240px"></areaCascader>
                      </el-form-item>
                      <el-form-item :label="$t('changeApply.Address')" class="postInfo-container-item" prop="sellerAddress">
                        <el-input :placeholder="'输入地址'" v-model="postForm.sellerAddress"  style="width:240px"></el-input>
                      </el-form-item>
                      <el-form-item :label="$t('changeApply.Phone')" class="postInfo-container-item" prop="sellerPhone">
                        <el-input :placeholder="'输入电话'" v-model="postForm.sellerPhone"  style="width:240px"></el-input>
                      </el-form-item>
                    </div>

                    <el-table :data="changeApply.changeItemInfo" border fit highlight-current-row  :header-cell-style="{background:'#ededed',color:'#666'}"  style="width: 100%;margin-bottom:20px">

                      <el-table-column min-width="100px" align="center" :label="$t('returnApply.returnItemInfo.sn')">
                        <template slot-scope="scope">
                          <span>{{scope.row.sn}}</span>
                        </template>
                      </el-table-column>

                      <el-table-column min-width="180px" align="center" :label="$t('returnApply.orderItemInfo.fullName')">
                        <template slot-scope="scope">
                          <span v-if="scope.row.isGift">{{scope.row.orderItemFullName}}<span class="red">[{{$t('orderCancelApply.orderItemInfo.isGift')}}]</span></span>
                          <span v-else>{{scope.row.orderItemFullName}}</span>
                        </template>
                      </el-table-column>

                      <el-table-column min-width="100px" align="center" :label="$t('returnApply.orderItemInfo.stock')">
                        <template slot-scope="scope">
                          <span>{{scope.row.stock}}</span>
                        </template>
                      </el-table-column>

                      <el-table-column min-width="100px" align="center" :label="$t('returnApply.orderItemInfo.shippedQuantity')">
                        <template slot-scope="scope">
                          <span>{{scope.row.shippedQuantity}}</span>
                        </template>
                      </el-table-column>

                      <el-table-column min-width="100px" align="center" :label="$t('changeApply.changedQuantity')">
                        <template slot-scope="scope">
                          <span>{{scope.row.changedQuantity}}</span>
                        </template>
                      </el-table-column>

                      <el-table-column min-width="100px" align="center" :label="$t('changeApply.changeQuantity')">
                        <template slot-scope="scope">
                          <span>{{scope.row.changeQuantity}}</span>
                        </template>
                      </el-table-column>

                      <el-table-column min-width="150px" align="center" :label="$t('returnApply.actions')">
                        <template slot-scope="scope">
                          <el-button @click="deliveryQuery(scope.row.changeid)" v-if="scope.row.hasDeliveryInfo">查看发货信息</el-button>
                          <span v-else>无发货信息</span>
                        </template>
                      </el-table-column>

                    </el-table>

                    <!-- 驳回 -->
                    <div class="reject-box">
                      <el-form-item label="驳回原因(可不填)：" label-position="top" labelWidth="180px" class="postInfo-container-item" prop="rejectContent">
                        <el-input  style='width: 500px;' type="textarea" :rows="10" placeholder="请输入驳回原因"  v-model="postForm.rejectContent" ></el-input>
                      </el-form-item>
                    </div>
                  </div>
                </el-form>

              </div>
            </div>
          </keep-alive>
        </el-tab-pane>
      </el-tabs>


      <!--支付 dialog-->
      <el-dialog title="物流信息" :visible.sync="deliveryDialogFormVisible" v-if="deliveryInfo">
        <el-table :data="deliveryInfo.buyerShippingInfo" label="买家发货物流信息"  border fit highlight-current-row style="width: 100%">

          <el-table-column min-width="100px" align="center" :label="$t('orderCancelApply.shippingInfo.sn')">
            <template slot-scope="scope">
              <span>{{scope.row.sn}}</span>
            </template>
          </el-table-column>

          <el-table-column min-width="100px" align="center" :label="$t('orderCancelApply.shippingInfo.shippingMethod')">
            <template slot-scope="scope">
              <span>{{scope.row.buyerShippingMethod}}</span>
            </template>
          </el-table-column>

          <el-table-column min-width="100px" align="center" :label="$t('orderCancelApply.shippingInfo.deliveryCorp')">
            <template slot-scope="scope">
              <span>{{scope.row.buyerDeliveryCorp}}</span>
            </template>
          </el-table-column>

          <el-table-column min-width="100px" align="center" :label="$t('orderCancelApply.shippingInfo.trackingNo')">
            <template slot-scope="scope">
              <span>{{scope.row.buyerTrackingNo}}</span>
            </template>
          </el-table-column>

          <el-table-column min-width="100px" align="center" :label="$t('orderCancelApply.shippingInfo.createDate')">
            <template slot-scope="scope">
              <span>{{scope.row.createDate}}</span>
            </template>
          </el-table-column>

        </el-table>

        <el-table :data="deliveryInfo.sellerShippingInfo" label="卖家发货物流信息"  border fit highlight-current-row style="width: 100%; margin-top: 40px;">

          <el-table-column min-width="100px" align="center" :label="$t('orderCancelApply.shippingInfo.sn')">
            <template slot-scope="scope">
              <span>{{scope.row.sn}}</span>
            </template>
          </el-table-column>

          <el-table-column min-width="100px" align="center" :label="$t('orderCancelApply.shippingInfo.shippingMethod')">
            <template slot-scope="scope">
              <span>{{scope.row.sellerShippingMethod}}</span>
            </template>
          </el-table-column>

          <el-table-column min-width="100px" align="center" :label="$t('orderCancelApply.shippingInfo.deliveryCorp')">
            <template slot-scope="scope">
              <span>{{scope.row.sellerDeliveryCorp}}</span>
            </template>
          </el-table-column>

          <el-table-column min-width="100px" align="center" :label="$t('orderCancelApply.shippingInfo.trackingNo')">
            <template slot-scope="scope">
              <span>{{scope.row.sellerTrackingNo}}</span>
            </template>
          </el-table-column>

          <el-table-column min-width="100px" align="center" :label="$t('orderCancelApply.shippingInfo.createDate')">
            <template slot-scope="scope">
              <span>{{scope.row.createDate}}</span>
            </template>
          </el-table-column>

        </el-table>
      </el-dialog>

    </div>
  </div>
</template>

<script>
  import { fetchView, fetchApproval, fetchReject, fetchDeliveryInfo } from "@/api/changeApply";
  import areaCascader from '@/components/cascader/areaCascader'
  import Sticky from "@/components/Sticky"; // 粘性header组件
  import { goback } from "@/utils/common";
  import { getAreaOptions } from '@/api/sys'
  import $ from 'jquery';
  import { getToken } from '@/utils/auth'

  const defaultForm = {
    sellerShipper: "", //发货人
    sellerZipCode: "", //邮编
    areaId: "", //地区
    sellerAddress: "", //地址
    sellerPhone: "", //电话
    rejectContent: "", //驳回原因
    id: undefined //编号
  };
  export default {
    name: "tab",
    //components: { paymentInfo },
    components: { Sticky, areaCascader},
    data() {
    return {
      isEdit: false,
      tabMapOptions: [
        { label: "换货申请", key: "ChangeInfo" }
        // { label: "退货详情", key: "shippingItemsInfo" }
      ],
      postForm: Object.assign({}, defaultForm),
      activeName: "ChangeInfo",
      deliveryDialogFormVisible: false,
      changeApply: null,
      areaOptions: [],
      selectedAreaOptions: [],
      deliveryInfo:[],
      loading: false,
      //表单验证
      rules: {
        /*areaId: [
         { required: true, message: "	地区必填", trigger: "blur" }
         ],
         address: [
         { required: true, message: "	地址必填", trigger: "blur" }
         ],
         phone: [
         { required: true, message: "	电话必填", trigger: "blur" }
         ],
         zipCode: [
         { required: true, message: "	邮编必填", trigger: "blur" }
         ],
         shipper: [
         { required: true, message: "	发货人必填", trigger: "blur" }
         ]*/
      }
    };
  },
  created() {
    this.isEdit = this.$route.query && this.$route.query.isEdit;
    this.postForm.id = this.$route.query && this.$route.query.id;
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;

      const id = this.$route.query && this.$route.query.id;
      const sn = this.$route.query && this.$route.query.sn;

      fetchView({id:id, sn:sn}).then(response => {

        this.changeApply = response.data.data;

      const area = this.changeApply.area
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
      //console.log(this.selectedAreaOptions)
      //this.$refs.areaCascaderRef.selectOptions=this.selectedAreaOptions;

      this.loading = false;
    });
  },
  closePage() {
    //返回列表
    goback(this.$route.fullPath);
  },
  currentSel(val) {
    console.log(val);
  },
  submitForm() {

    this.postForm.areaId = this.selectedAreaOptions[this.selectedAreaOptions.length-1]
    //console.log(this.postForm);
    this.$refs.postForm.validate(valid => {
      if (valid) {
      this.loading = true;
      fetchApproval(this.postForm).then(response => {
        if (response.data.result==='00000000') {
        this.$notify({
          title: '成功',
          message: '操作成功',
          type: 'success',
          duration: 2000
        })
        //this.$router.push({path:"/product/brand/list"})
        goback(this.$route.fullPath);
      }else{
        this.$notify({
          title: '失败',
          message: '操作失败', //response.data.msg
          type: 'error',
          duration: 2000
        })
      }
    })

    this.loading = false;
  } else {
    console.log("error submit!!");
    return false;
  }
  });
  },
  rejectForm() {
    //console.log(this.postForm);
    this.$refs.postForm.validate(valid => {
      if (valid) {
      this.loading = true;
      fetchReject(this.postForm).then(response => {
        if (response.data.result==='00000000') {
        this.$notify({
          title: '成功',
          message: '操作成功',
          type: 'success',
          duration: 2000
        })
        //this.$router.push({path:"/product/brand/list"})
        goback(this.$route.fullPath);
      }else{
        this.$notify({
          title: '失败',
          message: '操作失败',
          type: 'error',
          duration: 2000
        })
      }
    })

    this.loading = false;
  } else {
    console.log("error submit!!");
    return false;
  }
  });
  },
  handleChange(val) {
    console.log(val);
    //this.postForm.returnQuantity = val;
  }, //城市切换
  areaSuccessCBK(selectAreas) {
    this.selectedAreaOptions = selectAreas;
  },
  handleChangeAddress() {},

    //查看物流信息
    deliveryQuery(id) {
      this.getDeliveryInfo(id);
      this.deliveryDialogFormVisible = true;
    },

    //获取物流信息
    getDeliveryInfo(id){
        fetchDeliveryInfo({id:id}).then(response => {
          this.deliveryInfo = response.data.data;
      })
    }
  }
  };
</script>

<style rel="stylesheet/scss" lang="scss"scoped >
  @import "src/styles/mixin.scss";
  .tab-container {
    margin: 20px;
  }
  .color_mu {
    color: #ff3333;
  }
  .order-table-expand {
    font-size: 0;
  .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 33.333%;
  label {
    color: #393939;
    font-weight: normal;
  }
  }
  }
  .order-base-title {
  h3 {
    color: #2178f1;
    font-size: 18px;
    font-weight: normal;
    border-left: 3px solid #2178f1;
    padding-left: 12px;
  }
  }

  .createPost-container {
    position: relative;
  .createPost-main-container {
  // padding: 40px 45px 20px 50px;
  .postInfo-container {
    position: relative;
  @include clearfix;
    margin-bottom: 10px;
  .postInfo-container-item {
    float: left;
  }
  }
  .editor-container {
    min-height: 500px;
    margin: 0 0 30px;
  .editor-upload-btn-container {
    text-align: right;
    margin-right: 10px;
  .editor-upload-btn {
    display: inline-block;
  }
  }
  }
  }
  .word-counter {
    width: 40px;
    position: absolute;
    right: -10px;
    top: 0px;
  }
  }
</style>
