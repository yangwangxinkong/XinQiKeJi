<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="'名称'" v-model="pageable.searchValue">
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
      <el-table-column align="center" label="宽度" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.width}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="高度" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.height}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="类型" width="auto">
        <template slot-scope="scope">
          <span>{{scope.row.typeDesc}}</span>
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
      <el-form ref="dataForm" :rules="rules" :model="temp"  label-position="right" label-width="150px" style='width: 400px; margin-left:50px;'>
        <el-form-item  label="名称：" prop="name">
          <el-input v-model="temp.name" name="name"></el-input>
        </el-form-item>
        <el-form-item  label="宽度：" prop="width">
          <el-input v-model="temp.width" name="width" placeholder="必须大于1"></el-input>
        </el-form-item>
        <el-form-item label="高度：" prop="height">
          <el-input v-model="temp.height" name="height" placeholder="必须大于1"></el-input>
        </el-form-item>
        <el-form-item labelWidth="150px" label="类型：" prop="type">
          <el-select v-model="temp.type">
            <el-option
              v-for="item in adPositionTypes"
              :key="item.name"
              :label="item.desc"
              :value="item.name">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item  label="描述：" prop="description">
          <el-input v-model="temp.description" name="description"></el-input>
        </el-form-item>
        <!--<el-form-item label="模板：" prop="template">-->
          <!--<el-input style='width: 400px;' type="textarea" :rows="9" placeholder="请输入内容" v-model="temp.template" name="template"></el-input>-->
        <!--</el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{$t('table.cancel')}}</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">{{$t('table.confirm')}}</el-button>
        <el-button v-else type="primary" @click="updateData">{{$t('table.confirm')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import url from '@/api/apiUrl'
  import { get,post,execute } from '@/api/server'
  export default {
    name: 'adPositionTable',
    data(){
      return{
        data:[],
        adPositionTypes:[],
        total: null,
        listLoading: true,
        multipleSelection: [],
        temp: {
          id:'',
          name:'',
          width:'',
          height:'',
          description:'',
          template:'default',
          type:undefined
        },
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '新增',
          rules: {

          }
        },
        rules: {
          name: [{ required: true, message: '名称必填', trigger: 'change' }],
          width:[{ required: true, message: '宽度必填', trigger: 'change' }],
          height:[{ required: true, message: '高度必填', trigger: 'change' }],
//          template:[{ required: true, message: '模板必填', trigger: 'change' }],
        },
        pageable: {
          pageNumber: 1,
          pageSize: 20,
          orderProperty : 'id',
          orderDirection:'desc',
          searchValue:'',
          searchProperty:'name',
          filters:[]
        }
      }
    },
    created(){
      this.getList()
      this.initTypes()
    },
    methods:{
      initTypes(){
        get(url.adPositionTypes, {}).then(response => {
          this.adPositionTypes = response.data.data
          this.temp.type = this.adPositionTypes[0].name;
          console.log(this.temp.type)
        })
      },
      resetTemp() {
        this.temp = {
          id:'',
          name:'',
          width:'',
          height:'',
          description:'',
          template:'default',
          type:this.temp.type
        }
      },
      getList(){
        this.listLoading = true
        get(url.adPositionList, this.pageable).then(response => {
          this.data = response.data.list
          this.total = response.data.total
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
          get(url.adPositionDelete,{ids:ids}).then(response => {
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
                message:'操作失败',
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
      },
      handleUpdate(row){
        this.resetTemp()
        get(url.adPositionInfo, {id:row.id}).then(response => {
          this.temp.id = response.data.data.id
          this.temp.name = response.data.data.name
          this.temp.width = response.data.data.width
          this.temp.height = response.data.data.height
          this.temp.description = response.data.data.description
          this.temp.template = response.data.data.template
          this.temp.type = response.data.data.type
        })
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
      },
      createData(){
        this.$refs['dataForm'].validate(valid => {
            if(valid){
              execute(url.adPositionSave,this.temp).then(response => {
                if(response.data.result==='00000000'){
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
                    message:response.data.data,
                    type: 'error',
                    duration: 2000
                  })
                }
              })
            }
        })
      },
      updateData(){
        this.$refs['dataForm'].validate(valid => {
          if(valid){
            execute(url.adPositionSave,this.temp).then(response => {
              if(response.data.result==='00000000'){
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
                  message:response.data.data,
                  type: 'error',
                  duration: 2000
                })
              }
            })
          }
        })
      },
      handleSizeChange(val) {
        this.pageable.pageSize = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.pageable.pageNumber = val
        this.getList()
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      handleFilter(){
        this.getList()
      }
    }
  }
</script>
