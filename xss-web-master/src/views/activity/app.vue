<template>
 <div  class="bg-f5">
    <!-- 引用header -->
    <Header></Header>
     <!-- <Breadcrumb :routerList="routerList" :slotRight="true" :title="我要交社保"></Breadcrumb> -->
     <div class="page-content mb20">
       <div class="w1200 bg-white">
          <div class="mouldle-detail" data-mod-name="mui/mdv/zebra" mdv-cls="mui/desc-mods/custommodule/index" v-html='activity.text' v-if="activity">
              <!-- 读取富文本编辑器的内容 -->
            
          </div>
       </div>
      </div>
     <!-- 引入 微信弹窗 -->
     <weixinPopup></weixinPopup>
    <!-- 引入footer -->
    <Footer></Footer>
  </div>
</template>

<script type="text/ecmascript-6">
import Header from "@/components/header/index.vue";
import Breadcrumb from "@/components/breadcrumb/index.vue";
import weixinPopup from "@/components/weixinPopup/index.vue";
import Footer from "@/components/footer/index.vue";
import { get } from "@/modules/js/api/server";
import url from "@/modules/js/api/apiUrl";
import $ from "jquery";
import { getUrlKey } from "@/utils/common.js";
export default {
  components: { Header, Footer,Breadcrumb,weixinPopup },
  props:{},
  data(){
    return {
        id: undefined,
        activity: undefined,
        routerList: [
        {
          value: "缴社保",
          linkUrl: ""
        }
      ],
    }
  },
  watch:{},
  computed:{},
  methods:{
    getActivity(){
      // 编码
      let params = {
        activityId: this.id
      }
      get(url.getActivityInfo, params).then(response => {
        if (response.data.result == "00000000") {
          // top文章
          const activity = response.data.data.activity;
          if (activity) {
            this.activity = activity;
          }
        }
      });
    }
  },
  created(){
    this.id = getUrlKey("id");
    this.getActivity();
  },
  mounted(){}
}
</script>

<style  lang="less">
@import "~@/common/less/common.less";
.page-content{
  .mouldle-detail{
    padding:20px;
    min-height:200px;
    box-sizing: border-box;
    p{
      line-height:20px;
      font-size:14px;
    }
  }
}
</style>
<style >
  .jsb_why_box_l{

  }
  .jsb_why_box_l ul {
    overflow: hidden;
  }
  .jsb_why_box_l ul li {
    float: left;
    padding-left: 40px;
    margin-bottom: 30px;
  }
  .jsb_content {
    visibility: visible;
    text-align: center;
  }
  .jsb_content .jsb_title {
    display: inline-block;
    font-size: 48px;
    color: #02214c;
    margin-top: 110px;
    font-weight: 100;
    margin-bottom: 100px;
  }
  .jsb_why_box {
    overflow: hidden;
  }
  .jsb_why_box_l {
    width: 710px;
    float: left;
    overflow: hidden;
    padding-bottom: 100px;
  }
  .jsb_icon {
    width: 76px;
    height: 76px;
    border-radius: 50%;
    margin-right: 18px;
    position: relative;
    box-sizing: border-box;
    float: left;
    border: 1px solid #e1e1e1;

  }
  .jsb_icon_r {
    text-align: left;
    color: #969595;
    font-size: 15px;
    overflow: hidden;
  }
</style>
