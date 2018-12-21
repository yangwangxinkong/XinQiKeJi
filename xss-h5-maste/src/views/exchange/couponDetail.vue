<template>
 <div class="page">
    <div class="container ">
        <!-- 产品缩略图 -->
       <!--<div class="banner-container">
            <Swiper :list="swiperList" auto :aspect-ratio="750/750" class="swiper-index" dots-class="custom-bottom" dots-position="center"></Swiper>
        </div>-->
        <!-- 产品的基本信息 -->
        <div class="module-base-wrap padding-15 bg_f">
            <div class="product-title">
                <h2 class="f16">{{couponInfo.name}}</h2>
            </div>
            <div class="product-price inline-price">
                <p class="price-now color_red f16">兑换金豆：<span>{{couponInfo.point}}</span>个</p>
            </div>
            <div class="product-price inline-price">
              <p class="price-now color_red f16">有效日期：<span>{{couponInfo.beginDate}}至{{couponInfo.endDate}}</span>个</p>
            </div>
            <div class="product-additional mb5 mt5">
                <div class="flex-ui flex-start space-between grey_6 f14">
                    <span>可用金豆：<span>{{memberPoint}}个</span></span>
                </div>
            </div>
        </div>

         <!--底部操作组件  -->
        <div class="bottom-bar  flex-ui align-items-center padding-15">
            <div class="flex1 ">
              <x-button @click.native="toExchange" type="primary" v-if="isValid">我要兑换</x-button>
              <x-button type="primary" style="background-color:#666;" v-else>余额不足</x-button>
            </div>

        </div>
    </div>
 </div>
</template>

