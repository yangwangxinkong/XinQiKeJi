<!-- 政策解读 -->
<template>
  <div class="page">
      <div class="container bg_f">
          <div class="model-tool-wrap bg_f pt10">
              <ul class="div-ul padding-15 clearfix">
                  <li :key="item.id" v-for="item in list">
                      <router-link :to="{path:'/policy/detail',query:{code:item.code}}">
                          <div class="pic">
                              <img class="image" :src="item.image" alt="">
                          </div>
                          <p class="title pt10 f16">{{item.name}}</p>
                          <p class="title pt10 f14 text">{{item.memo}}</p>
                      </router-link>
                  </li>
              </ul>
          </div>
      </div>
      <!--引入底部组件  -->
    <v-footer></v-footer>
  </div>
</template>

<script>
import footer from "@/components/footer/index";
import url from '@/api/apiUrl'
import {get, post, execute} from '@/api/server'
export default {
  data() {
    return {
        list:[{
            id:1,
            img:require("@/assets/logo.png"),
            title:'买房政策',
            text:'买房政策要知道'
        }, {
            id:2,
            img: require("@/assets/logo.png"),
            title: '买车政策',
            text: '买车摇号需资格'
        },{
            id:3,
            img:require("@/assets/logo.png"),
            title:'上学政策',
            text:'还在上学怎么办'
        }, {
            id:4,
            img: require("@/assets/logo.png"),
            title: '落户政策',
            text: '买房政策要知道'
        }]
    }
  },

  components: {
    "v-footer": footer
  },

  computed: {},

  mounted() {},
  created(){
      this.getCategoryChildren()
  },

  methods: {
    getCategoryChildren(){
        get(url.articleCategoryChildren, {code: 'ZCJD'}).then(response => {
            //console.log("children category:" + JSON.stringify(response));
            if(response.data.result == "00000000"){
                this.list = response.data.data;
            }
        });
    }
  }
};
</script>
<style>
body{
    background-color: #ffffff;
}
</style>

<style lang='less' scoped>
.model-tool-wrap {
  .div-ul {
    li {
      width: 50%;
      height:200px;
      float: left;
      margin-bottom: 10px;
       a {
          display: block;
          width: 100%;
          height: 100%;
          border: 1px solid #f5f5f5;
          padding: 10px 15px;

          text-align: center;
        }
      &:nth-child(odd) {
         padding-right: 5px;
      }
      &:nth-child(2n){
         padding-left: 5px;
      }
      .pic {
        width: 108px;
        height:  108px;
        margin: 0 auto;
      }
    }
  }
}
.text{
    color: @color_content;
}
</style>
