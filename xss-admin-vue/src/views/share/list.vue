<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input style="width: 200px;" class="filter-item" placeholder="分享者手机" v-model="listQuery.shareMemberMobile">
      </el-input>
      <el-select v-model="listQuery.shareCategory" clearable placeholder="分享类别" class="filter-item">
        <el-option
          v-for="item in shareCategories"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">刷新</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">分享内容设置</el-button>
    </div>

    <el-table ref="multipleTable" :data="list" v-loading="listLoading" fit tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange" @sort-change='sortChange'>
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>

      <el-table-column
        prop="shareMemberMobile"
        label="分享者"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="sharedMemberMobile"
        label="分享对象"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="shareCategoryDesc"
        label="分享类别"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="registerMoney"
        label="注册分佣"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="orderMoney"
        label="订单分佣"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="createDate"
        label="创建日期"
        show-overflow-tooltip>
      </el-table-column>

<!--      <el-table-column align="center" label="操作" width="120">
        <template slot-scope="scope">
          <router-link :to="'/member/edit/'+scope.row.id">
            <el-button type="primary" size="small" icon="el-icon-edit">编辑</el-button>
          </router-link>
        </template>
      </el-table-column>-->

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
    name: 'shareTable',
    directives: {
      waves
    },
    data() {
      return {
        shareCategories: [{
          value: 's0',
          label: '链接分享'
        }, {
          value: 's1',
          label: '二维码分享'
        }],
        list: [],
        total: 0,
        listLoading: true,
        multipleSelection: [],
        listQuery: {
          pageNumber: 1,
          pageSize: 10,
          shareMemberMobile: undefined,
          shareCategory: undefined,
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
        get(url.shareList, this.listQuery).then(response => {
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
          get(url.memberDelete, {ids:ids}).then(response => {
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '删除会员成功',
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
