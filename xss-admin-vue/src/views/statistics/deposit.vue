<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" @click="handleRefresh" type="primary" icon="el-icon-refresh">刷新</el-button>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px; margin-left: 10px" class="filter-item" :placeholder="'操作员'" v-model="listQuery.searchValue">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
    </div>

    <el-table ref="multipleTable" :data="list" v-loading="listLoading" fit tooltip-effect="dark" style="width: 100%" @sort-change="sort" @selection-change="handleSelectionChange">

      <el-table-column
        prop="typeDesc"
        label="类型"
        sortable
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="credit"
        label="收入金额"
        sortable
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="debit"
        label="支出金额"
        sortable
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="balance"
        label="当前余额"
        sortable
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="operator"
        label="操作员"
        sortable
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="member"
        label="会员"
        sortable
        show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.member!==undefined && scope.row.member.username!==undefined">{{scope.row.member.username}}</span>
          <span v-else> - </span>
        </template>
      </el-table-column>

      <el-table-column
        prop="order"
        label="订单"
        sortable
        show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.order!==undefined && scope.row.order.sn!==undefined">{{scope.row.order.sn}}</span>
        </template>
      </el-table-column>

      <el-table-column
        prop="payment"
        label="收款单"
        sortable
        show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.payment!==undefined && scope.row.payment.sn!==undefined">{{scope.row.payment.sn}}</span>
          <span v-else> - </span>
        </template>
      </el-table-column>

      <el-table-column
        prop="memo"
        label="备注"
        width="160"
        sortable
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="createDate"
        label="创建日期"
        sortable
        width="115"
        show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{scope.row.createDate | parseTime('{y}-{m}-{d} {h}:{i}:{s}')}}</span>
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
  import waves from '@/directive/waves' // 水波纹指令
  import { fetchList } from '@/api/deposit'

  export default {
    name: 'depositTable',
    directives: {
      waves
    },
    data() {
      return {
        list: [],
        total: 0,
        listLoading: true,
        member: undefined,
        listQuery: {
          pageNumber: 1,
          pageSize: 10,
          memberId: undefined,
          searchProperty: 'operator',
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
          this.list = response.data.data.page.list
          this.total = response.data.data.page.total
          this.member = response.data.data.member
          console.log(JSON.stringify(this.list))

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

      // 排序
      sort(sort){
        if(sort && sort.column){
          if(sort.column.order === 'descending'){
            this.listQuery.orderDirection = 'desc'
          }else if(sort.column.order === 'ascending'){
            this.listQuery.orderDirection = 'asc'
          }

          if(sort.column.property === 'typeDesc'){
            this.listQuery.orderProperty = 'type'
          }else {
            this.listQuery.orderProperty = sort.column.property
          }
        }else {
          this.listQuery.orderProperty = undefined
          this.listQuery.orderDirection = undefined
        }

        this.getList()
      },


    }
  }
</script>
