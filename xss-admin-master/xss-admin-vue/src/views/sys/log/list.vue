<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select class="filter-item" v-model="listQuery.searchProperty" placeholder="请选择查询项">
        <el-option v-for="item in searchPropertyOptions" :key="item.value" :label="item.name" :value="item.value"></el-option>
      </el-select>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="请输入查询值" v-model="listQuery.searchValue">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>

      <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelte" type="danger" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleClear" type="danger" icon="el-icon-remove-outline">清空</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">刷新</el-button>
    </div>

    <el-table ref="multipleTable" :data="list" v-loading="listLoading" fit tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange" @sort-change='sortChange'>
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>

      <el-table-column
        prop="operation"
        label="操作"
        width="120"
        sortable="custom"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="operator"
        label="操作员"
        sortable="custom"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="ip"
        sortable="custom"
        label="IP">
      </el-table-column>

      <el-table-column
        prop="content"
        label="内容"
        sortable="custom"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="createDate"
        label="创建日期"
        sortable="custom">
      </el-table-column>

      <el-table-column align="center" label="操作" width="120">
        <template slot-scope="scope">
          <el-button class="filter-item" size="mini" type="primary" icon="el-icon-search" @click="handleView(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNumber"
                     :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!--查看dialog--------start------->
    <el-dialog title="查看" :visible.sync="dialogFormVisible">
      <el-form label-position="right" label-width="100px">
        <el-form-item label="操作：" prop="operation">
          <!--<el-col :span="10">-->
            <span>{{temp.operation}}</span>
          <!--</el-col>-->
        </el-form-item>
        <el-form-item label="操作员：" prop="operator">
          <!--<el-col :span="10">-->
            <span>{{temp.operator}}</span>
          <!--</el-col>-->
        </el-form-item>
        <el-form-item label="内容：" prop="content">
          <!--<el-col :span="10">-->
            <span>{{temp.operator}}</span>
          <!--</el-col>-->
        </el-form-item>

        <el-form-item label="请求参数：" prop="parameter">
          <!--<el-col :span="10">-->
            <el-input type="textarea" :rows="7" v-model="temp.parameter"></el-input>
          <!--</el-col>-->
        </el-form-item>

        <el-form-item label="IP：" prop="ip">
          <!--<el-col :span="10">-->
            <span>{{temp.ip}}</span>
          <!--</el-col>-->
        </el-form-item>
        <el-form-item label="创建日期：" prop="createDate">
          <!--<el-col :span="10">-->
            <span>{{temp.createDate}}</span>
          <!--</el-col>-->
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">返回</el-button>
      </div>
    </el-dialog>
    <!--查看dialog--------end------->

  </div>
</template>

<script>
  import waves from '@/directive/waves' // 水波纹指令
  import { getLogList,getLog,clearLog,deleteLog } from '@/api/sys'
  import ElCol from "element-ui/packages/col/src/col";

  export default {
    components: {ElCol},
    name: 'logTable',
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
          name: undefined,
          searchProperty: undefined,
          searchValue: undefined,
          orderProperty: undefined,
          orderDirection: undefined
        },
        dialogFormVisible: false,
        temp: {
          id:'',
          operation:'',
          operator:'',
          content:'',
          ip:'',
          parameter:'',
          createDate:''
        },
        searchPropertyOptions: [{name:'操作',value:'operation'},{name:'操作员',value:'operator'}]
      }
    },
    created() {
      this.getList()
    },
    methods: {
      resetTemp() {
        this.temp = {
          id:'',
          operation:'',
          operator:'',
          content:'',
          ip:'',
          parameter:'',
          createDate:''
        }
      },
      getList() {
        this.listLoading = true
        getLogList(this.listQuery).then(response => {
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
      handleView(row) {
        this.resetTemp()
        this.temp.id=row.id
        this.temp.operation=row.operation
        this.temp.operator=row.operator
        this.temp.content=row.content
        this.temp.ip=row.ip
        this.temp.parameter=row.parameter
        this.temp.createDate=row.createDate
        this.dialogFormVisible=true
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
          deleteLog({ids:ids}).then(response => {
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '删除日志成功',
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
      handleClear() {
        this.$confirm('清空无法恢复, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          clearLog().then(response => {
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '清空日志成功',
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
    }
  }
</script>
