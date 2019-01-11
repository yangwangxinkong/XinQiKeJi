<template>
  <div class="app-container">
    <el-form class="form-container" label-width="120px" :model="productInfo">
      <div class="createPost-main-container">
      
      <el-form-item label="编号：">
        <el-col :span="15">
        {{productInfo?productInfo.sn:""}}
        </el-col>
      </el-form-item>
      
      <el-form-item label="名称：">
        <el-col :span="15">
        {{productInfo?productInfo.fullName:""}}
        </el-col>
      </el-form-item>

      <el-form-item label="点赞人数：">
        <el-col :span="15">
         {{productInfo?productInfo.hits:0}}
        </el-col>
      </el-form-item>
      </div>
    </el-form>
    <div class="filter-container">
      <!-- <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="会员编号" v-model="listQuery.searchValue">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button> -->
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelte" type="danger" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">刷新</el-button>
    </div>

    <el-table ref="multipleTable" :data="list" v-loading="listLoading" fit tooltip-effect="dark" style="width: 100%"  @selection-change="handleSelectionChange" @sort-change='sortChange'>

      <el-table-column
        type="selection"
        width="55">
      </el-table-column>

      <el-table-column label="点赞者" width="180" sortable="custom" show-overflow-tooltip>
        <template slot-scope="scope">
          {{scope.row.member?scope.row.member.name:""}}
        </template>
      </el-table-column>

      <el-table-column prop="isPraise" label="点赞" width="80" sortable="custom" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.isPraise" style="color:green;font-size:14px">点赞</span>
          <span v-if="!scope.row.isPraise" style="color:red;font-size:14px">取消</span>
        </template>
      </el-table-column>

      <el-table-column prop="createDate" label="创建日期" show-overflow-tooltip>
        <template slot-scope="scope">
         {{scope.row.createDate | parseTime}}
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
  import { findById } from '@/api/product'
  import { commentTypes } from '@/api/comment'
  import { praiseSearchList,praiseRemove } from '@/api/praise'
  import { goback } from '@/utils/common'

  export default {
    name: 'praiseTable',
    directives: {
      waves
    },
    data() {
      return {
        productInfo: null,
        id:null,
        list: [],
        total: 0,
        listLoading: true,
        multipleSelection: [],
        listQuery: {
          pageNumber: 1,
          pageSize: 10,
          name: undefined,
          searchProperty: 'member',
          searchValue: undefined,
          orderProperty: undefined,
          orderDirection: undefined
        },
        praiseInfo:{type:'product'}
      }
    },
    created() {
      const id = this.$route.params && this.$route.params.id;
      this.id=id;
      this.getProduct();
      this.getPraiseList();
    },
    methods: {
      getProduct() {
        this.listLoading = true
        findById(this.id).then(response => {
          this.productInfo = response.data.data;
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        }).catch((e)=>{
          console.log(e)
          this.listLoading = false
        })
      },
      getPraiseList() {
        this.listLoading = true
        var searchData={"pageable":this.listQuery,"praiseSearch":{"type":"product","objId":this.id}};
        praiseSearchList(searchData).then(response => {
          this.list = response.data.list
          console.log(this.list);
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
          this.getPraiseList();
      },
      handleFilter() {
        this.listQuery.pageNumber = 1
        this.getPraiseList()
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
          praiseRemove({ids:ids}).then(response => {
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '删除成功',
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
            this.getPraiseList()
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
        this.getPraiseList()
      },
      handleCurrentChange(val) {
        this.listQuery.pageNumber = val
        this.getPraiseList()
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
        this.getPraiseList()
      }
    }
  }
</script>
