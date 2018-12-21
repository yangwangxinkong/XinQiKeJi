<template>
  <div class="page">
    <tab bar-active-color="#E60014" custom-bar-width="30px">
      <tab-item selected @on-item-click="onItemClick">商品列表</tab-item>
      <tab-item @on-item-click="onItemClick">热门活动</tab-item>
    </tab>
    <div :class="bgStyle" style="padding:10px;">
      <!--<div class="list_2">
        <ul>
          <li>
            <a href="">
              <img src="http://jdudu.sptxmall.com/upload/image/201807/fd291be7-a8ea-4574-969c-ed3cd14034ca-thumbnail.jpg" alt="商品图片" class="goods_photo">
              <h4 class="goods_title ellipsis">这里是商品标题1</h4>
              <em class="goods_price">￥4999.00</em>
            </a>
          </li>
          <li>
            <a href="">
              <img src="http://jdudu.sptxmall.com/upload/image/201807/fd291be7-a8ea-4574-969c-ed3cd14034ca-thumbnail.jpg" alt="商品图片" class="goods_photo">
              <h4 class="goods_title">这里是商品标题2</h4>
              <em class="goods_price">￥4999.00</em>
            </a>
          </li>
          <li>
            <a href="">
              <img src="http://jdudu.sptxmall.com/upload/image/201807/fd291be7-a8ea-4574-969c-ed3cd14034ca-thumbnail.jpg" alt="商品图片" class="goods_photo">
              <h4 class="goods_title">这里是商品标题3</h4>
              <em class="goods_price">￥4999.00</em>
            </a>
          </li>
          <li>
            <a href="">
              <img src="http://jdudu.sptxmall.com/upload/image/201807/fd291be7-a8ea-4574-969c-ed3cd14034ca-thumbnail.jpg" alt="商品图片" class="goods_photo">
              <h4 class="goods_title">这里是商品标题4</h4>
              <em class="goods_price">￥4999.00</em>
            </a>
          </li>
        </ul>
      </div>-->
      <!--<div class="list_2 padding-15">
        <ul v-infinite-scroll="loadMore" infinite-scroll-disabled="onFetching" infinite-scroll-distance="10" infinite-scroll-throttle-delay="300">
          <li v-for="(item,index) in lists" :key="index" >
            <router-link :to="{ path: '/product/detail',query:{id:item.id}}" style="padding-right: 5px;">
              <img :src="item.image" alt="商品图片" class="goods_photo">
              <h4 class="goods_title ellipsis">{{item.fullName}}</h4>
              <em class="goods_price">{{item.exchangePoint}}个金豆</em>
            </router-link>
          </li>
        </ul>
        <load-more :tip="tipText" v-if="dataStatus==0"></load-more>
        <divider v-if="dataStatus==2" class="pt10">{{pageTips}}</divider>
      </div>-->
      <div class="product-case-list" v-if="productList">
        <ul
          class="case-list-ul clearfix"
          v-infinite-scroll="loadMore"
          infinite-scroll-disabled="onFetching"
          infinite-scroll-distance="10"
          infinite-scroll-throttle-delay="300"
        >
          <li class="case-list-li" v-for="(item,index) in lists" :key="index">
            <router-link :to="{ path: '/product/detail',query:{id:item.id}}">
              <div class="list-box">
                <div class="case-list-pic">
                  <img class="image" :src="item.image" alt>
                </div>
                <div class="case-list-desc">
                  <p class="ellipsis">{{item.fullName}}</p>
                  <p class="ellipsis" style="color:#FF990A;;">{{item.exchangePoint}}个金豆可兑换</p>
                </div>
              </div>
            </router-link>
          </li>
        </ul>
        <load-more :tip="tipText" v-if="dataStatus==0"></load-more>
        <divider v-if="dataStatus==2" class="pt10">{{pageTips}}</divider>
      </div>
      <div class="activity-box" v-if="activityList">
        <!-- <flexbox :gutter="0" wrap="wrap">
          <flexbox-item :span="1/2" v-for="(activity,index) in activities" :key="index" v-if="activities">
            <div class="flex-item">
              <router-link :to="{path:activity.url + '&id='+activity.id}">
                <dl>
                  <dt><img :src="activity.bannerWx" class="image"></dt>
                  <dd class="nav_dd1 ellipsis">{{activity.mainTitle}}</dd>
                  <dd class="nav_dd2 ellipsis2">{{activity.subTitle}}</dd>
                </dl>
              </router-link>
            </div>
          </flexbox-item> 
        

        </flexbox>-->
        <ul class="activity-list" v-if="activities">
          <li v-for="(activity,index) in activities" :key="index">
            <router-link :to="{path:activity.url + '&id='+activity.id}">
              <img :src="activity.bannerWx" class="image">
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import { Tab, TabItem, Divider, LoadMore, Flexbox, FlexboxItem } from "vux";
import url from "@/api/apiUrl";
import { get } from "@/api/server";
import Page from "@/utils/page";
export default {
  components: { Tab, TabItem, Divider, LoadMore, Flexbox, FlexboxItem },
  props: {},
  data() {
    return {
      onFetching: false, //为true的时候禁止无线滚动
      pageTips: Page.noDataTip,
      dataStatus: Page.dataStatus,
      tipText: Page.loadingTip,
      nodata: false,
      lists: Page.lists,
      productType: "service",
      productList: true,
      activityList: false,
      activities: [],
      bgStyle: ""
    };
  },
  watch: {},
  computed: {},
  created() {
    this.loadMore();
    this.getActivities();
  },
  mounted() {},
  methods: {
    getActivities() {
      get(url.recommondActivities, {}).then(response => {
        if (response.data.result == "00000000") {
          this.activities = response.data.data;
        }
      });
    },
    loadMore() {
      //此处需自己写接口，替换下方注释的接口，list数据格式如下方静态数据
      Page.params.productType = this.productType;
      Page.list(url.productList).then(res => {
        this.dataStatus = Page.dataStatus;
        this.pageTips = Page.noDataTip;
        if (this.dataStatus != 0) {
          this.onFetching = true;
        } else {
          this.onFetching = false;
        }
        this.lists = Page.lists;
      });
    },
    onItemClick(index) {
      console.log("on item click:", index);
      if (index == 0) {
        this.productList = true;
        this.activityList = false;
        // this.bgStyle = 'bg_f';
      } else {
        this.productList = false;
        this.activityList = true;
        // this.bgStyle = '';
      }
    }
  }
};
</script>

