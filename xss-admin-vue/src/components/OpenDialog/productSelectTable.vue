<template>
  <el-dialog :title="title" :visible.sync="dialogTableVisible" width="70%">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 300px;" class="filter-item" placeholder="输入商品ID、编号、名称关键字搜索" v-model="listQuery.keyword">
      </el-input>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
    </div>
    <el-table ref="singleTable" highlight-current-row :data="gridData" @selection-change="selectionChange" :v-loading="loading">
      <el-table-column type="selection" width="40"></el-table-column>
      <el-table-column property="sn" label="编号" show-overflow-tooltip></el-table-column>
      <el-table-column property="fullName" label="名称" show-overflow-tooltip></el-table-column>
      <el-table-column property="price" label="价格" show-overflow-tooltip></el-table-column>
    </el-table>
    <div class="pagination-container">
      <el-pagination background @size-change="handlePageSizeChange" @current-change="handlePageCurrentChange" :current-page="listQuery.pageNumber"
                     :page-sizes="[5,10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogTableVisible = false">取 消</el-button>
      <el-button type="primary" @click="selectPrimary()">确 定</el-button>
    </div>
  </el-dialog>


</template>
<script>
  import {selectProducts} from '@/api/product'
  export default {
    props: {
      dialogVisible:{
        type: Boolean,
        default: false
      },
      title:{
        type: String,
        default: '数据选择列表'
      },
      isGift:{
        type: Boolean,
        default: false
      },
      productType:{
        type: String,
        default: undefined
      }
    },
    data() {
      return {
        loading: false,
        gridData: [],
        total: 0,
        dialogTableVisible: false,
        searchUrl:'',
        formLabelWidth: '120px',
        memberText:'',
        multipleSelection:[],
        listQuery: {
          pageNumber: 1,
          pageSize: 5,
          keyword: undefined,
          isGift: undefined,
          isMarketable: true,
          productType: undefined,
        },

      };
    },
    created() {
      this.getList()
    },
    methods: {
      getList(){
        this.loading = true
        this.listQuery.isGift = this.isGift
        this.listQuery.productType = this.productType
        selectProducts(this.listQuery).then(response => {
          this.gridData = response.data.list
          this.total = response.data.total
        }).catch(e => {
          console.log(e)
        })
        this.loading = false
      },
      handleFilter() {
        this.listQuery.pageNumber = 1
        this.getList()
      },
      handlePageSizeChange(val) {
        this.listQuery.pageSize = val
        this.getList()
      },
      handlePageCurrentChange(val) {
        this.listQuery.pageNumber = val
        this.getList()
      },
      selectionChange(val) {
        this.multipleSelection = val;
      },
      selectPrimary(){
        this.$emit('successCBK', this.multipleSelection);
        this.dialogTableVisible = false
      }
    }
  };
</script>
