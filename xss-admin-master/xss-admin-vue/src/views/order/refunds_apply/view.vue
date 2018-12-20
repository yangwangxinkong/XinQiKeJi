<template>
  <div class="tab-container">
    <el-form class="form-container">
      <sticky className="sub-navbar">
        <el-button type="success" @click="approval();" v-if="isEdit">{{$t('table.approval')}}</el-button>
        <el-button type="warning" @click="reject();" v-if="isEdit">{{$t('table.reject')}}</el-button>
        <el-button @click="closePage();">返回</el-button>
      </sticky>

      <el-tabs style='margin-top:15px;' v-model="activeName" type="border-card">
        <!--<el-tab-pane v-for="item in tabMapOptions" :label="item.label" :key='item.key' :name="item.key">
          <keep-alive>
            <order-info v-if='activeName==item.key' :type='item.key' @create='showCreatedTimes'></order-info>
          </keep-alive>
        </el-tab-pane>-->

        <!--//订单基本信息-->
        <el-tab-pane :label="tabMapOptions[0].label" :key='tabMapOptions[0].key' :name="tabMapOptions[0].key">
          <keep-alive>
            <el-form  v-if='activeName==tabMapOptions[0].key && refundsApply' label-position="left" inline class="order-table-expand" label-width="120px" :type='tabMapOptions[0].key' >
              <el-form-item :label="$t('refundsApply.orderSn')+'：'">
                <span>{{refundsApply.sn}}</span>
              </el-form-item>

              <el-form-item :label="$t('refundsApply.orderStatus')+'：'">
                <span>{{refundsApply.orderStatus}}</span>
              </el-form-item>

              <el-form-item :label="$t('refundsApply.paymentMethod')+'：'">
                <span>{{refundsApply.paymentMethod}}</span>
              </el-form-item>

              <el-form-item :label="$t('refundsApply.amount')+'：'">
                <span>{{refundsApply.amount}}</span>
              </el-form-item>

              <el-form-item :label="$t('refundsApply.userName')+'：'">
                <span>{{refundsApply.userName}}</span>
              </el-form-item>

              <el-form-item :label="$t('refundsApply.createDate2')+'：'">
                <span>{{refundsApply.createDate}}</span>
              </el-form-item>

              <el-form-item :label="$t('refundsApply.mobile')+'：'">
                <span>{{refundsApply.mobile}}</span>
              </el-form-item>

              <el-form-item :label="$t('refundsApply.shippingMethod')+'：'">
                <span>{{refundsApply.shippingMethod}}</span>
              </el-form-item>

              <el-form-item :label="$t('refundsApply.content')+'：'">
                <span>{{refundsApply.content}}</span>
              </el-form-item>

             <!-- <el-form-item :label="$t('refundsApply.rejectContent')+'：'">
                <el-input type="textarea" v-model="refundsApply.rejectContent"></el-input>
              </el-form-item>-->

              <div class="reject-box">
                <el-form-item  style="width: 80%; margin-top: 20px;"  :label="$t('refundsApply.rejectContent')+'：'" label-position="top" labelWidth="120px" class="postInfo-container-item" prop="rejectContent">
                  <el-input  style='width: 500px;' type="textarea" :rows="10" placeholder="请输入驳回原因"  v-model="refundsApply.rejectContent" ></el-input>
                </el-form-item>
              </div>

            </el-form>
          </keep-alive>
        </el-tab-pane>

        <!--//发货信息-->
        <el-tab-pane :label="tabMapOptions[1].label" :key='tabMapOptions[1].key' :name="tabMapOptions[1].key">
          <keep-alive>

            <el-table :data="refundsItemInfo" v-if='activeName==tabMapOptions[1].key' :type='tabMapOptions[1].key' border fit highlight-current-row style="width: 100%">

              <el-table-column min-width="100px" align="center" :label="$t('refundsApply.sn')">
                <template slot-scope="scope">
                  <span>{{scope.row.sn}}</span>
                </template>
              </el-table-column>

              <el-table-column min-width="180px" align="center" :label="$t('refundsApply.fullName')">
                <template slot-scope="scope">
                  <span>{{scope.row.fullName}}</span>
                </template>
              </el-table-column>

              <el-table-column min-width="100px" align="center" :label="$t('refundsApply.storeName')">
                <template slot-scope="scope">
                  <span>{{scope.row.storeName}}</span>
                </template>
              </el-table-column>

              <el-table-column min-width="100px" align="center" :label="$t('refundsApply.shippedQuantity')">
                <template slot-scope="scope">
                  <span>{{scope.row.shippedQuantity}}</span>
                </template>
              </el-table-column>

              <el-table-column min-width="100px" align="center" :label="$t('refundsApply.returnQuantity')">
                <template slot-scope="scope">
                  <span>{{scope.row.returnQuantity}}</span>
                </template>
              </el-table-column>

              <el-table-column min-width="100px" align="center" :label="$t('refundsApply.subtotal')">
                <template slot-scope="scope">
                  <span>{{scope.row.subtotal}}</span>
                </template>
              </el-table-column>

            </el-table>
          </keep-alive>
        </el-tab-pane>

      </el-tabs>
    </el-form>
  </div>