<style scoped lang="less">
/*.div-list-container{
    .div-list-ul{

    }
    .div-list-li{
      padding-top:10px;
      width: 50%;
    &:nth-child(odd){
    .list-box{
      padding-right:5px;
    }
    }
    &:nth-child(2n){
    .list-box{
      padding-left:5px;
    }
    }
    }
    }
    .case-pic{
      width: 60%;margin: .5rem auto;display: block;
  }*/
/*.list_2 {
    ul {
      li {
        width: 50%;float: left;padding: 1rem 0;
        outline: 1px solid #ddd;
        background: #fff;
        a {
          display: block;
          text-decoration: none; // 去除默认下划线
        }
        .goods_title,.goods_price {
          padding: 0 1rem; // 加上左右内填充,防止文字和边框粘结
          text-align: center;
        }
        .goods_photo {
          width: 60%;margin: .5rem auto;display: block;
        }
      }
    }
  }
   .goods_title,.goods_price {
     display: block;position: relative;
   }
  .goods_title {color:#000;font-size: 1rem;}
  .goods_price {color:#f60;font-size: 1rem;font-weight: bold;}*/
.product-case-list {
  .case-list-li {
    width: 50%;
    float: left;
    padding-bottom: 0.4rem;
    text-align: center;
    .list-box {
      padding: 10px;
      background: #fff;
    }
    .case-list-pic {
      width:3.6rem;
      height: 3.6rem;
      margin: 10px auto 10px;
      img {
        border-radius: 5px;
        border: 1px solid #f5f5f5;
      }
    }
    &:nth-child(odd) {
      padding-right: 5px;
    }
    &:nth-child(2n) {
      padding-left: 5px;
    }
    .case-list-desc {
      padding-top: 5px;
      font-size: 14px;
      p {
        color: @font-color;
      }
    }
  }
}
// .package {
//   .vux-flexbox-item {
//     padding: 10px;
//     &:nth-child(odd) {
//       padding-right: 5px;
//       padding-bottom: 0px;
//     }
//     &:nth-child(2n) {
//       padding-left: 5px;
//       padding-bottom: 0px;
//     }
//     .flex-item {
//       background-color: #fff;
//       padding: 15px 10px;
//       border-radius: 5px;
//       height: 95px;
//       width: 100%;
//       position: relative;
//       dl {
//         width: 100%;
//         dt {
//           position: absolute;
//           right: 10px;
//           width: 40px;
//           height: 40px;
//           top: 50%;
//           margin-top: -20px;
//         }
//         dd {
//           margin-right: 45px;
//           &.nav_dd1 {
//             color: #333333;
//             font-size: 16px;
//           }
//           &.nav_dd2 {
//             margin-top: 2px;
//             font-size: 12px;
//             color: #888888;
//           }
//         }
//       }
//     }
//   }
// }

.activity-list {
  li {
    width: 100%;
    background: #fff;
    height: 4.213rem;
    margin-bottom: 10px;
    img {
      display: block;
      width: 100%;
      height: 100%;
      border-radius: 5px;
    }
  }
}
</style>
