<template>
  <div class="tab-container">
    <el-button class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>
    <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">{{$t('table.refresh')}}</el-button>
    <el-tabs style='margin-top:15px;' v-model="activeName" type="border-card">
      <el-tab-pane v-for="item in tabMapOptions" :label="item.label" :key='item.key' :name="item.key">
        <keep-alive>
          <tab-pane v-if='activeName==item.key' :type='item.key' v-on:update-data="updateDatab"></tab-pane>
        </keep-alive>
      </el-tab-pane>
    </el-tabs>
    <tab-pane  v-show="false" ref="tabPane"></tab-pane>
    <!--新增及编辑dialog-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="temp"  label-position="left" label-width="70px" style='width: 400px; margin-left:50px;'>
        <el-form-item labelWidth="100px" label="名称：" prop="name">
          <el-input v-model="temp.name"></el-input>
        </el-form-item>
        <el-form-item labelWidth="100px" label="系统内容：" prop="sysContent">
          <el-select class="filter-item" v-model="temp.sysContent" @change="selectChange">
            <el-option
              v-for="item in defaultCategory" :key="item.path" :label="item.name" :value="item.path">
            </el-option>
            <el-option
              v-for="(item,index) in articleCategory" :style="'padding-left:' + ((item.grade)*20+20) + 'px'" :key="item.path" :label="item.name"  :value="item.path">
            </el-option>
            <el-option
              v-for="(item,index) in productCategory" :style="'padding-left:' + ((item.grade)*20+20) + 'px'" :key="item.path" :label="item.name"  :value="item.path">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item labelWidth="100px" label="链接地址：" prop="url">
          <el-input v-model="temp.url"></el-input>
        </el-form-item>
        <el-form-item labelWidth="100px" label="位置：" prop="position">
          <el-select class="filter-item" v-model="temp.position" >
            <el-option v-for="item in  position" :key="item.name" :label="item.name" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item labelWidth="100px" label="设置：" prop="isBlankTarget">
          <el-checkbox v-model="temp.isBlankTarget" label="是否新窗口打开" name="isBlankTarget"></el-checkbox>
        </el-form-item>
        <el-form-item labelWidth="100px" label="排序：" prop="order">
          <el-input v-model="temp.order"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{$t('table.cancel')}}</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">{{$t('table.confirm')}}</el-button>
        <el-button v-else type="primary" @click="updateResult">{{$t('table.confirm')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import tabPane from './components/navigationTabBak.vue'
  import { positionList } from '@/api/navigation'
  import { save } from '@/api/navigation'
  import {update} from '@/api/navigation'
  import { fetchTree} from '@/api/productCategory'
  import { articleTree} from '@/api/articleCategory'
  import ElOption from "../../../../node_modules/element-ui/packages/select/src/option";

  export default {
    name: 'tab',
    components: {
      ElOption,
      tabPane },
    data() {
      return {
        tabMapOptions: [
          { label: '顶部', key: 'top' },
          { label: '中间购物', key: 'middleShopping' },
          { label: '中间服务', key: 'middleService' },
          { label: '底部', key: 'bottom' }
        ],
        activeName: 'top',
        position:'',
        defaultCategory:[{name:"------------",path:""},{name:"首页",path:"/"},{name:"商品分类",path:"/product_category.jhtml"},{name:"友情链接",path:"/friend_link.jhtml"},{name:"会员中心",path:"/member/index.jhtml"}],
        productCategory:[],
        articleCategory:[],
        temp: {
          name:'',
          sysContent:[],
          position:[],
          isBlankTarget:'',
          url:'',
          order:''
        },
        jduduUrl: 'http://jdudu.sptxmall.com',
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
    methods: {
      resetTemp() {
        this.temp = {
          name:'',
          sysContent:[],
          position:[],
          isBlankTarget:'',
          url :'',
          order:''
        }
      },
      getProductCategory(){
        fetchTree({}).then(response => {
          this.productCategory = response.data.data
        })
      },
      getArticleCategory(){
        articleTree({}).then(response => {
          this.articleCategory = response.data.data
        })
      },
      handleCreate(){
        this.resetTemp()
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.positionList()
        this.getProductCategory()
        this.getArticleCategory()
      },
      handleRefresh(){
          this.$refs.tabPane.refresh()
      },
      updateDatab(row){
        positionList().then(response => {
          this.position = response.data.data
          for (var i=0;i<this.position.length;i++){
              if(row.position == this.position[i].name){
                this.temp = Object.assign({}, row)
                this.temp.position = this.position[i].value
                if (row.isBlankTarget == "是"){
                  this.temp.isBlankTarget = true
                }else {
                  this.temp.isBlankTarget = false
                }
                break
              }
          }
          this.temp.sysContent = ''
          this.dialogStatus = 'update'
          this.dialogFormVisible = true
        })
      },
      updateResult(){
        update(this.temp).then(response => {
          if (response.data.result==='00000000') {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
            this.$refs.tabPane.refresh()
          }else {
            this.$notify({
              title: '失败',
              message: '更新失败',
              type: 'error',
              duration: 2000
            })
          }
        })
      },
      positionList(){
        positionList().then(response => {
          this.position = response.data.data
        })
      },
      createData(){
        save(this.temp).then(response => {
          if (response.data.result==='00000000') {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.$refs.tabPane.refresh()
          }else {
            this.$notify({
              title: '失败',
              message: '创建失败',
              type: 'error',
              duration: 2000
            })
          }
        })
      },
      selectChange(val){
         this.temp.url = val
      }
    }
  }
</script>

<style scoped>
  .tab-container{
    margin: 30px;
  }
</style>
