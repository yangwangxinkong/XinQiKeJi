<template>
  <div class="tab-container">
    <sticky className="sub-navbar">
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

          <el-form v-if='activeName==tabMapOptions[0].key && returns' highlight-current-row style="border:none; width: 100%" :type='tabMapOptions[0].key' label-position="left" inline class="order-table-expand" label-width="100px">
            <el-form-item :label="$t('returns.returnsSn')+'：'">
              <span>{{returns.sn}}</span>
            </el-form-item>

            <el-form-item :label="$t('returns.createDate')+'：'">
              <span>{{returns.createDate}}</span>
            </el-form-item>

            <el-form-item :label="$t('returns.shippingMethod')+'：'">
              <span>{{returns.shippingMethod}}</span>
            </el-form-item>

            <el-form-item :label="$t('returns.deliveryCorp')+'：'">
              <span>{{returns.deliveryCorp}}</span>
            </el-form-item>

            <el-form-item :label="$t('returns.trackingNo')+'：'">
              <span>{{returns.trackingNo}}</span>
            </el-form-item>

            <el-form-item :label="$t('returns.freight')+'：'">
              <span>￥{{returns.freight}}</span>
            </el-form-item>

            <el-form-item :label="$t('returns.consignee')+'：'">
              <span>{{returns.consignee}}</span>
            </el-form-item>

            <el-form-item :label="$t('returns.phone')+'：'">
              <span>{{returns.phone}}</span>
            </el-form-item>

            <el-form-item :label="$t('returns.area')+'：'">
              <span>{{returns.area}}</span>
            </el-form-item>

            <el-form-item :label="$t('returns.address')+'：'">
              <span>{{returns.address}}</span>
            </el-form-item>

            <el-form-item :label="$t('returns.zipCode')+'：'">
              <span>{{returns.zipCode}}</span>
            </el-form-item>

            <el-form-item :label="$t('returns.orderSn')+'：'">
              <span>{{returns.orderSn}}</span>
            </el-form-item>

            <el-form-item :label="$t('returns.operator')+'：'">
              <span>{{returns.operator}}</span>
            </el-form-item>

            <el-form-item :label="$t('returns.storeName')+'：'">
              <span>{{returns.storeName}}</span>
            </el-form-item>

            <el-form-item :label="$t('returns.memo')+'：'">
              <span>{{returns.memo}}</span>
            </el-form-item>

          </el-form>

        </keep-alive>
      </el-tab-pane>

      <!--//发货信息-->
      <el-tab-pane :label="tabMapOptions[1].label" :key='tabMapOptions[1].key' :name="tabMapOptions[1].key">
        <keep-alive>

          <el-table :data="returnsItemsInfo" v-if='activeName==tabMapOptions[1].key' :type='tabMapOptions[1].key' border fit highlight-current-row style="width: 100%">

            <el-table-column min-width="100px" align="center" :label="$t('returns.returnItemSn')">
              <template slot-scope="scope">
                <span>{{scope.row.sn}}</span>
              </template>
            </el-table-column>

            <el-table-column min-width="180px" align="center" :label="$t('returns.name')">
              <template slot-scope="scope">
                <span>{{scope.row.name}}</span>
              </template>
            </el-table-column>

            <el-table-column min-width="100px" align="center" :label="$t('returns.returnQuantity')">
              <template slot-scope="scope">
                <span>{{scope.row.quantity}}</span>
              </template>
            </el-table-column>
          </el-table>
        </keep-alive>
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script>
import { fetchView } from '@/api/returns'
import Sticky from '@/components/Sticky' // 粘性header组件
import { goback } from '@/utils/common'

export default {
  name: 'tab',
  //components: { paymentInfo },
  components: { Sticky},
  data() {
    return {
      tabMapOptions: [
        { label: '基本信息', key: 'returnsInfo' },
        { label: '发货项', key: 'returnsItemsInfo' }
      ],
      activeName: 'returnsInfo',
      returns: null,
      returnsItemsInfo: null,
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
        this.returns = response.data.data.returnsInfo;
        this.returnsItemsInfo = response.data.data.returnsItemsInfo;
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

