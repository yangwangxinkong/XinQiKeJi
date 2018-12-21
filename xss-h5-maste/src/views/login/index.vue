<!--登录 -->
<template>

  <div class='page'>
    <div class="container">
      <!-- logo -->
      <div class="icon_login">
       <div class="logo-box">
           <img class="image" src="../../assets/logo.png" alt="">
       </div>
    </div>
      <!-- 表单 -->
      <div class="from-group">
         <div class="from-group-input">
            <x-input placeholder="请输入您的手机号" keyboard="number"  ref="refphone"  v-model="mobile" :max="11" is-type="china-mobile">
                <img slot="label" class="mr20" src="../../assets/icon-phone.png" width="24" height="24">
            </x-input>
            <x-input placeholder="请输入您的密码" type="password" v-model="password">
                <img slot="label" class="mr20" src="../../assets/icon-password.png" width="24" height="24">
            </x-input>
         </div>
        <div class="padding-15 mt20">
             <x-button @click.native="login" type="warn" class="color_base">登录</x-button>
        </div>
     </div>
      <div class="div-tip tc mt20">
         <p>
           忘记密码？ <router-link :to="{path:'/password/index'}" class="color_base">找回密码</router-link>
           没有账号？ <router-link :to="{path:'/register/index?redirect='+redirectUrl}" class="color_base">前往注册</router-link>
         </p>
     </div>
    </div>
  </div>
</template>

<script>
import { XInput, Group, XButton, Cell } from "vux";
import { getToken, setToken, removeToken } from '@/utils/auth'
import storage from "@/utils/common";
import url from "@/api/apiUrl";
import { get, post, execute } from "@/api/server";
export default {
  data() {
    return {
      mobile: "",//手机号码
      password:'',//密码
      redirectUrl:'/'
    };
  },
  components: { XInput, XInput, Group, XButton, Cell },

  computed: {},

  mounted() {},

  created() {
    if(this.$route.query && this.$route.query.redirect) {
      this.redirectUrl = this.$route.query.redirect;
    }    
  },

  methods: {
      login(){
        if(this.mobile==""){
          this.$vux.toast.text('请输入手机号', 'middle')
        }else if(!this.$refs.refphone.valid){
          this.$vux.toast.text('请输入正确手机号', 'middle')
        }else if(this.password == ''){
          this.$vux.toast.text('请输入密码', 'middle')
        }else{
          const params = {
            username:this.mobile,
            password:this.$md5(this.password)
          }
          // 调用接口
          execute(url.login, params).then(response => {
            console.log("login response:" + JSON.stringify(response));
            if(response.data.result=="00000000"){
              const data = response.data.data;
              setToken(data.token)
              storage.set("member", data);
              //console.log("redirectUrl:" + this.redirectUrl);
              if(this.redirectUrl){
                this.$router.push({ path: this.redirectUrl })
              }else{
                this.$router.push({ path: '/' })
              }
            }else{
              this.$vux.toast.text(response.data.msg);
            }
          });
        }
      }
  }
};
</script>
<style lang='less' scoped>
.icon_login {
  margin: 35px;
  .logo-box {
    width: 60px;
    height: 60px;
    margin: 0 auto;
    border: 1px solid @color_base;
    border-radius: 50%;
    img {
      border-radius: 50%;
    }
  }
}
.from-group {
  .weui-cell {
    padding: 15px;
  }
  .from-group-input {
    position: relative;
    &::after {
      content: " ";
      position: absolute;
      left: 0;
      bottom: 0;
      right: 0;
      height: 1px;
      border-bottom: 1px solid #d9d9d9;
      color: #d9d9d9;
      -webkit-transform-origin: 0 0;
      transform-origin: 0 0;
      -webkit-transform: scaleY(0.5);
      transform: scaleY(0.5);
    }
  }
  .weui-btn_primary{
      background-color: @color_base;
  }
}
</style>
