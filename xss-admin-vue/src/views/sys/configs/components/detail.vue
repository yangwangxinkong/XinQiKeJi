<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm" label-width="100px">
      <sticky className="sub-navbar">
         <el-button @click="closePage();">返回</el-button>
         <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">  {{$t('table.save')}}
        </el-button>
      </sticky>
      <div class="createPost-main-container">
        <el-row>
          <el-col :span="15">
            <el-form-item  label="编码：" class="postInfo-container-item" prop="code">
              <el-input placeholder="输入唯一编码" v-model="postForm.code" v-if="postForm.isSystem" disabled=""></el-input>
              <el-input placeholder="输入唯一编码" v-model="postForm.code" v-else></el-input>
            </el-form-item>
            <el-form-item  label="编码值：" class="postInfo-container-item" prop="codeValue">
              <el-input placeholder="输入编码值" v-model="postForm.codeValue" ></el-input>
            </el-form-item>
            <el-form-item  label="备注：" class="postInfo-container-item" prop="memo">
              <el-input placeholder="输入备注" v-model="postForm.memo" ></el-input>
            </el-form-item>
            <el-form-item label="系统内置：" prop="isSystem">
              <el-col :span="5">
                <el-checkbox :checked="postForm.isSystem" v-model="postForm.isSystem" >系统内置</el-checkbox>
              </el-col>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
    </el-form>


  </div>
</template>

<script>
import url from '@/api/apiUrl.js'
import { get,post,execute } from '@/api/server.js'
import Sticky from '@/components/Sticky' // 粘性header组件
import { goback } from '@/utils/common'

const defaultForm = {
  code: undefined,      //编码值
  codeValue: undefined, //编码值,
  memo: undefined,      //备注
  isSystem: false,     //系统内置
  id: undefined //编号
}

export default {
  name: 'detail',
  components: { Sticky},
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      postForm: Object.assign({}, defaultForm),
      loading: false,
      userListOptions: [],
      //表单验证
      rules: {
        code: [{required: true, message:'编码必填',trigger:'blur'}],
        codeValue: [{required: true, message:'编码值必填',trigger:'blur'}]
      }
    }
  },
  computed: {
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
      get(url.configInfo, {id:id}).then(response => {
        this.postForm = response.data.data
      }).catch(err => {
        console.log(err)
      })
    },
    submitForm() {
      this.$refs['postForm'].validate(valid => {
        if (valid) {
          this.loading = true
          execute(url.configSave, this.postForm).then(response => {
            //result
            var isSuc=response.data.msg=='success';
            if(isSuc){
              this.showMsg('成功',this.isEdit?'更新成功':'保存成功','success');
              goback(this.$route.path);
            }else{
              this.showMsg('失败',response.data.data,'error');
            }
          }).catch(err => {
            console.log(err)
            this.showMsg('接口异常',err,'error');
          })
        } else {
          console.log('error submit!!')
          this.showMsg('系统异常','刷新页面再试一次，如果任然错误，请联系管理员！','error');
          return false
        }
      })
    },
    showMsg(title,msg,type) {//显示消息 返回商品列表
      this.$notify({title: title, message: msg,type: type, duration: 2000 });
      this.loading = false;
    },
    closePage() {//返回商品列表
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
