<template>
  <div class="createPost-container">
    <el-form :rules="rules" ref="dataForm" :model="temp" labelPosition="right" label-width="100px">
      <sticky :className="'sub-navbar'">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loadingAdd"  style="margin-left: 10px;" type="success" @click="submitFormAdd">保存
        </el-button>
      </sticky>
      <div class="createPost-main-container" >
      <el-form-item  label="名称：" prop="title">
        <el-col :span="6">
           <el-input v-model="temp.title" name="title" maxlength='200'></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="文章分类：" prop="articleCategory" >
        <el-select class="filter-item" v-model="temp.articleCategory" name="articleCategory" @change="selectChange">
          <el-option
            v-for="(item,index) in articleCategory" :style="'padding-left:' + ((item.grade)*20+20) + 'px'"  :key="item.id" :label="item.name"  :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item  label="文章编码：" prop="code">
        <el-col :span="6">
        <el-input v-model="temp.code"></el-input>
         </el-col>
      </el-form-item>
      <el-form-item label="作者：" prop="author">
          <el-col :span="6">
        <el-input v-model="temp.author"></el-input>
          </el-col>
      </el-form-item>
      <el-form-item  label="设置：" prop="setting">
        <el-checkbox v-model="temp.isPublication" label="是否发布" name="isPublication"></el-checkbox>
        <el-checkbox v-model="temp.isTop" label="是否置顶" name="isTop"></el-checkbox>
      </el-form-item>
      <el-form-item  label="标签：" prop="tag">
        <el-checkbox-group v-model="tagId" @change="changeTag">
          <el-checkbox v-for="item in tagTypes" :key="item.id" :label="item.id">{{item.name}}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="文章封面图：" prop="photos" >
          <singleImage color="#1890ff" class="editor-upload-btn" :url="temp.cover" @successCBK="imageSuccessCBK" style="width:250px"></singleImage>
      </el-form-item>
      <el-form-item label="简介：" prop="introduction">
        <el-col :span="5">
          <el-input  style='width: 500px;' type="textarea" :rows="10" placeholder="请输入简介"  v-model="temp.introduction" ></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="内容：" prop="content" >
      <div class="editor-container">
        <Tinymce :height=400 ref="editor" v-model="content" />
      </div>
      </el-form-item>
      <el-form-item labelWidth="100px" label="页面标题：" prop="seoTitle">
        <el-col :span="5">
        <el-input v-model="temp.seoTitle"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item labelWidth="100px" label="页面关键词：" prop="seoKeywords">
        <el-col :span="5">
        <el-input v-model="temp.seoKeywords"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item labelWidth="100px" label="页面描述：" prop="seoDescription">
        <el-col :span="5">
        <el-input v-model="temp.seoDescription"></el-input>
        </el-col>
      </el-form-item>
       </div>
    </el-form>
  </div>
</template>
<script>
  import Tinymce from '@/components/Tinymce'
  import {goback} from '@/utils/common'
  import Sticky from '@/components/Sticky'
  import { info } from '@/api/article'
  import { save } from '@/api/article'
  import { findTypeList } from '@/api/tag'
//  import { articleTree} from '@/api/articleCategory'
  import url from '@/api/apiUrl'
  import { get,post,execute } from '@/api/server'
  import singleImage from '@/components/Upload/singleImage4'
  import { validateURL } from '@/utils/validate'

  export default {
    name: 'articleMethod',
    components: {
      Tinymce,
      Sticky,
      singleImage
    },
    props: {
      isEdit: {
        type: Boolean,
        default: false
      }
    },
      data(){
          return{
            type:"article",
            loadingAdd: false,
            loadingEdit: false,
            query:{
              id:''
            },
            rules: {
              title: [{required: true, message:'标题必填',trigger:'blur'}],
              articleCategory: [{required: true, message:'文章分类必选',trigger:'change'}],
            },
            tagTypes:[],
            tagId:[],
            content:'',
            articleCategory:[],
            temp: {
                id:'',
              title:'',
              articleCategory:'',
              code:'',
              author:'小豆社保',
              isPublication:'false',
              tagIds:[],
              isTop:'false',
              cover:'',
              introduction:'',
              content:'',
              seoTitle:'',
              seoKeywords:'',
              seoDescription:''
            }
          }
      },
    created(){
      this.tagData()
      if (this.isEdit) {
        this.query.id = this.$route.params && this.$route.params.id
        this.fetchData()
      }
      this.getArticleCategory()
    },
    methods:{
      fetchData() {
        info(this.query).then(response => {
          this.temp.id = response.data.data.id
          this.temp.articleCategory = response.data.data.articleCategory.id
          this.tagId = response.data.data.tagList
          this.temp.title = response.data.data.title
          this.temp.code = response.data.data.code
          this.temp.author = response.data.data.author
          this.temp.isPublication = response.data.data.isPublication
          this.temp.isTop = response.data.data.isTop
          this.temp.cover = response.data.data.cover
          this.temp.introduction = response.data.data.introduction
          this.content = response.data.data.content;
          this.temp.seoTitle = response.data.data.seoTitle
          this.temp.seoKeywords = response.data.data.seoKeywords
          this.temp.seoDescription = response.data.data.seoDescription
        }).catch(err => {
          console.log(err)
        })
      },
      getArticleCategory(){
        get(url.articleCategoryTree, {}).then(response => {
          this.articleCategory = response.data.data
        })
      },
      tagData(){
        findTypeList(this.type).then(response => {
            this.tagTypes = response.data.data
        }).catch(err => {
          console.log(err)
        })
      },
      submitFormAdd(){
        this.$refs['dataForm'].validate(valid => {
          if(valid){
              this.temp.content = this.content
            save(this.temp).then(response => {
              if (response.data.result==='00000000') {
                this.$notify({
                  title: '成功',
                  message: '操作成功',
                  type: 'success',
                  duration: 2000
                })
                goback(this.$route.path)
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
      changeTag(val){
      this.temp.tagIds = val
      },
      imageSuccessCBK(path){
        this.temp.cover = path
      },
      selectChange(val){
          this.temp.articleCategory = val
      },
      closePage(){
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

