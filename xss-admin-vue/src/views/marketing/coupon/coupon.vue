<template>
  <div class="app-container">

    <div class="filter-container">
      <router-link :to="'/marketing/coupon/add'" style="margin-right: 10px">
        <el-button type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>
      </router-link>
      <el-button type="danger" icon="el-icon-delete" @click="deleteCoupon">{{$t('table.delete')}}</el-button>
      <el-button type="primary" icon="el-icon-refresh" @click="refresh">{{$t('refresh')}}</el-button>
    </div>

    <el-table :data="tableData" ref="couponTable" v-loading="listLoading" @sort-change="sort" @selection-change="selectionChange"
              style="width: 100%" fit highlight-current-row>
      <el-table-column type="selection" width="40">
      </el-table-column>
      <el-table-column prop="name" :label="$t('coupon.name')" show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="prefix" :label="$t('coupon.prefix')" show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="beginDate" :label="$t('coupon.useStartDate')" show-overflow-tooltip>

      </el-table-column>
      <el-table-column prop="endDate" :label="$t('coupon.useEndDate')" show-overflow-tooltip>

      </el-table-column>
      <el-table-column prop="isEnabled" :label="$t('coupon.isEnabled')" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.isEnabled" class="el-icon-check" style="color: green;"></span>
          <span v-else class="el-icon-close" style="color: red"></span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" :label="$t('coupon.operation')" width="320">
        <template slot-scope="scope">
          <el-button @click="buildCoupon(scope.row.id)" type="primary" size="mini">{{$t('coupon.build')}}</el-button>
          &nbsp;
          <router-link :to="'/marketing/coupon/edit/'+scope.row.id">
            <el-button type="primary" size="mini">{{$t('table.edit')}}</el-button>
          </router-link>
          &nbsp;
          <router-link :to="'/marketing/coupon/code_list/'+scope.row.id">
            <el-button type="primary" size="mini">优惠码列表</el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination background @size-change="pageSizeChange" @current-change="pageCurrentChange" :current-page="listQuery.pageNumber" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>


    <!--// 生成优惠码-->
    <el-dialog :title="$t('coupon.build')" :visible.sync="couponBuildFormVisible">
      <el-form :rules="buildCouponRules" ref="couponBuildForm" :model="couponBuild" label-position="right" label-width="150px">
        <el-form-item :label="$t('coupon.name')+':'">
          <span>{{couponBuild.name}}</span>
        </el-form-item>
        <el-form-item :label="$t('coupon.useStartDate')+':'">
          <span>{{couponBuild.beginDate | parseTime('{y}-{m}-{d}')}}</span>
        </el-form-item>
        <el-form-item :label="$t('coupon.useEndDate')+':'">
          <span>{{couponBuild.endDate | parseTime('{y}-{m}-{d}')}}</span>
        </el-form-item>
        <el-form-item :label="$t('coupon.generatedQuantity')+':'">
          <span>{{couponBuild.totalCount}}</span>
        </el-form-item>
        <el-form-item :label="$t('coupon.useNumber')+':'">
          <span>{{couponBuild.usedCount}}</span>
        </el-form-item>
        <el-form-item :label="$t('coupon.buildNumber')+':'" prop="buildNumber">
          <el-input-number v-model="couponBuild.buildNumber" style="width: 350px;" :placeholder="$t('inputPositiveInteger')" :min="1"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="couponBuildFormVisible = false">{{$t('table.cancel')}}</el-button>
        <el-button type="primary" @click="downloadCoupon()" :loading="buildCouponConfirmLoding">{{$t('table.confirm')}}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import { fetchList, deleteCoupon, buildCoupon, downloadCoupon} from '@/api/marketing/coupon'
  import waves from '@/directive/waves' // 水波纹指令
  import { parseTime } from '@/utils'

  export default {
    name: 'couponTable',
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

        coupon: {
          id: undefined,
          name: '',
          prefix: '',
          beginDate: undefined,
          endDate: undefined,
          isEnabled: undefined
        },
        couponBuild: {
          id: undefined,
          name: undefined,
          beginDate: undefined,
          endDate: undefined,
          totalCount: undefined,
          usedCount: undefined,
          buildNumber: 1
        },
        buildCouponRules: {
          buildNumber: [
            {required: true, message: '必填', trigger: 'blur'}],
        },
        couponBuildFormVisible: false,
        buildCouponConfirmLoding: false,
        multipleSelection: [],
        // 优惠码数据，用来生成excel
        couponCodeData: [],


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
        }).catch(e => {
          console.log(e)
          this.listLoading = false
        })
      },

      // 删除优惠券
      deleteCoupon(){
        if(this.multipleSelection.length<1){
          this.$message("请选择至少一个删除项！");
          return
        }
        this.$confirm("确定删除所选优惠券吗？", '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const ids = new Array()
          for(var i=0; i<this.multipleSelection.length; i++){
            ids[i] = this.multipleSelection[i].id
          }
          console.log(ids)
          deleteCoupon(ids).then(response => {
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

      // 生成优惠码页面获取数据
      buildCoupon(couponId){
        buildCoupon(couponId).then(response => {
          if(response.data.result === "00000000"){
            response.data.data.buildNumber = 1
            this.couponBuild = response.data.data
            this.couponBuildFormVisible = true
            this.$nextTick(() => {
              this.$refs['couponBuildForm'].clearValidate()
            })
          }else{
            this.$message({
              type: 'error',
              message: "获取优惠券信息失败"
            });
          }
        }).catch(e => {
          console.log(e)
        })
      },

      // 生成优惠码
      downloadCoupon(){
        this.$refs['couponBuildForm'].validate((valid) => {
          if (valid) {
            downloadCoupon(this.couponBuild.id, this.couponBuild.buildNumber).then(response => {
              if(response.data.result === "00000000"){
                this.couponCodeData = response.data.data
                this.exportExcel()
                this.$refs['couponBuildForm'].resetFields()
                this.couponBuildFormVisible = false
                this.$message({
                  type: 'success',
                  message: '生成优惠码成功!'
                });
              }else{
                this.$message({
                  type: 'error',
                  message: '生成优惠码失败!'
                });
              }
            }).catch(e => {
              console.log(e)
            })
          }
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
        this.$refs['couponTable'].clearSort()
        this.fetchList()
      },

      selectionChange(val) {
        this.multipleSelection = val;
        console.log(this.multipleSelection)
      },

      // 生成优惠码excel文件
      exportExcel() {
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = ['优惠码']
          const filterVal = ['code']
          const list = this.couponCodeData
          const data = this.formatJson(filterVal, list)
          const now = new Date()
          console.log(now)
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: 'coupon_'+now.getFullYear()+now.getMonth()+now.getDay()+now.getHours()+(now.getMinutes()+1)+now.getSeconds(),
            autoWidth: true
          })
        })
      },
      formatJson(filterVal, jsonData) {
        return jsonData.map(v => filterVal.map(j => {
          if (j === 'timestamp') {
            return parseTime(v[j])
          } else {
            return v[j]
          }
        }))
      }




    }


  }

</script>
