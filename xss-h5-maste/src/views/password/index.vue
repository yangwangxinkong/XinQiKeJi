<!-- 找回密码 -->
<template>
  <div class='page'>
    <div class="container">
      <!-- 表单 -->
      <div class="from-group">
         <div class="from-group-input">
            <x-input placeholder="请输入您的手机号" keyboard="number"  ref="refphone"  v-model="mobile" :max="11" is-type="china-mobile">
                <img slot="label"
                class="mr20"
                src="../../assets/icon-phone.png"
                width="24" height="24">
            </x-input>
            <x-input placeholder="请输入验证码" keyboard="number" v-model="smsCode" :max="6">
                <img slot="label"
                class="mr20"
                src="../../assets/icon-code.png"
                width="24" height="24">
                 <x-button slot="right" class="f14 pl10 pr10" type="primary" plain  mini  v-if="computedTime" >已发送({{computedTime}}s)</x-button>
                <x-button slot="right"  class="f14" type="primary" mini @click.native="sendSmsCode"  v-else>发送验证码</x-button>
            </x-input>
            <x-input placeholder="请输入您的密码" type="password" v-model="password">
                <img slot="label"
                class="mr20"
                src="../../assets/icon-password.png"
                width="24" height="24">
            </x-input>

         </div>
        <div class="padding-15 mt20">
             <x-button @click.native="register"  type="primary" class="color_base">找回密码</x-button>
        </div>
     </div>
    </div>
  </div>
</template>

<script>
import { XInput, XButton,CheckIcon } from "vux";
import { getToken, setToken, removeToken } from '@/utils/auth'
import storage from "@/utils/common";
import url from "@/api/apiUrl";
import { get, post, execute } from "@/api/server";

export default {
  data() {
    return {
      mobile: "",//手机号码
      smsCode:'',//短信码
      password:'',//密码
      computedTime: 0 //倒数记时
    };
  },
  components: { XInput, XInput,XButton,CheckIcon },

  computed: {},

  mounted() {},
  created() {
    //this.redirectUrl = this.$route.query.redirect;
  },
  methods: {

       //发送短信验证码
      sendSmsCode(){
//        let pwd = this.$md5('hello');
//        console.log("pwd:" + pwd)
        if(this.mobile==""){
            this.$vux.toast.text('请输入手机号', 'middle')
        }else if(!this.$refs.refphone.valid){
            this.$vux.toast.text('请输入正确手机号', 'middle')
        }else{
            this.computedTime = 60;
            this.timer = setInterval(() => {
                this.computedTime --;
                if (this.computedTime == 0) {
                    clearInterval(this.timer)
                }
            }, 1000)
            const params = {
                mobile:this.mobile,
                resource:0
            }
            // 调用接口
             get(url.sendSmsCodeByFindPassword, params).then(response => {
                 this.$vux.toast.text(response.data.data)
             });
        }
      },
      register(){
        if(this.mobile==""){
          this.$vux.toast.text('请输入手机号', 'middle')
        }else if(!this.$refs.refphone.valid){
          this.$vux.toast.text('请输入正确手机号', 'middle')
        }else if(this.smsCode == ''){
          this.$vux.toast.text('请输入正确的短信验证码', 'middle')
        }else if(this.password == ''){
          this.$vux.toast.text('请输入密码', 'middle')
        }else{
          const params = {
            mobile:this.mobile,
            password:this.$md5(this.password),
            smsCode:this.smsCode
          }
          // 调用接口
           post(url.findPassword, params).then(response => {
               console.log("register response:" + JSON.stringify(response));
             if(response.data.result=="00000000"){
               this.$vux.toast.text("找回密码成功，请重新登录！");
               this.$router.push({ path: '/login/index' })
             }else{
               this.$vux.toast.text(response.data.msg);
             }
           });
        }
      }
  }
};
</script>
<style lang='less'>
    .weui-icon-success,.weui-icon-success-circle{
        font-size: 16px !important;
        &::before{
            color: @color_base !important;
        }

    }
    .weui-icon-circle{
          font-size: 16px !important;
    }
</style>

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
  .weui-btn_plain-primary{
      border-color:  @color_base;
      color:  @color_base;
  }

}

</style>
