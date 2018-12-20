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
                <div class="" v-if='activeName==tabMapOptions[0].key && returnApply'>
                  <div class="order-base-title"><h3>基本信息</h3></div>
                  <el-form label-position="left" inline class="order-table-expand">
                      <el-form-item :label="$t('returnApply.sn')+'：'">
                        <span>{{returnApply.sn}}</span>
                      </el-form-item>
                      <el-form-item :label="$t('returnApply.createDate2')+'：'">
                        <span>{{returnApply.createDate}}</span>
                      </el-form-item>
                      <el-form-item :label="$t('returnApply.consignee')+'：'">
                        <span>{{returnApply.consignee}}</span>
                      </el-form-item>
                      <el-form-item :label="$t('returnApply.phone')+'：'">
                        <span>{{returnApply.phone}}</span>
                      </el-form-item>
                        <el-form-item :label="$t('returnApply.userName')+'：'">
                        <span>{{returnApply.userName}}</span><span style="color: red; margin-left: 10px;">{{returnApply.rankName}}</span>
                      </el-form-item>
                      <el-form-item :label="$t('returnApply.freight')+'：'">
                        <span  class="color_mu">￥{{returnApply.freight}}</span>
                      </el-form-item>
                      <el-form-item :label="$t('returnApply.amount')+'：'">
                        <span  class="color_mu">￥{{returnApply.amount}}</span>
                      </el-form-item>

                       <el-form-item :label="$t('returnApply.amountPaid')+'：'">
                        <span class="color_mu">￥{{returnApply.amountPaid}}</span>
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
                            <span>{{returnApply.content}}</span>
                          </el-form-item>
                       </el-col>
                      </el-row>
                       <el-table :data="returnApply.orderItemInfo"  border fit
                             :header-cell-style="{background:'#ededed',color:'#666'}">
                          <!--<el-table-column prop="id" width="150px" label="商品编号"></el-table-column>-->
                          <el-table-column prop="fullName" label="产品名称">
                            <template slot-scope="scope">
                              <div class="shop-pic" style="float:left">
                                <!--<a :href="scope.row.path}">-->
                                  <img :src="scope.row.image" width="100" height="100" >
                                <!--</a>-->
                              </div>
                              <div class="shop-name" style="width：300px;float:left;margin-left:12px">{{scope.row.fullName}}</div>
                            </template>
                          </el-table-column>
                          <el-table-column  width="100px"  prop="quantity" :label="$t('returnApply.orderItemInfo.quantity')"></el-table-column>
                          <!--<el-table-column  width="100px"  prop="minPrice" label="现价"></el-table-column>-->
                      </el-table>

                   </el-form>
                  <div class="order-base-title"><h3 v-if="isEdit">填写退货单</h3><h3 v-else>退货单信息</h3></div>
                    <div class="createPost-container">
                      <el-form class="form-container"  :inline="true" :model="postForm" :rules="rules" ref="postForm" label-width="100px">
                        <div class="createPost-main-container">
                            <div class="write-form" v-if="isEdit">
                            <el-row>
                              <el-form-item  label="运单号" class="postInfo-container-item" prop="trackingNo">
                                <el-input :placeholder="'输入运单号'" v-model="postForm.trackingNo"  style="width:240px"></el-input>
                              </el-form-item>
                              <el-form-item  label="物流费用" class="postInfo-container-item" prop="freight">
                                <el-input :placeholder="'输入物流费用'" v-model="postForm.freight"  style="width:240px"></el-input>
                              </el-form-item>
                              <el-form-item  label="配送方式" class="postInfo-container-item" prop="shippingMethodId">
                                <el-select v-model="postForm.shippingMethodId" placeholder="选择配送方式" style="width:240px">
                                  <el-option
                                    v-for="item in returnApply.shippingMethods"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                                  </el-option>
                                </el-select>
                              </el-form-item>
                              </el-row>
                              <el-row>
                                <el-form-item  label="物流公司" class="postInfo-container-item" prop="deliveryCorpId">
                                  <el-select v-model="postForm.deliveryCorpId" placeholder="选择物流公司" style="width:240px"  @change="currentSel">
                                    <el-option
                                      v-for="item in returnApply.deliveryCorps"
                                      :key="item.id"
                                      :label="item.name"
                                      :value="item.id">
                                    </el-option>
                                </el-select>
                               </el-form-item>
                              <el-form-item  label="发货人" class="postInfo-container-item" prop="shipper">
                                <el-input :placeholder="'输入发货人'" v-model="postForm.shipper"  style="width:240px"></el-input>
                              </el-form-item>
                               <el-form-item  label="邮编" class="postInfo-container-item" prop="zipCode">
                                <el-input :placeholder="'输入邮编'" v-model="postForm.zipCode"  style="width:240px"></el-input>
                              </el-form-item>
                              </el-row>
                               <el-row>
                              <el-form-item  label="地区" class="postInfo-container-item" prop="areaId">
                                <!--<el-cascader
                                    :options="options"
                                    v-model="selectedOptions"
                                    @change="handleChangeAddress"
                                    style="width:240px">
                                  </el-cascader>-->
                                <areaCascader @successCBK="areaSuccessCBK" ref="areaCascaderRef" style="width:240px"></areaCascader>
                              </el-form-item>
                              <el-form-item  label="地址" class="postInfo-container-item" prop="address">
                                <el-input :placeholder="'输入地址'" v-model="postForm.address"  style="width:240px"></el-input>
                              </el-form-item>
                               <el-form-item  label="电话" class="postInfo-container-item" prop="phone">
                                <el-input :placeholder="'输入电话'" v-model="postForm.phone"  style="width:240px"></el-input>
                              </el-form-item>
                               </el-row>
                               <el-form-item  label="备注" class="postInfo-container-item" prop="memo">
                                <el-input :placeholder="'输入备注'" v-model="postForm.memo"  style="width:240px"></el-input>
                              </el-form-item>
                          </div>

                            <el-table :data="returnApply.returnItemInfo" border fit highlight-current-row  :header-cell-style="{background:'#ededed',color:'#666'}"  style="width: 100%;margin-bottom:20px" v-if="!isEdit">

                                <el-table-column min-width="100px" align="center" :label="$t('returnApply.returnItemInfo.sn')">
                                  <template slot-scope="scope">
                                    <span>{{scope.row.sn}}</span>
                                  </template>
                                </el-table-column>

                                <el-table-column min-width="180px" align="center" :label="$t('returnApply.returnItemInfo.shippingMethod')">
                                  <template slot-scope="scope">
                                    <span>{{scope.row.shippingMethod}}</span>
                                  </template>
                                </el-table-column>

                                <el-table-column min-width="100px" align="center" :label="$t('returnApply.returnItemInfo.deliveryCorp')">
                                  <template slot-scope="scope">
                                    <span>{{scope.row.deliveryCorp}}</span>
                                  </template>
                                </el-table-column>

                                <el-table-column min-width="100px" align="center" :label="$t('returnApply.returnItemInfo.trackingNo')">
                                  <template slot-scope="scope">
                                    <span>{{scope.row.trackingNo}}</span>
                                  </template>
                                </el-table-column>

                                <el-table-column min-width="100px" align="center" :label="$t('returnApply.returnItemInfo.shipper')">
                                  <template slot-scope="scope">
                                    <span>{{scope.row.shipper}}</span>
                                  </template>
                                </el-table-column>

                                <el-table-column min-width="100px" align="center" :label="$t('returnApply.returnItemInfo.createDate')">
                                  <template slot-scope="scope">
                                    <span>{{scope.row.createDate}}</span>
                                  </template>
                                </el-table-column>

                              </el-table>

                            <el-table :data="orderItems"  border fit highlight-current-row  :header-cell-style="{background:'#ededed',color:'#666'}" style=" margin-bottom: 20px;">
                              <el-table-column min-width="100px" align="center" :label="$t('returnApply.orderItemInfo.sn')">
                                <template slot-scope="scope">
                                  <span>{{scope.row.sn}}</span>
                                </template>
                              </el-table-column>

                              <el-table-column min-width="180px" align="center" :label="$t('returnApply.orderItemInfo.fullName')">
                                <template slot-scope="scope">
                                  <span>{{scope.row.fullName}}</span>
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

                              <el-table-column min-width="100px" align="center" :label="$t('returnApply.orderItemInfo.returnedQuantity')">
                                <template slot-scope="scope">
                                  <span>{{scope.row.returnedQuantity}}</span>
                                </template>
                              </el-table-column>

                              <el-table-column min-width="100px" align="center" :label="$t('returnApply.orderItemInfo.returnQuantity')">
                                <template slot-scope="scope">
                                  <el-form-item label="" class="postInfo-container-item" prop="returnQuantity">
                                    <el-input-number v-model="scope.row.returnQuantity" controls-position="right"  :min="0" @change="handleChange" style="width:120px" v-if="scope.row.returnQuantity <= 0" disabled></el-input-number>
                                    <el-input-number v-model="scope.row.returnQuantity" controls-position="right"  :min="0" @change="handleChange" style="width:120px" v-else></el-input-number>
                                  </el-form-item>
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
    </div>
  </div>
