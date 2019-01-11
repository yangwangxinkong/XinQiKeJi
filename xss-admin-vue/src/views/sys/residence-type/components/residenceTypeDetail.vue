<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm" label-width="120px" label-position="right">

      <sticky :className="'sub-navbar'">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loadingAdd" v-if="isEdit" style="margin-left: 10px;" type="success" @click="submitFormAdd">更新
        </el-button>
        <el-button v-loading="loadingEdit" v-else style="margin-left: 10px;" type="success" @click="submitFormEdit">保存
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item label="所属城市：" prop="city">
          <el-col :span="5">
            <el-select v-model="postForm.city.id"  placeholder="所属城市" class="filter-item">
              <el-option
                v-for="item in cityCategory" :style="'padding-left:' + ((item.grade)*20+20) + 'px'" :key="item.id"  :label="item.name"  :value="item.id">
              </el-option>
            </el-select>

          </el-col>
        </el-form-item>

        <el-form-item label="名称：" prop="name">
          <el-input style="width: 200px;" class="filter-item" placeholder="名称" v-model="postForm.name" name="name">
          </el-input>
        </el-form-item>

        <el-form-item label="排序：" prop="order">
          <el-input-number style="width: 200px;" class="filter-item" placeholder="排序" v-model="postForm.order" :min="0">
          </el-input-number>
        </el-form-item>
      </div>
    </el-form>

  </div>
</template>

<script>
import Sticky from '@/components/Sticky' // 粘性header组件
import {getToken} from '@/utils/auth'
import { goback } from '@/utils/common'
import url from '@/api/apiUrl'
import { get, post, execute } from '@/api/server'

const defaultForm = {
  id: undefined,
  name: undefined,
  city: {id:undefined},//城市
  order: undefined
}

export default {
  name: 'residenceType',
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
    return {
      postForm: Object.assign({}, defaultForm),
      loadingAdd: false,
      loadingEdit: false,
      cityCategory: [],
      rules: {
        name: [{required: true, message:'名称必填',trigger:'blur'}],
        city: [{required: true, message:'城市必选',trigger:'blur'}],
      }
    }
  },
  created() {
    this.getCityList()
    if (this.isEdit) {
      const id = this.$route.params && this.$route.params.id
      this.fetchData(id)
    } else {
      this.postForm = Object.assign({}, defaultForm)
    }
  },
  methods: {
    getCityList() {
      get(url.cityCategoryTree, {}).then(response => {
        this.cityCategory = response.data.data
      })
    },

    fetchData(id) {
      //getPaymentMethodInfo(id).then(response => {
      get(url.residenceTypeInfo, {id:id}).then(response => {
        //console.log(response.data.data)
        this.postForm = response.data.data
      }).catch(err => {
        console.log(err)
      })
    },
    submitFormAdd() {
      //console.log(this.postForm)
      this.$refs.postForm.validate(valid => {
        if (valid) {
          this.loadingAdd = true
          //createPaymentMethod(this.postForm).then(response => {
          execute(url.residenceTypeSave, this.postForm).then(response => {
            //console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '新增户口性质成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              this.loadingAdd = false
              setTimeout(() => {
                this.$router.replace({ path: '/sys/residence_type/list'})
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '新增户口性质失败',
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
      //console.log(this.postForm)
      this.$refs.postForm.validate(valid => {
        if (valid) {
          this.loadingEdit = true
          //updatePaymentMethod(this.postForm).then(response => {
          execute(url.residenceTypeSave, this.postForm).then(response => {
            //console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '修改户口性质成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              this.loadingEdit = false
              setTimeout(() => {
                this.$router.replace({ path: '/sys/residence_type/list'})
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '修改户口性质失败',
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
