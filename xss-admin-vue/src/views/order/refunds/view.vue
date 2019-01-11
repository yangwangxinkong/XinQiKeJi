<template>
  <div class="tab-container">

    <sticky className="sub-navbar">
      <el-button @click="closePage();">返回</el-button>
    </sticky>

      <!--//订单基本信息-->

    <el-form label-position="left" inline class="order-table-expand" label-width="100px"  v-if="refunds">
      <el-form-item :label="$t('refunds.sn')+'：'">
        <span>{{refunds.sn}}</span>
      </el-form-item>

      <el-form-item :label="$t('refunds.createDate')+'：'">
        <span>{{refunds.createDate}}</span>
      </el-form-item>

      <el-form-item :label="$t('refunds.method')+'：'">
        <span>{{$t('refunds.methods.'+refunds.method)}}</span>
      </el-form-item>

      <el-form-item :label="$t('refunds.paymentMethod')+'：'">
        <span>{{refunds.paymentMethod}}</span>
      </el-form-item>

      <el-form-item :label="$t('refunds.bank')+'：'">
        <span>{{refunds.bank}}</span>
      </el-form-item>

      <el-form-item :label="$t('refunds.account')+'：'">
        <span>{{refunds.account}}</span>
      </el-form-item>

      <el-form-item :label="$t('refunds.payee')+'：'">
        <span>{{refunds.payee}}</span>
      </el-form-item>

      <el-form-item :label="$t('refunds.amount')+'：'">
        <span>￥{{refunds.amount}}</span>
      </el-form-item>

      <el-form-item :label="$t('refunds.orderSn')+'：'">
        <span>{{refunds.orderSn}}</span>
      </el-form-item>

      <el-form-item :label="$t('refunds.operator')+'：'">
        <span>{{refunds.operator}}</span>
      </el-form-item>

      <!--<el-form-item :label="$t('order.sn')+'：'">
        <span>{{refunds.storeSn}}</span>
      </el-form-item>-->

      <el-form-item :label="$t('refunds.storeName')+'：'">
        <span>{{refunds.storeName}}</span>
      </el-form-item>

      <el-form-item :label="$t('refunds.memo')+'：'">
        <span>{{refunds.memo}}</span>
      </el-form-item>

    </el-form>

  </div>
</template>

<script>
import { fetchView } from '@/api/refunds'
import Sticky from '@/components/Sticky' // 粘性header组件
import { goback } from '@/utils/common'

export default {
  name: 'tab',
  //components: { paymentInfo },
  components: { Sticky},
  data() {
    return {
      refunds: null,
      loading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true

      const id = this.$route.params && this.$route.params.id;

      fetchView(id).then(response => {
        this.refunds = response.data.data;
        this.loading = false
      })
    },
    closePage() {//返回列表
      goback(this.$route.path);
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
