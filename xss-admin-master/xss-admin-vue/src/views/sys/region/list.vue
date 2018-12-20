<template>
  <div class="app-container">

    <div class="filter-container">
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-edit">{{$t('table.add')}}</el-button>
      <router-link v-if="parentId!=undefined" :to="{path: '/sys/region/list',query: {id:parent_parentId}}">
        <el-button class="filter-item" type="primary" icon="el-icon-arrow-up">上级区域</el-button>
      </router-link>
    </div>

      <table class="el-table" v-loading="listLoading">
        <tr class="el-table__row">
          <th colspan="5" class="is-center">
            <span v-if="parentId===undefined">
              顶级区域
            </span>
            <span v-else>上级区域-{{parentName}}</span>
          </th>
        </tr>
        <tbody>
          <tr v-for="(row,i) in listTemp">
            <td v-for="(cell,j) in row" width="500">
              <router-link :to="{path: '/sys/region/list',query: {id: cell.id}}">
                {{cell.name}}
              </router-link>
              <a href="javascript:void(0)" @click="handleUpdate(cell)">[编辑]</a>
              <a href="javascript:void(0)" @click="handleDelete(cell.id)">[删除]</a>
            </td>
          </tr>
          <tr v-if="list.length===0">
            <td colspan="5" class="is-center">无下级区域</td>
          </tr>
        </tbody>
      </table>

    <!--新增及编辑dialog--------start------->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible"  >
      <el-form ref="dataForm" :rules="textMap.rules" :model="temp"  label-position="right" label-width="100px" style='width: 400px; margin-left:50px;'>
        <el-form-item  label="上级区域：" prop="parent">
          <el-input v-model="temp.parent" v-show></el-input>
          <span v-if="parentId!=undefined">{{parentName}}</span>
          <span v-else>顶级区域</span>
        </el-form-item>
        <el-form-item  label="名称：" prop="name">
          <el-input v-model="temp.name" name="name" style="width: 200px;"></el-input>
        </el-form-item>

        <el-form-item label="所属地区：" prop="areaSelect">
          <areaCascader @successCBK="areaSuccessCBK" ref="areaCascaderRef"></areaCascader>
        </el-form-item>

        <el-form-item  label="排序：" prop="orders">
          <el-input-number v-model="temp.order" :min="0"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{$t('table.cancel')}}</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">{{$t('table.confirm')}}</el-button>
        <el-button v-else type="primary" @click="updateData">{{$t('table.confirm')}}</el-button>
      </div>
    </el-dialog>
    <!--新增及编辑dialog--------end------->


  </div>
</template>

<script>
import treeTable from '@/components/TreeTable'
import { getRegionList,createRegion,updateRegion,deleteRegion,getAreaOptions } from '@/api/sys'
import areaCascader from '@/components/cascader/areaCascader1'

export default {
  name: 'regionList',
  components: { treeTable,areaCascader },
  data() {
    return {
      listLoading: true,
      list: [],
      parentId: undefined,
      parentName: undefined,
      parent_parentId: undefined,
      temp: {
        id:'',
        name:'',
        parent:'',
        order:undefined,
        areaId:''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增',
        rules: {
          name: [{required: true, message:'名称必填',trigger:'blur'}],
        }
      },
      areaOptions: [],
      selectedAreaOptions: []
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
        order:undefined,
        areaId:''
      }
    },
    getList(id){
      this.listLoading = true
      let param = {}
      if(id != undefined){
        param.parentId=id
      }
      getRegionList(param).then(response => {
        this.list = response.data.data.regions
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
//      this.selectedAreaOptions=[]
      this.resetTemp()
      this.temp.parent=this.parentId
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.areaCascaderRef.selectOptions=[]
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleUpdate(cell) {
      console.log(cell)
      this.dialogStatus = 'update'
      this.resetTemp()
      this.temp.id=cell.id
      this.temp.name=cell.name
      this.temp.order=cell.order
      this.temp.parent=this.parentId
      this.selectedAreaOptions=[]
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
      const area = cell.area
      if(area){
        let treePath = area.treePath;
        treePath += area.id;
        treePath = treePath.substring(1);
        let areaIds = treePath.split(",");
        this.selectedAreaOptions = [];
        for(var i in areaIds){
          this.selectedAreaOptions.push(parseInt(areaIds[i]));
        }
      }
      //Dom更新完才执行
      this.$nextTick(() => {
        this.$refs.areaCascaderRef.selectOptions=this.selectedAreaOptions
      })
    },
    createData() {
      this.temp.areaId = this.selectedAreaOptions[this.selectedAreaOptions.length-1]
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          console.log(this.temp)
          createRegion(this.temp).then(response => {
            console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '添加区域成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              setTimeout(() => {
                this.dialogFormVisible = false
                this.getList(this.parentId)
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '添加区域失败',
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
      this.temp.areaId = this.selectedAreaOptions[this.selectedAreaOptions.length-1]
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          updateRegion(this.temp).then(response => {
            console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '更新区域成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              setTimeout(() => {
                this.dialogFormVisible = false
                this.getList(this.parentId)
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '更新区域失败',
                type: 'error',
                duration: 1.5 * 1000
              })
            }
          }).catch((e)=>{
            console.log(e)
          })
        } else{
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
        deleteRegion(id).then(response => {
          if (response.data.result==='00000000'){
            this.$message({
              message: '删除区域成功',
              type: 'success'
            })
            this.getList(this.parentId)
          }else {
            this.$message({
              message: '删除区域失败',
              type: 'error'
            })
          }
        })
      }).catch(err => {
        console.log(err)
      })
    },
    areaSuccessCBK(selectAreas) {
      this.selectedAreaOptions = selectAreas;
    },
  }
}
</script>
