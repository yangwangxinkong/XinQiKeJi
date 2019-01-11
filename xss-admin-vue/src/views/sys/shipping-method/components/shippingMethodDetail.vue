<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm" label-position="right" label-width="120px">

      <sticky :className="'sub-navbar'">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loadingAdd" :disabled="isEdit" style="margin-left: 10px;" type="success" @click="submitFormAdd">发布
        </el-button>
        <el-button v-loading="loadingEdit" :disabled="!isEdit" style="margin-left: 10px;" type="success" @click="submitFormEdit">更新
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item label="名称：" prop="name">
          <el-input style="width: 200px;" class="filter-item" placeholder="名称" v-model="postForm.name" name="name">
          </el-input>
        </el-form-item>

        <el-form-item label="默认物流公司：">
          <el-select v-model="postForm.defaultDeliveryCorp" placeholder="请选择">
            <el-option
              v-for="item in deliveryCorpOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
              <span style="float: left">{{ item.name }}</span>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="首重量：" prop="firstWeight">
          <el-input-number style="width: 200px;" class="filter-item" placeholder="首重量" v-model="postForm.firstWeight" name="firstWeight" :min="1">
          </el-input-number>
        </el-form-item>
        <el-form-item label="续重量：" prop="continueWeight">
          <el-input-number style="width: 200px;" class="filter-item" placeholder="续重量" v-model="postForm.continueWeight" name="continueWeight" :min="1">
          </el-input-number>
        </el-form-item>
        <el-form-item label="首重价格：" prop="firstPrice">
          <el-input-number style="width: 200px;" class="filter-item" placeholder="首重价格" v-model="postForm.firstPrice" name="firstPrice" :min="1">
          </el-input-number>
        </el-form-item>
        <el-form-item label="续重价格：" prop="continuePrice">
          <el-input-number style="width: 200px;" class="filter-item" placeholder="续重价格" v-model="postForm.continuePrice" name="continuePrice" :min="1">
          </el-input-number>
        </el-form-item>
        <el-form-item label="排序：">
          <el-input-number style="width: 200px;" class="filter-item" placeholder="排序" v-model="postForm.order" :min="0">
          </el-input-number>
        </el-form-item>

        <el-form-item label="图标：" prop="icon">
          <el-col :span="5">
            <singleImage color="#1890ff" class="editor-upload-btn" @successCBK="imageSuccessCBK" :url="postForm.icon"></singleImage>
          </el-col>
        </el-form-item>
        <el-form-item label="介绍：" prop="description">
          <div class="editor-container">
            <Tinymce :height=400 ref="editor" v-model="postForm.description" />
          </div>
        </el-form-item>

      </div>
    </el-form>

  </div>
</template>

<script>
import singleImage from '@/components/Upload/singleImage4'
import Tinymce from '@/components/Tinymce'
import Sticky from '@/components/Sticky' // 粘性header组件
import { createShippingMethod,getShippingMethod,getDeliveryCorpList,updateShippingMethod } from '@/api/sys'
import { goback } from '@/utils/common'

const defaultForm = {
  id: undefined,
  name: undefined, // 名称
  firstWeight: undefined, // 页面标题
  continueWeight: undefined, // 页面关键词
  firstPrice: undefined, // 页面描述
  continuePrice: undefined, // 页面描述
  order: undefined, // 排序
  defaultDeliveryCorp:undefined,
  description:undefined,
  icon:''//图标
}

export default {
  name: 'shippingMethod',
  components: {
      Tinymce,Sticky,singleImage
  },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      postForm: Object.assign({}, defaultForm),
      loadingAdd: false,
      loadingEdit: false,
      userListOptions: [],
      deliveryCorpOptions: [],
      rules: {
        name: [{required: true, message:'名称必填',trigger:'blur'}],
        firstWeight: [{required: true, message:'首重量必填,单位：克',trigger:'change'}],
        continueWeight: [{required: true, message:'续重量必填,单位：克',trigger:'change'}],
        firstPrice: [{required: true, message:'首重价格必填',trigger:'change'}],
        continuePrice: [{required: true, message:'续重价格必填',trigger:'change'}]
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
    this.getDeliveryCorpOptions()
  },
  methods: {
    fetchData(id) {
      getShippingMethod(id).then(response => {
        this.postForm = response.data.data
      }).catch(err => {
        console.log(err)
      })
    },
    submitFormAdd() {
      console.log(this.postForm)
      this.$refs.postForm.validate(valid => {
        if (valid) {
          this.loadingAdd = true
          createShippingMethod(this.postForm).then(response => {
            console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '新增配送方式成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              this.loadingAdd = false
              setTimeout(() => {
                this.$router.replace({ path: '/sys/shipping_method/list'})
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '新增配送方式失败',
                type: 'error',
                duration: 2000
              })
              this.loadingAdd = false
            }
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    submitFormEdit() {
      console.log(this.postForm)
      this.$refs.postForm.validate(valid => {
        if (valid) {
          this.loadingEdit = true
          updateShippingMethod(this.postForm).then(response => {
            console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '修改配送方式成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              this.loadingEdit = false
              setTimeout(() => {
                this.$router.replace({ path: '/sys/shipping_method/list'})
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '修改配送方式失败',
                type: 'error',
                duration: 2000
              })
              this.loadingEdit = false
            }
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    getDeliveryCorpOptions() {
      getDeliveryCorpList({pageNumber: 1,pageSize: 100}).then(response => {
        this.deliveryCorpOptions = response.data.list
      })
    },
    imageSuccessCBK(path) {
      console.log("path:" + path);
      this.postForm.icon = path;
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
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
