<template>
  <div class="page">
    <tab>
      <tab-item selected @on-item-click="onItemClick">兑换商品</tab-item>
      <tab-item @on-item-click="onItemClick">代金券</tab-item>
    </tab>
    <div :class="bgStyle" style="padding-left: 15px;padding-right: 15px;">
      <div class="zhuangxiu-case-list  pt15" v-if="productList">
        <ul class="case-list-ul clearfix" v-infinite-scroll="loadMore" infinite-scroll-disabled="onFetching" infinite-scroll-distance="10" infinite-scroll-throttle-delay="300">
          <li class="case-list-li " v-for="(item,index) in lists" :key="index">
            <router-link :to="{ path: '/product/detail',query:{id:item.id}}">
              <div class="case-list-pic">
                <img class="image" :src="item.image" alt="">
              </div>
              <div class="case-list-desc">
                <p class="ellipsis">
                  <i class=" iconfont icon-dingwei"></i>{{item.fullName}}</p>
                <p class="ellipsis" style="color:#f60;">{{item.exchangePoint}}个金豆可兑换</p>
              </div>
            </router-link>
          </li>
        </ul>
        <load-more :tip="tipText" v-if="dataStatus==0"></load-more>
        <divider v-if="dataStatus==2" class="pt10">{{pageTips}}</divider>
      </div>
      <div class="zhuangxiu-case-list  pt15" v-if="couponList">
        <ul class="case-list-ul clearfix" v-infinite-scroll="loadMoreCoupon" infinite-scroll-disabled="onFetching" infinite-scroll-distance="10" infinite-scroll-throttle-delay="300">
          <li class="case-list-li " v-for="(item,index) in coupons" :key="index">
            <router-link :to="{ path: '/coupon/detail',query:{id:item.id}}">
              <!--<div class="case-list-pic">
                <img class="image" :src="item.image" alt="">
              </div>-->
              <div class="case-list-desc">
                <p class="ellipsis">
                  <i class=" iconfont icon-dingwei"></i>{{item.name}}</p>
                <p class="ellipsis" style="color:#f60;">{{item.point}}个金豆可兑换</p>
              </div>
            </router-link>
          </li>
        </ul>
        <load-more :tip="tipText" v-if="dataStatus==0"></load-more>
        <divider v-if="dataStatus==2" class="pt10">{{pageTips}}</divider>
      </div>
    </div>

  </div>
</template>

<script type="text/ecmascript-6">
  import { Tab, TabItem, Divider, LoadMore,Flexbox, FlexboxItem } from "vux";
  import url from "@/api/apiUrl";
  import { get } from "@/api/server";
  import Page from '@/utils/page'
  export default {
    components: {Tab, TabItem, Divider,LoadMore,Flexbox, FlexboxItem},
    props: {},
    data() {
      return {
        onFetching: false,//为true的时候禁止无线滚动
        pageTips:Page.noDataTip,
        dataStatus:Page.dataStatus,
        tipText: Page.loadingTip,
        nodata:false,
        lists: Page.lists,
        productType:'service',
        productList:true,
        couponList:false,
        coupons:[],
        bgStyle: 'bg_f'
      };
    },
    watch: {},
    computed: {

    },
    created() {
      this.loadMore();
      this.loadMoreCoupon();
    },
    mounted() {

    },
    methods: {
      loadMore() {
        //此处需自己写接口，替换下方注释的接口，list数据格式如下方静态数据
        Page.params.productType = this.productType;
        Page.list(url.productList).then(res => {
          this.dataStatus = Page.dataStatus;
          this.pageTips = Page.noDataTip;
          if(this.dataStatus != 0){
            this.onFetching = true;
          }else{
            this.onFetching = false;
          }
          this.lists = Page.lists;
        });
      },
      loadMoreCoupon() {
        //此处需自己写接口，替换下方注释的接口，list数据格式如下方静态数据
        Page.params.isExchange = true;
        Page.list(url.couponList).then(res => {
          this.dataStatus = Page.dataStatus;
          this.pageTips = Page.noDataTip;
          if(this.dataStatus != 0){
            this.onFetching = true;
          }else{
            this.onFetching = false;
          }
          this.coupons = Page.lists;
        });
      },
      onItemClick (index) {
        console.log('on item click:', index)
        if(index == 0){
          this.coupons = [];
          this.loadMore();
          this.productList = true;
          this.couponList = false;
        }else{
          this.lists = [];
          this.loadMoreCoupon();
          this.productList = false;
          this.couponList = true;
        }
      },
    }
  };
</script>

<style scoped lang="less">

  .zhuangxiu-case-list {
  .case-list-li {
    width: 50%;
    float: left;
    padding-bottom: 0.4rem;
  .case-list-pic {
    width: 100%;
    height: 120px;
  img {
    border-radius: 5px;
    border: 1px solid #f5f5f5;
  }
  }
  &:nth-child(odd) {
  .case-list-pic {
    padding-right: 5px;
  }
  }
  &:nth-child(2n) {
  .case-list-pic {
    padding-left: 5px;
  }
  }
  .case-list-desc {
    padding-top: 5px;
    font-size: 12px;
  i {
    font-size: 14px;
    margin-right: 3px;
  }
  p:first-child {
    color: #0e0e0e;
    font-weight: 600;
    font-size: 14px;
  }
  p:last-child {
    color: #bebebe;
    font-size: 12px;
  }
  }
  }
  }
  .package {
  .vux-flexbox-item {
    padding: 10px;
  &:nth-child(odd) {
     padding-right: 5px;
     padding-bottom: 0px;
   }
  &:nth-child(2n) {
     padding-left: 5px;
     padding-bottom: 0px;
   }
  .flex-item {
    background-color: #fff;
    padding: 15px 10px;
    border-radius: 5px;
    height: 95px;
    width: 100%;
    position: relative;
  dl {
    width: 100%;
  dt {
    position: absolute;
    right: 10px;
    width: 40px;
    height: 40px;
    top: 50%;
    margin-top: -20px;
  }
  dd {
    margin-right: 45px;
  &.nav_dd1 {
     color: #333333;
     font-size: 16px;
   }
  &.nav_dd2 {
     margin-top: 2px;
     font-size: 12px;
     color: #888888;
   }
  }
  }
  }
  }
  }
</style>
