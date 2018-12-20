<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm">

      <sticky className="sub-navbar">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">保存
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item labelWidth="150px" label="用户名：" prop="username">
          <el-col :span="7">
          <el-input placeholder="请输入用户名" v-model="postForm.username"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="密码：" prop="password">
          <el-col :span="7">
            <el-input placeholder="请输入密码" type="password" v-model="postForm.password"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="确认密码：" prop="confirmPassword">
          <el-col :span="7">
            <el-input placeholder="请输入密码" type="password" v-model="postForm.confirmPassword"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="昵称：" prop="name">
          <el-col :span="7">
            <el-input placeholder="请输入昵称" v-model="postForm.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="手机：" prop="mobile">
          <el-col :span="7">
            <el-input placeholder="请输入手机" v-model="postForm.mobile"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="openId：" prop="openId" v-if="isEdit">
          <el-col :span="7">
            <el-input v-model="postForm.openId" disabled></el-input>
          </el-col>
        </el-form-item>

        <el-form-item labelWidth="150px" label="积分：" prop="point">
          <el-col :span="7">
            <span v-if="isEdit">{{ postForm.point }}</span>
            <el-input-number placeholder="请输入积分" v-model="postForm.point" v-else></el-input-number>
          </el-col>
        </el-form-item>

        <el-form-item labelWidth="150px" label="是否VIP会员：" prop="isVip">
          <el-col :span="7">
            <span v-if="isVip">是</span>
            <span v-else>否</span>
          </el-col>
        </el-form-item>

        <el-form-item labelWidth="150px" label="是否新手：" prop="isNew">
          <el-col :span="7">
            <span v-if="isNew">是</span>
            <span v-else>否</span>
          </el-col>
        </el-form-item>

        <el-form-item labelWidth="150px" label="积分调整（增/减）：" prop="modifyPoint" v-if="isEdit">
          <el-col :span="7">
            <el-input placeholder="正数代表增加，负数代表减少" v-model="postForm.modifyPoint"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="余额：" prop="balance">
          <el-col :span="7">
            <span v-if="isEdit">{{ postForm.balance }}</span>
            <el-input-number placeholder="请输入余额" v-model="postForm.balance" v-else></el-input-number>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="余额调整（充/扣）:" prop="modifyBalance" v-if="isEdit">
          <el-col :span="7">
            <el-input placeholder="正数代表充值，负数代表扣除" v-model="postForm.modifyBalance"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="备注:" prop="depositMemo" v-if="isEdit">
          <el-col :span="7">
            <el-input placeholder="余额调整备注" v-model="postForm.depositMemo"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="设置：" prop="isEnabled">
          <el-col :span="7">
            <el-checkbox :label="postForm.isEnabled" v-model="postForm.isEnabled">是否启用</el-checkbox>
            <el-checkbox :label="postForm.isLocked" v-model="postForm.isLocked">是否锁定</el-checkbox>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="消费金额:" prop="amount" v-if="isEdit">
          <el-col :span="7">
            <span v-if="isEdit">{{ postForm.amount }}</span>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="身份证正面：" prop="idFaces">
          <el-col :span="7">
            <a v-if="idFace" target="_blank" :href="idFace" style="float: left; margin-left: 12px; color: rgb(10, 118, 164);">查看</a>
            <!-- <a v-if="idFace" href="javascript:void(0);" @click="downloadImg(idFace)" style="float: left; margin-left: 12px; color: rgb(10, 118, 164);">下载</a> -->
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="身份反面：" prop="idBackFaces">
          <el-col :span="7">
            <a v-if="idBackFace" target="_blank" :href="idBackFace" style="float: left; margin-left: 12px; color: rgb(10, 118, 164);">查看</a>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="户口本首页：" prop="hukouIndeies">
          <el-col :span="7">
            <a v-if="hukouIndex" target="_blank" :href="hukouIndex" style="float: left; margin-left: 12px; color: rgb(10, 118, 164);">查看</a>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="户口本本人页：" prop="hukouPersons">
          <el-col :span="7">
            <a v-if="hukouPerson" target="_blank" :href="hukouPerson" style="float: left; margin-left: 12px; color: rgb(10, 118, 164);">查看</a>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="本人一寸照片：" prop="onePhones">
          <el-col :span="7">
            <a v-if="onePhone" target="_blank" :href="onePhone" style="float: left; margin-left: 12px; color: rgb(10, 118, 164);">查看</a>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="创建日期:" prop="createDate" v-if="isEdit">
          <el-col :span="7">
            <span v-if="isEdit">{{ postForm.createDate }}</span>
          </el-col>
        </el-form-item>
        <el-form-item labelWidth="150px" label="注册IP:" prop="registerIp" v-if="isEdit">
          <el-col :span="7">
            <span v-if="isEdit">{{ postForm.registerIp }}</span>
          </el-col>
        </el-form-item>


      </div>
    </el-form>

  </div>
