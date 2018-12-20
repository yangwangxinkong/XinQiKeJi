<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm">

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
        <el-form-item label="编码：" prop="code">
          <el-col :span="5">
          <el-input placeholder="请输入编码" required v-model="postForm.code"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="总站：" prop="isMaster">
          <el-col :span="5">
          <el-checkbox :checked="postForm.isMaster" v-model="postForm.isMaster" >是否总站</el-checkbox>
          </el-col>
        </el-form-item>
        <el-form-item label="地区：" prop="area">
          <el-col :span="5">
          <areaCascader @successCBK="areaSuccessCBK" ref="areaCascaderRef"></areaCascader>
          </el-col>
        </el-form-item>
        <el-form-item label="共享：">
          <el-col>
          <!--<el-checkbox :checked="postForm.sharedMember" v-model="postForm.sharedMember" >会员</el-checkbox>
          <el-checkbox :checked="postForm.sharedProduct" v-model="postForm.sharedProduct" >商品</el-checkbox>
          <el-checkbox :checked="postForm.sharedOrder" v-model="postForm.sharedOrder" >订单</el-checkbox>
          <el-checkbox :checked="postForm.sharedDesigner" v-model="postForm.sharedDesigner" >设计师</el-checkbox>-->
          <el-checkbox :checked="postForm.sharedExposure" v-model="postForm.sharedExposure" >曝光台</el-checkbox>
          <el-checkbox :checked="postForm.sharedContract" v-model="postForm.sharedContract" >合同备案</el-checkbox>
          <el-checkbox :checked="postForm.sharedStandard" v-model="postForm.sharedStandard" >行业标准</el-checkbox>
          <el-checkbox :checked="postForm.sharedComplain" v-model="postForm.sharedComplain" >维权</el-checkbox>
          </el-col>
        </el-form-item>
      </div>
    </el-form>

  </div>
</template>

<script>
import areaCascader from '@/components/cascader/areaCascader'
import Tinymce from '@/components/Tinymce'
import Sticky from '@/components/Sticky' // 粘性header组件
import { validateURL } from '@/utils/validate'
import { info,save } from '@/api/city'
import { goback } from '@/utils/common'

const defaultForm = {
  name: '', // 名称
  code:'',//编码
  isMaster: false,// 是否总站
  area:{id:undefined},// 地区
//  sharedMember: false,// 共享会员
//  sharedProduct: false ,// 共享商品
//  sharedOrder: false ,// 共享订单
  sharedExposure: false ,// 曝光台
  sharedContract: false ,// 合同备案
  sharedStandard: false ,// 共享行业标准
  sharedComplain: false ,// 共享维权
  id: undefined
}

export default {
  name: 'detail',
  components: { Sticky,areaCascader, Tinymce},
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {

    return {
      areaListOptions: [],
      data:[],
      postForm: Object.assign({}, defaultForm),
      loading: false,
      rules: {
        name: [{required: true, message:'名称必填',trigger:'blur'}],
        code: [{required: true, message:'名称编码',trigger:'blur'}]
      },
    }
  },
  created() {
    //this.getProvinceData();
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
        //console.log(JSON.stringify(response.data))
        this.postForm = response.data.data;
        const area = this.postForm.area;
        if(area){
            let treePath = area.treePath;
            treePath += area.id;
            treePath = treePath.substring(1);
            let areaIds = treePath.split(",");
            this.areaListOptions = [];
            for(var i in areaIds){
              this.areaListOptions.push(parseInt(areaIds[i]));
            }
        }else{
          this.postForm.area={id:undefined};
        }

        this.$refs.areaCascaderRef.selectOptions=this.areaListOptions;
      }).catch(err => {
        console.log(err)
      })
    },
    submitForm() {
      this.postForm.area.id = this.areaListOptions[this.areaListOptions.length-1]
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
              goback(this.$route.path);
            }else{
              this.$notify({
                title: response.data.msg,
                message: response.data.data,
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
    areaSuccessCBK(selectAreas) {
      this.areaListOptions = selectAreas;
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
