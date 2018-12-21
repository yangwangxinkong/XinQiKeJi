<template>
  <div class="page">
    <div class="container shop-container" v-if='productDetail'>
      <!-- 产品缩略图 -->
      <div class="banner-container">
        <Swiper :list="swiperList" auto class="swiper-index" dots-class="custom-bottom" dots-position="center">></Swiper>
      </div>
      <!-- 产品的基本信息 -->
      <div class="module-base-wrap padding-15 bg_f">
        <div class="product-title">
          <h2 class="f16">{{productDetail.fullName}}</h2>
        </div>
        <div class="product-price inline-price">
          <!--<p class="price-original grey_be f12">金豆：<del>{{productDetail.point}}</del>个</p>-->
          <p class="price-now color_red f16">兑换金豆：
            <span>{{productDetail.exchangePoint}}</span>个</p>
        </div>
        <div class="product-additional mb5 mt5">
          <div class="flex-ui flex-start space-between grey_6 f14">
            <span>可用金豆：
              <span>{{memberPoint}}个</span>
            </span>
            <!--<span>发货时间：<span>{{productDetail.deliveryTime}}</span></span>
                    <span>发货地：<span>{{productDetail.deliveryPlace}}</span></span>-->
          </div>
        </div>
        <div class="product-sku">
          <div class="skuText clearfix f14 mb5" @click="showSku=true" v-for="(specification, index) in productDetail.specificationValueNames" :key="index">
            <div class="fl pr10 grey_6">{{specification.specificationName}}</div>
            <div class="fl grey_0" v-for="(specificationValue, index) in specification.specificationValues" :key="index">{{specificationValue.name}}</div>
            <i class=" fr grey_be iconfont icon-arrow-right"></i>
          </div>
        </div>
      </div>
      <!-- 读取富文本编辑器的内容  -->
      <!--<div class="module-html-wrap item-detail-info mt10 bg_f">
            <h3 class="module-title f16">详情信息</h3>
            <div class="module-content">
                    <div class="mui-custommodule mdv-custommodule module-content" data-mod-name="mui/mdv/zebra" mdv-cls="mui/desc-mods/custommodule/index" v-html='productDetail.introduction'>

                    </div>
            </div>
        </div>-->

      <!--底部操作组件  -->
      <!-- <div class="bottom-bar  flex-ui align-items-center padding-15">
        <div class="flex1 ">
          <x-button @click.native="buyTap" type="primary" v-if="isValid">我要兑换</x-button>
          <x-button type="primary" style="background-color:#666;" v-else>余额不足</x-button>
        </div>

      </div> -->
      <!-- 底部按钮 -->
      <section class="footer-bar">
        <flexbox>
          <flexbox-item>
            <div style="padding:6px 0 5px 15px" v-if="isValid">
              <p style="line-height:.533rem;font-size:.373rem">应付金豆：
                <span class="color_base">{{productDetail.exchangePoint}}</span>个</p>
              <p style="line-height:.533rem;font-size:.32rem;color: rgba(45,46,70,0.50);">您目前可用金豆：{{memberPoint}}</p>
            </div>
            <p class="amount-text" v-else>
              <span style="color: #2D2E46;">积分不足，赚点再来</span>
            </p>
          </flexbox-item>
          <flexbox-item :span="5">
            <div class="flex1 tc white footer-btn" @click="buyTap()" v-if="isValid">
              <span class="f16 pl0">我要兑换</span>
            </div>
            <div class="flex1 tc white footer-btn" @click="handlerJump()" v-else>
              <span class="f16 pl0">赚积分</span>
            </div>
          </flexbox-item>
        </flexbox>
      </section>
      <!-- 底部弹窗 -->
      <div v-transfer-dom>
        <popup v-model="showSku">
          <div class="sku-container">
            <!--头部  -->
            <div class="sku-header padding-15">
              <div class="sku-header-image">
                <img class="image" :src="productDetail.image" alt="">
              </div>
              <div class="sku-header-goods-info f14">
                <div class="sku-oods-name ellipsis">{{productDetail.title}}</div>
                <div class="sku-goods-price color_red">
                  <span class="sku-price-num">{{productDetail.exchangePoint}}个</span>
                </div>
                <div class="sku-selected f14" v-if="productDetail.specificationIds && productDetail.specificationIds.length>0">已选：
                  <span v-for="(specification, index) in productDetail.specificationIds" :key="index">{{specification.name}} &nbsp;&nbsp;</span>
                </div>
                <i class="close-icon iconfont icon-close" @click="showSku=false"></i>
              </div>
            </div>
            <!-- 主体 -->
            <div class="sku-body">
              <div class="sku-group-container">
                <div class="sku-row" v-for="(specification, index) in productDetail.specifications" :key="index">
                  <div class="sku-row-title">{{specification.name}}：</div>
                  <span class="sku-row-item " :class="{'sku-row-item-active' : selected(productDetail.specificationIds, specificationValue.id)}" v-for="(specificationValue, index) in specification.specificationValues" :key="index" @click="changeColor(productDetail.specificationValueNames, specification.id, specificationValue.id)">{{specificationValue.name}}</span>
                  <!-- <span class="sku-row-item">奶油米黄色</span> -->
                </div>
                <div class="sku-row">
                  <div class="clearfix">
                    <div class="sku-row-title  pb0" style="line-height:32px;">数量：<span style="font-size:.347rem;color: rgba(45,46,70,0.50);">剩余<span>{{productDetail.availableStock}}</span>件数</span></div>
                    <!--数量加减框 -->
                    <div class="sku-stepper">
                      <button class="stepper-minus " :class="{'stepper-minus-disabled':disabledMin}" @click="sub">－</button>
                      <input type="number" class="stepper-input" v-model.number="currentValue">
                      <button class="stepper-plus" @click="add" :class="{'stepper-minus-disabled':disabledMax}">＋</button>
                    </div>
                  </div>
                  <!-- <div class="sku-stock f12 grey_be">剩余
                    <span>{{productDetail.availableStock}}</span>件数</div> -->
                </div>
              </div>
            </div>
            <!-- 按钮 -->
            <div class="sku-actions">
              <div class="flex-ui">
                <x-button @click.native="buyNow" type="warn">我要兑换</x-button>
                <!--<a href="javascript:void(0);" class="flex1 tc"  @click="buyNow" >
                          <span class="f16 pl0">我要兑换</span>
                      </a >-->
              </div>
            </div>
          </div>
        </popup>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import {
  TransferDom,
  Swiper,
  Divider,
  LoadMore,
  Popup,
  XButton,
  Flexbox,
  FlexboxItem
} from "vux";
import { get, post } from "@/api/server";
import url from "@/api/apiUrl";
import storage from "@/utils/common";
export default {
  directives: {
    TransferDom
  },
  components: {
    Swiper,
    Divider,
    LoadMore,
    Popup,
    XButton,
    Flexbox,
    FlexboxItem
  },
  props: {},
  data() {
    return {
      memberPoint: 0,
      isValid: false,
      commentType: "product",
      id: this.$route.query && this.$route.query.id,
      swiperList: [], //轮播图s
      productDetail: [],
      retarValue: 4.5, //评分
      showSku: false, //控制弹出框的显示影藏
      colorList: [
        // { id: 0, name: "薄荷橄榄绿" },
        // { id: 1, name: "奶油米黄色" },
        // { id: 2, name: "天然湖水蓝" }
      ],
      currentColorValue: 0,
      currentColor: "",
      colorSpecificationValue: 6,
      currentValue: 1,
      isFavorite: false,
      // 收藏数据
      favorite: {
        objId: undefined,
        type: "product"
      },
      min: 1, //最小值
      max: 9 //最大值
    };
  },
  watch: {
    currentValue(newValue, old) {
      if (newValue !== "") {
        if (typeof this.min !== "undefined" && this.currentValue < this.min) {
          this.currentValue = this.min;
        }
        if (this.max && this.currentValue > this.max) {
          this.currentValue = this.max;
        }
      }
    },
    // 监听showSku，弹出层出现后，禁止内容层滚动
    showSku: {
      handler(val, oldVal) {
        this.popShow1 = val;
        document.body.style.overflow = val ? "hidden" : "";
        document.querySelector("html").style.overflow = val ? "hidden" : "";
        document.body.style.height = val ? "100%" : "";
        document.querySelector("html").style.height = val ? "100%" : "";
      },
      deep: true
    }
  },
  computed: {
    //控制最小值
    disabledMin() {
      return typeof this.min === "undefined"
        ? false
        : this.currentValue === "" ? true : this.currentValue <= this.min;
    },
    //控制最大值
    disabledMax() {
      return typeof this.max === "undefined"
        ? false
        : this.currentValue === "" ? true : this.currentValue >= this.max;
    }
  },
  methods: {
    //获取banner数据
    getSwiperList() {
      get(url.productDetail, { id: this.id }).then(response => {
        this.productDetail = response.data.data;
        this.isValid = this.productDetail.exchangePoint <= this.memberPoint;
        let productImageList = response.data.data.productImages;
        this.max = this.productDetail.availableStock;
        if (productImageList != null && productImageList.length > 0) {
          for (var i = 0; i < productImageList.length; i++) {
            let arr = { url: "javascript:", img: "" };
            var el = productImageList[i];
            arr.img = el.source;
            this.swiperList.push(arr);
          }
        }

        this.colorList = response.data.data.specificationValues;
        if (this.colorList != null && this.colorList.length > 0) {
          for (var p in this.colorList) {
            if (
              this.colorList["0"].specification_id ==
              this.colorSpecificationValue
            ) {
              this.currentColorValue = 0;
              this.currentColor = this.colorList[p].name;
              break;
            }
          }
        }
        //console.log("this.adList=" + response.data.data.adList)
      });
    },

    //立即购买弹出sku
    buyTap() {
      this.showSku = true;
    },
    //切换颜色
    changeColor(selectedValues, specificationId, specificationValueChangedId) {
      //this.currentColorValue = index;
      //this.currentColor = name;

      for (var p in selectedValues) {
        if (selectedValues[p].specificationId == specificationId) {
          selectedValues[p].specificationValues[
            "0"
          ].id = specificationValueChangedId;
        }
      }

      for (var p in this.productDetail.productSpecificationValues) {
        let isHas = true;
        for (var k in this.productDetail.productSpecificationValues[p]
          .specificationValues) {
          if (
            this.productDetail.productSpecificationValues[p]
              .specificationValues[k].id ==
            selectedValues[k].specificationValues["0"].id
          ) {
          } else {
            isHas = false;
            break;
          }
        }

        if (isHas) {
          this.id = this.productDetail.productSpecificationValues[p].productId;

          this.getSwiperList();
        }
      }
    },
    //选中判断
    selected(values, value) {
      for (var p in values) {
        if (values[p].id == value) {
          return true;
        }
      }
      return false;
    },
    //立即购买
    buyNow() {
      this.showSku = false;

      let params = {
        id: this.id,
        quantity: this.currentValue
      };
      post(url.productBuyNow, params).then(response => {
        this.showSku = false;
        let message = response.data.msg;
        if (response.data.result === "00000000") {
          //调用接口
          localStorage.setItem("order_type", "order");
          this.$router.push({
            path: "/order/info",
            query: { cartItemIds: response.data.data.cartItems }
          });
        } else if (response.data.result === "10000007") {
          this.$router.push({
            path: "/login",
            query: { redirect: this.$route.fullPath }
          });
        } else {
          this.$vux.toast.show({
            type: "alert",
            text: message,
            width: "120px",
            time: 1000
          });
        }
      });
    },
    //加
    add() {
      if (!this.disabledMax) {
        this.currentValue++;
      } else {
        //提示达到上限
        this.$vux.toast.text("达到上限", "middle");
      }
    },
    //减
    sub() {
      if (!this.disabledMin) {
        this.currentValue--;
      }
    },
    //积分不足的时候调整到缴社保页面
    handlerJump() {
      this.$router.push({
        path: "/order/orderMember?type=SB"
      });
    }
  },
  created() {
    localStorage.setItem("order_type", null);
    this.getSwiperList();
    let member = storage.get("member");
    this.memberPoint = member.point;
  },
  mounted() {},
  beforeDestroy() {
    this.showSku = false;
    document.body.style.overflow = "";
    document.querySelector("html").style.overflow = "";
    document.body.style.height = "";
    document.querySelector("html").style.height = "";
    console.log("sss");
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
      float: left;
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
    bottom: 0px;
    height: 1.333rem;
    line-height: 1.333rem;
    width: 100%;
    button {
      height: 1.333rem;
      line-height: 1.333rem;
      border-radius: 0;
      font-size: 16px;
    }
    // a.flex1 {
    //   height: 38px;
    //   line-height: 38px;
    //   background-color: @color_base;
    //   color: #ffffff;
    //   &.disabled {
    //     background-color: #a6a6a6;
    //   }
    //   &:first-child {
    //     border-radius: 30px 0 0 30px;
    //   }
    //   &:last-child {
    //     border-radius: 0 30px 30px 0;
    //   }
    //   span {
    //     display: block;
    //     width: 100%;
    //     color: #fff;
    //   }
    // }
  }
}
.weui-btn_primary {
  background-color: @color_base;
}
</style>
