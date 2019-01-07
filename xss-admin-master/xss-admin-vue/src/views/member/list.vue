<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="$t('table.username')" v-model="listQuery.username">
      </el-input>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="$t('table.mobile')" v-model="listQuery.mobile">
      </el-input>
      <el-select v-model="listQuery.isVip" clearable placeholder="是否VIP会员" class="filter-item" style="width: 140px;">
        <el-option
          v-for="item in isVipOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-select v-model="listQuery.isNew" clearable placeholder="是否新手" class="filter-item" style="width: 140px;">
        <el-option
          v-for="item in isNewOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <router-link :to="'/member/add'">
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>
      </router-link>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelte" type="danger" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">刷新</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="exportMember" type="primary" icon="el-icon-refresh">导出</el-button>
    </div>

    <el-table ref="multipleTable" :data="list" v-loading="listLoading" fit tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange" @sort-change='sortChange'>
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>

      <el-table-column
        prop="username"
        label="用户名"
        width="120"
        sortable="custom"
        show-overflow-tooltip>
      </el-table-column>
      <el-table-column
        prop="nickName"
        label="昵称"
        width="120"
        sortable="custom"
        show-overflow-tooltip>
      </el-table-column>
      <el-table-column
        prop="name"
        label="姓名"
        width="120"
        sortable="custom"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="mobile"
        label="手机号码"
        show-overflow-tooltip>
      </el-table-column>
      <!--<el-table-column
        prop="openId"
        label="openId"
        width="220"
        show-overflow-tooltip>
      </el-table-column>-->

      <el-table-column
        prop="createDate"
        label="创建日期"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="memberStatus"
        label="会员状态">
      </el-table-column>

      <el-table-column
        prop="isVip"
        label="VIP">
        <template slot-scope="scope">
          <span v-if="scope.row.isVip">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>

      <el-table-column
        prop="isNew"
        label="新手">
        <template slot-scope="scope">
          <span v-if="scope.row.isNew">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="120">
        <template slot-scope="scope">
          <router-link :to="'/member/edit/'+scope.row.id">
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
  import url from '@/api/apiUrl.js'
  import { get,post,execute } from '@/api/server.js'
  import { parseTime } from '@/utils'
  export default {
    name: 'memberTable',
    directives: {
      waves
    },
    data() {
      return {
        isVipOptions: [{
          value: 'true',
          label: '是'
        }, {
          value: 'false',
          label: '否'
        }],
        isNewOptions: [{
          value: 'true',
          label: '是'
        }, {
          value: 'false',
          label: '否'
        }],
        list: [],
        total: 0,
        listLoading: true,
        multipleSelection: [],
        listQuery: {
          pageNumber: 1,
          pageSize: 10,
          name: undefined,
          username: undefined,
          mobile: undefined,
          isVip: undefined,
          isNew: undefined,
          searchProperty: undefined,
          searchValue: undefined,
          orderProperty: undefined,
          orderDirection: undefined,
          memberData:[]
        }
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = true
        get(url.memberList, this.listQuery).then(response => {
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
          get(url.memberDelete, {ids:ids}).then(response => {
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '删除会员成功',
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
      },

      // 导出用户数据
      exportMember(){
        post(url.exportMember, {}).then(response => {
          if(response.data.result === "00000000"){
            this.memberData = response.data.data
            console.log("memberData:" + JSON.stringify(this.memberData))
            this.exportExcel()
            this.$message({
              type: 'success',
              message: '导出用户信息成功!'
            });
          }else{
            this.$message({
              type: 'error',
              message: '导出用户信息失败!'
            });
          }
        })
      },

      exportExcel() {
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = ['账号','名称','号码','类型']
          const filterVal = ['userName', 'name', 'mobile', 'type']
          const list = this.memberData
          const data = this.formatJson(filterVal, list)
          console.log("data:" + data);
          const now = new Date()
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: 'member_'+now.getFullYear()+now.getMonth()+now.getDay()+now.getHours()+(now.getMinutes()+1)+now.getSeconds(),
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
