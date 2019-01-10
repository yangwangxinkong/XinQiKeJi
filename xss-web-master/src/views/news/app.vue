<template>
    <div  class="bg-f5">
        <!-- 引用header -->
        <Header></Header>
        <Breadcrumb :routerList="routerList"></Breadcrumb>
        <!-- 新闻列表 -->
        <div class="page-content mb20">
            <div class="new-list w1200 bg-white">
                <ul>
                    <li class="list-item" v-for="(item, index) in articles" :key="index" v-if="articles">
                        <a :href="'/news-detail.html?index=3&id='+item.id+'&code='+code" target="_blank" class="clearfix">
                            <div class="pic fl zoomBox border mt5">
                                <img :src="item.cover" class="image zoom" alt="">
                            </div>
                            <div class="con">
                                <div class="title-box color-blue f18">{{item.title}}</div>
                                <div class="text-box f14 grey-6 div-content" v-html='item.introduction.replace("\n", "<br/>")' v-if="item.introduction"></div>
                                <!-- <div class="text-box f14 grey-6 div-content" data-mod-name="mui/mdv/zebra" mdv-cls="mui/desc-mods/custommodule/index" v-html='item.content' v-if="item.content"></div> -->
                                <div class="time-box f12 grey-9">更新时间： {{item.createDate}}</div>
                            </div>
                        </a>
                    </li>                   
                </ul>
            </div>
            <!-- 引入分页 -->
            <Pagination :page-index="pageNumber" :total="count" :page-size="pageSize" @change="pageChange"></Pagination>
        </div>
        <!-- 引入footer -->
        <Footer></Footer>
    </div>
</template>

<script type="text/ecmascript-6">
import Header from "@/components/header/index.vue";
import Breadcrumb from "@/components/breadcrumb/index.vue";
import Footer from "@/components/footer/index.vue";
import Pagination from "@/components/pagination/index.vue";
import { get } from "@/modules/js/api/server";
import url from "@/modules/js/api/apiUrl";
import $ from "jquery";
import { getUrlKey } from "@/utils/common.js";
export default {
  components: { Header, Footer, Breadcrumb, Pagination },
  props: {},
  data() {
    return {
      articles: [],
      routerList: [
        {
          value: "新闻咨询",
          linkUrl: ""
        }
      ],
      code: "",
      pageSize: 10, //每页显示10条数据
      pageNumber: 1, //当前页码
      count: 0 //总记录数
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
        pageNumber: this.pageNumber,
        pageSize: this.pageSize,
        code: this.code
      }
      get(url.getArticleList, params).then(response => {

        //if (response.data.result == "00000000") {
            this.count = response.data.total;
          // top文章
          const articles = response.data.list;
          if (articles && articles.length > 0) {
            this.articles = articles;
          }
       // }
      });

    },
    //改变当前页码
    pageChange(page) {
      this.pageNumber = page;
      this.getList();
    },

    // toDetail(id, code) {
    //     this.$router.push({path:"/news-detail.html", query:{id: id, code: code}})
    // }
  },
  created() {
    this.code = getUrlKey("code");
    if(this.code === undefined || this.code === null) {
      this.code = "XWZX"
    }
    this.getList();
  },
  mounted() {}
};
</script>

<style  lang="less">
@import "~@/common/less/common.less";
.new-list {
  padding: 20px;
  li.list-item {
    margin-bottom: 10px;
    padding-bottom: 10px;
    border-bottom: 1px solid #f5f5f5;
    .pic {
      width: 80px;
      height: 80px;
      overflow: hidden;
      margin-right: 20px
    }
    .con {
      padding-left: 80px;
    }
    .div-content {
      line-height: 20px;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
    //   -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
    }
  }
}
</style>
