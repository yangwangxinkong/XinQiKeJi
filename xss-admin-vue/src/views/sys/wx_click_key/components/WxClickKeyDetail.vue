<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm" label-width="150px" label-position="right">

      <sticky className="sub-navbar">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">保存
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item label="关键字名称：" prop="name">
          <el-col :span="5">
          <el-input placeholder="请输入关键字名称" v-model="postForm.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="关键字：" prop="keyString">
          <el-col :span="5">
          <el-input placeholder="请输入关键字" v-model="postForm.keyString"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="关键字分类：" prop="clickType">
          <el-col :span="5">
            <el-select v-model="postForm.clickType" placeholder="关键字分类">
              <el-option label="图文" value="PICTURE"></el-option>
              <el-option label="文字" value="WORD"></el-option>
            </el-select>
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
import { save,info } from '@/api/wxClickKey'

const defaultForm = {
  name: '',               // 关键字名称
  keyString: undefined,  // 关键字
  clickType: undefined,    // 关键字分类
  id: undefined
}

export default {
  name: 'WxClickKeyDetail',
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
  },
  methods: {

    fetchData(id) {
      info(id).then(response => {
        console.log(JSON.stringify(response.data.data))
        const data = response.data.data;
        this.postForm = data
        this.postForm.clickType = data.clickTypeValue;
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
              path:"/sys/wx_click_key/list"})
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
