<template>
  <div class="page">
    <div class="container  pb0">
      <div class="address-list bg_f" v-if="couponList && couponList.length>0">
        <div class="radio-group">
          <div class="cell-group hairline--top-bottom">
            <div class="cell cell--clickable hairline address-item" v-for="(item,index) in couponList" :key="index">
              <div class="cell__value cell__value--alone" @click="handleClick(item, item.id)">
                <div class="radio">
                 <span class="radio__input">
                     <input type="radio" class="radio__control" value="1">
                     <i class="iconfont " :class="[currentCouponId===item.id ? 'icon-checked color_green':'icon-check']"></i>
                  </span>
                  <span class="radio__label">
                      <div class="address-item__name">{{item.name}}</div>
                      <div class="address-item__address">{{item.discount}}折</div>
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 没有数据的默认展示 -->
      <load-more :show-loading="false" tip="暂无数据" background-color="#fbf9fe" v-else></load-more>

    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import { LoadMore } from 'vux'
  import url from '@/api/apiUrl'
  import { get } from '@/api/server'

  export default {
    components: {LoadMore},
    props: {},
    data() {
      return {
        couponList:[],
        currentCouponId:null
      };
    },
    watch: {},
    computed: {},
    methods: {
      getCouponList() {
        get(url.memberCoupons, {isUsed:false}).then(response => {
          this.couponList = response.data.list
          // console.log("this.navList=" + response.data.data)
        });
      },
      handleClick(couponItem, id){
        //1、选中该列表项；2、保存到页面缓存；3、跳转到订单确认页面同时展示选中的地址信息
        this.currentCouponId=id;
        localStorage.setItem("couponCode",JSON.stringify(couponItem));
        this.$router.push({ path:'/order/orderConfirm'});
      }
    },
    created() {
      this.getCouponList();
    },
    mounted() {}
  };
</script>

<style scoped lang="less">
  @import "../../../../assets/styles/less/public.less";
  .address-list {
    height: 100%;
    box-sizing: border-box;
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
  }
  }
  .address-item .cell__value {
    color: #333;
    padding-right: 34px;
    position: relative;
  }
  .radio {
    overflow: hidden;
    -webkit-user-select: none;
    user-select: none;
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
  .radio__label {
    margin-left: 27px;
    line-height: 20px;
  }
  }
  .address-item__name {
    font-size: 14px;
    font-weight: 500;
    line-height: 20px;
    margin-bottom: 5px;
  }
  .address-item__address {
    font-size: 12px;
    line-height: 16px;
    color: #666;
  }
  .address-item__edit {
    position: absolute;
    top: 50%;
    right: 15px;
    font-size: 16px;
    -webkit-transform: translate(0, -50%);
    transform: translate(0, -50%);
  }
  }
</style>
