<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm" label-position="right" label-width="100px">

      <sticky :className="'sub-navbar'">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loadingAdd" :disabled="isEdit" style="margin-left: 10px;" type="success" @click="submitFormAdd">发布
        </el-button>
        <el-button v-loading="loadingEdit" :disabled="!isEdit" style="margin-left: 10px;" type="success" @click="submitFormEdit">更新
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item label="名称:" prop="name">
          <el-input style="width: 200px;" class="filter-item" placeholder="名称" v-model="postForm.name" name="name">
          </el-input>
        </el-form-item>
        <el-form-item label="网址:" prop="url">
          <el-input style="width: 200px;" class="filter-item" placeholder="网址" v-model="postForm.url" name="url">
          </el-input>
        </el-form-item>
        <el-form-item label="代码:">
          <el-input style="width: 200px;" class="filter-item" placeholder="代码" v-model="postForm.code">
          </el-input>
        </el-form-item>
        <el-form-item label="电话:">
          <el-input style="width: 200px;" class="filter-item" placeholder="电话" v-model="postForm.tel">
          </el-input>
        </el-form-item>
        <el-form-item label="排序:">
          <el-input-number style="width: 200px;" class="filter-item" v-model="postForm.order" :min="0">
          </el-input-number>
        </el-form-item>
      </div>
    </el-form>

  </div>
</template>

<script>
import Sticky from '@/components/Sticky' // 粘性header组件
import { validateURL } from '@/utils/validate'
import { createDeliveryCorp,getDeliveryCorp,updateDeliveryCorp } from '@/api/sys'
import { goback } from '@/utils/common'


const defaultForm = {
  id: undefined,
  name: undefined,
  url: undefined,
  code: undefined,
  del: undefined,
  order: undefined
}

export default {
  name: 'deliveryCorp',
  components: {
      Sticky
  },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    const isURL = (rule, value,callback)=>{
      if(value===undefined || value===''){
        callback()
      } else if (!validateURL(value)){
        callback(new Error('请输入正确的网址'))
      }else {
        callback()
      }
    }
    return {
      postForm: Object.assign({}, defaultForm),
      loadingAdd: false,
      loadingEdit: false,
      rules: {
        name: [{required: true, message:'名称必填',trigger:'blur'}],
        url: [{required: false, trigger:'blur',validator: isURL}]
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
  },
  methods: {
    fetchData(id) {
      getDeliveryCorp(id).then(response => {
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
          createDeliveryCorp(this.postForm).then(response => {
            console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '新增物流公司成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              this.loadingAdd = false
              setTimeout(() => {
                this.$router.replace({ path: '/sys/delivery_corp/list'})
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '新增物流公司失败',
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
          updateDeliveryCorp(this.postForm).then(response => {
            console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '更新物流公司成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              this.loadingEdit = false
              setTimeout(() => {
                this.$router.replace({ path: '/sys/delivery_corp/list'})
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '更新物流公司失败',
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
