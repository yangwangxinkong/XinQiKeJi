<!--新手帮助  -->
<template>
  <div class='page'>
    <group :gutter="0">
      <div v-for="(item, index) in data" :key="index">
      <!--<div v-for="(item, index) in data" :key="index">-->
        <!--<cell class="f14" :title="item.name" is-link :border-intent="false" :arrow-direction="showContents[index] ? 'up' : 'down'" @click.native="showContents[index] = !showContents[index]">-->
        <!--</cell>-->
        <cell class="f14" :title="item.name" is-link :border-intent="false" :arrow-direction="item.showContent ? 'up' : 'down'" @click.native="changeItem(index)" >
        </cell>

        <div class="slide" :class="item.showContent?'animate':''" v-if="item.articles">
          <cell class="f14" :title="article.title"  v-for="(article, i) in item.articles" :key="i" :link="'/article/detail?id='+article.id"></cell>
        </div>
      </div>
        <!--<cell class="f14" title="下单流程" is-link :border-intent="false" :arrow-direction="showContent ? 'up' : 'down'" @click.native="showContent = !showContent">-->
        <!--</cell>-->
        <!--<div class="slide" :class="showContent?'animate':''">-->
          <!--<cell   class="f14" title="缴纳社保流程"  is-link></cell>-->
           <!--<cell   class="f14" title="缴纳公积金流程"  is-link></cell>-->
        <!--</div>-->
         <!--<cell-->
          <!--class="f14"-->
          <!--title="常见问题"-->
          <!--is-link-->
          <!--:border-intent="false"-->
          <!--:arrow-direction="showContent2 ? 'up' : 'down'"-->
          <!--@click.native="showContent2 = !showContent2">-->
        <!--</cell>-->
        <!--<div class="slide" :class="showContent2?'animate':''">-->
          <!--<cell   class="f14" title="常见问题一"  is-link></cell>-->
           <!--<cell  class="f14"  title="常见问题二"  is-link></cell>-->
        <!--</div>-->
    </group>

  </div>
</template>

<script>
import { Cell, CellBox, CellFormPreview, Group, Badge } from "vux";
import url from '@/api/apiUrl'
import {get, post, execute} from '@/api/server'
export default {
  data() {
    return {
      data:[]
    };
  },

  components: { Cell, CellBox, CellFormPreview, Group, Badge },

  computed: {},

  mounted() {},

  created() {
      this.initData()
  },

  methods: {
      initData(){
          get(url.articleCategoriesChildren, {codes:'XDLC,CJWT'}).then(response => {
              //console.log("init data:" + JSON.stringify(response))
              if(response.data.result == "00000000"){
                  const data = response.data.data
                  if(data && data.length>0){
                    this.data = data
                    for(let i in data){
                      this.$set(this.data[i], 'showContent', true);
                    }
                  }
              }
              //console.log("data:" + JSON.stringify(this.data));
          });
      },
      changeItem(index){
        //console.log("index:" + this.data[index].showContent);
        this.data[index].showContent = !this.data[index].showContent;
        //console.log("index1:" + this.data[index].showContent);
      }
  }
};
</script>
<style lang='less' scoped>
.slide {
  overflow: hidden;
  max-height: 0;
  transition: max-height 0.5s cubic-bezier(0, 1, 0, 1) -0.1s;
  .weui-cell {
  padding: 10px 15px 10px 25px;
    &::before {
      left: 0;
    }
  }
}
.animate {
  max-height: 9999px;
  transition-timing-function: cubic-bezier(0.5, 0, 1, 0);
  transition-delay: 0s;
  position: relative;
  &:before {
    content: " ";
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    height: 1px;
    border-top: 1px solid #d9d9d9;
    color: #d9d9d9;
    -webkit-transform-origin: 0 0;
    transform-origin: 0 0;
    -webkit-transform: scaleY(0.5);
    transform: scaleY(0.5);
  }
}
</style>
