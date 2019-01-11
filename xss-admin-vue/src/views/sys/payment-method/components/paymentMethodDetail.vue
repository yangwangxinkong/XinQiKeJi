<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm" label-width="120px" label-position="right">

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

        <el-form-item label="方式：" prop="method">
          <el-select class="filter-item" v-model="postForm.method" placeholder="请选择">
            <el-option v-for="item in  statusOptions" :key="item" :label="$t('paymentMethod.'+item)" :value="item" name="method">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="支持配送方式：">
          <el-checkbox-group v-model="postForm.shippingMethods">
            <el-checkbox v-for="item in shippingMethodsOptions" :label="item.id"  :key="item.id">{{item.name}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="超时时间：">
          <el-input style="width: 200px;" class="filter-item" placeholder="超时时间" v-model="postForm.timeout">
          </el-input>
        </el-form-item>
        <el-form-item label="介绍：">
          <el-input style="width: 200px;" class="filter-item" placeholder="介绍" v-model="postForm.description">
          </el-input>
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

        <el-form-item label="内容：" prop="content">
          <div class="editor-container">
            <Tinymce :height=400 ref="editor" v-model="postForm.content" />
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
import { getPaymentMethod,getPaymentMethodInfo,getShippingMethodList,createPaymentMethod,updatePaymentMethod } from '@/api/sys'
import {getToken} from '@/utils/auth'
import { goback } from '@/utils/common'

const defaultForm = {
  id: undefined,
  name: undefined,
  method: undefined,
  timeout: undefined,
  description: undefined,
  order: undefined,
  content: undefined,
  icon:'',
  shippingMethods:[]
}

export default {
  name: 'paymentMethod',
  components: {
      Tinymce,singleImage,Sticky
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
        method: [{required: true, message:'方式必选',trigger:'blur'}],
      },
      statusOptions: [],
      shippingMethodsOptions:[],
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
    this.getShippingMethodsOptions()
  },
  methods: {
    fetchData(id) {
      getPaymentMethodInfo(id).then(response => {
        console.log(response.data.data)
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
          createPaymentMethod(this.postForm).then(response => {
            console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '新增支付方式成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              this.loadingAdd = false
              setTimeout(() => {
                this.$router.replace({ path: '/sys/payment_method/list'})
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '新增支付方式失败',
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
          updatePaymentMethod(this.postForm).then(response => {
            console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '修改支付方式成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              this.loadingEdit = false
              setTimeout(() => {
                this.$router.replace({ path: '/sys/payment_method/list'})
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '修改支付方式失败',
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
      getPaymentMethod({}).then(response=>{
          console.log(response.data.data.data)
        this.statusOptions = response.data.data.data
      })
    },
    getShippingMethodsOptions() {
      getShippingMethodList({pageNumber: 1,pageSize: 100}).then(response => {
        this.shippingMethodsOptions = response.data.list
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
