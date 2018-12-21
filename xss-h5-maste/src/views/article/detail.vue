<template>
  <div class="page">
    <div class="container bg_f padding-15 pt15" style="    background-color: #ffffff !important;">
      <div class="weui-article">
        <h2 class="weui-weixin-title">{{article.title}}</h2>
        <div class="weui-weixin-info">
          <!--meta-->
          <em class="weui-weixin-em">
            <span class>{{article.createDate}}</span>
          </em>
          <a
            class="weui-weixin-a weui-weixin-nickname fr"
            href="javascript:void(0);"
            v-if="article.author"
          >
            <img src="../../assets/default-img.png" alt="" class="pr5" style="width.533rem;height:.533rem;float:left">
            {{article.author}}
          </a>
        </div>
        <!--meta结束-->
        <div
          class="module-content bg_f f14"
          data-mod-name="mui/mdv/zebra"
          mdv-cls="mui/desc-mods/custommodule/index"
          v-html="article.content"
        >
          <!--<p style="text-indent: 0em; text-align: left;" v-if="article.cover">
                <span style="font-family: 微软雅黑, Microsoft\ YaHei; color: rgb(89, 89, 89); font-size: 16px;" >
                    <strong>
                        <img :src="article.cover" width="900" height="507">
                    </strong>
                </span>
          </p>-->
          <!-- <p style="font-size: 18px;padding: 10px 0" class="135brush" v-html="article.content">

          </p>-->
        </div>
        <!--内容结束-->
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import url from "@/api/apiUrl";
import { get, post, execute } from "@/api/server";
export default {
  components: {},
  props: {},
  data() {
    return {
      id: undefined,
      article: {}
    };
  },
  watch: {},
  computed: {},
  created() {
    this.id = this.$route.query && this.$route.query.id;
    this.getInfo();
  },
  mounted() {},
  methods: {
    getInfo() {
      get(url.articleInfo, { id: this.id }).then(response => {
        console.log("article info:" + JSON.stringify(response));
        if (response.data.result == "00000000") {
          this.article = response.data.data;
          document.title = this.article.title;
        }
      });
    }
  }
};
</script>

<style lang="less">
.module-content {
  img {
    width: 100%;
    display: block;
  }
}
</style>

<style scoped lang="less">
.weui-weixin-title {
  font-size: 20px;
  color:@font-color;
  font-weight: 400;
  margin-bottom: .227rem;
  line-height: 1.4;
}
.weui-weixin-info {
  margin-bottom: 5px;
  line-height: 20px;
  font-size: 0;
  .weui-weixin-a {
    display: inline-block;
    vertical-align: middle;
    margin-right: 12px;
    margin-bottom: 10px;
    font-size: 16px;
    color: #81839F;
  }
  .weui-weixin-em {
    color: #8c8c8c;
    font-style: normal;
    display: inline-block;
    font-size: 16px;
    margin-bottom: 10px;
    margin-right: 8px;
    vertical-align: middle;
  }
}
.weui-weixin-content {
  img {
    height: auto !important;
    max-width: 100%;
    display: block;
  }
}
</style>
