<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="$t('table.title')" v-model="pageable.searchValue">
      </el-input>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <router-link :to="'/content/ad/add'">
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>
      </router-link>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelete"  type="danger" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">刷新</el-button>
    </div>
    <el-table ref="multipleTable" :data="data"  v-loading="listLoading"  style="width: 100%" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55">
      </el-table-column>0
      <el-table-column align="center" label="标题" width="160">
        <template slot-scope="scope">
          <span>{{scope.row.title}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="广告位" width="160">
        <template slot-scope="scope">
          <span>{{scope.row.adPosition.name}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="类型" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.typeDesc}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="起始日期" width="160">
        <template slot-scope="scope">
          <span>{{scope.row.beginDate}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="结束日期" width="160">
        <template slot-scope="scope">
          <span>{{scope.row.endDate}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="排序" width="80">
        <template slot-scope="scope">
          <span>{{scope.row.order}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('table.actions')" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <router-link :to="'/content/ad/edit/'+scope.row.id">
            <el-button type="primary" size="mini">{{$t('table.edit')}}</el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageable.pageNumber" :page-sizes="[10,20,30, 50]" :page-size="pageable.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
  </div>
</template>
<script>
  import url from '@/api/apiUrl'
  import { get,post,execute } from '@/api/server'

  export default {
    name: 'adTable',
    listLoading: true,
    data(){
      return {
        data:[],
        total: null,
        listLoading: true,
        multipleSelection: [],
        pageable: {
          pageNumber: 1,
          pageSize: 20,
          orderProperty : '',
          orderDirection:'',
          searchValue:'',
          searchProperty:'title',
          filters:[]
        }
      }
    },
    created(){
      this.getList()
    },
    methods:{
      getList(){
        this.listLoading = true
        get(url.adList, this.pageable).then(response => {
          //console.log(JSON.stringify(response.data.list))
          this.data = response.data.list
          this.total = response.data.total
          setTimeout(() => {
            this.listLoading = false
          }, 1000)
        })
      },
      handleSizeChange(val) {
        this.pageable.pageSize = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.pageable.pageNumber = val
        this.getList()
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      handleDelete(){
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
          get(url.adDelete, {ids:ids}).then(response => {
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '操作成功',
                type: 'success',
                duration: 2000
              })
            } else {
              this.$notify({
                title: '操作失败',
                message: response.data.msg,
                type: 'error',
                duration: 2000
              })
            }
            this.getList()
          })
        }).catch(err => {
        })
      },
      handleRefresh(){
        this.getList()
      },
      handleFilter(){
        this.getList()
      }
    }
  }
</script>
