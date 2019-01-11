<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm">

      <sticky className="sub-navbar">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">保存
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item label="所属城市：" prop="city.code">
          <el-col :span="5">
          <!-- <el-select v-model="postForm.city.id" placeholder="所属城市">
              <el-option
                v-for="item in cityList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select> -->
            <el-select v-model="postForm.city.id"  placeholder="所属城市" class="filter-item">
              <el-option
                v-for="item in cityCategory" :style="'padding-left:' + ((item.grade)*20+20) + 'px'" :key="item.id"  :label="item.name"  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>

        <!-- <el-form-item label="缴费对象：" prop="payFrom">
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
        </el-form-item> -->

        <el-form-item label="缴费类别：" prop="feeCategory">
          <el-col :span="5">
          <el-select v-model="postForm.feeCategory" placeholder="缴费类别：">
              <el-option
                v-for="item in feeCategoryList"
                :key="item.name"
                :label="item.desc"
                :value="item.name">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>

        <el-form-item label="缴费方式：" prop="payCategory">
          <el-col :span="5">
          <el-select v-model="postForm.payCategory" placeholder="缴费方式：">
              <el-option
                v-for="item in payCategoryList"
                :key="item.name"
                :label="item.desc"
                :value="item.name">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>

        <el-form-item label="缴费月数：" prop="monthCount">
          <el-col :span="5">
          <el-input-number placeholder="请输入缴费月数：" required v-model="postForm.monthCount" :min="0" @change="handleChange"></el-input-number>
          </el-col>
        </el-form-item>

        <el-form-item label="月服务费" prop="monthFee">
          <el-col :span="5">
          <el-input-number placeholder="请输入月服务费：" required v-model="postForm.monthFee" :min="0" @change="handleChange"></el-input-number>
          </el-col>
        </el-form-item>

        <el-form-item label="总服务费" prop="fee">
          <el-col :span="5">
          <el-input required v-model="postForm.fee" readonly="readonly"></el-input>
          </el-col>
        </el-form-item>

      </div>
    </el-form>

  </div>
</template>

<script>
import Sticky from '@/components/Sticky' // 粘性header组件
import { validateURL } from '@/utils/validate'
import { fetchInfo, fetchSave, fetchCityList } from '@/api/serviceFee'
import { goback } from '@/utils/common'
import { validIsBlank, validNum } from '@/utils/validate'
import url from '@/api/apiUrl'
import { get } from '@/api/server'

const defaultForm = {
  city: {id:undefined},//城市
  feeCategory: undefined,//缴费类别
  payCategory: undefined,//缴费方式
  //payFrom: undefined,//缴费对象
  monthCount: undefined,// 缴费月数
  monthFee: undefined, //月服务费
  fee: undefined,//服务费
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
      // payFromList: [{
      //   name:"pf0",
      //   value: 0,
      //   desc: '个人'
      // }, {
      //   name:"pf1",
      //   value: 1,
      //   desc: '公司'
      // }],
      feeCategoryList: [{
        name:"fc0",
        value: 0,
        desc: '社保'
      }, {
        name:"fc1",
        value: 1,
        desc: '公积金'
      }, {
        name:"fc2",
        value: 2,
        desc: '社保+公积金'
      }],  
      payCategoryList: [{
        name:"pc0",
        value: 0,
        desc: '新缴'
      }, {
        name:"pc1",
        value: 1,
        desc: '续缴'
      }, {
        name:"pc2",
        value: 2,
        desc: '补缴'
      }],     
      cityList: [],
      cityCategory: [],
      data:[],
      postForm: Object.assign({}, defaultForm),
      loading: false,
      rules: {
        //city: [{required: true, message:'城市必选',trigger:'blur'}],
        //payFrom: [{required: true, message:'缴费对象必选',trigger:'blur'}],
        monthCount: [{required: true, message:'缴费月数必填',trigger:'blur'}],
        fee: [{required: true, message:'服务费必填',trigger:'blur'}]
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
        this.postForm = response.data.data;
        //console.log(JSON.stringify(this.postForm))
      }).catch(err => {
        console.log(err)
      })
    },

    handleChange(value) {
      this.$nextTick(() => {
        if(!validIsBlank(this.postForm.monthCount) && !validNum(this.postForm.monthCount)
        && !validIsBlank(this.postForm.monthFee) && !validNum(this.postForm.monthFee)) {
          this.postForm.fee = Number(parseInt(this.postForm.monthCount) * Number(this.postForm.monthFee).toFixed(2)).toFixed(2);
        } else {
          this.postForm.fee = undefined;
        }
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

          // if(!(this.postForm.payFrom)){
          //   this.$notify({
          //     title: '失败',
          //     message: '必须选择缴费对象',
          //     type: 'error',
          //     duration: 2000
          //   })
          //   return;
          // }

          if(!(this.postForm.feeCategory)){
            this.$notify({
              title: '失败',
              message: '必须选择缴费类别',
              type: 'error',
              duration: 2000
            })
            return;
          }

          if(!(this.postForm.payCategory)){
            this.$notify({
              title: '失败',
              message: '必须选择缴费方式',
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
