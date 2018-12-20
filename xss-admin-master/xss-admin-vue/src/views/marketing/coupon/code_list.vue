<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">刷新</el-button>
    </div>

    <el-table ref="multipleTable" :data="list" v-loading="listLoading" fit tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange" @sort-change='sortChange'>

      <el-table-column
        prop="coupon.name"
        label="优惠券"
        width="200"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="code"
        label="优惠码"
        width="400"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="isUsed"
        label="已使用"
        width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.isUsed">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>

      <el-table-column
        prop="member.mobile"
        label="领取用户"
        width="110"
        show-overflow-tooltip>
        <template slot-scope="scope">
          <router-link :to="'/member/edit/'+scope.row.member.id">
            {{scope.row.member.mobile}}
          </router-link>
        </template>
      </el-table-column>

      <el-table-column
        prop="order.sn"
        label="使用订单编码"
        show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.order">
            <router-link :to="'/order/order/view/'+scope.row.order.id">
              {{scope.row.order.sn}}
            </router-link>
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>

      <el-table-column
        prop="createDate"
        label="创建日期"
        show-overflow-tooltip>
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
  import url from '@/api/apiUrl.js'
  import { get,post,execute } from '@/api/server.js'
  import { parseTime } from '@/utils'
  export default {
    name: 'couponCodeTable',
    directives: {
      waves
    },
    data() {
      return {
        list: [],
        total: 0,
        listLoading: true,
        multipleSelection: [],
        listQuery: {
          pageNumber: 1,
          pageSize: 10,
          couponId: this.$route.params.id
        }
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = true
        get(url.couponCodeList, this.listQuery).then(response => {
          this.list = response.data.list
          this.total = response.data.total
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
