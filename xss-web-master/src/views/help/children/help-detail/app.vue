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
                        <div class="div-menu-submenu">
                            <div class="div-menu-submenu-title f14">
                                <span>导航一</span>
                            </div>
                            <ul class="div-menu-sub">
                                <li class="div-menu-item-selected">
                                    <a href="" class="ellipsis">我是子级</a>
                                </li>
                                <li>
                                    <a href="" class="ellipsis">我是子级</a>
                                </li>
                            </ul>
                        </div>
                        <div class="div-menu-submenu">
                            <div class="div-menu-submenu-title f14">
                                <span>导航二</span>
                            </div>
                            <ul class="div-menu-sub">
                                <li>
                                    <a href="" class="ellipsis">我是子级</a>
                                </li>
                                <li>
                                    <a href="" class="ellipsis">我是子级</a>
                                </li>
                            </ul>
                        </div>
                        <div class="div-menu-submenu">
                            <div class="div-menu-submenu-title f14">
                                <span>导航三</span>
                            </div>
                            <ul class="div-menu-sub">
                                <li>
                                    <a href="" class="ellipsis">我是子级</a>
                                </li>
                                <li>
                                    <a href="" class="ellipsis">我是子级</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="pull-right fr">
                    <!-- 咨询详情 -->
                    <div class="div-detail">
                      <div class="div-detail-head">
                        <div class="head-title f20">详情标题</div> 
                      </div> 
                      <div class="div-detail-body pt20">
                        <!-- 读取富文本编辑器的内容 -->
                        <div class="knowlwdgeLinkDetails">
                              <p>
                              <span style="color:#333333;font-family:&quot;font-size:14px;white-space:normal;background-color:#FFFFFF;">为贯彻落实成都市创建“放心舒心消费示范成都”要求，解决消费者家装过程中的痛点，成都市消费者协会今年三月在成都市建筑装饰协会设计投诉站，受理消费者对于家庭装饰装修质量和服务的投诉。</span><br style="box-sizing:content-box;color:#333333;font-family:&quot;font-size:14px;white-space:normal;background-color:#FFFFFF;">
                            <span style="color:#333333;font-family:&quot;font-size:14px;white-space:normal;background-color:#FFFFFF;">为了充分保障消费者的合法权益，广大消费者可以通过关注便民装微信公众号获取装修咨询、合同备案、维权保障等便民服务。</span><br style="box-sizing:content-box;color:#333333;font-family:&quot;font-size:14px;white-space:normal;background-color:#FFFFFF;">
                            <span style="color:#333333;font-family:&quot;font-size:14px;white-space:normal;background-color:#FFFFFF;">扫描二维码，为您的家装上一份保险</span>
                            </p>
                            <p>
                              <span style="color:#333333;font-family:&quot;font-size:14px;white-space:normal;background-color:#FFFFFF;"><img src="http://www.sptxmall.com/cdbmz/upload/image/201711/8df327b8-834b-4eb6-b9a6-89070c27ec4b.jpg" alt=""><br>
                            </span>
                            </p>
                            <p>
                              <span style="color:#333333;font-family:&quot;font-size:14px;white-space:normal;background-color:#FFFFFF;"><img src="http://www.sptxmall.com/cdbmz/upload/image/201711/2e39802c-81f9-4f83-9ce3-b011295c9a8d.jpg" alt=""><br>
                            </span>
                            </p>
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
import $ from "jquery";
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
      routerList: [
        {
          value: "新闻咨询",
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
  methods: {},
  created() {},
  mounted() {
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