<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm" label-width="150px" label-position="right">

      <sticky className="sub-navbar">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">保存
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item label="所属事件：" prop="wxClickKey.id">
          <el-col :span="10">
            <el-select v-model="postForm.wxClickKey.id" placeholder="所属事件">
              <el-option
                v-for="item in wxClickKeys"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="响应名称：" prop="title">
          <el-col :span="10">
          <el-input placeholder="请输入响应名称" v-model="postForm.title"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="响应描述：" prop="content">
          <el-col :span="10">
          <el-input type="textarea" placeholder="请输入响应描述" :rows="5" v-model="postForm.content"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="图片：" prop="postForm.imagePath">
          <el-col :span="10">
            <singleImage color="#1890ff" class="editor-upload-btn" @successCBK="imageSuccessCBK"></singleImage>
          </el-col>
        </el-form-item>
        <el-form-item label="菜单授权：" prop="category">
          <el-col :span="10">
            <el-select v-model="postForm.category" placeholder="菜单类型">
              <el-option label="不授权" value="notAuthorize"></el-option>
              <el-option label="页面授权" value="authorize"></el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="跳转URL：" prop="url">
          <el-col :span="10">
            <el-input placeholder="请输入跳转URL" v-model="postForm.url"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="排序：" prop="order">
          <el-col :span="10">
            <el-input-number style="width: 200px;" class="filter-item" placeholder="请输入排序" v-model="postForm.order" name="order" :min="1">
            </el-input-number>
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
import { goback } from '@/utils/common'
import { save,info, selectWxClickKey } from '@/api/wxClickValue'

const defaultForm = {
  title: '',               // 响应名称
  content: undefined,      // 响应描述
  wxClickKey:{id:undefined},//所属事件
  imagePath:undefined,      //图片
  category:undefined,       //是否授权
  url:undefined,            //跳转url
  order:undefined,          //排序
  id: undefined
}

export default {
  name: 'WxClickKeyDetail',
  components: { Sticky, singleImage},
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      wxClickKeys:[],
      postForm: Object.assign({}, defaultForm),
      loading: false,
      rules: {
        name: [{required: true, message:'关键字名称必填',trigger:'blur'}],
        keyString: [{required: true, message:'关键字必填',trigger:'blur'}]
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
    this.getWxClickKeys();
  },
  methods: {
    getWxClickKeys(){
      selectWxClickKey().then(response => {
        this.wxClickKeys = response.data.data;
        console.log(JSON.stringify(this.wxClickKeys))
        this.postForm.wxClickKey.id = this.wxClickKeys[0].id;
      }).catch(err => {
        console.log(err)
      })
    },
    fetchData(id) {
      info(id).then(response => {
        console.log(JSON.stringify(response.data.data))
        const data = response.data.data;
        this.postForm.id = data.id
        this.postForm.title = data.title;
        this.postForm.content = data.content;
        this.postForm.keyString = data.keyString;
        this.postForm.order = data.order;
        this.postForm.wxClickKey.id = data.clickKeyId;
        this.postForm.category = data.category;
        this.postForm.url = data.url;
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
              path:"/sys/wx_click_value/list"})
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
      this.postForm.imagePath = path;
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
