<template>
  <div class="app-container">
    <el-form class="form-container" label-width="120px" :model="companyCaseInfo">
      <div class="createPost-main-container">
      
      <el-form-item label="案例名称：">
        <el-col :span="15">
        {{companyCaseInfo?companyCaseInfo.name:""}}
        </el-col>
      </el-form-item>
      
      <el-form-item label="所属企业：">
        <el-col :span="15">
        {{companyCaseInfo?companyCaseInfo.companyName:""}}
        </el-col>
      </el-form-item>

      <el-row>
        <el-col :span="8">
          <el-form-item label="评分人数：">
            {{companyCaseInfo?companyCaseInfo.scoreNum:0}}
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="评分平均数：">
            {{companyCaseInfo?companyCaseInfo.averageNum:0}}
          </el-form-item>
        </el-col>
      </el-row>
      
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

      <el-table-column label="评分者" width="180" sortable="custom" show-overflow-tooltip>
        <template slot-scope="scope">
          {{scope.row.member?scope.row.member.name:""}}
        </template>
      </el-table-column>

      <el-table-column prop="ratingDimension" label="评分维度" width="180" sortable="custom" show-overflow-tooltip>
        <template slot-scope="scope">
          {{scope.row.ratingDimension?scope.row.ratingDimension.name:""}}
        </template>
      </el-table-column>

      <el-table-column prop="value" label="评分值" width="140" sortable="custom" show-overflow-tooltip>
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
  import { info } from '@/api/companyCase'
  import { commentTypes } from '@/api/comment'
  import { scoreSearchList,scoreRemove } from '@/api/score'
  import { goback } from '@/utils/common'

  export default {
    name: 'scoreTable',
    directives: {
      waves
    },
    data() {
      return {
        companyCaseInfo: null,
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
        scoreInfo:{type:'company_case'}
      }
    },
    created() {
      const id = this.$route.params && this.$route.params.id;
      this.id=id;
      this.getCompany();
      this.getScoreList();
    },
    methods: {
      getCompany() {
        this.listLoading = true
        info({id:this.id}).then(response => {
          this.companyCaseInfo = response.data.data;
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        }).catch((e)=>{
          console.log(e)
          this.listLoading = false
        })
      },
      getScoreList() {
        this.listLoading = true
        var searchData={"pageable":this.listQuery,"scoreSearch":{"type":"company_case","objId":this.id}};
        scoreSearchList(searchData).then(response => {
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
          this.getScoreList();
      },
      handleFilter() {
        this.listQuery.pageNumber = 1
        this.getScoreList()
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
          scoreRemove({ids:ids}).then(response => {
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
            this.getScoreList()
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
        this.getScoreList()
      },
      handleCurrentChange(val) {
        this.listQuery.pageNumber = val
        this.getScoreList()
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
        this.getScoreList()
      }
    }
  }
</script>