</template>

<script>

import Sticky from '@/components/Sticky' // 粘性header组件
import { validateURL, validMobile } from '@/utils/validate'
import url from '@/api/apiUrl.js'
import { get,post,execute, download } from '@/api/server.js'
import { goback } from "@/utils/common";

const defaultForm = {
  username: '', // 用户名
  password:'',//密码
  confirmPassword:'',//确认密码
  name:'',//昵称
  mobile:'',//手机
  point:0,//积分
  modifyPoint:'',//积分调整
  balance:0,//余额
  modifyBalance:'',//余额调整
  depositMemo:'',//余额调整备注
  isEnabled:true,//是否启用
  isLocked:false,//是否禁用
  registerIp:'',//注册IP
  amount:undefined,//消费金额
  createDate:undefined,//创建日期
  openId:'',
  id: undefined
}

export default {
  name: 'memberDetail',
  components: { Sticky},
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    const isValidPassword = (rule, value,callback)=>{
        if(!this.isEdit) {
          if (!value) {
            callback(new Error('密码必填'))
          } else {
            callback()
          }
        }else{
          callback()
        }
    }
    const validateConfirmPassword = (rule, value, callback) => {
      if(!this.isEdit){
        if (!value){
          callback(new Error('确认密码必填'))
        }else if (value != this.postForm.password) {
          callback(new Error('2次密码输入不一致'))
        } else {
          callback()
        }
      }else{
        if (value != this.postForm.password) {
          callback(new Error('2次密码输入不一致'))
        } else {
          callback()
        }
      }

    }
    const isValidMobile = (rule, value,callback)=>{
      if (!value){
        callback(new Error('手机号码必填'))
      }else  if (!validMobile(value)){
        callback(new Error('请输入正确的11位手机号码'))
      }else {
        callback()
      }
    }
    return {
      data:[],
      postForm: Object.assign({}, defaultForm),
      loading: false,
      idFace: undefined,
      idBackFace: undefined,
      hukouIndex: undefined,
      hukouPerson: undefined,
      onePhone: undefined,
      isVip: undefined,
      isNew: undefined,
      rules: {
        username: [{required: true, message:'用户名必填',trigger:'blur'}],
        password: [{trigger:'blur',validator: isValidPassword}],
        confirmPassword: [{trigger:'blur',validator: validateConfirmPassword}],
        mobile: [{required: true, trigger:'blur',validator: isValidMobile}],
        point: [{required: true, message:'积分必填',trigger:'blur'}],
        balance: [{required: true, message:'余额必填',trigger:'blur'}]
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
      get(url.memberInfo, {id:id}).then(response => {
        console.log(JSON.stringify(response.data))
        const data = response.data.data;
        this.postForm.id = data.id
        this.postForm.username = data.username
        this.postForm.password = data.password
        this.postForm.confirmPassword = data.password
        this.postForm.name = data.name
        this.postForm.mobile = data.mobile
        this.postForm.point = data.point
        this.postForm.balance = data.balance
        this.postForm.amount = data.amount
        this.postForm.createDate = data.createDate
        this.postForm.registerIp = data.registerIp
        this.postForm.memberRank = data.memberRank
        this.postForm.isLocked = data.isLocked
        this.postForm.openId = data.openId;
        this.idFace = data.idFace
        this.idBackFace = data.idBackFace
        this.hukouIndex = data.hukouIndex
        this.hukouPerson = data.hukouPerson
        this.onePhone = data.onePhone
        this.isVip = data.isVip
        this.isNew = data.isNew


      }).catch(err => {
        console.log(err)
      })
    },
    submitForm() {
      this.$refs.postForm.validate(valid => {
        if (valid) {
          if (this.postForm.password) {
            let pwd = this.$md5(this.postForm.password);

            this.postForm.password = pwd;
          }
          this.postForm.createDate = undefined;
//          console.log("pwd:" + pwd)
//          return false;
          this.loading = true

          execute(url.memberSave, this.postForm).then(response => {
            if (response.data.result==='00000000') {
              this.$notify({
                title: '成功',
                message: '操作成功',
                type: 'success',
                duration: 2000
              })
              this.$router.push({
              path:"/member/list"})
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
    // downloadImg(filePath) {
    //   download(url.imgDownload, {filePath: filePath}).then(response => {
    //   })
    // }
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
