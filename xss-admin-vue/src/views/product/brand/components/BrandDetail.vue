<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm" label-width="100px" label-position="right">

      <sticky className="sub-navbar">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">保存
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item label="名称：" prop="name">
          <el-col :span="5">
          <el-input placeholder="请输入名称" required v-model="postForm.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="类型：" prop="type">
          <el-col :span="5">
            <el-select v-model="postForm.type"  @change="currentSel">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="logo：" prop="logo" v-if="showLogo">
          <el-col :span="5">
            <singleImage color="#1890ff" class="editor-upload-btn" @successCBK="imageSuccessCBK"></singleImage>
          </el-col>
        </el-form-item>
        <el-form-item label="网址：" prop="url">
          <el-col :span="5">
          <el-input placeholder="请输入网址" v-model="postForm.url"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="排序：" prop="order">
          <el-col :span="5">
            <el-input-number style="width: 200px;" class="filter-item" placeholder="请输入排序" v-model="postForm.order" name="order" :min="1">
            </el-input-number>
          </el-col>
        </el-form-item>
        <!--介绍-->
        <div class="editor-container">
          <Tinymce :height=400 ref="editor" v-model="postForm.introduction" />
        </div>
      </div>
    </el-form>

  </div>
</template>

<script>
import singleImage from '@/components/Upload/singleImage4'
import Tinymce from '@/components/Tinymce'
import Sticky from '@/components/Sticky' // 粘性header组件
import { validateURL } from '@/utils/validate'
import { info,save } from '@/api/brand'
import { goback } from '@/utils/common'

const defaultForm = {
  name: '', // 标题
  logo:'',//logo
  type:'text',//类型
  url:'',//网址
  order:'',//排序
  introduction:'',//介绍
  id: undefined
}

export default {
  name: 'brandDetail',
  components: { Sticky,singleImage, Tinymce},
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {

    return {
      options: [
        {
          value: 'text',
          label: '文本'
        },
        {
          value: 'image',
          label: '图片'
        }
      ],

      imageUrl: '',
      data:[],
      postForm: Object.assign({}, defaultForm),
      loading: false,
      userListOptions: [],
      rules: {
        name: [{required: true, message:'名称必填',trigger:'blur'}]
      },
      showLogo:false,//控制logo的显示
    }
  },
  created() {
    if (this.isEdit) {
      const id = this.$route.params && this.$route.params.id
      this.fetchData(id)
    } else {
      this.postForm = Object.assign({}, defaultForm)
    }
  },
  methods: {
    fetchData(id) {
      info(id).then(response => {
        console.log(JSON.stringify(response.data))
        const data = response.data.data;
        this.postForm.id = data.id
        this.postForm.name = data.name
        this.postForm.logo = data.logo
        this.imageUrl = data.logo
        this.postForm.type = data.type
        this.postForm.url = data.url
        this.postForm.order = data.order
        this.postForm.introduction = data.introduction
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
              path:"/product/brand/list"})
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
    currentSel(val){
      (val=="image")?this.showLogo=true:this.showLogo=false
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
