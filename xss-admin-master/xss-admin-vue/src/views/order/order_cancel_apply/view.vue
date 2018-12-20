<template>
  <div class="tab-container">
    <sticky className="sub-navbar"  :zIndex="9">
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
          <el-form  v-if='activeName==tabMapOptions[0].key && orderCancelApplyInfo' label-position="left" inline class="order-table-expand" label-width="120px" :type='tabMapOptions[0].key' >
            <el-form-item :label="$t('orderCancelApply.orderCancelInfo.sn')+'：'">
              <span>{{orderCancelApplyInfo.sn}}</span>
            </el-form-item>

            <el-form-item :label="$t('orderCancelApply.orderCancelInfo.orderStatus')+'：'">
              <span>{{orderCancelApplyInfo.orderStatus}}</span>
            </el-form-item>

            <el-form-item label="停缴月数：">
              <span>{{orderCancelApplyInfo.monthCount}}个月</span>
            </el-form-item>

            <el-form-item label="退款金额：">
              <el-input style="width:100px" v-model="orderCancelApplyInfo.amount" v-if="isEdit"></el-input>
              <span v-else>{{orderCancelApplyInfo.amount}}</span>元
            </el-form-item>

            <el-form-item label="停缴开始年月：">
              <span>{{orderCancelApplyInfo.startDate}}</span>
            </el-form-item>

            <el-form-item label="停缴结束年月：">
              <span>{{orderCancelApplyInfo.endDate}}</span>
            </el-form-item>

            <el-form-item :label="$t('orderCancelApply.orderCancelInfo.content')+'：'">
              <span>{{orderCancelApplyInfo.content}}</span>
            </el-form-item>

            <!--<el-form-item :label="$t('orderCancelApply.orderCancelInfo.rejectContent')+'：'">
              <el-input type="textarea" readonly="readonly" v-model="orderCancelApplyInfo.rejectContent"></el-input>
            </el-form-item>-->
            <!-- 驳回 -->
            <div class="reject-box">
              <el-form-item  style="width: 80%; margin-top: 20px;"  :label="$t('orderCancelApply.orderCancelInfo.rejectContent')+'：'" label-position="top" labelWidth="120px" class="postInfo-container-item" prop="rejectContent">
                <el-input  style='width: 500px;' type="textarea" :rows="10" placeholder="请输入驳回原因"  v-model="orderCancelApplyInfo.rejectContent" ></el-input>
              </el-form-item>
            </div>

          </el-form>
        </keep-alive>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>
import { fetchView, fetchApproval, fetchReject  } from '@/api/orderCancelApply'
import Sticky from '@/components/Sticky' // 粘性header组件
import { goback } from '@/utils/common'

export default {
  name: 'orderCancelApply',
  //components: { paymentInfo },
  components: { Sticky},
  data() {
    return {
      tabMapOptions: [
        { label: '取消订单审核', key: 'orderCancelInfo' }
      ],
      activeName: 'orderCancelInfo',
      isEdit: false,
      orderCancelApplyInfo: null,
      loading: false
    }
  },
  created() {

    this.isEdit = this.$route.query && this.$route.query.isEdit;

    this.getList()
  },
  methods: {
    getList() {
      this.loading = true;

      const sn = this.$route.query && this.$route.query.sn;

      fetchView(sn).then(response => {
        this.orderCancelApplyInfo = response.data.data;
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

      fetchApproval({sn: sn, amount: this.orderCancelApplyInfo.amount}).then(response => {
        var isSuc=response.data.msg=='success';
      if(isSuc){
        this.showMsg('成功','提交成功','success');
        goback(this.$route.fullPath);
        //this.$router.push({path:"/order/order_cancel_apply/list"})
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
        strRejectContent: this.orderCancelApplyInfo.rejectContent
      }).then(response => {
        var isSuc=response.data.msg=='success';
      if(isSuc){
        this.showMsg('成功','提交成功','success');
        goback(this.$route.fullPath);
        //this.$router.push({path:"/order/order_cancel_apply/list"})
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
      //goback(this.$route.path + "?sn=" + this.$route.query.sn + "&isEdit=" + this.$route.query.isEdit);
      // this.$router.push({
      //   path: "/member/memberRank_list"
      // });
    },

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
