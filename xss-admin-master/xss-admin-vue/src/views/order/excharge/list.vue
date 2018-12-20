<template>
  <div class="app-container">

    <div class="filter-container">
      <el-select v-model="listQuery.orderStatusId" clearable :placeholder="$t('order.orderStatusSelect')" class="filter-item">
        <el-option
          v-for="item in orderOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>

      <el-select v-model="listQuery.paymentStatusId" clearable :placeholder="$t('order.paymentStatusSelect')" class="filter-item">
        <el-option
          v-for="item in paymentOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>

      <el-select v-model="listQuery.shippingStatusId" clearable :placeholder="$t('order.shippingStatusSelect')" class="filter-item">
        <el-option
          v-for="item in shippingOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>

      <el-select v-model="listQuery.hasExpired" clearable :placeholder="$t('order.expiredSelect')" class="filter-item">
        <el-option
          v-for="item in expiredOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>

      <!--<el-select v-model="value" placeholder="$t('order.orderSelect')">
        <el-option-group
          v-for="group in options3"
          :key="group.label"
          :label="group.label">
          <el-option
            v-for="item in group.options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-option-group>
      </el-select>-->
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="$t('order.sn')" v-model="listQuery.searchValue">
      </el-input>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="$t('order.memberName')" v-model="listQuery.memberName">
      </el-input>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="$t('order.memberPhone')" v-model="listQuery.memberMobile">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <!--<router-link :to="'/member/add'">
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>
      </router-link>-->
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelte" type="primary" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">{{$t('table.refresh')}}</el-button>
    </div>

    <el-table ref="multipleTable" :data="orderlist" v-loading="listLoading"  fit highlight-current-row style="width: 100%" @selection-change="handleSelectionChange" @sort-change='sortChange'><!--border-->
      <el-table-column
        type="selection"
        width="35">
      </el-table-column>

      <el-table-column
        prop="sn"
        :label="$t('order.sn')"
        min-width="80px"
        sortable="sn">
      </el-table-column>

      <el-table-column
        prop="point"
        label="订单金豆"
        min-width="80px">
      </el-table-column>

      <el-table-column
        prop="consignee"
        :label="$t('order.consignee')"
        width="100px">
      </el-table-column>

      <el-table-column
        prop="phone"
        label="联系电话"
        width="120px">
      </el-table-column>

      <el-table-column
        prop="address"
        label="收货地址">
      </el-table-column>

      <el-table-column
        prop="orderStatus"
        :label="$t('order.orderStatus')"
        width="80px">
      </el-table-column>

      <el-table-column
        prop="createDate"
        :label="$t('order.createDate')"
        width="110px"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column align="center" :label="$t('order.actions')" min-width="170px">
        <template slot-scope="scope">
          <router-link :to="'view/'+scope.row.id">
            <el-button type="primary" size="small" icon="el-icon-edit">{{$t('order.handle')}}</el-button>
          </router-link>

          <!--<router-link :to="'view/'+scope.row.id">-->
            <el-button type="primary" size="small" @click="toEdit(scope.row.id);" icon="el-icon-edit" v-bind:disabled="!(!scope.row.isExpired && scope.row.orderStatusId == 0)">{{$t('order.edit')}}</el-button>
          <!--</router-link>-->
        </template>
      </el-table-column>

    </el-table>

    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNumber"
                     :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
  import { fetchExchargeList, fetchRemove } from '@/api/order'
  import waves from "@/directive/waves"; // 水波纹指令
  export default {
    name: 'orderList',
    directives: {
      waves
    },
    data() {
    return {
      orderOptions: [{
        value: '0',
        label: '未确认'
      }, {
        value: '1',
        label: '已确认'
      }, {
        value: '4',
        label: '已完成'
      }, {
        value: '5',
        label: '已取消'
      }],
      paymentOptions: [{
        value: '0',
        label: '未支付'
      }, {
        value: '1',
        label: '部分支付'
      }, {
        value: '2',
        label: '已支付'
      }, {
        value: '3',
        label: '部分退款'
      }, {
        value: '4',
        label: '已退款'
      }],
      shippingOptions: [{
        value: '0',
        label: '未发货',
      }, {
        value: '1',
        label: '部分发货'
      }, {
        value: '2',
        label: '已发货'
      }, {
        value: '3',
        label: '已收货'
      }, {
        value: '4',
        label: '申请退货'
      }
      // , {
      //   value: '5',
      //   label: '申请换货'
      // }
      , {
        value: '6',
        label: '部分退货'
      }, {
        value: '7',
        label: '已退货'
      }],
      expiredOptions: [{
        value: 'true',
        label: '已过期'
      }, {
        value: 'false',
        label: '未过期'
      }],
      value:'',
      orderlist: [],
      total: 0,
      listLoading: true,
      multipleSelection: [],
      listQuery: {
        pageNumber: 1,
        pageSize: 10,
        orderType: 'service',
        orderStatusId: undefined,
        paymentStatusId: undefined,
        shippingStatusId: undefined,
        hasExpired: undefined,
        memberName: undefined,
        memberMobile: undefined,
        searchProperty: 'sn',
        searchValue: undefined,
        orderProperty: undefined,
        orderDirection: undefined
      }
    }
  },

  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true;
      fetchExchargeList(this.listQuery).then(response => {
        this.orderlist = response.data.data.order;
        this.total = response.data.data.total;

        setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
      //console.log(response);
      //console.log(this.orderlist);
    }).catch((e)=>{
    console.log(e)
     this.listLoading = false
  })
  },
  handleRefresh() {
    this.getList();
  },
  handleFilter() {
    this.listQuery.pageNumber = 1
    this.getList()
  },
  handleDelte() {
    if (!this.multipleSelection.length) {
      this.$message({
        message: '请先选择需要删除的数据',
        type: 'error',
        duration: 2 * 1000
      })
      return
    }
    this.$confirm('删除无法恢复, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      var ids = this.multipleSelection.map(item => item.id).join()
    fetchRemove({ids:ids}).then(response => {
      if(response.data.result==='00000000'){
      this.$notify({
        title: '成功',
        message: '删除订单成功',
        type: 'success',
        duration: 2000
      })
    } else {
      this.$notify({
        title: '失败',
        message: response.data.msg,
        type: 'error',
        duration: 2000
      })
    }
    this.getList()
  })
  }).catch(err => {
    console.log(err)
  })
  },
  handleSelectionChange(val) {
    this.multipleSelection = val;
  },
  handleSizeChange(val) {
    this.listQuery.pageSize = val
    this.getList()
  },
  handleCurrentChange(val) {
    this.listQuery.pageNumber = val
    this.getList()
  },
  sortChange(sort) {
    if(sort && sort.column){
      if(sort.column.order === 'descending'){
        this.listQuery.orderDirection = 'desc'
      }else if(sort.column.order === 'ascending'){
        this.listQuery.orderDirection = 'asc'
      }
      this.listQuery.orderProperty = sort.column.property
    }else {
      this.listQuery.orderProperty = undefined
      this.listQuery.orderDirection = undefined
    }
    this.getList()
  },
  toEdit(id) {//返回商品列表
    //goback(this.$route.path);
     this.$router.push({
       path: "edit/" + id
     });
  }
  }
  }
</script>

<style scoped>
  .edit-input {
    padding-right: 100px;
  }
  .cancel-btn {
    position: absolute;
    right: 15px;
    top: 10px;
  }
</style>
