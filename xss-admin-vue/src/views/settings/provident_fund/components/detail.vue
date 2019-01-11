<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm">

      <sticky className="sub-navbar">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">保存
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item label="所属城市：" prop="city.id">
          <el-col :span="5">
          <!-- <el-select v-model="postForm.city.id" placeholder="所属城市">
              <el-option
                v-for="item in cityList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select> -->
            <el-select v-model="postForm.city.id" :placeholder="$t('settings.citySelect')" class="filter-item">
              <el-option
                v-for="item in cityCategory" :style="'padding-left:' + ((item.grade)*20+20) + 'px'" :key="item.id"  :label="item.name"  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>

        <el-form-item label="缴费对象：" prop="payFrom">
          <el-col :span="5">
          <el-select v-model="postForm.payFrom" placeholder="缴费对象">
              <el-option
                v-for="item in payFromList"
                :key="item.name"
                :label="item.desc"
                :value="item.name">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>

        <el-form-item label="缴费比例：" prop="ratio">
          <el-col :span="5">
          <el-input-number placeholder="请输入缴费比例：" required v-model="postForm.ratio" :min="0"></el-input-number>
          </el-col>
        </el-form-item>
      </div>
    </el-form>

  </div>
</template>

<script>
import Sticky from '@/components/Sticky' // 粘性header组件
import { validateURL } from '@/utils/validate'
import { fetchInfo, fetchSave, fetchCityList } from '@/api/providentFund'
import { goback } from '@/utils/common'
import url from '@/api/apiUrl'
import { get } from '@/api/server'

const defaultForm = {
  city: { code:undefined },//城市
  payFrom: undefined,//缴费对象
  ratio: undefined,// 缴费比例
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
      payFromList: [{
          name: 'pf0',
          value: 0,
          desc: '个人'
        }, {
          name: 'pf1',
          value: 1,
          desc: '公司'
        }],
      cityList: [],
      cityCategory: [],
      data:[],
      postForm: Object.assign({}, defaultForm),
      loading: false,
      rules: {
        //city: [{required: true, message:'城市必选',trigger:'blur'}],
        //payFrom: [{required: true, message:'缴费对象必选',trigger:'blur'}],
        ratio: [{required: true, message:'缴费比例必填',trigger:'blur'}]
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
      //this.listLoading = true
      // fetchCityList().then(response => {
      //   this.cityList = response.data.data
      // }).catch((e)=>{
      //   console.log(e)
      //   this.listLoading = false
      // })
      get(url.cityCategoryTree, {}).then(response => {
        this.cityCategory = response.data.data
      })
    },

    getDetail(id) {
      fetchInfo(id).then(response => {
        //console.log(JSON.stringify(response.data))
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

          if(!(this.postForm.payFrom)){
            this.$notify({
              title: '失败',
              message: '必须选择缴费对象',
              type: 'error',
              duration: 2000
            })
            return;
          }

          this.loading = true
          fetchSave(this.postForm).then(response => {
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
