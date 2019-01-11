<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm" label-width="100px" label-position="right">

      <sticky className="sub-navbar">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">保存
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item label="菜单名称：" prop="name">
          <el-col :span="5">
          <el-input placeholder="请输入名称" v-model="postForm.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="上级菜单：" prop="parent.id">
          <el-col :span="5">
            <el-select v-model="postForm.parent.id" placeholder="顶级菜单">
              <el-option label="顶级菜单" value=""></el-option>
              <el-option
                v-for="item in data" :style="'padding-left:' + ((item.grade)*20+20) + 'px'"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="菜单类型：" prop="type">
          <el-col :span="5">
            <el-select v-model="postForm.type" placeholder="菜单类型">
              <el-option label="点击" value="click"></el-option>
              <el-option label="跳转" value="view"></el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="菜单授权：" prop="category">
          <el-col :span="5">
            <el-select v-model="postForm.category" placeholder="菜单类型">
              <el-option label="不授权" value="notAuthorize"></el-option>
              <el-option label="页面授权" value="authorize"></el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item  label="关键字：" prop="keyName">
          <el-col :span="5">
            <el-input placeholder="关键字" v-model="postForm.keyName"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="菜单路径：" prop="url">
          <el-col :span="5">
            <el-input placeholder="菜单路径" v-model="postForm.url"></el-input>
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
import { save,info, fetchTree } from '@/api/wxMenu'

const defaultForm = {
  name: '',               // 名称
  type: undefined,       // 类型
  category: undefined,    // 授权类别
  keyName: undefined,    // 关键字
  url: undefined,       // 菜单路径
  parent:{id:undefined},// 菜单父类
  id: undefined
}

export default {
  name: 'wxMenuDetail',
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
        //console.log(JSON.stringify(response.data))
        const data = response.data.data;
        this.postForm.id = data.id;
        this.postForm.name = data.name;
        this.postForm.keyName = data.keyName;
        this.postForm.url = data.url;
        this.postForm.type = data.type;
        this.postForm.category = data.category;
        if(data.parent){
          this.postForm.parent = data.parent;
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
              path:"/sys/wx_menu/list"})
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
