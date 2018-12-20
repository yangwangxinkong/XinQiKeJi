<template>
  <div class="tab-container">
    <sticky className="sub-navbar">
      <el-button @click="closePage();">返回</el-button>
    </sticky>
      <!--//订单基本信息-->
            <el-form label-position="left" inline class="order-table-expand" label-width="100px"  v-if="payment">
              <el-form-item :label="$t('payment.sn')+'：'">
                <span>{{payment.sn}}</span>
              </el-form-item>
              <el-form-item :label="$t('payment.createDate')+'：'">
                <span>{{payment.createDate}}</span>
              </el-form-item>
            <el-form-item :label="$t('payment.status')+'：'">
                <span>{{payment.statusDesc}}</span>
              </el-form-item>
              <el-form-item :label="$t('payment.paymentDate')+'：'">
                <span>{{payment.paymentDate}}</span>
              </el-form-item>
              <el-form-item :label="$t('payment.type')+'：'">
                <span>{{payment.typeDesc}}</span>
              </el-form-item>
               <el-form-item :label="$t('payment.method')+'：'">
                <span>{{payment.methodDesc}}</span>
              </el-form-item>
               <el-form-item :label="$t('payment.paymentMethod')+'：'">
                <span>{{payment.paymentMethod}}</span>
              </el-form-item>
              <el-form-item :label="$t('payment.operator')+'：'">
                <span>{{payment.operator}}</span>
              </el-form-item>
               <el-form-item :label="$t('payment.bank')+'：'">
                <span>{{payment.bank}}</span>
              </el-form-item>
              <el-form-item :label="$t('payment.account')+'：'">
                <span>{{payment.account}}</span>
              </el-form-item>
              <el-form-item :label="$t('payment.payer')+'：'">
                <span>{{payment.payer}}</span>
              </el-form-item>
               <el-form-item :label="$t('payment.amount')+'：'">
                <span v-if="payment.fee > 0">{{payment.amount}}元 (包含服务费:{{payment.fee}}元)</span>
                <span v-else>{{payment.amount}}元</span>
              </el-form-item>
               <el-form-item :label="$t('payment.username')+'：'">
                <span>{{payment.username}}</span>
              </el-form-item>
               <el-form-item :label="$t('payment.orderSn')+'：'">
                <span>{{payment.orderSn}}</span>
              </el-form-item>
              <!-- <el-form-item :label="$t('payment.storeName')+'：'">
                <span>{{payment.storeName}}</span>
              </el-form-item> -->
               <el-form-item :label="$t('payment.memo')+'：'">
                 <span>{{payment.memo}}</span>
              </el-form-item>
          </el-form>
  </div>
</template>

<script>
import { fetchView } from '@/api/payment'
import Sticky from '@/components/Sticky' // 粘性header组件
import { goback } from '@/utils/common'

export default {
  name: 'tab',
  //components: { paymentInfo },
  components: { Sticky},
  data() {
    return {
      payment: null,
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
        this.payment = response.data.data;
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
