<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="$t('table.title')" v-model="pageable.searchValue">
      </el-input>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelete"  type="danger" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">刷新</el-button>
    </div>
    <el-table ref="multipleTable" :data="data"  v-loading="listLoading"  style="width: 100%" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column align="center" label="名称" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="类型" width="auto">
        <template slot-scope="scope">
          <span>{{$t('tagType.'+scope.row.type)}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="图标" width="auto">
        <template slot-scope="scope">
          <a v-if="scope.row.icon" :href="'http://'+scope.row.icon"
                target="_blank"
                class="buttonText">查看</a>
          <a v-else>-</a>
        </template>
      </el-table-column>
      <el-table-column align="center" label="排序" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.order}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建日期" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.createDate}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('table.actions')" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">{{$t('table.edit')}}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageable.pageNumber" :page-sizes="[10,20,30, 50]" :page-size="pageable.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
    <!--新增及编辑dialog-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" labelPosition="right" label-width="150px" style='width: 400px; margin-left:50px;'>
        <el-form-item  label="名称：" prop="name">
          <el-input v-model="temp.name"></el-input>
        </el-form-item>
        <el-form-item  label="类型：" prop="type">
          <el-select v-if="dialogStatus == 'create'" class="filter-item" v-model="temp.type" >
            <el-option v-for="item in type" :label="$t('tagType.'+item)" :value="item" :key="item">
            </el-option>
          </el-select>
          <el-select v-else class="filter-item" disabled v-model="temp.type" >
            <el-option v-for="item in type" :label="$t('tagType.'+item)" :value="item" :key="item" >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="图标：" prop="icon" >
          <el-col :span="20">
            <singleImage color="#1890ff" class="editor-upload-btn" :url="temp.icon" @successCBK="imageSuccessCBK"></singleImage>
          </el-col>
        </el-form-item>
        <el-form-item  label="备注：" prop="memo">
          <el-input v-model="temp.memo"></el-input>
        </el-form-item>
        <el-form-item  label="排序：" prop="order">
          <el-input v-model="temp.order"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelDialog">{{$t('table.cancel')}}</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">{{$t('table.confirm')}}</el-button>
        <el-button v-else type="primary" @click="createData">{{$t('table.confirm')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import singleImage from '@/components/Upload/singleImage4'
  import { fetchList } from '@/api/tag'
  import { save } from '@/api/tag'
  import { remove } from '@/api/tag'
  import { findType } from '@/api/tag'
  import { info } from '@/api/tag'
  import { validateURL } from '@/utils/validate'

  export default {
    name: 'tagTable',
    components: {
      singleImage
    },
    data(){
      return{
        listLoading: true,
        data:[],
        type:[],
        total:null,
        query:{
          id:''
        },
        rules: {
          name: [{required: true, message:'名称',trigger:'blur'}],
          type: [{required: true, message:'类型必选',trigger:'change'}]
        },
        pageable: {
          pageNumber: 1,
          pageSize: 20,
          orderProperty :'',
          orderDirection:'',
          searchValue:'',
          searchProperty:'name',
          filters:[]
        },
        temp: {
          name:'',
          type:'',
          icon:'',
          memo:'',
          order:'',
          id:undefined
        },
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '新增',
          rules: {

          }
        }
      }
    },
    created(){
      this.getList()
    },
    methods:{
      resetTemp() {
        this.temp = {
          name:'',
          type:'',
          icon:'',
          memo:'',
          order:'',
          id:undefined
        }
      },
      getList(){
        this.listLoading = true
        fetchList(this.pageable).then(response => {
          this.data = response.data.list
          this.total = response.data.total
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
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
      handleUpdate(val){
          this.resetTemp()
          this.query.id = val.id
        findType().then(response => {
          this.type = response.data.data.types
        })
        info(this.query).then(response => {
          this.temp.name = response.data.data.name
          this.temp.type = response.data.data.type
          this.temp.icon = response.data.data.icon
          this.temp.memo = response.data.data.memo
          this.temp.order = response.data.data.order
          this.temp.id = response.data.data.id
        })
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
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
                title: '操作失败',
                message: response.data.msg,
                type: 'error',
                duration: 2000
              })
            }
            this.getList()
          })
        }).catch(err => {
        })
      },
      handleCreate(){
        this.resetTemp()
        findType().then(response => {
          this.type = response.data.data.types
        })
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
      },
      handleRefresh(){
        this.getList()
      },
      createData(){
        this.$refs['dataForm'].validate(valid => {
          if(valid){
            save(this.temp).then(response => {
              if (response.data.result==='00000000') {
                this.$notify({
                  title: '成功',
                  message: '操作成功',
                  type: 'success',
                  duration: 1.5 * 1000
                })
                setTimeout(() => {
                  this.dialogFormVisible = false
                  this.getList()
                }, 1.6 * 1000)
              }else{
                this.$notify({
                  title: '失败',
                  message: '操作失败',
                  type: 'error',
                  duration: 2000
                })
              }
            })
          }
        })
      },
      imageSuccessCBK(path){
        this.temp.icon = path
      },
      handleFilter(){
          this.getList()
      },
      cancelDialog(){
        this.dialogFormVisible = false
        this.$refs['dataForm'].resetFields();
      }
    }
  }
</script>
