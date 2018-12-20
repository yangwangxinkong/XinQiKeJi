<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="名称" v-model="pageable.searchValue">
      </el-input>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <router-link :to="'navigation/add'">
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>
      </router-link>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelete"  type="danger" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">刷新</el-button>
    </div>
    <el-table ref="multipleTable" :data="data"  v-loading="listLoading"  style="width: 100%" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column align="center" label="名称" width="name">
        <template slot-scope="scope">
          <span>{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="位置" width="position">
        <template slot-scope="scope">
          <span>{{scope.row.position}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否新窗口打开" width="isBlankTarget">
        <template slot-scope="scope">
          <span>{{scope.row.isBlankTarget}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="排序" width="order">
        <template slot-scope="scope">
          <span>{{scope.row.order}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="120">
        <template slot-scope="scope">
          <router-link :to="'navigation/edit/'+scope.row.id">
            <el-button type="primary" size="small" icon="el-icon-edit">编辑</el-button>
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
  import { fetchList , remove , save  } from '@/api/navigation'
  export default {
    name: 'navigationTable',
    data(){
      return{
        data: [],
        total: null,
        listLoading: true,
        multipleSelection: [],
        pageable: {
          pageNumber: 1,
          pageSize: 20,
          orderProperty : 'position',
          orderDirection:'asc',
          searchValue:'',
          searchProperty:'name',
          filters:[]
        }
      }
    },
    created(){
      this.getList()
    },
    methods: {
      getList(){
        this.listLoading = true
        fetchList(this.pageable).then(response => {
          this.data = response.data.list
          this.total = response.data.total
          setTimeout(() => {
            this.listLoading = false
          }, 1 * 1000)
        })
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      handleSizeChange(val) {
        this.pageable.pageSize = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.pageable.pageNumber = val
        this.getList()
      },
      handleFilter(){
        this.getList()
      },
      handleDelete() {
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
                message: '操作成功',
                type: 'success',
                duration: 2000
              })
            } else {
              this.$notify({
                title: '失败',
                message: '操作失败',
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
      }
    }
  }
</script>
