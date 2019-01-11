<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm">

      <sticky className="sub-navbar">
        <el-button @click="closePage();">返回</el-button>
        <!--<el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">保存
        </el-button>-->
      </sticky>

      <div class="createPost-main-container">
        <el-form-item labelWidth="150px" label="反馈用户：" prop="username">
          <span>{{postForm.member.username}}</span>
        </el-form-item>
        <el-form-item labelWidth="150px" label="手机号码：" prop="phone">
          <span>{{postForm.phone}}</span>
        </el-form-item>
        <el-form-item labelWidth="150px" label="反馈类型：" prop="typeDesc">
          <span>{{postForm.typeDesc}}</span>
        </el-form-item>
        <el-form-item labelWidth="150px" label="反馈内容：" prop="content">
          <span>{{postForm.content}}</span>
        </el-form-item>
      </div>
    </el-form>

  </div>
</template>

<script>

import Sticky from '@/components/Sticky' // 粘性header组件
import { validateURL, validMobile } from '@/utils/validate'
import url from '@/api/apiUrl.js'
import { get,post,execute, download } from '@/api/server.js'
import { goback } from "@/utils/common";

const defaultForm = {
  id: undefined,
  member:{
      id: undefined,
      mobile: undefined,
      username: undefined
  },
  phone: undefined,
  typeDesc: undefined,
  content: undefined
}

export default {
  name: 'feedbackDetail',
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
      postForm: Object.assign({}, defaultForm),
      loading: false
    }
  },
  created() {
    const id = this.$route.params && this.$route.params.id
    this.fetchData(id);
    this.postForm = Object.assign({}, defaultForm)
  },
  methods: {
    fetchData(id) {
      get(url.feedbackInfo, {id:id}).then(response => {
        console.log(JSON.stringify(response.data))
        const data = response.data.data;
        this.postForm = data

      }).catch(err => {
        console.log(err)
      })
    },
    closePage() {//返回列表
      goback(this.$route.path);
    }
    // downloadImg(filePath) {
    //   download(url.imgDownload, {filePath: filePath}).then(response => {
    //   })
    // }
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
