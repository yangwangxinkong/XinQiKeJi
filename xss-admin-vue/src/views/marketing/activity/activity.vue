<template>
  <div class="app-container">

    <div class="filter-container">
      <router-link :to="'/marketing/activity/add'" style="margin-right: 10px">
        <el-button type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>
      </router-link>
      <el-button type="danger" icon="el-icon-delete" @click="deleteActivity">删除</el-button>
      <el-button type="primary" icon="el-icon-refresh" @click="refresh">{{$t('refresh')}}</el-button>
    </div>
    <el-tabs style='margin-top:15px;' v-model="listQuery.dataType" :value="listQuery.dataType" type="border-card" @tab-click="getList">
      <el-tab-pane v-for="item in tabMapOptions" :label="item.label" :key='item.key' :name="item.key">
        <keep-alive>
          <tab-pane v-if='listQuery.dataType===item.key' :type='item.key' v-on:update-data="getList">
            <el-table :data="tableData" ref="activityTable" v-loading="listLoading" @sort-change="sort" @selection-change="selectionChange"
              style="width: 100%" fit highlight-current-row>
              <el-table-column type="selection" width="40">
              </el-table-column>
              <el-table-column prop="mainTitle" label="主标题" sortable show-overflow-tooltip>
              </el-table-column>
              <el-table-column prop="subTitle" label="副标题" sortable show-overflow-tooltip>
              </el-table-column>
              <el-table-column prop="startDate" label="开始时间" sortable show-overflow-tooltip>
                <template slot-scope="scope">
                  <span>{{scope.row.startDate | parseTime('{y}-{m}-{d} {h}:{i}:{s}')}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="endDate" label="结束时间" sortable show-overflow-tooltip>
                <template slot-scope="scope">
                  <span>{{scope.row.endDate | parseTime('{y}-{m}-{d} {h}:{i}:{s}')}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" sortable show-overflow-tooltip>
                <template slot-scope="scope">
                  <span v-if="scope.row.status==='auditing'">待审核</span>
                  <span v-if="scope.row.status==='auditThroughNotStart'">审核通过</span>
                  <span v-if="scope.row.status==='auditFail'">审核失败</span>
                  <span v-if="scope.row.status==='underway'">进行中</span>
                  <span v-if="scope.row.status==='discontinue'">已中止</span>
                  <span v-if="scope.row.status==='end'">已结束</span>
                </template>
              </el-table-column>
              <el-table-column prop="type" label="类型" sortable show-overflow-tooltip>
                <template slot-scope="scope">
                  <span v-if="scope.row.type==='advertise'">专题活动</span>
                  <span v-if="scope.row.type==='promotion'">促销活动</span>
                  <span v-if="scope.row.type==='signup'">报名活动</span>
                </template>
              </el-table-column>
              <el-table-column prop="url" label="URL" show-overflow-tooltip>
              </el-table-column>
              <el-table-column fixed="right" :label="$t('promotion.operation')" width="180">
                <template slot-scope="scope">
                  <router-link
                    v-if="scope.row.status!=='auditFail' && scope.row.status!=='discontinue' && scope.row.status!=='end'"
                    :to="'/marketing/activity/edit/'+scope.row.id">
                    <el-button type="primary" size="mini">{{$t('table.edit')}}</el-button>
                    &nbsp;
                  </router-link>
                  <router-link :to="'/marketing/activity/info/'+scope.row.id">
                    <el-button type="primary" size="mini">审核</el-button>
                  </router-link>
                </template>
              </el-table-column>
            </el-table>

            <div class="pagination-container">
              <el-pagination background @size-change="pageSizeChange" @current-change="pageCurrentChange" :current-page="listQuery.pageNumber" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
              </el-pagination>
            </div>

          </tab-pane>
        </keep-alive>
      </el-tab-pane>
    </el-tabs>
    

  </div>
</template>

<script>
  import { fetchList,deleteActivity} from '@/api/marketing/activity'
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
          status: undefined,
          type: undefined,
          dataType: 'audit',
          searchProperty: undefined,
          searchValue: undefined,
          orderProperty: undefined,
          orderDirection: undefined
        },

        tabMapOptions: [
          { label: '审核列表', key: 'audit' },
          { label: '活动列表', key: 'list' },
        ],

        activityType: [
          {value:'advertise',desc:'专题活动'},
          {value:'promotion',desc:'促销活动'},
          {value:'signup',desc:'报名活动'},
        ],
        multipleSelection: [],
        // 优惠码数据，用来生成excel
        promotionCodeData: [],


      }
    },


    created(){
      this.getList()
    },


    methods: {

      getList() {
        this.listLoading = true
        fetchList(this.listQuery).then(response => {
          console.log(response)

          this.tableData = response.data.list
          this.total = response.data.total

          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        }).catch(e => {
          console.log(e)
          this.listLoading = false
        })
      },

      // 删除活动
      deleteActivity(){
        if(this.multipleSelection.length<1){
          this.$message("请选择至少一个删除项！");
          return
        }
        this.$confirm("确定删除所选活动信息吗？", '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const ids = []
          for(var i=0; i<this.multipleSelection.length; i++){
            ids[i] = this.multipleSelection[i].id
          }
          console.log(ids)
          deleteActivity(ids).then(response => {
            if(response.data.result === "00000000"){
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              this.getList()
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
        this.getList()
      },
      pageCurrentChange(val) {
        this.listQuery.pageNumber = val
        this.getList()
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

        this.getList()
      },

      refresh(){
        this.listQuery.orderProperty = undefined
        this.listQuery.orderDirection = undefined
        this.listQuery.pageNumber = 1
        this.listQuery.pageSize = 10
        this.$refs['activityTable'].clearSort()
        this.getList()
      },

      selectionChange(val) {
        this.multipleSelection = val;
      },




    }


  }

</script>
