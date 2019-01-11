<template>
  <div class="app-container">

    <div class="filter-container">
      <el-button type="primary" icon="el-icon-plus" @click="addPromotion">{{$t('table.add')}}</el-button>
      <el-button type="danger" icon="el-icon-delete" @click="deletePromotion">{{$t('table.delete')}}</el-button>
      <el-button type="primary" icon="el-icon-refresh" @click="refresh">{{$t('refresh')}}</el-button>
    </div>

    <el-table :data="tableData" ref="promotionTable" v-loading="listLoading" @sort-change="sort" @selection-change="selectionChange"
              style="width: 100%" fit highlight-current-row>
      <el-table-column type="selection" width="40">
      </el-table-column>
      <el-table-column prop="name" :label="$t('promotion.name')" sortable show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="title" :label="$t('promotion.title')" sortable show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="beginDate" :label="$t('promotion.beginDate')" sortable show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{scope.row.beginDate | parseTime('{y}-{m}-{d}')}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="endDate" :label="$t('promotion.endDate')" sortable show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{scope.row.endDate | parseTime('{y}-{m}-{d}')}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="order" :label="$t('promotion.order')" sortable>
      </el-table-column>
      <el-table-column fixed="right" :label="$t('promotion.operation')" width="200">
        <template slot-scope="scope">
          <router-link :to="'/marketing/promotion/edit/'+scope.row.id">
            <el-button type="primary" size="mini">{{$t('table.edit')}}</el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination background @size-change="pageSizeChange" @current-change="pageCurrentChange" :current-page="listQuery.pageNumber" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
  import { fetchList, deletePromotion, downloadPromotion} from '@/api/marketing/promotion'
  import waves from '@/directive/waves' // 水波纹指令
  import { parseTime } from '@/utils'

  export default {
    name: 'promotionTable',
    directives: {
      waves
    },
    data() {
      return {
        listLoading: false,
        total: 1,
        tableData: null,
        listQuery: {
          pageNumber: 1,
          pageSize: 10,
          searchProperty: undefined,
          searchValue: undefined,
          orderProperty: undefined,
          orderDirection: undefined
        },

        multipleSelection: [],
        // 优惠码数据，用来生成excel
        promotionCodeData: [],


      }
    },


    created(){
      this.fetchList()
    },


    methods: {

      fetchList() {
        this.listLoading = true
        fetchList(this.listQuery).then(response => {
          console.log(response)

          this.tableData = response.data.list
          this.total = response.data.total

          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        })
      },

      // 添加促销
      addPromotion(){
        this.$router.push({name: 'promotionAdd'});
      },

      // 删除促销
      deletePromotion(){
        if(this.multipleSelection.length<1){
          this.$message("请选择至少一个删除项！");
          return
        }
        this.$confirm("确定删除所选促销信息吗？", '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const ids = new Array()
          for(var i=0; i<this.multipleSelection.length; i++){
            ids[i] = this.multipleSelection[i].id
          }
          console.log(ids)
          deletePromotion(ids).then(response => {
            if(response.data.result === "00000000"){
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              this.fetchList()
            }else{
              this.$message({
                type: 'error',
                message: '删除失败!'
              });
            }
          })
        })

      },

      pageSizeChange(val) {
        this.listQuery.pageSize = val
        this.fetchList()
      },
      pageCurrentChange(val) {
        this.listQuery.pageNumber = val
        this.fetchList()
      },

      // 排序
      sort(sort){
        if(sort && sort.column){
          if(sort.column.order === 'descending'){
            this.listQuery.orderDirection = 'desc'
          }else if(sort.column.order === 'ascending'){
            this.listQuery.orderDirection = 'asc'
          }

        }else {
          this.listQuery.orderProperty = undefined
          this.listQuery.orderDirection = undefined
        }

        this.fetchList()
      },

      refresh(){
        this.listQuery.orderProperty = undefined
        this.listQuery.orderDirection = undefined
        this.listQuery.pageNumber = 1
        this.listQuery.pageSize = 10
        this.$refs['promotionTable'].clearSort()
        this.fetchList()
      },

      selectionChange(val) {
        this.multipleSelection = val;
        console.log(this.multipleSelection)
      },




    }


  }

</script>
