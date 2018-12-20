<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm">

      <sticky className="sub-navbar">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">保存
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item labelWidth="200px" label="所属城市：" prop="city.id">
          <el-col :span="5">
            <el-select v-model="postForm.city.id"  placeholder="所属城市" class="filter-item" :disabled="isEdit">
              <el-option
                v-for="item in cityCategory" :style="'padding-left:' + ((item.grade)*20+20) + 'px'" :key="item.id"  :label="item.name"  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>

        <el-form-item labelWidth="200px" label="社保缴费基数最小值：" prop="socialBaseMin">
          <el-col :span="5">
            <el-input-number placeholder="请输入社保缴费基数最小值：" required v-model="postForm.socialBaseMin" :min="0"></el-input-number>
          </el-col>
        </el-form-item>

        <el-form-item labelWidth="200px" label="社保缴费基数最大值：" prop="socialBaseMax">
          <el-col :span="5">
          <el-input-number placeholder="请输入社保缴费基数最大值：" required v-model="postForm.socialBaseMax" :min="0"></el-input-number>
          </el-col>
        </el-form-item>

        <el-form-item labelWidth="200px" label="养老社保缴费基数最小值：" prop="ssc0BaseMin">
          <el-col :span="5">
            <el-input-number placeholder="请输入养老社保缴费基数最小值：" required v-model="postForm.ssc0BaseMin" :min="0"></el-input-number>
          </el-col>
        </el-form-item>

        <el-form-item  labelWidth="200px" label="失业社保缴费基数最小值：" prop="ssc1BaseMin">
          <el-col :span="5">
            <el-input-number placeholder="请输入失业社保缴费基数最小值：" required v-model="postForm.ssc1BaseMin" :min="0"></el-input-number>
          </el-col>
        </el-form-item>

        <el-form-item labelWidth="200px" label="工伤社保缴费基数最小值：" prop="ssc2BaseMin">
          <el-col :span="5">
            <el-input-number placeholder="请输入工伤社保缴费基数最小值：" required v-model="postForm.ssc2BaseMin" :min="0"></el-input-number>
          </el-col>
        </el-form-item>

        <el-form-item labelWidth="200px" label="医疗社保缴费基数最小值：" prop="ssc3BaseMin">
          <el-col :span="5">
            <el-input-number placeholder="请输入医疗社保缴费基数最小值：" required v-model="postForm.ssc3BaseMin" :min="0"></el-input-number>
          </el-col>
        </el-form-item>

        <el-form-item labelWidth="200px" label="生育社保缴费基数最小值：" prop="ssc4BaseMin">
          <el-col :span="5">
            <el-input-number placeholder="请输入生育社保缴费基数最小值：" required v-model="postForm.ssc4BaseMin" :min="0"></el-input-number>
          </el-col>
        </el-form-item>

        <el-form-item labelWidth="200px" label="残保 社保缴费基数最小值：" prop="ssc5BaseMin">
          <el-col :span="5">
            <el-input-number placeholder="请输入残保 社保缴费基数最小值：" required v-model="postForm.ssc5BaseMin" :min="0"></el-input-number>
          </el-col>
        </el-form-item>

        <el-form-item labelWidth="200px" label="大病 社保缴费基数最小值：" prop="ssc6BaseMin">
          <el-col :span="5">
            <el-input-number placeholder="请输入大病 社保缴费基数最小值：" required v-model="postForm.ssc6BaseMin" :min="0"></el-input-number>
          </el-col>
        </el-form-item>


        <el-form-item labelWidth="200px" label="公积金缴费基数最小值：" prop="providentBaseMin">
          <el-col :span="5">
            <el-input-number placeholder="请输入公积金缴费基数最小值：" required v-model="postForm.providentBaseMin" :min="0"></el-input-number>
          </el-col>
        </el-form-item>

        <el-form-item  labelWidth="200px" label="公积金缴费基数最大值：" prop="providentBaseMax">
          <el-col :span="5">
            <el-input-number placeholder="请输入公积金缴费基数最大值：" required v-model="postForm.providentBaseMax" :min="0"></el-input-number>
          </el-col>
        </el-form-item>

      </div>
    </el-form>

  </div>
</template>

<script>
import Sticky from '@/components/Sticky' // 粘性header组件
import { goback } from '@/utils/common'
import url from '@/api/apiUrl'
import { get, post, execute } from '@/api/server'

const defaultForm = {
  city: {id:undefined},//城市
  socialBaseMin: undefined,//社保缴费基数最小值
  socialBaseMax: undefined,//社保缴费基数最大值
  ssc0BaseMin: undefined,//社保缴费基数 养老 最小值
  ssc1BaseMin: undefined,// 社保缴费基数 失业 最小值
  ssc2BaseMin: undefined, //社保缴费基数 工伤 最小值
  ssc3BaseMin: undefined, //社保缴费基数 医疗 最小值
  ssc4BaseMin: undefined, //社保缴费基数 生育 最小值
  ssc5BaseMin: undefined, //社保缴费基数 残保 最小值
  ssc6BaseMin: undefined, //社保缴费基数 大病 最小值
  providentBaseMin: undefined, //公积金缴费基数最小值
  providentBaseMax: undefined, //公积金缴费基数最大值
  id: undefined
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
      cityList: [],
      cityCategory: [],
      data:[],
      postForm: Object.assign({}, defaultForm),
      loading: false,
      rules: {
      },
    }
  },
  created() {
    this.getCityList()

    if (this.isEdit) {
      const id = this.$route.params && this.$route.params.id
      this.getDetail(id)
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

    getDetail(id) {
      get(url.payBaseInfo, {id: id}).then(response => {
        this.postForm = response.data.data;
      }).catch(err => {
        console.log(err)
      })
    },

    submitForm() {
      this.$refs.postForm.validate(valid => {
        if (valid) {

          if(!(this.postForm.city.id)){
            this.$notify({
              title: '失败',
              message: '必须选择城市',
              type: 'error',
              duration: 2000
            })
            return;
          }

          this.loading = true
          execute(url.payBaseSave, this.postForm).then(response => {
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
                title: '失败',
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
