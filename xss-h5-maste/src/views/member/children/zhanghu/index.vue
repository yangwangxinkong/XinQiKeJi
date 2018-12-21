
<!--账户信息  -->
<template>
  <div class="page">
    <div class="container" style="padding-bottom:0px;">
      <div class="user_content tc">
        <h2 class="f18">账户余额</h2>
        <p class="mt10">全部余额（元）</p>
        <p class="mt5 f25">{{balance}}</p>
        <!--<div class="user_border mt10">
            可用余额提现
        </div>-->
        <grid class="user_data">
          <grid-item v-for="(item,index) in list" :key="index">
            <p class="color_base fw">{{item.value}}</p>
            <p>{{item.title}}</p>
          </grid-item>
        </grid>
      </div>
      <div class="tc pt30">您还没有收支明细哦~</div>
    </div>
  </div>
</template>

<script>
import { XButton, Grid, GridItem } from "vux";
import storage from "@/utils/common";
import url from "@/api/apiUrl";
import { get, post, execute } from "@/api/server";
export default {
  components: { XButton, Grid, GridItem },
  data() {
    return {
      balance: "0.00",
      list: [
        {
          title: "可用余额",
          value: "0.00"
        },
        {
          title: "锁定余额",
          value: "0.00"
        },
        {
          title: "金豆余额",
          value: "0.00"
        }
      ]
    };
  },

  computed: {},

  mounted() {},

  created() {
    this.getMemberInfo();
  },

  methods: {
    getMemberInfo() {
      let member = storage.get("member");
      get(url.current, { id: member.id }).then(response => {
        if (response.data.result == "00000000") {
          this.balance = response.data.data.balance;
          this.list[0].value = this.balance;
          this.list[2].value = response.data.data.point;
          storage.set("member", response.data.data);
        }
      });
    }
  }
};
</script>
<style lang='less' >
.user_content {
  background: url(../../../../assets/balance-bg.png) no-repeat;
  width: 100%;
  background-size: 100% 100%;
  background-position: center;
  color: #fff;
  position: relative;
  padding: 20px 0 60px;
  margin-bottom: 60px;
  .user_border {
    border-radius: 20px;
    width: 120px;
    height: 34px;
    line-height: 34px;
    border: 1px solid #fff;
    margin: auto;
  }
  .user_data {
    width: 90%;
    position: absolute;
    margin: 0 5%;
    background-color: #fff;
    bottom: -50px;
  }
  .fw {
    font-weight: bold;
  }
   .weui-grids{
     box-shadow: 0 1px 4px 0 rgba(45,46,70,0.05);
     &:after,&:before{
       content: none;
     }
   }
  .weui-grid {
    &:before {
      top: 50%;
      transform: translate(-50%, -50%);
    }
    &:after {
      height: 0;
      border-bottom:0;
    }

  }
  .weui-grid:last-child:before {
    border-right: 0;
    width: 0;
  }
}
</style>
