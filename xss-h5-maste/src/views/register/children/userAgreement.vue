<!-- 用户协议 -->
<template>
  <div class='page'>
    <div class="container">
      <div class="introduce padding-15">
        <h3 class="tc mt20 f16 mb20" style="font-weight:bold">{{title}}</h3>
        <div class="introduce-html" v-html="content"></div>
      </div>
    </div>
  </div>
</template>

<script>
  import url from "@/api/apiUrl";
  import { get, post, execute } from "@/api/server";
  export default {
    data () {
      return {
          title:'小豆社保服务协议',
          content:""
      };
    },
    components: {},
    computed: {},
    mounted(){},
    created(){
        get(url.agreement, {}).then(response => {
            if(response.data.result == "00000000"){
                this.title = response.data.data.title;
                this.content = response.data.data.content;
            }
        })
    },
    methods: {}
  }

</script>
<style lang='less' scoped>
</style>
