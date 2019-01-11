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
          <!-- <el-select v-model="postForm.city.id" placeholder="所属城市" @change="changeCity()">
              <el-option
                v-for="item in cityList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select> -->

            <el-select v-model="postForm.city.id"  placeholder="所属城市" @change="changeCity()" class="filter-item">
              <el-option
                v-for="item in cityCategory" :style="'padding-left:' + ((item.grade)*20+20) + 'px'" :key="item.id"  :label="item.name"  :value="item.id">
              </el-option>
            </el-select>

          </el-col>
        </el-form-item>

        <el-form-item label="户口性质：" prop="residenceTypeId">
          <el-col :span="5">
          <el-select v-model="residenceTypeId" placeholder="户口性质">
              <el-option
                v-for="item in residenceTypeList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>

        <el-form-item label="社保类别：" prop="socialSecurityCategory">
          <el-col :span="5">
          <el-select v-model="postForm.socialSecurityCategory" placeholder="社保类别">
              <el-option
                v-for="item in socialSecurityCategoryList"
                :key="item.name"
                :label="item.desc"
                :value="item.name">
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

        <el-form-item label="缴费固定金额设置：">
          <el-col :span="15">
            <el-checkbox v-model="postForm.fixed" @change="fixedChange">固定</el-checkbox>
          </el-col>
        </el-form-item>

        <el-form-item label="缴费固定金额：" prop="fixedValue" v-if="fixed">
          <el-col :span="5">
          <el-input-number placeholder="请输入缴费固定金额：" required v-model="postForm.fixedValue" :min="0"></el-input-number>
          </el-col>
        </el-form-item>

        <el-form-item label="缴费比例：" prop="ratio" v-else>
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
import { fetchInfo, fetchSave, fetchCityList, fetchResidenceTypeList } from '@/api/socialSecurity'
import { goback } from '@/utils/common'
import url from '@/api/apiUrl'
import { get } from '@/api/server'

const defaultForm = {
  city: {id:undefined},//城市
  residenceType: {id:undefined},//户口性质
  socialSecurityCategory: undefined,//社保类别
  payFrom: undefined,//缴费对象
  fixed: false,
  fixedValue: undefined,
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
      fixed: false,
      payFromList: [{
        name: 'pf0',
        value: 0,
        desc: '个人'
      }, {
        name: 'pf1',
        value: 1,
        desc: '公司'
      }],
      residenceTypeId: undefined,
      residenceTypeList: [],
      // accountPropertyList: [{
      //   name: 'ap0',
      //   value: 0,
      //   desc: '本市城镇职工'
      // }, {
      //   name: 'ap1',
      //   value: 1,
      //   desc: '本市农村人口'
      // }, {
      //   name: 'ap2',
      //   value: 2,
      //   desc: '外埠城镇职工'
      // }, {
      //   name: 'ap3',
      //   value: 3,
      //   desc: '外埠农村人口'
      // }],
      socialSecurityCategoryList: [{
        name: 'ssc0',
        value: 0,
        desc: '养老'
      }, {
        name: 'ssc1',
        value: 1,
        desc: '失业'
      }, {
        name: 'ssc2',
        value: 2,
        desc: '工伤'
      }, {
        name: 'ssc3',
        value: 3,
        desc: '医疗'
      }, {
        name: 'ssc4',
        value: 4,
        desc: '生育'
      }, {
        name: 'ssc5',
        value: 5,
        desc: '残保'
      }],
      cityList: [],
      cityCategory: [],
      data:[],
      postForm: Object.assign({}, defaultForm),
      loading: false,
      rules: {
        //city: [{required: true, message:'城市必选',trigger:'blur'}],
        //payFrom: [{required: true, message:'缴费对象必选',trigger:'blur'}],
        ratio: [{required: true, message:'缴费比例必填',trigger:'blur'}],
        fixedValue: [{required: true, message:'缴费固定金额必填',trigger:'blur'}]
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

    changeCity() {
      //this.listLoading = true
      fetchResidenceTypeList({id: this.postForm.city.id}).then(response => {
        this.postForm.residenceType.id = undefined
        this.residenceTypeList = response.data.list
      }).catch((e)=>{
        console.log(e)
        //this.listLoading = false
      })
    },

    getDetail(id) {
      fetchInfo(id).then(response => {
        this.postForm = response.data.data;
        this.residenceTypeId = response.data.data.residenceType.id;
        this.changeCity(response.data.data.city.id);
        this.fixedChange(this.postForm.fixed);
      }).catch(err => {
        console.log(err)
      })
    },
    submitForm() {
      this.postForm.residenceType.id = this.residenceTypeId;

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

          if(!(this.postForm.residenceType.id)){
            this.$notify({
              title: '失败',
              message: '必须选择户口性质',
              type: 'error',
              duration: 2000
            })
            return;
          }

          if(!(this.postForm.socialSecurityCategory)){
            this.$notify({
              title: '失败',
              message: '必须选择社保类别',
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
              this.postForm.city.id = undefined;
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
    fixedChange(val) {
      this.fixed = val;
      if(val) {
        this.postForm.ratio = 0;
      } else {
        this.postForm.fixedValue = 0;
      }
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
