<template>
    <div  class="bg-f5">
        <!-- 引用header -->
        <Header></Header>
        <Breadcrumb :routerList="routerList">
          <div slot="slot-right" class="breadcrumb-action pa">
            <a href="javascript:void(0)" class="btn btn-orange ">我要缴社保</a>
          </div> 
        </Breadcrumb>
        <div class="page-content mb20">
            <div class="w1200 clearfix article-box bg-white">
                <div class="pull-left fl">
                    <h4 class="tc f14 title">新闻分类</h4>
                    <!-- 折叠面板 -->
                    <div id="accordion" class="div-menu-inline">
                        <div class="div-menu-submenu" v-for="(item, index) in articles" :key="index" v-if="articles">
                            <div class="div-menu-submenu-title f14">
                                <span>{{item.name}}</span>
                            </div>
                            <ul class="div-menu-sub">
                                <li v-for="(articleDetail, index2) in item.articles" :key="index2">
                                    <a href="javascript:void(0)" @click="toDetail(articleDetail.id)" class="ellipsis">{{articleDetail.title}}</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="pull-right fr">
                    <!-- 咨询详情 -->
                    <div class="div-detail" v-if="article">
                      <div class="div-detail-head">
                        <div class="head-title f20">{{article.title}}</div> 
                      </div> 
                      <div class="div-detail-body pt20">
                        <!-- 读取富文本编辑器的内容 -->
                        <div class="knowlwdgeLinkDetails" data-mod-name="mui/mdv/zebra" mdv-cls="mui/desc-mods/custommodule/index" v-html='article.content'>
							    	    </div>
                      </div> 
                    </div> 
                </div>
            </div>

        </div>
        <!-- 引入footer -->
        <Footer></Footer>
    </div>
</template>

<script type="text/ecmascript-6">
import Header from "@/components/header/index.vue";
import Breadcrumb from "@/components/breadcrumb/index.vue";
import Footer from "@/components/footer/index.vue";
import { get } from "@/modules/js/api/server";
import url from "@/modules/js/api/apiUrl";
import $ from "jquery";
import { getUrlKey } from "@/utils/common.js";

var Accordion = function(el, multiple) {
  this.el = el || {};
  this.multiple = multiple || false;
  var links = this.el.find(".div-menu-submenu-title");
  links.on(
    "click",
    {
      el: this.el,
      multiple: this.multiple
    },
    this.dropdown
  );
};

export default {
  components: { Header, Footer, Breadcrumb },
  props: {},
  data() {
    return {
      id: undefined,
      code: undefined,
      articles: [],
      article: undefined,
      routerList: [
        {
          value: "新闻资讯",
          linkUrl: "news.html"
        },
        {
          value: "新闻详情",
          linkUrl: ""
        }
      ]
    };
  },
  watch: {},
  computed: {},
  methods: {
    // 获取列表数据
    getList() {
      //调用接口获取数据
      // 编码
      let params = {
        //pageNumber: this.pageNumber,
        //pageSize: this.pageSize,
        code: this.code
      }
      get(url.getCategoryArticleList, params).then(response => {

        if (response.data.result == "00000000") {
          // top文章
          const articles = response.data.data;
          if (articles && articles.length > 0) {
            this.articles = articles;
          }
        }
      });
    },

    // 获取列表数据
    getInfo() {
      //调用接口获取数据
      // 编码
      let params = {
        //pageNumber: this.pageNumber,
        //pageSize: this.pageSize,
        id: this.id
      }
      get(url.articleInfo, params).then(response => {

        if (response.data.result == "00000000") {
          // top文章
          const article = response.data.data;
          if (article) {
            this.article = article;
          }
        }
      });
    },

    toDetail(id) {
      console.log(id)
      this.id = id;
      this.getInfo();
    }
      
  },
  created() {

    this.id = getUrlKey("id");
    this.code = getUrlKey("code");

    this.getList();
    this.getInfo();
  },
  mounted() {
    setTimeout(function(){                        
      Accordion.prototype.dropdown = function(e) {
        var $el = e.data.el;
        var $this = $(this);
        var $next = $this.next();
        $next.slideToggle();
        $this.parent().toggleClass("div-menu-submenu-open");
        if (!e.data.multiple) {
          $el
            .find(".div-menu-sub")
            .not($next)
            .slideUp()
            .parent()
            .removeClass("div-menu-submenu-open");
        }
      };
      var accordion = new Accordion($("#accordion"), false);
     }, 500);
  }
};
</script>

<style  lang="less">
@import "~@/common/less/common.less";
.article-box {
  .pull-left {
    width: 240px;
    float: left;
    min-height: 500px;
   
      box-sizing: border-box;
    .title {
      height: 40px;
      line-height: 40px;
      font-size: 16px;
      border-bottom: 1px solid @grey-f5;
    }
    .menu-hidden {
      display: none;
    }
    .div-menu-inline {
      .div-menu-submenu-title {
        padding: 0 20px;
        height: 42px;
        line-height: 42px;
        position: relative;
        cursor: pointer;
        &::after {
          display: inline-block;
          content: " ";
          height: 6px;
          width: 6px;
          border-width: 0 1px 1px 0;
          border-color: #999999;
          border-style: solid;
          transform: matrix(0.71, 0.71, -0.71, 0.71, 0, 0);
          transform-origin: center;
          transition: transform 0.3s;
          position: absolute;
          top: 50%;
          right: 20px;
          margin-top: -5px;
        }
      }
      .div-menu-submenu-open {
        .div-menu-submenu-title {
          color: @color-orange;
          &::after {
            transform-origin: center;
            transform: rotate(-135deg);
            transition: transform 0.3s;
            border-color: @color-orange;
          }
        }
        .div-menu-sub {
          display: block;
        }
      }
      .div-menu-sub {
        display: none;
        li {
          padding-left: 28px;
          font-size: 12px;
          line-height: 42px;
          &.div-menu-item-selected{
            background-color: #ffce1d2b;
            border-right: 2px solid @color-orange;
            a{
              color:@color-orange;
            }
          }
          a {
            color: #1a1a1a;
            display: block;
            &:hover{
                 color:@color-orange;
            }
          }
        }
      }
    }
  }
  .pull-right {
       -webkit-box-flex: 1;
    -ms-flex: auto;
    -webkit-flex: auto;
    flex: auto;
    width: 960px;
    min-height: 500px;
    overflow: hidden;
    padding-left: 45px;
    padding-right: 45px;
    box-sizing: border-box;
     border-left: 1px solid @grey-f5;
    .div-detail-head{
      height:35px;
      line-height:35px;
      padding:20px 0;
      border-bottom:1px solid #f5f5f5;

    }
    .div-detail-body{
      p{
        color: #4a4a4a;
        line-height: 30px;
        font-size:14px;
      }
    }
  }
}
</style>