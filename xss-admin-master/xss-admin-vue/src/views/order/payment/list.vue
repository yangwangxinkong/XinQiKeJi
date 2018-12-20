<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="$t('payment.sn')" v-model="listQuery.sn">
      </el-input>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="$t('payment.payer')" v-model="listQuery.payer">
      </el-input>
      <el-select v-model="listQuery.typeId" clearable placeholder="收款类型" class="filter-item">
        <el-option
          v-for="item in typeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelte" type="primary" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">{{$t('table.refresh')}}</el-button>
    </div>

    <el-table :data="paymentList" ref="multipleTable"  v-loading="listLoading"  fit highlight-current-row style="width: 100%" @selection-change="handleSelectionChange" @sort-change='sortChange'>
      <el-table-column
        type="selection"
        width="35">
      </el-table-column>

      <el-table-column min-width="100px" align="center" :label="$t('payment.sn')" prop="sn" sortable="sn" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{scope.row.sn}}</span>
        </template>
      </el-table-column>

      <el-table-column width="80px" align="center" :label="$t('payment.type')">
        <template slot-scope="scope">
          <span>{{scope.row.typeDesc}}</span>
        </template>
      </el-table-column>

      <el-table-column min-width="80px" align="center" :label="$t('payment.method')">
        <template slot-scope="scope">
          <span>{{scope.row.methodDesc}}</span>
        </template>
      </el-table-column>

      <el-table-column width="80px" align="center" :label="$t('payment.paymentMethod')">
        <template slot-scope="scope">
          <span>{{scope.row.paymentMethod}}</span>
        </template>
      </el-table-column>

      <el-table-column width="80px" align="center" :label="$t('payment.amount')">
        <template slot-scope="scope">
          <span>{{scope.row.amount}}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" align="center" :label="$t('payment.username')">
        <template slot-scope="scope">
          <span>{{scope.row.username}}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" align="center" :label="$t('payment.orderSn')">
        <template slot-scope="scope">
          <span>{{scope.row.orderSn}}</span>
        </template>
      </el-table-column>

      <el-table-column width="80px" align="center" :label="$t('payment.status')">
        <template slot-scope="scope">
          <span>{{scope.row.statusDesc}}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" align="center" :label="$t('payment.paymentDate')">
        <template slot-scope="scope">
          <span>{{scope.row.paymentDate}}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" align="center" :label="$t('payment.createDate')">
        <template slot-scope="scope">
          <span>{{scope.row.createDate}}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('payment.actions')" min-width="100px">
        <template slot-scope="scope">
          <!--<router-link :to="'view/'+scope.row.id">-->
          <router-link :to="'view/'+scope.row.id">
            <el-button type="primary" size="small" icon="el-icon-edit">{{$t('payment.handle')}}</el-button>
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
  import { fetchList, remove } from '@/api/payment'
  import waves from "@/directive/waves"; // 水波纹指令

  export default {
    name: 'paymentList',
    directives: {
      waves
    },
    data() {
    return {
      typeOptions: [{
        value: '0',
        label: '订单支付'
      }, {
        value: '1',
        label: '会员充值'
      }],
      paymentList: null,
      total: 0,
      listLoading: true,
      multipleSelection: [],
      listQuery: {
        pageNumber: 1,
        pageSize: 10,
        sn: undefined,
        payer: undefined,
        typeId: undefined,
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
        this.paymentList = response.data.list;
        this.total = response.data.total;
        // setTimeout(() => { }, 1.5 * 1000);
         setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)

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
    remove({ids:ids}).then(response => {
      if(response.data.result==='00000000'){
      this.$notify({
        title: '成功',
        message: '删除付款成功',
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
