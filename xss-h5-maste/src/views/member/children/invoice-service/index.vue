<!--  发票管理-->
<template>
  <div class="page">
    <div class="container bg_f" style="padding-bottom:0px;">
      <!-- center-con -->
      <div class="center-con">
        <group :gutter="0">
          <cell title="发票索取" link="/member/invoice/selectOrder"></cell>
          <cell title="发票列表" link="/member/invoice/list"></cell>
          <cell title="发票信息管理" link="/member/invoice/info"></cell>
          <!--<cell title="发票寄送地址管理" link="/member/ziliao/index"></cell>-->
        </group>

      </div>
    </div>

  </div>
</template>

<script>
  import { Panel, Cell,Group } from 'vux'
  import storage from "@/utils/common"
  import url from '@/api/apiUrl'
  import {get, post, execute} from '@/api/server'
  export default {
    data() {
      return {
        memberInfo: {
          id: undefined,
          name: '小豆社保',
          username: '',
          mobile:undefined
        }
      };
    },

    components: {
      Panel, Cell,Group
    },

    computed: {},

    mounted() {},

    created(){
    },

    methods: {}
  };
</script>
<style lang='less' scoped>
  @import "~vux/src/styles/weui/widget/weui_media_box/weui_media_box";
</style>
