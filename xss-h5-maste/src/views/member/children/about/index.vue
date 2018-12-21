<!--登录 -->
<template>

  <div class='page'>
    <div class="container about-container" style="padding-bottom:0px;">
      <!-- logo -->
      <div class="icon_login">
       <div class="logo-box">
           <img class="image" src="../../../../assets/xdsb-logo.png" alt="小豆社保">
       </div>
      </div>
      <group :gutter="0" label-width="5.5em" >
        <cell class="f14" title="微信公众号" value="小豆社保" ></cell>
        <cell class="f14" title="服务热线" value="400-9999-075" @click.native="call" is-link></cell>
        <cell class="f14" title="市场合作" value="manager@xincci.com" ></cell>
        <cell class="f14" title="联系地址" value="北京市大兴区经海六路3号院晶能梦谷B座530" ></cell>
      </group>

    </div>
  </div>
</template>

<script>
import { Group, Cell } from "vux";
import { getToken, setToken, removeToken } from '@/utils/auth'
import storage from "@/utils/common";
import url from "@/api/apiUrl";
import { get, post, execute } from "@/api/server";
export default {
  data() {
    return {

    };
  },
  components: { Group, Cell },

  computed: {},

  mounted() {},

  created() {

  },

  methods: {
      call(){
          window.location.href="tel:400-9999-075"
      }
  }
};
</script>
<style lang='less' >
.icon_login {
  margin: 35px;
  .logo-box {
    width: 2.667rem;
    height: 2.667rem;
    margin: 0 auto;
  }
}
.about-container{
  .weui-cell__ft {
    text-align: right;
    color: @font-color !important;
}
}

</style>
