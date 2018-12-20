<template>
  <div class="app-container">

    <div class="filter-container">
      <el-select v-model="listQuery.invoiceTypeId" clearable placeholder="开具类型" class="filter-item">
        <el-option
          v-for="item in invoiceTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>

      <el-select v-model="listQuery.invoiceCategoryId" clearable placeholder="发票类型" class="filter-item">
        <el-option
          v-for="item in invoiceCategoryOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>

      <el-select v-model="listQuery.invoiceStatusId" clearable placeholder="发票状态" class="filter-item">
        <el-option
          v-for="item in invoiceStatusOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" clearable class="filter-item" placeholder="发票抬头" v-model="listQuery.invoiceTitle">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <!-- <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelte" type="primary" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button> -->
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">{{$t('table.refresh')}}</el-button>
    </div>

    <el-table ref="multipleTable" :data="invoicelist" v-loading="listLoading"  fit highlight-current-row style="width: 100%" @selection-change="handleSelectionChange" @sort-change='sortChange'><!--border-->
      <el-table-column
        type="selection"
        width="35">
      </el-table-column>

      <el-table-column
        prop="invoiceTypeDesc"
        label="开具类型"
        min-width="80px"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="invoiceCategoryDesc"
        label="发票类型"
        min-width="80px"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="invoiceTitle"
        label="发票抬头"
        min-width="120px"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="amount"
        label="发票金额"
        min-width="80px"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="invoiceStatusDesc"
        label="发票状态"
        width="80px"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="consignee"
        label="收货人"
        width="100px"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="createDate"
        :label="$t('order.createDate')"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column align="center" :label="$t('order.actions')" min-width="100px">
        <template slot-scope="scope">
          <router-link :to="'view/'+scope.row.id">
            <el-button type="primary" size="small" icon="el-icon-edit">{{$t('order.handle')}}</el-button>
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
  import { fetchList } from '@/api/invoice'
  import waves from "@/directive/waves"; // 水波纹指令
  export default {
    name: 'invoiceList',
    directives: {
      waves
    },
    data() {
    return {
      invoiceTypeOptions: [{
        value: '0',
        label: '企业单位'
      }, {
        value: '1',
        label: '个人'
      }],
      invoiceCategoryOptions: [{
        value: '0',
        label: '增值税普通发票'
      }, {
        value: '1',
        label: '部分增值税专票支付'
      }],      
      invoiceStatusOptions: [{
        value: '0',
        label: '未开票'
      }, {
        value: '1',
        label: '已开票'
      }, {
        value: '2',
        label: '已邮寄'
      }],
      value:'',
      invoicelist: [],
      total: 0,
      listLoading: true,
      multipleSelection: [],
      listQuery: {
        pageNumber: 1,
        pageSize: 10,
        invoiceTypeId: undefined,
        invoiceCategoryId: undefined,
        invoiceStatusId: undefined,
        invoiceTitle: undefined,
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
      this.listLoading = true;
      fetchList(this.listQuery).then(response => {
        this.invoicelist = response.data.list;
        this.total = response.data.total;

        setTimeout(() => {
            this.listLoading = false
          }, 1.0 * 1000)
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
