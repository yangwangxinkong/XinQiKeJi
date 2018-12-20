<template>
  <el-menu class="navbar" mode="horizontal">
    <hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened"></hamburger>

    <breadcrumb class="breadcrumb-container"></breadcrumb>

    <div class="right-menu">
      <!-- <error-log class="errLog-container right-menu-item"></error-log> -->

      <el-tooltip effect="dark" :content="$t('navbar.screenfull')" placement="bottom">
        <screenfull class="screenfull right-menu-item"></screenfull>
      </el-tooltip>
      <!--
      <lang-select class="international right-menu-item"></lang-select>

      <el-tooltip effect="dark" :content="$t('navbar.theme')" placement="bottom">
        <theme-picker class="theme-switch right-menu-item"></theme-picker>
      </el-tooltip>
      -->

      <el-dropdown class="avatar-container right-menu-item" trigger="click">
        <div class="avatar-wrapper">
          <img class="user-avatar" src="/static/images/bmz_avatar.png">
          <i class="el-icon-caret-bottom"></i>
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/">
            <el-dropdown-item>
              {{$t('navbar.dashboard')}}
            </el-dropdown-item>
          </router-link>
          <!--
          <a target='_blank' href="https://github.com/PanJiaChen/vue-element-admin/">
            <el-dropdown-item>
              {{$t('navbar.github')}}
            </el-dropdown-item>
          </a>
          -->
          <el-dropdown-item divided>
            <span @click="showChangePwd" style="display:block;">修改密码</span>
          </el-dropdown-item>
          <el-dropdown-item divided>
            <span @click="logout" style="display:block;">{{$t('navbar.logOut')}}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

      <!--新增及编辑dialog-->
      <el-dialog title="修改密码" :visible.sync="dialogFormVisible">
        <el-form ref="pwdForm" :rules="rules" :model="temp"  label-position="right" style='width: 400px; margin-left:10px;'>
          <el-form-item  label="旧密码：" prop="oldPassword">
            <el-input v-model="temp.oldPassword" name="oldPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item  label="新密码：" prop="newPassword">
            <el-input v-model="temp.newPassword" name="newPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="确认密码：" prop="rePassword">
            <el-input v-model="temp.rePassword" name="rePassword" type="password"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">{{$t('table.cancel')}}</el-button>
          <el-button type="primary" @click="changePwd">{{$t('table.confirm')}}</el-button>
        </div>
      </el-dialog>

    </div>
  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import ErrorLog from '@/components/ErrorLog'
import Screenfull from '@/components/Screenfull'
import LangSelect from '@/components/LangSelect'
import ThemePicker from '@/components/ThemePicker'
import {changePwdToDb} from '@/api/login'

//修改密码的校验
export default {
  data(){
    const validator_oldPassword = (rule, value, callback) => {
      if (value==='') {
        callback(new Error('密码不能为空！'))
      } else if(this.temp.oldPassword.length<8||this.temp.oldPassword.length>20){
        callback(new Error('原密码长度为8至20之间！'))
      } else {
        callback()
      }
    }
    const validator_newPassword = (rule, value, callback) => {
      if (this.temp.newPassword.length< 8||this.temp.newPassword.length>20) {
        callback(new Error('密码长度为8至20之间！'))
        if (value==='') {
          callback(new Error('请重新输入密码！'))
        }
      } else if(value==this.temp.oldPassword){
        callback(new Error('原密码不能和新密码不能一致,请重新输入 ！'))
      } else {
        callback()
      }
    }
    const validator_rePassword = (rule, value, callback) => {
      if (this.temp.rePassword.length< 8||this.temp.rePassword.length>20) {
        callback(new Error('密码长度为8至20之间！'))
        if (value==='') {
          callback(new Error('请重新输入密码！'))
        }
      } else if(value!==this.temp.newPassword){
        callback(new Error('两次密码不一致,请重新输入 ！'))
      } else {
        callback()
      }
    }


  return{
    dialogFormVisible: false,
    temp: {
        oldPassword:'',
        newPassword:'',
        rePassword:''
      },
    rules: {                                                                                   //失去焦点的事件
      oldPassword: [{ required: true, message: '旧密码必填', trigger: 'change' },{validator:validator_oldPassword,trigger:'blur'}],
      newPassword:[{ required: true, message: '新密码必填', trigger: 'change' },{validator:validator_newPassword,trigger:'blur'}],
      rePassword:[{ required: true, message: '确认密码必填', trigger: 'change' },{validator:validator_rePassword,trigger:'blur'}],
      },
    }
  },
  components: {
    Breadcrumb,
    Hamburger,
    ErrorLog,
    Screenfull,
    LangSelect,
    ThemePicker
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'name',
      'avatar'
    ])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('toggleSideBar')
    },
    logout() {
      this.$store.dispatch('FedLogOut').then(() => {
        location.reload()// In order to re-instantiate the vue-router object to avoid bugs
      })
    },
    showChangePwd(){    //修改密码去空
      this.temp.oldPassword=''
      this.temp.newPassword=''
      this.temp.rePassword=''
      this.dialogFormVisible = true
    },
    changePwd(){
        this.$refs['pwdForm'].validate(valid => {
            if(valid){
              changePwdToDb(this.temp).then(response => {
                if(response.data.result==='00000000'){
                  this.$notify({
                    title: '成功',
                    message: response.data.data,
                    type: 'success',
                    duration: 1.5 * 1000
                  })
                  setTimeout(() => {
                    this.dialogFormVisible = false
                  }, 1.6 * 1000)
                }else{
                  this.$message({
                    message: response.data.msg,
                    type: 'error'
                  })
                }
              })
            }
        })
      },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .breadcrumb-container{
    float: left;
  }
  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }
  .right-menu {
    float: right;
    height: 100%;
    &:focus{
     outline: none;
    }
    .right-menu-item {
      display: inline-block;
      margin: 0 8px;
    }
    .screenfull {
      height: 20px;
    }
    .international{
      vertical-align: top;
    }
    .theme-switch {
      vertical-align: 15px;
    }
    .avatar-container {
      height: 50px;
      margin-right: 30px;
      .avatar-wrapper {
        cursor: pointer;
        margin-top: 5px;
        position: relative;
        .user-avatar {
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }
        .el-icon-caret-bottom {
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