</template>

<script>
import { fetchView, fetchApproval, fetchReject } from "@/api/returnApply";
import areaCascader from '@/components/cascader/areaCascader'
import Sticky from "@/components/Sticky"; // 粘性header组件
import { goback } from "@/utils/common";
import { getAreaOptions } from '@/api/sys'
import $ from 'jquery';
import { getToken } from '@/utils/auth'

const defaultForm = {
  trackingNo: "", //运单号
  freight: "", //物流费用
  shippingMethodId: undefined, //{ id: undefined }, //配送方式
  deliveryCorpId: undefined, //{ id: undefined },
  shipper: "", //发货人
  zipCode: "", //邮编
  areaId: "", //地区
  address: "", //地址
  phone: "", //电话
  memo: "", //备注
  orderItems: undefined, //退货数量
  rejectContent: "", //驳回原因
  sn: undefined //编号
};
export default {
  name: "tab",
  //components: { paymentInfo },
  components: { Sticky, areaCascader},
  data() {
    return {
      isEdit: false,
      tabMapOptions: [
        { label: "退货申请", key: "ReturnInfo" }
        // { label: "退货详情", key: "shippingItemsInfo" }
      ],
      postForm: Object.assign({}, defaultForm),
      activeName: "ReturnInfo",
      returnApply: null,
      orderItems: [],
      areaOptions: [],
      selectedAreaOptions: [],
      loading: false,
      //表单验证
      rules: {
        trackingNo: [
          { required: true, message: "运单号必填", trigger: "blur" }
        ],
       /* returnQuantity: [
          { required: true, message: "	退货数量必填", trigger: "blur" }
        ],*/
        areaId: [
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
        ]
      }
    };
  },
  created() {
    this.isEdit = this.$route.query && this.$route.query.isEdit;
    this.postForm.sn = this.$route.query && this.$route.query.sn;
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;

      const sn = this.$route.query && this.$route.query.sn;

      fetchView(sn).then(response => {
        this.returnApply = response.data.data;
        this.orderItems = this.returnApply.orderItemInfo;
        //this.postForm.returnsItems.quantity = this.returnApply.orderItemInfo.quantity;

      this.postForm.shipper = this.returnApply.consignee;
      this.postForm.zipCode = this.returnApply.zipCode;
      this.postForm.address = this.returnApply.address;
      this.postForm.phone = this.returnApply.phone;

        const area = returnApply.area;
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
        this.$refs.areaCascaderRef.selectOptions=this.selectedAreaOptions;

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
      //this.postform.sn = this.$route.query && this.$route.query.sn;
      /*var orderItemsObj = {
        "quantity":0
      }*/
      /*for(var i in this.orderItems){

        orderItemsObj.quantity = this.orderItems[i].quantity;
        this.postForm.returnsItems.push(orderItemsObj);
        console.log(this.orderItems[i].quantity)
      }*/

      var validFlag = true;
      for(var p in this.orderItems){
        if(this.orderItems[p].returnQuantity == "" || this.orderItems[p].returnQuantity == undefined){
          validFlag = false;
        }
      }

      if (!validFlag){
        this.$notify({
          title: '失败',
          message: '退货数量必填',
          type: 'error',
          duration: 2000
        })
        return false;
      }

      this.postForm.areaId = this.selectedAreaOptions[this.selectedAreaOptions.length-1]
      this.postForm.orderItems = JSON.stringify(this.orderItems);
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
    handleChangeAddress() {}
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