</template>

<script>
import { fetchView, fetchApproval, fetchReject } from '@/api/refundsApply'
import Sticky from '@/components/Sticky' // 粘性header组件
import { goback } from '@/utils/common'

export default {
  name: 'tab',
  //components: { paymentInfo },
  components: { Sticky},
  data() {
    return {
      tabMapOptions: [
        { label: '退款单审核', key: 'refundsApply' },
        { label: '商品信息', key: 'refundsItemInfo' }
      ],
      activeName: 'refundsApply',
      isEdit: false,
      refundsApply: null,
      refundsItemInfo: null,
      loading: false
    }
  },
  created() {
    this.isEdit = this.$route.query && this.$route.query.isEdit;
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true

      const sn = this.$route.query && this.$route.query.sn;

      fetchView(sn).then(response => {
        this.refundsApply = response.data.data;
        this.refundsItemInfo = response.data.data.refundsItemInfo;
        this.loading = false
      })
    },
    approval() {//通过

        this.$confirm('通过后无法恢复, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
              this.loading = true

              const sn = this.$route.query && this.$route.query.sn;

              fetchApproval(sn).then(response => {
                var isSuc=response.data.msg=='success';
                if(isSuc){
                  this.showMsg('成功','提交成功','success');
                  goback(this.$route.fullPath);
                  //this.$router.push({path:"/order/refunds_apply/list"})
                  //goback(this.$route.path + "?sn=" + this.$route.query.sn + "&isEdit=" + this.$route.query.isEdit);
                }else{
                  this.showMsg('失败',response.data.msg,'error');
                }
              this.loading = false
            })
      })
    },
    reject() {//拒绝
        this.$confirm('拒绝后无法恢复, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
              this.loading = true

              const sn = this.$route.query && this.$route.query.sn;

              fetchReject({
                sn: sn,
                strRejectContent: this.refundsApply.rejectContent
              }).then(response => {
                var isSuc=response.data.msg=='success';
                if(isSuc){
                  this.showMsg('成功','提交成功','success');
                  goback(this.$route.fullPath);
                  //this.$router.push({path:"/order/refunds_apply/list"})
                  //goback(this.$route.path + "?sn=" + this.$route.query.sn + "&isEdit=" + this.$route.query.isEdit);
                }else{
                  this.showMsg('失败',response.data.msg,'error');
                }
                this.loading = false
              this.loading = false
            })
      })
    },
    showMsg(title,msg,type) {//显示消息 返回商品列表
      this.$notify({title: title, message: msg,type: type, duration: 2000 });
      this.loading = false;
    },
    closePage() {//返回列表
      goback(this.$route.fullPath);
      //goback(this.$route.path);
      //goback(this.$route.path + "?sn=" + this.$route.query.sn + "&isEdit=" + this.$route.query.isEdit);
      // this.$router.push({
      //   path: "/member/memberRank_list"
      // });
    }
  }
}
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
  label{
    width: 90px;
    color:#393939;
    font-weight: normal
  }
  }
  }
</style>
