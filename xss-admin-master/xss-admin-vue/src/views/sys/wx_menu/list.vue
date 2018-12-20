<template>
  <div class="app-container">
  <router-link :to="'/sys/wx_menu/add'">
    <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>
  </router-link>
    <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">{{$t('table.refresh')}}</el-button>
    <el-button class="filter-item" style="margin-left: 10px;" @click="handlePublish" type="primary" icon="el-icon-refresh">发布到公众号</el-button>
    <tree-table :data="data" :columns="columns">
      <el-table-column align="center" label="类型" width="60">
        <template slot-scope="scope">
          <span>{{scope.row.typeDesc}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="路径" width="400">
        <template slot-scope="scope">
          <span>{{scope.row.url}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="链接权限" width="100">
        <template slot-scope="scope">
          <span>{{scope.row.categoryDesc}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建日期" width="150">
        <template slot-scope="scope">
          <span>{{scope.row.createDate}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('table.actions')" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <router-link :to="'/sys/wx_menu/edit/'+scope.row.id">
          <el-button type="primary" size="mini" >{{$t('table.edit')}}</el-button>
          </router-link>
          <el-button  size="mini" type="danger" @click="handleDelete(scope.row.id)">{{$t('table.delete')}}
          </el-button>
        </template>
      </el-table-column>
    </tree-table>
  </div>
</template>
<script>
  import treeTable from '@/components/TreeTable'
  import { fetchList,remove, publish } from '@/api/wxMenu'

  export default {
    name: 'wxMenuTable',
    components: { treeTable },
    data() {
      return {
        columns: [{
          text: '名称',
          value: 'name',
        }],
        data:[],
        listLoading: true

      }
    },
    created(){
      this.getList()
    },
    methods: {
      getList(){
          //this.listLoading = true
          fetchList({}).then(response => {
              console.log("response:" + JSON.stringify(response))
              this.data = response.data.data
              setTimeout(() => {
                this.listLoading = false
              }, 1.5 * 1000)
        })
      },
      handleRefresh(){
        this.getList()
      },
      handleDelete(val){
        this.id=val
        this.$confirm('删除无法恢复, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          remove(this.id).then(response => {
            if (response.data.result==='00000000'){
              this.$message({
                message: '删除成功',
                type: 'success'
              })
              this.getList()
            }else {
              this.$message({
                message: response.data.data,
                type: 'error'
              })
            }
          })
        })
      },
      handlePublish(){
        this.$confirm('确认发布到公众号?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          publish({}).then(response => {
            console.log("publish response:" + JSON.stringify(response))
            if (response.data.result==='00000000'){
              this.$message({
                message: response.data.data,
                type: 'success'
              })
            }else {
              this.$message({
                message: response.data.data,
                type: 'error'
              })
            }
          })
        })

      }
    }
  }
</script>
