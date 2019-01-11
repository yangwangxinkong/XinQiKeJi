<template>
  <el-table :data="list" border fit highlight-current-row style="width: 100%">
    <el-table-column align="center" label="ID" width="65"  v-loading="loading"
                     element-loading-text="请给我点时间！">
      <template slot-scope="scope">
        <span>{{scope.row.id}}</span>
      </template>
    </el-table-column>
    <el-table-column width="auto" align="center" label="Name">
      <template slot-scope="scope">
        <span>{{scope.row.name}}</span>
      </template>
    </el-table-column>
    <el-table-column width="auto" align="center" label="Position">
      <template slot-scope="scope">
        <span>{{scope.row.position}}</span>
      </template>
    </el-table-column>
    <el-table-column width="auto" align="center" label="isBlankTarget">
      <template slot-scope="scope">
        <span>{{scope.row.isBlankTarget}}</span>
      </template>
    </el-table-column>
    <el-table-column width="auto" align="center" label="orders">
      <template slot-scope="scope">
        <span>{{scope.row.order}}</span>
      </template>
    </el-table-column>
    <el-table-column align="center" :label="$t('table.actions')" width="230" class-name="small-padding fixed-width">
      <template slot-scope="scope">
        <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">{{$t('table.edit')}}</el-button>
        <el-button  size="mini" type="danger" @click="handleDelete(scope.row.id)">{{$t('table.delete')}}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
  import { findList } from '@/api/navigation'
  import { deleteById } from '@/api/navigation'

  export default {
    props: {
      type: {
        type: String,
        default: 'top'
      }
    },
    data() {
      return {
        list: null,
        listQuery: {
          position: this.type
        },
        deleteIds:{
          ids:''
        },
        loading: false
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getList() {
        this.loading = true
        findList(this.listQuery).then(response => {
          this.list = response.data.data
          this.loading = false
        })
      },
      refresh(){
        this.getList()
      },
      handleUpdate(row){
        this.$emit("update-data",row)
      },
      handleDelete(query){
        this.$confirm('删除无法恢复, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteIds.ids = query
          deleteById(this.deleteIds).then(response => {
            if (response.data.result==='00000000'){
              this.$message({
                message: '删除成功',
                type: 'success'
              })
              this.getList()
            }else {
              this.$message({
                message: '删除失败',
                type: 'error'
              })
            }
          })
        })
      }
    }
  }
</script>
