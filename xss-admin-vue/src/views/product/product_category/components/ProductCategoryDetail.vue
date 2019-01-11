<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm" label-width="100px" label-position="right">

      <sticky className="sub-navbar">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">保存
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item label="分类标题：" prop="name">
          <el-col :span="5">
          <el-input placeholder="请输入标题" v-model="postForm.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="上级分类：" prop="parent.id">
          <el-col :span="5">
            <el-select v-model="postForm.parent.id" placeholder="顶级分类">
              <el-option label="顶级分类" value=""></el-option>
              <el-option
                v-for="item in data" :style="'padding-left:' + ((item.grade)*20+20) + 'px'"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="分类图标：" prop="image">
          <el-col :span="5">
            <singleImage color="#1890ff" class="editor-upload-btn" @successCBK="imageSuccessCBK"></singleImage>
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
import { fetchTree,info,save } from '@/api/productCategory'
import { goback } from '@/utils/common'


const defaultForm = {

  name: '', // 标题
  parent:{id:undefined},//上级分类
  image:'',//分类图标
  order:'',//排序
  id: undefined
}

export default {
  name: 'productCategoryDetail',
  components: { Sticky,singleImage},
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      imageUrl: '',
      data:[],
      postForm: Object.assign({}, defaultForm),
      loading: false,
      rules: {
        name: [{required: true, message:'名称必填',trigger:'blur'}]
      }
    }
  },
  created() {
    if (this.isEdit) {
      const id = this.$route.params && this.$route.params.id
      console.log("id:"+id);
      this.fetchData(id)
    } else {
      this.postForm = Object.assign({}, defaultForm)
    }
    this.getList()
  },
  methods: {
    getList(){
      fetchTree({}).then(response => {
        this.data = response.data.data
      })
    },
    fetchData(id) {
      info(id).then(response => {
        console.log(JSON.stringify(response.data))
        const data = response.data.data;
        this.postForm.id = data.id
        this.postForm.name = data.name
        this.postForm.image = data.image
        this.imageUrl = data.image
        this.postForm.order = data.order
        if(data.parent){
          this.postForm.parent.id = data.parent.id;
        }
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
              this.$router.push({
              path:"/product/product_category/list"})
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
