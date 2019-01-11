<template>
  <div class="app-container">
    <el-form class="form-container" label-width="120px" :model="companyInfo">
      <div class="createPost-main-container">
      <el-form-item label="企业名称：">
        <el-col :span="15">
        {{companyInfo?companyInfo.name:""}}
        </el-col>
      </el-form-item>
      <el-form-item label="评论人数：">
        <el-col :span="15">
         {{companyInfo?companyInfo.commentNum:0}}
        </el-col>
      </el-form-item>
      </div>
    </el-form>
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="评论内容" v-model="listQuery.searchValue">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelte" type="danger" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">刷新</el-button>
    </div>

    <el-table ref="multipleTable" :data="list" v-loading="listLoading" fit tooltip-effect="dark" style="width: 100%"  @selection-change="handleSelectionChange" @sort-change='sortChange'>

      <el-table-column
        type="selection"
        width="55">
      </el-table-column>

      <el-table-column label="发送者" width="160" sortable="custom" show-overflow-tooltip>
        <template slot-scope="scope">
          {{scope.row.sendMember?scope.row.sendMember.name:""}}
          {{scope.row.sendAdmin?scope.row.sendAdmin.name:""}}
        </template>
      </el-table-column>

      <el-table-column class-name="contentText"  prop="content" label="评论" width="200" sortable="custom" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-html="scope.row.content">{{scope.row.content}}</span>
        </template>
      </el-table-column>

      <el-table-column label="接收者" width="160" sortable="custom" show-overflow-tooltip>
        <template slot-scope="scope">
          {{scope.row.receiveMember?scope.row.receiveMember.name:""}}
          {{scope.row.receiveAdmin?scope.row.receiveAdmin.name:""}}
        </template>
      </el-table-column>

      <el-table-column prop="isEffective" label="生效" width="80" sortable="custom" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.isEffective" style="color:green;font-size:14px">生效</span>
          <span v-if="!scope.row.isEffective" style="color:red;font-size:14px">驳回</span>
        </template>
      </el-table-column>

      <el-table-column class-name="contentText"  prop="remark" label="备注" width="200" sortable="custom" show-overflow-tooltip>
      </el-table-column>

      <el-table-column prop="createDate" width="160" label="创建日期" show-overflow-tooltip>
        <template slot-scope="scope">
         {{scope.row.createDate | parseTime}}
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="110">
        <template slot-scope="scope">
          <el-button @click="handleEffective(scope.row)" type="primary" size="small" icon="el-icon-edit">{{scope.row.isEffective?"驳回":"生效"}}</el-button>
        </template>
      </el-table-column>

    </el-table>

    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNumber"
                     :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <el-dialog title="审核评论" :visible.sync="dialogCommentShow">
      <el-form :model="commentInfo" label-width="160">
        <el-form-item label="发送者：">
          {{commentInfo.sendMember?commentInfo.sendMember.name:""}}
          {{commentInfo.sendAdmin?commentInfo.sendAdmin.name:""}}
        </el-form-item>
        <label>评论内容：</label>
        <el-form-item >
          <div style="border:1px solid #aaa;padding:5px;height:100px;overflow:auto;" v-html="commentInfo.content">{{commentInfo.content}}</div>
        </el-form-item>
        <el-form-item label="接收者：">
          {{commentInfo.receiveMember?commentInfo.receiveMember.name:""}}
          {{commentInfo.receiveAdmin?commentInfo.receiveAdmin.name:""}}
        </el-form-item>
        <el-form-item label="生效：">
          <el-checkbox v-model="commentInfo.isEffective" auto-complete="off"></el-checkbox>
        </el-form-item>
        <el-form-item label="备注：">
          <el-input type="textarea" v-model="commentInfo.remark"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCommentShow = false">取 消</el-button>
        <el-button type="primary" @click="reviewComment">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import waves from '@/directive/waves' // 水波纹指令
  import { info } from '@/api/company'
  import { commentSearchList,commentTypes,commentReview,commentRemove } from '@/api/comment'
  import { goback } from '@/utils/common'

  export default {
    name: 'commentTable',
    directives: {
      waves
    },
    data() {
      return {
        companyInfo: null,
        id:null,
        list: [],
        total: 0,
        listLoading: true,
        multipleSelection: [],
        listQuery: {
          pageNumber: 1,
          pageSize: 10,
          name: undefined,
          searchProperty: 'content',
          searchValue: undefined,
          orderProperty: undefined,
          orderDirection: undefined
        },
        dialogCommentShow:false,
        commentInfo:{type:'company'}
      }
    },
    created() {
      const id = this.$route.params && this.$route.params.id;
      this.id=id;
      this.getCompany();
      this.getCommentList();
    },
    methods: {
      getCompany() {
        this.listLoading = true
        info(this.id).then(response => {
          this.companyInfo = response.data.data;
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        }).catch((e)=>{
          console.log(e)
          this.listLoading = false
        })
      },
      getCommentList() {
        this.listLoading = true
        var searchData={"pageable":this.listQuery,"commentSearch":{"type":"company","objId":this.id}};
        commentSearchList(searchData).then(response => {
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
      handleEffective(commentInfo) {
        this.commentInfo=commentInfo;
        this.dialogCommentShow=true;
      },reviewComment() {
        this.listLoading = true
        commentReview(this.commentInfo).then(response => {
          if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '操作成功',
                type: 'success',
                duration: 2000
              })
            this.getCommentList();
            this.dialogCommentShow=false;
            } else {
              this.$notify({
                title: '失败',
                message: response.data.msg,
                type: 'error',
                duration: 2000
              })
            }
            this.listLoading = false
        }).catch((e)=>{
          console.log(e)
          this.listLoading = false
        })
      },
      handleRefresh() {
          this.getCommentList();
      },
      handleFilter() {
        this.listQuery.pageNumber = 1
        this.getCommentList()
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
          commentRemove({ids:ids}).then(response => {
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
            this.getCommentList()
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
        this.getCommentList()
      },
      handleCurrentChange(val) {
        this.listQuery.pageNumber = val
        this.getCommentList()
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
        this.getCommentList()
      }
    }
  }
</script>
<style rel="stylesheet/scss" lang="css">
.contentText{
    white-space: nowrap;
    text-overflow:ellipsis;
    overflow:hidden;
}
.el-form-item{
  margin-bottom: 5px;
}
</style>
