<template>
  <div class="app-container">
    <div class="filter-container">
      <router-link :to="'/marketing/share_config/shareConfig/add'">
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>
      </router-link>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelete"  type="danger" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">刷新</el-button>
    </div>
    <el-table ref="multipleTable" :data="data" v-loading="listLoading" style="width: 100%" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column align="center" label="分享标题" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.shareTitle}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="分享描述" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.shareDesc}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="分享图标" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.shareImg}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="分享类型" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.shareTypes}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.createDate | formatDate}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="修改时间" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.modifyDate | formatDate}}</span> <!-- | formatDate  将时间戳转换成指定日期格式-->
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('table.actions')" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <router-link :to="'/marketing/share_config/shareConfig/edit/'+scope.row.id">
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
  import { shareConfigList , remove} from '@/api/shareConfig'
  import { formatDate } from '@/api/date'

  export default {
    name: 'articleTable',
    data(){
      return{
        data: [],
        total: null,
        listLoading: true,
        multipleSelection: [],
        pageable: {
          pageNumber: 1,
          pageSize: 20,
          orderProperty : 'id',
          orderDirection:'desc',
          searchValue:'',
          searchProperty:'title',
          filters:[]
        }
      }
    },
    filters: {
      formatDate(time) {
        var date = new Date(time);
        return formatDate(date, 'yyyy-MM-dd hh:mm');
      }
    },
    created(){
          this.getList()
        },
    methods: {
      getList(){
        this.listLoading = true
        shareConfigList(this.pageable).then(response => {
          this.data = response.data.list
          this.total = response.data.total
          setTimeout(() => {
            this.listLoading = false
          }, 1.0 * 1000)
        }).catch( (e) =>{
          console.log(e)
          this.listLoading = false
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
