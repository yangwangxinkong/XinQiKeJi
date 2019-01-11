<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm" labelPosition="right" label-width="150px">

      <sticky className="sub-navbar">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">保存
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item label="名称：" prop="name">
          <el-col :span="5">
            <el-input placeholder="请输入名称" required v-model="postForm.name" name="name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="系统内容：" prop="sysContent">
          <el-col :span="5">
            <el-select v-model="sysContent"  @change="sysContentSe">
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
          </el-col>
        </el-form-item>
        <el-form-item label="链接地址：" prop="url" >
          <el-col :span="5">
            <el-input placeholder="请输入链接地址" required v-model="postForm.url" name="url"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="位置：" prop="position">
          <el-col :span="5">
            <el-select v-model="postForm.position" @change="positionSe">
              <el-option
                v-for="item in position"
                :key="item.value"
                :label="item.name"
                :value="item.value">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item  label="设置：" prop="isBlankTarget">
          <el-checkbox v-model="postForm.isBlankTarget" label="是否新窗口打开" name="isBlankTarget"></el-checkbox>
        </el-form-item>
        <el-form-item label="排序：" prop="order">
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
  import Tinymce from '@/components/Tinymce'
  import Sticky from '@/components/Sticky' // 粘性header组件
  import { validateURL } from '@/utils/validate'
//  import { fetchTree} from '@/api/productCategory'
//  import { articleTree} from '@/api/articleCategory'
  import {goback} from '@/utils/common'
  import url from '@/api/apiUrl'
  import { get,post,execute } from '@/api/server'
  import { info , positionList , save} from '@/api/navigation'


  export default {
    name: 'navigationDetail',
    components: { Sticky,},
    props: {
      isEdit: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        rules: {
          name: [{required: true, message:'名称必填',trigger:'blur'}],
          url: [{required: true, message:'链接必填',trigger:'blur'}],
          position: [{required: true, message:'位置必填',trigger:'blur'}],
        },
        position:[],
        sysContent:'',
        defaultCategory:[{name:"------------",path:""},{name:"首页",path:"/"},{name:"商品分类",path:"/product_category.jhtml"},{name:"友情链接",path:"/friend_link.jhtml"},{name:"会员中心",path:"/member/index.jhtml"}],
        productCategory:[],
        articleCategory:[],
        data:[],
        postForm:{
            id:'',
          name:'',
          position:'',
          isBlankTarget:'false',
          url :'',
          order:''
        },
        loading: false,
        showLogo:false,//控制logo的显示
      }
    },
    created() {
      if (this.isEdit) {
        const id = this.$route.params && this.$route.params.id
        this.fetchData(id)
      }
      this.getProductCategory()
      this.getArticleCategory()
      this.positionList()
    },
    methods: {
      fetchData(id) {
        info({id:id}).then(response => {
          this.postForm.id = response.data.data.id
          this.postForm.name = response.data.data.name
          this.postForm.position = response.data.data.position
          this.postForm.isBlankTarget = response.data.data.isBlankTarget
          this.postForm.url = response.data.data.url
          this.postForm.order = response.data.data.order
        }).catch(err => {
          console.log(err)
        })
      },
      submitForm() {
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

            this.loading = false
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      positionList(){
        positionList().then(response => {
          this.position = response.data.data
        })
      },
      getProductCategory(){
        fetchTree({}).then(response => {
          this.productCategory = response.data.data
        })
      },
      getArticleCategory(){
        get(url.articleCategoryTree,{}).then(response => {
          this.articleCategory = response.data.data
        })
      },
      sysContentSe(val){
        this.postForm.url = val
      },
      positionSe(val){
        this.postForm.position = val
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
