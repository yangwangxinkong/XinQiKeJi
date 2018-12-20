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

          <el-form v-if='activeName==tabMapOptions[0].key && change' highlight-current-row style="border:none; width: 100%" :type='tabMapOptions[0].key' label-position="left" inline class="order-table-expand" label-width="100px">
            <el-form-item :label="$t('change.changeInfo.changeSn')+'：'">
              <span>{{change.sn}}</span>
            </el-form-item>

            <el-form-item :label="$t('change.changeInfo.createDate')+'：'">
              <span>{{change.createDate}}</span>
            </el-form-item>

            <el-form-item :label="$t('change.changeInfo.orderSn')+'：'">
              <span>{{change.orderSn}}</span>
            </el-form-item>

            <el-form-item :label="$t('change.changeInfo.buyerZipCode')+'：'">
              <span>{{change.buyerZipCode}}</span>
            </el-form-item>

            <el-form-item :label="$t('change.changeInfo.buyerShippingMethod')+'：'">
              <span>{{change.buyerShippingMethod}}</span>
            </el-form-item>

            <el-form-item :label="$t('change.changeInfo.buyerDeliveryCorp')+'：'">
              <span>{{change.buyerDeliveryCorp}}</span>
            </el-form-item>

            <el-form-item :label="$t('change.changeInfo.buyerTrackingNo')+'：'">
              <span>{{change.buyerTrackingNo}}</span>
            </el-form-item>

            <el-form-item :label="$t('change.changeInfo.buyerFreight')+'：'">
              <span>{{change.buyerFreight}}</span>
            </el-form-item>

            <el-form-item :label="$t('change.changeInfo.buyerShipper')+'：'">
              <span>{{change.buyerShipper}}</span>
            </el-form-item>

            <el-form-item :label="$t('change.changeInfo.buyerPhone')+'：'">
              <span>{{change.buyerPhone}}</span>
            </el-form-item>

            <el-form-item :label="$t('change.changeInfo.buyerArea')+'：'">
              <span>{{change.buyerArea}}</span>
            </el-form-item>

            <el-form-item :label="$t('change.changeInfo.buyerAddress')+'：'">
              <span>{{change.buyerAddress}}</span>
            </el-form-item>

            <el-form-item :label="$t('change.changeInfo.operator')+'：'">
              <span>{{change.operator}}</span>
            </el-form-item>

            <el-form-item :label="$t('change.changeInfo.memo')+'：'">
              <span>{{change.memo}}</span>
            </el-form-item>

          </el-form>

        </keep-alive>
      </el-tab-pane>

      <!--//发货信息-->
      <el-tab-pane :label="tabMapOptions[1].label" :key='tabMapOptions[1].key' :name="tabMapOptions[1].key">
        <keep-alive>

          <el-form v-if='activeName==tabMapOptions[1].key && change' highlight-current-row style="border:none; width: 100%" :type='tabMapOptions[1].key' label-position="left" inline class="order-table-expand" label-width="100px">
            <el-form-item :label="$t('change.changeInfo.changeItemSn')+'：'" style="width: 30%">
              <span>{{change.changeItemSn}}</span>
            </el-form-item>
            <el-form-item :label="$t('change.changeInfo.name')+'：'" style="width: 30%">
              <span>{{change.name}}</span>
            </el-form-item>
            <el-form-item :label="$t('change.changeInfo.changeQuantity')+'：'" style="width: 30%">
              <span>{{change.changeQuantity}}</span>
            </el-form-item>
          </el-form>
        </keep-alive>
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script>
import { fetchView } from '@/api/change'
import Sticky from '@/components/Sticky' // 粘性header组件
import { goback } from '@/utils/common'

export default {
  name: 'tab',
  //components: { paymentInfo },
  components: { Sticky},
  data() {
    return {
      tabMapOptions: [
        { label: '换货详情', key: 'changeInfo' },
        { label: '换货商品', key: 'changeItemsInfo' }
      ],
      activeName: 'changeInfo',
      change: null,
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
        this.change = response.data.data.changeInfo;
        this.changeItemsInfo = response.data.data.changeItemsInfo;
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
