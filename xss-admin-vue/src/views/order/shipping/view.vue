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
          <el-form v-if='activeName==tabMapOptions[0].key && shipping' highlight-current-row style="border:none; width: 100%" :type='tabMapOptions[0].key' label-position="left" inline class="order-table-expand" label-width="100px">
              <el-form-item :label="$t('shipping.shippingSn')+'：'">
                <span>{{shipping.sn}}</span>
              </el-form-item>

              <el-form-item :label="$t('shipping.createDate')+'：'">
                <span>{{shipping.createDate}}</span>
              </el-form-item>

              <el-form-item :label="$t('shipping.shippingMethod')+'：'">
                <span>{{shipping.shippingMethod}}</span>
              </el-form-item>

              <el-form-item :label="$t('shipping.deliveryCorp')+'：'">
                <span>{{shipping.deliveryCorp}}</span>
              </el-form-item>

              <el-form-item :label="$t('shipping.trackingNo')+'：'">
                <span>{{shipping.trackingNo}}</span>
              </el-form-item>

              <el-form-item :label="$t('shipping.freight')+'：'">
                <span>{{shipping.freight}}</span>
              </el-form-item>

              <el-form-item :label="$t('shipping.consignee')+'：'">
                <span>{{shipping.consignee}}</span>
              </el-form-item>

              <el-form-item :label="$t('shipping.phone')+'：'">
                <span>{{shipping.phone}}</span>
              </el-form-item>

              <el-form-item :label="$t('shipping.area')+'：'">
                <span>{{shipping.area}}</span>
              </el-form-item>

              <el-form-item :label="$t('shipping.address')+'：'">
                <span>{{shipping.address}}</span>
              </el-form-item>

              <el-form-item :label="$t('shipping.zipCode')+'：'">
                <span>{{shipping.zipCode}}</span>
              </el-form-item>

              <el-form-item :label="$t('shipping.orderSn')+'：'">
                <span>{{shipping.orderSn}}</span>
              </el-form-item>

              <el-form-item :label="$t('shipping.operator')+'：'">
                <span>{{shipping.operator}}</span>
              </el-form-item>

              <el-form-item :label="$t('shipping.storeName')+'：'">
                <span>{{shipping.storeName}}</span>
              </el-form-item>

              <el-form-item :label="$t('shipping.memo')+'：'">
                <span>{{shipping.memo}}</span>
              </el-form-item>

            </el-form>
        </keep-alive>
      </el-tab-pane>

      <!--//发货信息-->
      <el-tab-pane :label="tabMapOptions[1].label" :key='tabMapOptions[1].key' :name="tabMapOptions[1].key">
        <keep-alive>

          <el-table :data="shippingItemsInfo" v-if='activeName==tabMapOptions[1].key' :type='tabMapOptions[1].key' border fit highlight-current-row style="width: 100%">

            <el-table-column min-width="100px" align="center" :label="$t('shipping.shippingSn')">
              <template slot-scope="scope">
                <span>{{scope.row.sn}}</span>
              </template>
            </el-table-column>

            <el-table-column min-width="180px" align="center" :label="$t('shipping.name')">
              <template slot-scope="scope">
                <span>{{scope.row.name}}</span>
              </template>
            </el-table-column>

            <el-table-column min-width="100px" align="center" :label="$t('shipping.quantity')">
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
import { fetchView } from '@/api/shipping'
import Sticky from '@/components/Sticky' // 粘性header组件
import { goback } from '@/utils/common'

export default {
  name: 'tab',
  //components: { paymentInfo },
  components: { Sticky},
  data() {
    return {
      tabMapOptions: [
        { label: '基本信息', key: 'ShippingInfo' },
        { label: '发货项', key: 'shippingItemsInfo' }
      ],
      activeName: 'ShippingInfo',
      shipping: null,
      shippingItemsInfo: null,
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
        this.shipping = response.data.data.shippingInfo;
        this.shippingItemsInfo = response.data.data.shippingItemsInfo;
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
