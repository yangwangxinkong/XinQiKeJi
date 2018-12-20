<template>
  <div class="app-container">
  <router-link :to="'/product/product_category/add'">
    <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>
  </router-link>
    <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">{{$t('table.refresh')}}</el-button>
    <tree-table :data="data" >
      <el-table-column v-if="false" align="center" label="ID" width="auto"  v-loading="loading"
                             element-loading-text="请给我点时间！">
        <template slot-scope="scope">
          <span>{{scope.row.id}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="名称" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="排序" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.order}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('table.actions')" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <router-link :to="'/product/product_category/edit/'+scope.row.id">
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
  import { fetchList,remove } from '@/api/productCategory'

  export default {
    name: 'productCategoryTable',
    components: { treeTable },
    data() {
      return {
        data:[],
        listLoading: true,
        temp: {
          name:'',
          parent:[],
          seoTitle:'',
          seoKeywords:'',
          seoDescription:'',
          order:''
        },
        id:'',
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '新增',
          rules: {
//            type: [{ required: true, message: 'type is required', trigger: 'change' }],
//            timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
//            title: [{ required: true, message: 'title is required', trigger: 'blur' }]
          }
        }
      }
    },
    created(){
      this.getList()
    },
    methods: {
      resetTemp() {
        this.temp = {
          name:'',
          parent:[],
          seoTitle:'',
          seoKeywords:'',
          seoDescription:'',
          order:''
        }
      },
      getList(){
          //this.listLoading = true
          fetchList({}).then(response => {
          this.data = response.data.data
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        })
      },
      handleCreate(){
        this.resetTemp()
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
      },
      handleRefresh(){
        this.getList()
      },
      handleUpdate(row){
        findById(row.id).then(response => {
          this.temp.name  = response.data.data.name
          this.temp.seoTitle  =  response.data.data.seoTitle
          this.temp.seoKeywords =  response.data.data.seoKeywords
          this.temp.seoDescription =  response.data.data.seoDescription
          this.temp.order =  response.data.data.order
        })
        this.dialogStatus = 'update'
        this.dialogFormVisible = true

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
      createData(){

      },
      updateData(){

      }
    }
  }
</script>
