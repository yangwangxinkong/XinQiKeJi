<!-- 售后服务 -->
<template>
  <div class="page">
      <div class="container bg_f">

        <div class="after_sales_service_list">
            <div class="pt20 ml30">
              <h2 class="f18">公积金服务售后</h2>
              <p class="mt10 mb20">温馨提示：适用于在人人保公积金服务期限内的用户</p>
            </div>
            <grid class="bg_f tc">
               <grid-item key="gjj1">
                <p>新参/续费订单</p>
                <x-button class="mt10 br20 base_btn_color" mini type="primary" @click.native="apply">申请减员</x-button>
              </grid-item>
              <grid-item key="gjj2">
                <p>补缴订单</p>
                <x-button class="mt10 br20 base_btn_color" mini type="primary" @click.native="apply">申请停缴</x-button>
              </grid-item>
            </grid>
        </div>
        <div class="after_sales_service_list">
            <div class="pt20 ml30">
              <h2 class="f18">增值服务售后</h2>
              <p class="mt10 mb20">温馨提示：适用于在人人保增值服务期限内的用户</p>
            </div>
            <grid class="bg_f tc">
               <grid-item key="zz1">
                <p>新参/续费订单</p>
                <x-button class="mt10 br20 base_btn_color" mini type="primary" @click.native="apply">申请减员</x-button>
              </grid-item>
            </grid>
        </div>
      </div>
  </div>
</template>
<script>
import { Grid, GridItem,XButton } from 'vux'
export default {
  components:{ Grid, GridItem,XButton},
  data(){
    return{
      list:[{
        text:'新参/续费订单',
        btnText:'申请减员'
      }, {
        text: '新参/续费订单',
        btnText: '申请减员'
      }]
    }
  }
}
</script>
<style lang="less" scoped>
.after_sales_service_list{
  width: 90%;
  margin: 20px auto;
  border-radius: 5px;
  color: #fff;
  background-color: @color_base;
  box-shadow: 2px 2px 2px 2px #ddd;
}
.br20{
  border-radius: 20px;
}
.base_btn_color{
  background-color: @color_base;
}
</style>


