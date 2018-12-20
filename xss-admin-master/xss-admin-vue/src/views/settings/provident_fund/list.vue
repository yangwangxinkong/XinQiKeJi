<template>
  <div class="app-container">
    <div class="filter-container">
      <!-- <el-select v-model="listQuery.cityCode" clearable :placeholder="$t('settings.citySelect')" class="filter-item">
        <el-option
          v-for="item in cityList"
          :key="item.code"
          :label="item.name"
          :value="item.code">
        </el-option>
      </el-select> -->
      <el-select v-model="listQuery.cityCode" clearable :placeholder="$t('settings.citySelect')" class="filter-item">
        <el-option
          v-for="item in cityCategory" :style="'padding-left:' + ((item.grade)*20+20) + 'px'" :key="item.code"  :label="item.name"  :value="item.code">
        </el-option>
      </el-select>

      <el-select v-model="listQuery.payFromId" clearable :placeholder="$t('settings.payFromSelect')" class="filter-item">
        <el-option
          v-for="item in payFromList"
          :key="item.value"
          :label="item.desc"
          :value="item.value">
        </el-option>
      </el-select>

      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <router-link :to="'/settings/provident_fund/add'">
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>
      </router-link>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelte" type="danger" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">{{$t('table.refresh')}}</el-button>
    </div>

    <el-table ref="multipleTable" :data="list" v-loading="listLoading" fit tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange" @sort-change='sortChange'>
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>

      <el-table-column
        prop="city.fullName"
        label="城市"
        width="120"
        sortable="custom"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="payFromDesc"
        label="缴费对象"
        sortable="custom"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="ratio"
        label="缴费比例"
        show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{scope.row.ratio}}</span>
        </template>
      </el-table-column>

      <el-table-column
        prop="createDate"
        label="创建日期"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column align="center" label="操作" width="120">
        <template slot-scope="scope">
          <router-link :to="'/settings/provident_fund/edit/'+scope.row.id">
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
  import { fetchList, fetchRemove, fetchCityList } from '@/api/providentFund'
  import url from '@/api/apiUrl'
  import { get } from '@/api/server'

  export default {
    name: 'providentFundTable',
    directives: {
      waves
    },
    data() {
      return {
        payFromList: [{
          value: '0',
          desc: '个人'
        }, {
          value: '1',
          desc: '公司'
        }],
        cityList: [],
        list: [],
        total: 0,
        listLoading: true,
        multipleSelection: [],
        cityCategory: [],
        listQuery: {
          pageNumber: 1,
          pageSize: 10,
          cityCode: undefined,
          payFromId: undefined,
          searchProperty: undefined,
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
          fetchRemove({ids:ids}).then(response => {
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
