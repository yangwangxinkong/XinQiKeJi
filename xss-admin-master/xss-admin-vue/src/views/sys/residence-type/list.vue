<template>
  <div class="app-container">
    <div class="filter-container">

      <el-select v-model="listQuery.cityCode" clearable :placeholder="$t('settings.citySelect')" class="filter-item">
        <el-option
          v-for="item in cityCategory" :style="'padding-left:' + ((item.grade)*20+20) + 'px'" :key="item.code"  :label="item.name"  :value="item.code">
        </el-option>
      </el-select>

      <el-input @keyup.enter.native="handleFilter" clearable style="width: 200px;" class="filter-item" placeholder="名称" v-model="listQuery.searchValue">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <router-link :to="'/sys/residence_type/add'">
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
        prop="name"
        label="名称"
        sortable="custom"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="city.fullName"
        label="所属城市"
        sortable="custom"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="order"
        sortable="custom"
        label="排序">
      </el-table-column>

      <el-table-column align="center" label="操作" width="120">
        <template slot-scope="scope">
          <router-link :to="'/sys/residence_type/edit/'+scope.row.id">
            <el-button type="primary" size="small" icon="el-icon-edit">编辑</el-button>
          </router-link>
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
  import url from '@/api/apiUrl'
  import { get, post } from '@/api/server'

  export default {
    name: 'residenceTypeTable',
    directives: {
      waves
    },
    data() {
      return {
        list: [],
        total: 0,
        cityCategory: [],
        listLoading: true,
        multipleSelection: [],
        listQuery: {
          pageNumber: 1,
          pageSize: 10,
          cityCode: undefined,
          searchProperty: 'name',
          searchValue: undefined,
          orderProperty: undefined,
          orderDirection: undefined
        }
      }
    },
    created() {
      this.getCityList(),
      this.getList()
    },
    methods: {
      getCityList() {
        this.listLoading = true
        // fetchCityList().then(response => {
        //   this.cityList = response.data.data
        // }).catch((e)=>{
        //   console.log(e)
        //   this.listLoading = false
        // })
        get(url.cityCategoryTree, {}).then(response => {
          this.cityCategory = response.data.data
        })
      },
      getList() {
        this.listLoading = true
        get(url.residenceTypeList, this.listQuery).then(response => {
        //getPaymentMethodList(this.listQuery).then(response => {
          //console.log(response.data.list)
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
          post(url.residenceTypeDelete, {ids:ids}).then(response => {
          //deletePaymentMethods({ids:ids}).then(response => {
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '删除户口性质成功',
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
