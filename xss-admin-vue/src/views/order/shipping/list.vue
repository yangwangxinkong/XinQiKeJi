<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="$t('shipping.shippingSn')" v-model="listQuery.sn">
      </el-input>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="$t('shipping.trackingNo')" v-model="listQuery.trackingNo">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelte" type="primary" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">{{$t('table.refresh')}}</el-button>
    </div>

    <el-table :data="shippingList" ref="multipleTable"  v-loading="listLoading" fit highlight-current-row style="width: 100%" @selection-change="handleSelectionChange" @sort-change='sortChange'>
      <el-table-column
        type="selection"
        width="35">
      </el-table-column>

      <el-table-column min-width="100px" align="center" :label="$t('shipping.shippingSn')" prop="sn" sortable="sn" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{scope.row.sn}}</span>
        </template>
      </el-table-column>

      <el-table-column width="80px" align="center" :label="$t('shipping.shippingMethod')">
        <template slot-scope="scope">
          <span>{{scope.row.shippingMethod}}</span>
        </template>
      </el-table-column>

      <el-table-column width="80px" align="center" :label="$t('shipping.deliveryCorp')">
        <template slot-scope="scope">
          <span>{{scope.row.deliveryCorp}}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" align="center" :label="$t('shipping.trackingNo')">
        <template slot-scope="scope">
          <span>{{scope.row.trackingNo}}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" align="center" :label="$t('shipping.consignee')">
        <template slot-scope="scope">
          <span>{{scope.row.consignee}}</span>
        </template>
      </el-table-column>

      <el-table-column min-width="100px" align="center" :label="$t('shipping.orderSn')">
        <template slot-scope="scope">
          <span>{{scope.row.orderSn}}</span>
        </template>
      </el-table-column>

      <el-table-column min-width="120px" align="center" :label="$t('shipping.storeName')">
        <template slot-scope="scope">
          <span>{{scope.row.storeName}}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" align="center" :label="$t('shipping.createDate')">
        <template slot-scope="scope">
          <span>{{scope.row.createDate}}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('shipping.actions')" min-width="100px">
        <template slot-scope="scope">
          <!--<router-link :to="'view/'+scope.row.id">-->
          <router-link :to="'view/'+scope.row.id">
            <el-button type="primary" size="small" icon="el-icon-edit">{{$t('shipping.handle')}}</el-button>
          </router-link>
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
  import { fetchList, remove } from '@/api/shipping'
  import waves from "@/directive/waves"; // 水波纹指令

  export default {
    name: 'shippingList',
    directives: {
      waves
    },
    data() {
    return {
      shippingList: null,
      total: 0,
      listLoading: true,
      multipleSelection: [],
      listQuery: {
        pageNumber: 1,
        pageSize: 10,
        sn: undefined,
        trackingNo: undefined,
        searchProperty: undefined,
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
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.shippingList = response.data.list;
        this.total = response.data.total;
        setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
      //console.log(response);
      //console.log(this.paymentList);
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
      var ids = this.multipleSelection.map(item => item.id).join();
      console.log(ids);
      remove({ids:ids}).then(response => {
        if(response.data.result==='00000000'){
        this.$notify({
          title: '成功',
          message: '删除发货成功',
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
    console.log(err);
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
