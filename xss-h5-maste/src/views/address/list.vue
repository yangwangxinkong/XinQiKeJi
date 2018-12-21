<template>
  <div class="page">
    <div class="container  pb0">
      <div class="address-list mt10 bg_f" v-if="addressList && addressList.length>0">
        <div class="radio-group">
          <div class="cell-group hairline--top-bottom">
            <div class="cell cell--clickable hairline address-item" v-for="(item,index) in addressList" :key="index">
              <div class="cell__value cell__value--alone" @click="handleClick(item, item.id)">
                <div class="radio">
                  <div style="line-height:20px">{{item.consignee}}</div>
                  <p class="default-tag" v-if="item.isDefault">
                    <span>默认</span>
                  </p>
                  <!-- <span class="radio__input">
                    <input type="radio" class="radio__control" :value="index">
                    <i class="iconfont " :class="[currentAddressId===item.id ? 'icon-checked color_green':'icon-check']"></i>
                  </span> -->
                  <!-- <span class="radio__label">
                    <div class="address-item__name">{{item.phone}}</div>
                    <div class="address-item__address">{{item.areaName}}{{item.address}}</div>
                  </span> -->
                </div>
                <div class="radio__label flex1">
                  <div class="address-item__name">{{item.phone.substring(0,3)}}****{{item.phone.substring(item.phone.length-4)}}</div>
                  <div class="address-item__address">{{item.areaName}}{{item.address}}</div>
                </div>
              </div>
              <router-link :to="{ path:'/address/add',query: {id: item.id, cartItemIds: cartItemIds} }" class="edit-box">
                <i class="iconfont icon-edit address-item__edit f20"></i>
              </router-link>
            </div>
          </div>
        </div>
      </div>
      <!-- 没有数据的默认展示 -->
      <load-more :show-loading="false" tip="暂无数据" background-color="#fbf9fe" v-else></load-more>
      <!-- footer -->
      <section class="footer-actionBar">
        <section class="footer-actionBar-container flex-ui " style="left:.587rem;right:.587rem;bottom:.587rem;border-radius:25px;">
          <a href="javascript:void(0);" class="flex1" @click="addAddress">
            <span class="f16 pl0">新增地址</span>
          </a>
        </section>
      </section>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import { LoadMore } from "vux";
import url from "@/api/apiUrl";
import { get } from "@/api/server";

export default {
  components: { LoadMore },
  props: {},
  data() {
    return {
      cartItemIds: this.$route.query && this.$route.query.cartItemIds,
      addressList: [],
      // addressList:[{
      //     id:1,
      //     name:'某某某',
      //     phone:'15998917033',
      //     addressDetail:'浙江省杭州市西湖区文三路 138 号东方通信大厦 7 楼 501 室',
      //     AddressCode:11
      // },
      // {
      //     id:2,
      //     name:'某某某',
      //     phone:'15998917033',
      //     addressDetail:'浙江省杭州市西湖区文三路 138 号东方通信大厦 7 楼 501 室',
      //      AddressCode:22
      // }],
      currentAddressId: null
    };
  },
  watch: {},
  computed: {},
  methods: {
    // 获取找材料分类
    getAddressList() {
      get(url.addressList, {}).then(response => {
        this.addressList = response.data.list;
        // console.log("this.navList=" + response.data.data)
      });
    },
    addAddress() {
      this.$router.push({
        path: "/address/add",
        query: { cartItemIds: this.cartItemIds }
      });
    },
    handleClick(addressItem, id) {
      //1、选中该列表项；2、保存到页面缓存；3、跳转到订单确认页面同时展示选中的地址信息
      this.currentAddressId = id;
      localStorage.setItem("address", JSON.stringify(addressItem));
      this.$router.push({
        path: "/order/info",
        query: { cartItemIds: this.cartItemIds }
      });
    }
  },
  created() {
    this.getAddressList();
  },
  mounted() {}
};
</script>

<style scoped lang="less">
@import "../../assets/styles/less/public.less";
.address-list {
  height: 100%;
  box-sizing: border-box;
  position: relative;
  &:before {
    content: "";
    left: 0;
    right: 0;
    top: 0;
    height: 4px;
    position: absolute;
    background: -webkit-repeating-linear-gradient(
      135deg,
      #ff841a 0,
      #ff841a 20%,
      transparent 0,
      transparent 25%,
      #5f9ce7 1 0,
      #5f9ce7 45%,
      transparent 0,
      transparent 50%
    );
    background: repeating-linear-gradient(
      -45deg,
      #ff841a 0,
      #ff841a 20%,
      transparent 0,
      transparent 25%,
      #5f9ce7 0,
      #5f9ce7 45%,
      transparent 0,
      transparent 50%
    );
    background-size: 80px;
    z-index: 9;
  }

  .cell-group {
    background-color: #fff;
    .cell {
      width: 100%;
      .flex-ui;
      padding: 15px;
      box-sizing: border-box;
      line-height: 24px;
      position: relative;
      background-color: #fff;
      color: #333;
      font-size: 14px;
      overflow: hidden;
      &:not(:last-child):after {
        content: "";
        position: absolute;
        top: 0;
        width: 200%;
        height: 200%;
        -webkit-transform: scale(0.5);
        transform: scale(0.5);
        -webkit-transform-origin: 0 0;
        transform-origin: 0 0;
        pointer-events: none;
        box-sizing: border-box;
        border: 0 solid #e5e5e5;
        left: 15px;
        right: 0;
        width: auto;
        -webkit-transform: scale(1, 0.5);
        transform: scale(1, 0.5);
        border-bottom-width: 1px;
      }
    }
    .cell__value {
      overflow: hidden;
      text-align: left;
      position: relative;
      vertical-align: middle;
      display: flex;
      align-items: flex-start;
      .radio__label {
        margin-left:.72rem;
        line-height: 20px;
      }
    }
  }
  .address-item .cell__value {
    color: #2D2E46;;
    padding-right: 34px;
    position: relative;
  }
  .radio {
    overflow: hidden;
    -webkit-user-select: none;
    user-select: none;
    width: 1.333rem;
    .default-tag {
      span {
        background: url(../../assets/default-tag.png) no-repeat;
        background-size: 100% 100%;
        font-size: 0;
        width: .933rem;
        height: .48rem;
        display: inline-block;
      }
    }
    .radio__input,
    .radio__label {
      display: inline-block;
      vertical-align: middle;
    }
    .radio__input {
      top: 50%;
      left: 0;
      font-size: 16px;
      position: absolute;
      -webkit-transform: translate(0, -50%);
      transform: translate(0, -50%);
      .radio__control {
        position: absolute;
        top: 0;
        left: 0;
        opacity: 0;
        margin: 0;
        width: 100%;
        height: 100%;
      }
    }
  }
  .address-item__name {
    font-size: .453rem;
    font-weight: 500;
    line-height: 20px;
    margin-bottom: 5px;
  }
  .address-item__address {
    font-size:.4rem;
    line-height: 20px;
    color:  rgba(45,46,70,0.60);
  }
  .address-item__edit {
    position: absolute;
    top:15px;
    right: 15px;
    font-size: 16px;
    color: rgba(45,46,70,0.60);
    // -webkit-transform: translate(0, -50%);
    // transform: translate(0, -50%);
  }
}
</style>
