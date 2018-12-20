<template>
  <div class="createPost-container tab-container">

    <sticky className="sub-navbar" v-if="invoice">
      <el-button @click="closePage();">返回</el-button>
      <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm()">保存
      </el-button>
    </sticky>
    <el-tabs style='margin-top:15px;' v-model="activeName" type="border-card">
      <!--//发票基本信息-->
      <el-tab-pane :label="tabMapOptions[0].label" :key='tabMapOptions[0].key' :name="tabMapOptions[0].key" v-if="invoice">
        <keep-alive>
            <el-form label-position="right" inline class="order-table-expand" label-width="130px"  v-if='activeName===tabMapOptions[0].key'>
              <el-form-item label="发票抬头：">
                <span>{{invoice.invoiceTitle}}</span>
              </el-form-item>
              <el-form-item label="发票税号：">
                <span>{{invoice.invoiceNo}}</span>
              </el-form-item>
              <el-form-item label="发票金额：">
                <span>{{invoice.amount}}</span>
              </el-form-item>
              <el-form-item label="开具类型：">
                <span>{{invoice.invoiceTypeDesc}}</span>
              </el-form-item>
              <el-form-item label="发票类型：">
                <span>{{invoice.invoiceCategoryDesc}}</span>
              </el-form-item>
              <el-form-item :label="$t('order.createDate')+'：'">
                <span>{{invoice.createDate}}</span>
              </el-form-item>
              <el-form-item label="收货人姓名：">
                <span>{{invoice.consignee}}</span>
              </el-form-item>
              <el-form-item label="收货人电话：">
                <span>{{invoice.phone}}</span>
              </el-form-item>
              <el-form-item label="收货人地址：">
                <span>{{invoice.address}}</span>
              </el-form-item>
              <el-form-item label="邮编：">
                <span>{{invoice.zipCode}}</span>
              </el-form-item>
            
              <el-form-item :label="$t('order.memo')+'：'">
                <span v-if="invoice.memo">{{invoice.memo}}</span>
                <span v-else>无</span>
              </el-form-item>

              <el-form-item label="开票状态：">                
                <el-select v-model="invoiceStatusId" class="filter-item" @change="selectInvoiceStatusChange">
                  <el-option
                    v-for="item in invoiceStatusOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item> 
          </el-form>
        </keep-alive>
      </el-tab-pane>

      <!-- //订单信息-->
      <el-tab-pane :label="tabMapOptions[1].label" :key='tabMapOptions[1].key' :name="tabMapOptions[1].key">
        <keep-alive>

          <el-table :data="orderList" v-if='activeName==tabMapOptions[1].key' :type='tabMapOptions[1].key'  border fit highlight-current-row style="width: 100%">

            <el-table-column min-width="100px" align="center" label="订单编号">
              <template slot-scope="scope">
                <span>{{scope.row.sn}}</span>
              </template>
            </el-table-column>

            <el-table-column width="100px" align="center" label="所属城市">
              <template slot-scope="scope">
                <span>{{scope.row.cityFullName}}</span>
              </template>
            </el-table-column>

            <el-table-column width="100px" align="center" label="缴费类型">
              <template slot-scope="scope">
                <span>{{scope.row.feeCategoryName}}</span>
              </template>
            </el-table-column>

            <el-table-column width="100px" align="center" label="缴费方式">
              <template slot-scope="scope">
                <span>{{scope.row.payCategoryName}}</span>
              </template>
            </el-table-column>

            <el-table-column min-width="120px" align="center" label="缴费期间">
              <template slot-scope="scope">
                <span>{{scope.row.startDate}} 至 {{scope.row.endDate}}</span>
              </template>
            </el-table-column>

            <el-table-column width="80px" align="center" label="服务费">
              <template slot-scope="scope">
                <span>{{scope.row.fee}}</span>
              </template>
            </el-table-column>

            <el-table-column width="120px" align="center" label="用户名">
              <template slot-scope="scope">
                <span>{{scope.row.userName}}</span>
              </template>
            </el-table-column>

            <el-table-column width="100px" align="center" label="电话号码">
              <template slot-scope="scope">
                <span>{{scope.row.mobile}}</span>
              </template>
            </el-table-column>
          </el-table>
        </keep-alive>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>
import { fetchView, fetchUpdate } from "@/api/invoice";
import Sticky from '@/components/Sticky' // 粘性header组件
import { goback } from '@/utils/common'
import $ from 'jquery';
import { getToken } from '@/utils/auth'

export default {
  name: "invoiceTab",
  components: { Sticky},
  data() {
    return {
      tabMapOptions: [
        { label: "发票基本信息", key: "Invoice" },
        { label: "订单信息", key: "Orders" }
      ],
      invoiceStatusOptions: [{
        value: 0,
        label: '未开票'
      }, {
        value: 1,
        label: '已开票'
      }, {
        value: 2,
        label: '已邮寄'
      }],
      id: undefined,
      activeName: "Invoice",
      invoice: null,
      invoiceStatusId: undefined,
      loading: false
    };
  },
  created() {
    this.id = this.$route.params && this.$route.params.id;
    this.getDetail();
  },
  methods: {
    getDetail() {
      fetchView(this.id).then(response => {
        this.invoice = response.data.data;
        this.invoiceStatusOptions = response.data.data.invoiceStatusOptions;
        this.orderList = response.data.data.orders;
        this.invoiceStatusId = response.data.data.invoiceStatusId;
        this.loading = false;
      });
    },
    //确认
    submitForm() {
      fetchUpdate({orderInvoiceId:this.id, invoiceStatusId:this.invoiceStatusId}).then(response => {
        if(response.data.result==='00000000'){
          this.$notify({
            title: '成功',
            message: '发票状态更新完成',
            type: 'success',
            duration: 2000
          })
        } else {
          this.$notify({
            title: '发票状态更新失败',
            message: response.data.msg,
            type: 'error',
            duration: 2000
          })
        }
        this.closePage();
      })
    },
     selectInvoiceStatusChange(val){
      this.invoiceStatusId = val
    },
    closePage() {
      goback(this.$route.path);
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
