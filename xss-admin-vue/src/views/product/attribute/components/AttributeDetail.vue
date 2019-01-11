<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm"  label-width="100px" label-position="right">
      <sticky className="sub-navbar">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">保存
        </el-button>
      </sticky>
      <div class="createPost-main-container">
        <el-form-item label="分类：" prop="productCategory.id">
          <el-col :span="5">
            <el-select :disabled="disable"  v-model="postForm.productCategory.id" placeholder="商品分类">
              <el-option
                v-for="item in data" :style="'padding-left:' + ((item.grade)*20+20) + 'px'"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="名称：" prop="name">
          <el-col :span="5">
          <el-input placeholder="请输入名称" required v-model="postForm.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="排序：" prop="order">
          <el-col :span="5">
            <el-input-number style="width: 200px;" class="filter-item" placeholder="请输入排序" v-model="postForm.order" name="order" :min="1">
            </el-input-number>
          </el-col>
        </el-form-item>
        <div class="filter-container">
          <el-button class="filter-item" @click="handleCreateOption" type="success">添加可选项</el-button>
        </div>
        <el-table class="el-tb-edit" :data="options" ref="optionTable" border fit highlight-current-row empty-text="点击‘添加可选项’按钮" >
          <el-table-column prop="option" label="可选项">
            <template slot-scope="scope">
              <el-input v-model="scope.row.option" size="mini" controls-position="right"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" @click="delOption(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-form>

  </div>
</template>

<script>
import Sticky from '@/components/Sticky' // 粘性header组件
import { validateURL } from '@/utils/validate'
import { info,save } from '@/api/attribute'
import { fetchProductCategoryTree } from '@/api/productCategory'
import { goback } from '@/utils/common'


const defaultForm = {
  name: '', // 标题
  order:'',//排序,
  productCategory:{id: undefined},//商品分类
  optionArr:[],//可选项
  id: undefined
}

export default {
  name: 'attributeDetail',
  components: { Sticky},
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {

    return {
      data:[],
      options: [],
      disable:false,
      postForm: Object.assign({}, defaultForm),
      loading: false,
      userListOptions: [],
      rules: {
        name: [{required: true, message:'名称必填',trigger:'blur'}]
      }
    }
  },
  created() {
    if (this.isEdit) {
      const id = this.$route.params && this.$route.params.id
      this.disable = true
      this.fetchData(id)
    } else {
      this.postForm = Object.assign({}, defaultForm)
    }
    this.initProductCategory()
  },
  methods: {
    initProductCategory(){
      fetchProductCategoryTree({}).then(response => {
        this.data = response.data.data
      })
    },
    handleCreateOption() {
      var optionObj = {
        option: ""
      };
      this.options.push(optionObj);
      setTimeout(() => {
        this.$refs.optionTable.setCurrentRow(optionObj);
      }, 10); //<==用于延时渲染后选中这行
    },
    delOption(index, r) {
      console.log(index, r);
      this.options.splice(index, 1);
    },
    fetchData(id) {
      info(id).then(response => {
        const data = response.data.data;
        this.postForm.id = data.id
        this.postForm.name = data.name
        this.postForm.productCategory = data.productCategory
        this.postForm.order = data.order
        this.options = data.options
      }).catch(err => {
        console.log(err)
      })
    },
    submitForm() {
      if(this.options.length == 0){
        this.$notify({
          title: '失败',
          message: '必须至少添加一个可选项',
          type: 'error',
          duration: 2000
        })
        return false;
      }
      var validFlag = true;
      for(var p in this.options){
        if(this.options[p].option == ""){
          validFlag = false;
        }
      }
      if (!validFlag){
        this.$notify({
          title: '失败',
          message: '必须填写可选项',
          type: 'error',
          duration: 2000
        })
        return false;
      }
      this.postForm.optionArr = this.options
      //console.log("postForm:" + JSON.stringify(this.postForm))
      this.$refs.postForm.validate(valid => {
        if (valid) {
          this.loading = true
          save(this.postForm).then(response => {
            if (response.data.result==='00000000') {
              this.$notify({
                title: '成功',
                message: '操作成功',
                type: 'success',
                duration: 2000
              })
              this.$router.push({
              path:"/product/attribute/list"})
            }else{
              this.$notify({
                title: '失败',
                message: '操作失败',
                type: 'error',
                duration: 2000
              })
            }
          })

          this.loading = false
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    closePage() {//返回列表
      goback(this.$route.path);
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
@import "src/styles/mixin.scss";
.createPost-container {
  position: relative;
  .createPost-main-container {
    padding: 40px 45px 20px 50px;
    .postInfo-container {
      position: relative;
      @include clearfix;
      margin-bottom: 10px;
      .postInfo-container-item {
        float: left;
      }
    }
    .editor-container {
      min-height: 500px;
      margin: 0 0 30px;
      .editor-upload-btn-container {
        text-align: right;
        margin-right: 10px;
        .editor-upload-btn {
          display: inline-block;
        }
      }
    }
  }
  .word-counter {
    width: 40px;
    position: absolute;
    right: -10px;
    top: 0px;
  }
}
</style>
