<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm" label-width="100px" label-position="right">

      <sticky className="sub-navbar">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">保存
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item label="上级分类：" prop="parent">
          <el-col :span="5">
            <el-select class="filter-item" v-model="postForm.parent" name="parent" >
              <el-option label="顶级分类" value=""></el-option>
              <el-option
                v-for="(item,index) in articleCategory" :style="'padding-left:' + ((item.grade)*20+20) + 'px'" :key="item.id"  :label="item.name"  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="分类标题：" prop="name">
          <el-col :span="5">
          <el-input placeholder="请输入标题" v-model="postForm.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="分类编码：" prop="code">
          <el-col :span="5">
            <el-input placeholder="编码不可重复" v-model="postForm.code"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="分类图标：" prop="image">
          <el-col :span="5">
            <singleImage color="#1890ff" class="editor-upload-btn" :url="postForm.image" @successCBK="imageSuccessCBK"></singleImage>
          </el-col>
        </el-form-item>
        <el-form-item label="分类排序：" prop="order">
          <el-col :span="5">
          <el-input placeholder="请输入排序" v-model="postForm.order"></el-input>
          </el-col>
        </el-form-item>

      </div>
    </el-form>

  </div>
</template>

<script>
import singleImage from '@/components/Upload/singleImage4'
import Sticky from '@/components/Sticky' // 粘性header组件
import { validateURL } from '@/utils/validate'
import url from '@/api/apiUrl'
import { get,post,execute } from '@/api/server'
import { goback } from '@/utils/common'


const defaultForm = {
  id:undefined,
  name:'',
  parent:undefined,
  code:'',
  seoTitle:'',
  seoKeywords:'',
  seoDescription:'',
  image: '',
  order:''
}

export default {
  name: 'articleCategoryDetail',
  components: { Sticky,singleImage},
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },

  data() {
    const validateCode = (rule, value,callback)=>{
      if(this.postForm.code === this.tmpCode){
        callback()
      }else{
        get(url.articleCategoryCheckCode, {id:this.postForm.id, code:value}).then(response => {
          if(response.data.result==='00000000'){
            if(response.data.data){
              callback(new Error('编号已存在'))
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
      tmpCode:'',
      articleCategory:[],
      imageUrl: '',
      postForm: Object.assign({}, defaultForm),
      loading: false,
      rules: {
        name: [{required: true, message:'名称必填',trigger:'blur'}],
        code: [{validator: validateCode, trigger: 'change' }]
      }
    }
  },
  created() {
    if (this.isEdit) {
      const id = this.$route.params && this.$route.params.id
      this.fetchData(id)
    } else {
      this.postForm = Object.assign({}, defaultForm)
    }
    this.getList()
  },
  methods: {
    getList(){
      get(url.articleCategoryTree, {}).then(response => {
        this.articleCategory = response.data.data
      })
    },
    fetchData(id) {
      get(url.articleCategoryInfo, {id:id}).then(response => {
        this.postForm.id = response.data.data.id
        this.postForm.name  = response.data.data.name
        this.postForm.parent = response.data.data.parent
        this.postForm.code = response.data.data.code
        this.tmpCode = response.data.data.code
        this.postForm.seoTitle  =  response.data.data.seoTitle
        this.postForm.seoKeywords =  response.data.data.seoKeywords
        this.postForm.seoDescription =  response.data.data.seoDescription
        this.postForm.image =  response.data.data.image
        this.postForm.order =  response.data.data.order
      })
    },
    submitForm() {
      this.$refs.postForm.validate(valid => {
        if (valid) {
          this.loading = true
          execute(url.articleCategorySave, this.postForm).then(response => {
            if (response.data.result==='00000000') {
              this.$notify({
                title: '成功',
                message: '操作成功',
                type: 'success',
                duration: 2000
              })
              this.$router.push({
              path:"/content/article_category/list"})
            }else{
              this.$notify({
                title: '失败',
                message: response.data.data,
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
    imageSuccessCBK(path) {
      console.log("path:" + path);
      this.postForm.image = path;
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
