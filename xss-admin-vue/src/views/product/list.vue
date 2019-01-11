<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="$t('table.name')" v-model="listQuery.searchValue">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <router-link :to="'/product/add'">
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>
      </router-link>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelte" type="danger" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">刷新</el-button>
    </div>

    <el-table ref="multipleTable" :data="list" v-loading="listLoading" fit tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange" @sort-change='sortChange'>
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>

      <el-table-column
        prop="sn"
        label="编号"
        width="120"
        sortable="custom"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="fullName"
        label="名称"
        width="120"
        sortable="custom"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="productCategory.name"
        label="商品分类"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="productType.desc"
        label="商品类型"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="price"
        label="销售价"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="stock"
        label="库存"
        width="80"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="isMarketable"
        label="上架"
        width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.isMarketable" style="color: green; cursor: pointer" @click="updateMarketable(scope.row.sn)">√</span>
          <!--<span v-else style="color: red; cursor: pointer" @click="updateMarketable(scope.row.sn)">×</span>-->
          <span v-else style="color: red">×</span>
        </template>
      </el-table-column>

      <el-table-column
        prop="createDate"
        sortable="custom"
        label="创建日期"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column align="center" label="操作" width="240">
        <template slot-scope="scope">
          <router-link :to="'/product/edit/'+scope.row.id">
            <el-button type="primary" size="small" icon="el-icon-edit">编辑</el-button>
          </router-link>
          <!--<el-button @click="handleCopy(scope.row.id)" type="primary" size="small" icon="el-icon-news">复制</el-button>-->
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
  import { fetchList,deleteProducts,copy,updateMarketable } from '@/api/product'

  export default {
    name: 'productTable',
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
          searchProperty: 'name',
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
          deleteProducts({ids:ids}).then(response => {
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '删除商品成功',
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
      handleCopy(id) {
        copy(id).then(response => {
          if(response.data.result==='00000000'){
            this.$notify({
              title: '成功',
              message: '复制商品成功',
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
        }).catch(e => {
          console.log(e)
        })
      },
      updateMarketable(sn) {
        updateMarketable(sn).then(response => {
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
              message: response.data.msg,
              type: 'error',
              duration: 2000
            })
          }
          this.getList()
        }).catch(e => {
          console.log(e)
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
