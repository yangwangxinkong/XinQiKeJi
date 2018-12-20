<template>
  <div class="app-container">

    <div class="filter-container">

      <el-button class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-edit">{{$t('table.add')}}</el-button>
      <!-- <el-button class="filter-item" style="margin-left: 10px;" @click="createFile" type="primary" icon="el-icon-edit">生成城市级联文件</el-button> -->

      <router-link v-if="parentId!=undefined" :to="{path: '/sys/city/list',query: {id:parent_parentId}}">
        <el-button class="filter-item" type="primary" icon="el-icon-arrow-up">上级城市</el-button>
      </router-link>
    </div>

      <table class="el-table" v-loading="listLoading">
        <tr class="el-table__row">
          <th colspan="5" class="is-center">
            <span v-if="parentId===undefined">
              顶级城市
            </span>
            <span v-else>上级城市-{{parentName}}</span>
          </th>
        </tr>
        <tbody>
          <tr v-for="(row,i) in listTemp">
            <td v-for="(cell,j) in row" width="500">
              <router-link :to="{path: '/sys/city/list',query: {id: cell.id}}">
                {{cell.name}}
              </router-link>
              <a href="javascript:void(0)" @click="handleUpdate(cell)">[编辑]</a>
              <a href="javascript:void(0)" @click="handleDelete(cell.id)">[删除]</a>
            </td>
          </tr>
          <tr v-if="list.length===0">
            <td colspan="5" class="is-center">无下级城市</td>
          </tr>
        </tbody>
      </table>

<!-- 新增及编辑dialog -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="textMap.rules" :model="temp"  label-position="right" label-width="100px" style='width: 400px; margin-left:50px;'>
        <el-form-item label="上级城市：" prop="parent">
          <el-input v-model="temp.parent" v-show></el-input>
          <span v-if="parentId!=undefined">{{parentName}}</span>
          <span v-else>顶级城市</span>
        </el-form-item>
        <el-form-item label="城市名称：" prop="name">
          <el-input v-model="temp.name" name="name" style="width: 200px;"></el-input>
        </el-form-item>
        <el-form-item label="城市编码：" prop="code">
          <el-input v-model="temp.code" name="code" style="width: 200px;"></el-input>
        </el-form-item>
        <el-form-item label="排序：" prop="orders">
          <el-input-number v-model="temp.order" :min="0"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{$t('table.cancel')}}</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">{{$t('table.confirm')}}</el-button>
        <el-button v-else type="primary" @click="updateData">{{$t('table.confirm')}}</el-button>
      </div>
    </el-dialog>
<!-- 新增及编辑dialog -->

  </div>
</template>

<script>
import treeTable from '@/components/TreeTable'
import url from '@/api/apiUrl.js'
import { get,post,execute } from '@/api/server.js'

export default {
  name: 'cityList',
  components: { treeTable },
  data() {
    const validateCode = (rule, value,callback)=>{
      if(this.temp.code === this.tmpCode){
        callback()
      }else{
        get(url.cityCheckCode, {id:this.temp.id, code:value}).then(response => {
          if(response.data.result==='00000000'){
            if(response.data.data){
              callback(new Error('城市编号已存在'))
            } else {
              callback()
            }
          } else {
            callback()
          }
        })
      }
    }

    return {
      listLoading: true,
      list: [],
      parentId: undefined,
      parentName: undefined,
      parent_parentId: undefined,
      temp: {
        id:'',
        name:'',
        code:'',
        isMaster: false,// 是否总站
        parent:'',
        order:undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增',
        rules: {
          name: [{required: true, message:'名称必填',trigger:'blur'}],
          code: [{required: true, message:'名称必填',trigger:'blur'},
                  {validator: validateCode, trigger: 'change' }]
        }
      }
    }
  },
  computed: {
    listTemp: function () {
      var list = this.list;
      var arrTemp = [];
      var index = 0;
      var sectionCount = 5;
      for (var i = 0; i < list.length; i++) {
        index = parseInt(i / sectionCount);
        if (arrTemp.length <= index) {
          arrTemp.push([]);
        }
        arrTemp[index].push(list[i]);
      }
      return arrTemp;
    }
  },
  created(){
    const id = this.$route.query && this.$route.query.id
    console.log(id)
    this.getList(id)
  },
  methods:{
    resetTemp() {
      this.temp = {
        id:'',
        name:'',
        parent:'',
        order:undefined
      }
    },
    getList(id){
      this.listLoading = true
      let param = {}
      if(id != undefined){
        param.parentId=id
      }
      get(url.cityChildren, param).then(response => {
      //getAreaList(param).then(response => {
        console.log(response.data.data)
        this.list = response.data.data.citys
        this.parentId = response.data.data.parentId
        this.parentName = response.data.data.parentName
        this.parent_parentId = response.data.data.parent_parentId
        this.temp.parent = response.data.data.parentId
        this.listLoading = false
      }).catch((e)=>{
        console.log(e)
        this.listLoading = false
      })
    },
    handleCreate() {
      this.resetTemp()
      this.temp.parent=this.parentId
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleUpdate(cell) {
      //console.log(cell)
      this.resetTemp()
      this.temp.id=cell.id
      this.temp.name=cell.name
      this.temp.code=cell.code
      this.temp.order=cell.order
      this.temp.parent=this.parentId
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      //console.log(this.temp)
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          execute(url.citySave, this.temp).then(response => {
          //createArea(this.temp).then(response => {
            console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '添加城市成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              setTimeout(() => {
                this.dialogFormVisible = false
                this.getList(this.parentId)
                this.createLocalFile()
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '添加城市失败',
                type: 'error',
                duration: 1.5 * 1000
              })
            }
          }).catch((e)=>{
            console.log(e)
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    updateData(){
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          execute(url.cityUpdate, this.temp).then(response => {
          //updateArea(this.temp).then(response => {
            //console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              setTimeout(() => {
                this.dialogFormVisible = false
                this.getList(this.parentId)
                this.createLocalFile()
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '更新城市失败',
                type: 'error',
                duration: 1.5 * 1000
              })
            }
          }).catch((e)=>{
            console.log(e)
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    handleDelete(id) {
      this.$confirm('删除无法恢复, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //deleteArea(id).then(response => {
        post(url.cityDelete, {id:id}).then(response => {
          if (response.data.result==='00000000'){
            this.$notify({
              title: '成功',
              message: '删除城市成功',
              type: 'success',
              duration: 2000
            })
            this.getList(this.parentId)
            this.createLocalFile()
          }else {
            this.$notify({
              title: '失败',
              message: response.data.msg,
              type: 'error',
              duration: 2000
            })
          }
        })
      }).catch(err => {
        console.log(err)
      })
    },
    // createLocalFile(){
    //   createAreaFile().then(response => {
    //   })
    // },
    // createFile(){
    //   createAreaFile().then(response => {
    //     if (response.data.result==='00000000'){
    //       this.$message({
    //         message: '生成文件成功',
    //         type: 'success'
    //       })
    //     }else {
    //       this.$message({
    //         message: response.data.msg,
    //         type: 'error'
    //       })
    //     }
    //   })
    // }
  }
}
</script>