<script type="text/ecmascript-6">
import { TransferDom, Swiper, Divider, LoadMore, Popup,XButton } from "vux";
import { get, post } from '@/api/server'
import url from '@/api/apiUrl'
import storage from "@/utils/common"
export default {
  directives: {
    TransferDom
  },
  components: { Swiper, Divider, LoadMore, Popup, XButton },
  props: {},
  data() {
    return {
      memberPoint: 0,
      isValid : false,
      id: this.$route.query && this.$route.query.id,
      couponInfo: {}
    };
  },
  watch: {
  },
  computed: {
  },
  methods: {
    getCouponInfo() {
      get(url.couponDetail, {id: this.id}).then(response => {
        if(response.data.result == "00000000"){
            this.couponInfo = response.data.data;
            console.log("couponInfo:" + JSON.stringify(this.couponInfo))
            this.isValid = (this.couponInfo.point <= this.memberPoint);
        }
      })
    },
    toExchange() {
      get(url.couponExchange, {id: this.id}).then(response => {
        if(response.data.result == "00000000"){
          this.$router.push({ path:'/member/index'});
        }else{
          this.$vux.toast.show({
            type: 'alert',
            text: response.data.data,
            width: '120px',
            time: 1000
          })
        }
      })
    }
  },
  created() {
    localStorage.setItem("order_type", null);
    //this.getSwiperList();
    this.getCouponInfo();
    let member = storage.get("member");
    this.memberPoint = member.point;
  },
  mounted() {}
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
@import "../../assets/styles/less/public.less";
.banner-container {
  font-size: 0;
}
.module-base-wrap {
  overflow: hidden;
  padding-top: 15px;
  background: #fff;
  .product-price {
    .price-now {
      .icon-text {
        display: inline-block;
        font-size: 12px;
        color: #ffffff;
        background-color: @color_red;
        border: 1px solid @color_red;
        border-radius: 20px;
        line-height: 1.4;
        padding: 2px 6px;
        margin-right: 12px;
        margin-top: 8px;
        margin-left: 12px;
      }
    }
  }
  .product-sku {
    .skuText {
    }
  }
}
// 详情信息
.module-html-wrap {
  .module-title {
    padding-left: 15px;
    height: 30px;
    line-height: 30px;
  }
  .module-content {
    img {
      width: 100%;
      display: block;
    }
  }
}

// 底部操作按钮
.bottom-bar {
  position: fixed;
  bottom: 0;
  width: 100%;
  margin: 0 auto;
  z-index: 110;
  background-color: #fff;
  height: 50px;
  line-height: 50px;
  &:before {
    content: " ";
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    height: 1px;
    border-top: 1px solid #c0bfc4;
    color: #c0bfc4;
    -webkit-transform-origin: 0 0;
    transform-origin: 0 0;
    -webkit-transform: scaleY(0.5);
    transform: scaleY(0.5);
  }
  > div {
    line-height: 50px;
  }
  .btn-fav {
    padding: 0 10px;
  }
  div.flex1 {
    display: flex;
    justify-content: flex-end;
    button {
      outline: none;
      border: none;
    }
    .bottom-bar-btn {
      height: 35px;
      line-height: 35px;
      width: 50%;
      &.bottom-bar-btn-disabled {
        background-color: #a6a6a6;
        background-image: none;
      }
      &:first-child {
        border-radius: 30px 0 0 30px;
      }
      &:last-child {
        border-radius: 0 30px 30px 0;
        background-image: -webkit-linear-gradient(left, #00bf63, #06ac48);
        background-image: linear-gradient(to right, #00bf63, #06ac48);
      }
      a.action {
        display: block;
        width: 100%;
      }
      span {
        display: block;
        width: 100%;
        color: #fff;
        font-size: 16px;
      }
    }
  }
}

// sku
.sku-container {
  background-color: #fff;
  padding-bottom: 50px;
  .sku-header {
    padding: 15px;
    .sku-header-image {
      position: relative;
      float: left;
      width: 80px;
      height: 80px;
      background: #ffffff;
      border-radius: 2px;
    }
    .sku-header-goods-info {
      padding: 10px 60px 10px 10px;
      min-height: 82px;
      overflow: hidden;
      box-sizing: border-box;
    }
    .close-icon {
      top: 10px;
      right: 15px;
      font-size: 20px;
      color: #999;
      position: absolute;
      text-align: center;
    }
  }
  .sku-body {
    max-height: 380px;
    .sku-group-container {
      margin-left: 15px;
      padding: 12px 0 2px;
    }
    .sku-row {
      margin: 0 15px 10px 0;
      font-size: 14px;
      .sku-row-title {
        padding-bottom: 10px;
      }
      .sku-row-item {
        display: inline-block;
        padding: 5px 12px;
        margin: 0 10px 10px 0;
        height: 28px;
        min-width: 52px;
        line-height: 16px;
        font-size: 12px;
        color: #333;
        text-align: center;
        border: 1px solid #f5f5ff;
        background-color: #f5f5ff;
        border-radius: 20px;
        box-sizing: border-box;
        &.sku-row-item-active {
          color: #fff;
          border-color: @color_base;
          background: @color_base;
        }
      }
    }
    .sku-stepper {
      float: right;
      font-size: 0;
      .stepper-minus,
      .stepper-plus {
        width: 40px;
        height: 30px;
        box-sizing: border-box;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        position: relative;
        padding: 5px;
        vertical-align: middle;
        font-size: 14px;
        color: #666;
      }
      .stepper-minus {
        border-radius: 2px 0 0 2px;
      }
      .stepper-minus-disabled,
      .stepper-plus-disabled {
        background-color: #f8f8f8;
      }
      .stepper-plus {
        border-radius: 0 2px 2px 0;
      }
      .stepper-input {
        width: 33px;
        height: 26px;
        padding: 1px;
        border: 1px solid #e5e5e5;
        border-width: 1px 0;
        border-radius: 0;
        box-sizing: content-box;
        color: #666;
        font-size: 14px;
        vertical-align: middle;
        text-align: center;
        -webkit-appearance: none;
      }
    }
  }
  .sku-actions {
    position: absolute;
    bottom: 10px;
    width: 100%;
    a.flex1 {
      height: 38px;
      line-height: 38px;
      background-color: @color_base;
      color: #ffffff;
      &.disabled {
        background-color: #a6a6a6;
      }
      &:first-child {
        border-radius: 30px 0 0 30px;
      }
      &:last-child {
        border-radius: 0 30px 30px 0;
      }
      span {
        display: block;
        width: 100%;
        color: #fff;
      }
    }
  }
}
.weui-btn_primary{
  background-color: @color_base;
}
</style>
