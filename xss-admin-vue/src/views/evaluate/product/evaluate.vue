<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="$t('table.title')" v-model="listQuery.searchValue">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
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
        <template slot-scope="scope">
          <a :title="scope.row.name" style="text-decoration:underline;" :href="'#/product/audit/'+scope.row.id">{{scope.row.fullName}}</a>
        </template>
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

      <el-table-column label="人数">
        <el-table-column
          prop="commentNum"
          label="评论"
          width="80"
          show-overflow-tooltip>
        </el-table-column>

        <el-table-column
          prop="hits"
          label="点赞"
          width="80"
          show-overflow-tooltip>
        </el-table-column>

        <el-table-column
          prop="scoreCount"
          label="评分"
          width="80"
          show-overflow-tooltip>
        </el-table-column>
      </el-table-column>
      
      <el-table-column
        prop="score"
        label="评分平均数"
        width="100"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="credit"
        label="信用评分"
        width="80"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column align="center" label="查看" width="280">
        <template slot-scope="scope">
          <router-link :to="'/evaluate/product/comment/'+scope.row.id">
            <el-button type="primary" size="small" icon="el-icon-search">评论</el-button>
          </router-link>
          <router-link :to="'/evaluate/product/praise/'+scope.row.id">
            <el-button type="primary" size="small" icon="el-icon-search">点赞</el-button>
          </router-link>
          <router-link :to="'/evaluate/product/score/'+scope.row.id">
            <el-button type="primary" size="small" icon="el-icon-search">评分</el-button>
          </router-link>
          <!-- <router-link :to="'/product/edit/'+scope.row.id">
            <el-button type="primary" size="small" icon="el-icon-edit">编辑</el-button>
          </router-link> -->
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
  import { fetchList } from '@/api/product'

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
